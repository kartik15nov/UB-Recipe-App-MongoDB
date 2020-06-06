package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.RecipeCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.RecipeCommandToRecipe;
import com.unknownbrain.recipeapp.converters.toCommand.RecipeToRecipeCommand;
import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeReactiveRepository recipeRepository;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    //    @Transactional
    @Test
    void testSaveOfDescriptionToCommandObjectThenSaveTheCommandObjectToRepository() {
        //given
        Recipe recipe = recipeService.getRecipes().blockFirst();
        assert recipe != null;

        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
        assert recipeCommand != null;

        //when
        recipeCommand.setDescription(NEW_DESCRIPTION);
        Mono<RecipeCommand> savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        //then
        assertNotNull(savedRecipeCommand);
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.block().getDescription());
        assertEquals(recipe.getId(), savedRecipeCommand.block().getId());
        assertEquals(recipe.getCategories().size(), savedRecipeCommand.block().getCategories().size());
        assertEquals(recipe.getIngredients().size(), savedRecipeCommand.block().getIngredients().size());
    }
}