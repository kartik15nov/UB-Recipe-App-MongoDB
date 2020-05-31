package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.IngredientCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.IngredientCommandToIngredient;
import com.unknownbrain.recipeapp.converters.toCommand.IngredientToIngredientCommand;
import com.unknownbrain.recipeapp.domain.Ingredient;
import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.repositories.RecipeRepository;
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

    RecipeRepository recipeRepository;
    RecipeReactiveRepository recipeReactiveRepository;
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    IngredientToIngredientCommand ingredientToIngredientCommand;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
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

        String recipeId = command.getRecipeId();
        Objects.requireNonNull(recipeId);

        //Find the recipe from the repository, if found then find the ingredient, if found update it, else add the new ingredient to recipe. Save the recipe, get the ingredient from recipe and convert it to command and return.
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            log.error("Recipe not found for id : " + recipeId);
            return Mono.just(new IngredientCommand());
        }

        Recipe recipe = optionalRecipe.get();
        Ingredient updatedIngredient = null;

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

            if (updatedIngredient.getUom() == null) new RuntimeException("UOM NOT FOUND");
        } else {
            updatedIngredient = ingredientCommandToIngredient.convert(command);
            recipe.addIngredient(updatedIngredient);
        }

        //Save the Recipe
        Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

        //convert the ingredient to command and return with Mono
        Ingredient finalUpdatedIngredient = updatedIngredient;

        assert savedRecipe != null;
        Ingredient savedIngredient = savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> {
                    assert finalUpdatedIngredient != null;
                    return ingredient.getId().equals(finalUpdatedIngredient.getId());
                })
                .findFirst().get();

        IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredient);
        assert ingredientCommandSaved != null;
        ingredientCommandSaved.setRecipeId(recipe.getId());

        return Mono.just(ingredientCommandSaved);
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NullPointerException::new);

        recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst()
                .ifPresent(ingredient -> {
                    log.debug("Deleting ingredient :{} from recipe :{}", ingredient.getId(), recipeId);
                    recipe.getIngredients().remove(ingredient);
                });
        recipeRepository.save(recipe);
        return Mono.empty();
    }
}
