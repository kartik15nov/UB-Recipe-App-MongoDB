package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.IngredientCommand;
import com.unknownbrain.recipeapp.commands.UnitOfMeasureCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.IngredientCommandToIngredient;
import com.unknownbrain.recipeapp.converters.toCommand.IngredientToIngredientCommand;
import com.unknownbrain.recipeapp.converters.toCommand.UnitOfMeasureToUnitOfMeasureCommand;
import com.unknownbrain.recipeapp.domain.Ingredient;
import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.domain.UnitOfMeasure;
import com.unknownbrain.recipeapp.repositories.RecipeRepository;
import com.unknownbrain.recipeapp.repositories.reactive.RecipeReactiveRepository;
import com.unknownbrain.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    IngredientToIngredientCommand ingredientToIngredientCommand;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientService = new IngredientServiceImpl(recipeRepository, recipeReactiveRepository, unitOfMeasureReactiveRepository, ingredientToIngredientCommand, ingredientCommandToIngredient);
    }

    @Test
    void findByRecipeIdAndIngredientId() {

        //given
        Recipe recipe = new Recipe();
        recipe.setId("5");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("6");
        recipe.addIngredient(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("7");
        recipe.addIngredient(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("8");
        recipe.addIngredient(ingredient3);


        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        //when
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("5", "7").block();

        //then
        assertNotNull(ingredientCommand);
        assertEquals("7", ingredientCommand.getId());
        verify(recipeReactiveRepository).findById(anyString());
    }

    @Test
    void saveIngredientCommand() {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId("12");
        command.setRecipeId("20");
        command.setUom(new UnitOfMeasureCommand());
        command.getUom().setId("1234");

        Recipe recipe = new Recipe();
        recipe.addIngredient(new Ingredient());
        recipe.getIngredients().get(0).setId("12");

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId("1234");

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(unitOfMeasure));

        //when
        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(command).block();

        //then
        assertNotNull(savedIngredientCommand);
        assertEquals("12", savedIngredientCommand.getId());
        verify(recipeRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }


    @Test
    void deleteById() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId("1");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2");
        recipe.addIngredient(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");
        recipe.addIngredient(ingredient3);

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        //when
        ingredientService.deleteById("1", "2");

        //then
        assertEquals(1, recipe.getIngredients().size());
        verify(recipeRepository, atLeastOnce()).findById(anyString());
        verify(recipeRepository, atLeastOnce()).save(any());
    }
}