package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.IngredientCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.IngredientCommandToIngredient;
import com.unknownbrain.recipeapp.converters.toCommand.IngredientToIngredientCommand;
import com.unknownbrain.recipeapp.domain.Ingredient;
import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.domain.UnitOfMeasure;
import com.unknownbrain.recipeapp.repositories.RecipeRepository;
import com.unknownbrain.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Service
public class IngredientServiceImpl implements IngredientService {

    RecipeRepository recipeRepository;
    UnitOfMeasureRepository unitOfMeasureRepository;
    IngredientToIngredientCommand ingredientToIngredientCommand;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        Optional<Recipe> optional = recipeRepository.findById(recipeId);

        AtomicReference<IngredientCommand> ingredientCommand = new AtomicReference<>();

        optional.flatMap(recipe -> recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst())
                .ifPresent(ingredient -> ingredientCommand.getAndSet(ingredientToIngredientCommand.convert(ingredient)));

        return ingredientCommand.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Objects.requireNonNull(ingredientCommand);

//        String recipeId = ingredientCommand.getRecipeId();

        Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId()).orElseThrow(NullPointerException::new);

        final int[] indexOfIngredient = {-1};
        Ingredient ingredientFromRecipe = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
                    assert ingredient != null;
                    recipe.addIngredient(ingredient);
                    return ingredient;
                });
        indexOfIngredient[0] = recipe.getIngredients().indexOf(ingredientFromRecipe);

        ingredientFromRecipe.setDescription(ingredientCommand.getDescription());
        ingredientFromRecipe.setAmount(ingredientCommand.getAmount());

        if (ingredientCommand.getUom() != null && ingredientCommand.getUom().getId() != null) {
            ingredientFromRecipe.setUom(unitOfMeasureRepository
                    .findById(ingredientCommand.getUom().getId())
                    .orElse(new UnitOfMeasure())); //TODO address this
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        return ingredientToIngredientCommand.convert(savedRecipe.getIngredients().get(indexOfIngredient[0]));
    }

    @Override
    public void deleteById(String recipeId, String ingredientId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(NullPointerException::new);

        recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst()
                .ifPresent(ingredient -> {
                    log.debug("Deleting ingredient :{} from recipe :{}", ingredient.getId(), recipeId);

//                    ingredient.setRecipe(null);
                    recipe.getIngredients().remove(ingredient);
                });
        recipeRepository.save(recipe);
    }
}
