package com.unknownbrain.recipeapp.converters.fromCommand;

import com.unknownbrain.recipeapp.commands.NotesCommand;
import com.unknownbrain.recipeapp.commands.RecipeCommand;
import com.unknownbrain.recipeapp.models.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryConverter;
    private final IngredientCommandToIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryConverter, IngredientCommandToIngredient ingredientConverter, NotesCommandToNotes notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Override
    public Recipe convert(RecipeCommand command) {
        Objects.requireNonNull(command);

        final Recipe recipe = new Recipe();

        recipe.setId(command.getId());
        recipe.setCookTime(command.getCookTime());
        recipe.setPrepTime(command.getPrepTime());
        recipe.setDescription(command.getDescription());
        recipe.setDifficulty(command.getDifficulty());
        recipe.setDirections(command.getDirections());
        recipe.setServings(command.getServings());
        recipe.setSource(command.getSource());
        recipe.setUrl(command.getUrl());
        recipe.setImage(command.getImage());
        NotesCommand notesCommand = command.getNotes() != null ? command.getNotes() : new NotesCommand();
        recipe.setNotes(notesConverter.convert(notesCommand));

        if (command.getCategories() != null && command.getCategories().size() > 0) {
            command.getCategories()
                    .forEach(categoryCommand -> recipe.getCategories().add(categoryConverter.convert(categoryCommand)));
        }

        if (command.getIngredients() != null && command.getIngredients().size() > 0) {
            command.getIngredients()
                    .forEach(ingredientCommand -> recipe.getIngredients().add(ingredientConverter.convert(ingredientCommand)));
        }

        return recipe;
    }
}
