package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.RecipeCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.RecipeCommandToRecipe;
import com.unknownbrain.recipeapp.converters.toCommand.RecipeToRecipeCommand;
import com.unknownbrain.recipeapp.exceptions.NotFoundException;
import com.unknownbrain.recipeapp.models.Recipe;
import com.unknownbrain.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

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

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        Recipe recipeReturned = recipeService.findById("1");

        assertNotNull(recipeReturned);
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void getRecipeByIdTestNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            Optional<Recipe> recipeOptional = Optional.empty();

            when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

            Recipe recipeReturned = recipeService.findById("1");
        });

    }

    @Test
    public void getRecipeCommandByIdTest() {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById("1");

        assertNotNull(commandById);
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void getRecipesTest() {

        List<Recipe> recipeData = new ArrayList<>();
        Recipe recipe1 = new Recipe();
        recipeData.add(recipe1);

        when(recipeRepository.findAll()).thenReturn(recipeData);

        List<Recipe> recipes = recipeService.getRecipes();
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