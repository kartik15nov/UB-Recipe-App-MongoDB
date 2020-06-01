package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.IngredientCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.IngredientCommandToIngredient;
import com.unknownbrain.recipeapp.converters.toCommand.IngredientToIngredientCommand;
import com.unknownbrain.recipeapp.domain.Ingredient;
import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.repositories.reactive.RecipeReactiveRepository;
import com.unknownbrain.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class IngredientServiceImpl implements IngredientService {

    RecipeReactiveRepository recipeReactiveRepository;
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    IngredientToIngredientCommand ingredientToIngredientCommand;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {

        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;

        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    assert command != null;
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Objects.requireNonNull(command);

        //Find the recipe from the repository, if found then find the ingredient, if found update it, else add the new ingredient to recipe. Save the recipe, get the ingredient from recipe and convert it to command and return.
        Recipe recipe = recipeReactiveRepository.findById(command.getRecipeId()).block();

        if (recipe != null) {
            Ingredient updatedIngredient;

            Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equalsIgnoreCase(command.getId()))
                    .findFirst();

            //If not present, convert the command to ingredient and add to recipe
            if (optionalIngredient.isPresent()) {
                updatedIngredient = optionalIngredient.get();
                updatedIngredient.setDescription(command.getDescription());
                updatedIngredient.setAmount(command.getAmount());
                updatedIngredient.setUom(unitOfMeasureReactiveRepository.findById(command.getUom().getId()).block());
            } else {
                updatedIngredient = ingredientCommandToIngredient.convert(command);
                Objects.requireNonNull(updatedIngredient).setUom(unitOfMeasureReactiveRepository.findById(command.getUom().getId()).block());
                recipe.addIngredient(updatedIngredient);
            }

            //Save the Recipe
            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();
            assert savedRecipe != null;

            //convert the ingredient to command and return with Mono
            Ingredient finalUpdatedIngredient = updatedIngredient;
            Ingredient savedIngredient = savedRecipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(finalUpdatedIngredient.getId()))
                    .findFirst().get();

            IngredientCommand savedCommand = ingredientToIngredientCommand.convert(savedIngredient);
            assert savedCommand != null;

            savedCommand.setRecipeId(recipe.getId());
            return Mono.just(savedCommand);

        } else {
            log.error("Recipe not found for id : " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
        }
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe != null) {
            Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (optionalIngredient.isPresent()) {
                log.debug("Deleting ingredient :{} from recipe :{}", ingredientId, recipeId);
                recipe.getIngredients().remove(optionalIngredient.get());
                recipeReactiveRepository.save(recipe).block();
            } else
                log.debug("Ingredient Id Not found. Id:" + ingredientId);

        } else
            log.debug("Recipe Id Not found. Id:" + recipeId);

        return Mono.empty();
    }
}
