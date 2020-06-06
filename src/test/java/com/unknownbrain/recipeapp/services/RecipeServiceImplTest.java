package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.RecipeCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.RecipeCommandToRecipe;
import com.unknownbrain.recipeapp.converters.toCommand.RecipeToRecipeCommand;
import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeReactiveRepository recipeRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    void getRecipeByIdTest() {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById("1").block();

        assertNotNull(recipeReturned);
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getRecipeCommandByIdTest() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById("1").block();

        assertNotNull(commandById);
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void getRecipesTest() {

        List<Recipe> recipeData = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipeData.add(recipe1);

        when(recipeRepository.findAll()).thenReturn(Flux.just(recipe1));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();
        assert recipes != null;
        assertEquals(recipes.size(), 1);

        //To verify the interactions, e.g: to check how many time the findAll() method gets called, then don this..
        //Since in this scenario, the method gets called only once during the testing, so expected is 1 time..
        //Hence if we change the times to 2, then below code will fail since findAll() method never called twice.
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void testDeleteById() {
        String idToDelete = "2";

        recipeService.deleteById(idToDelete);

        verify(recipeRepository).deleteById(anyString());
    }
}