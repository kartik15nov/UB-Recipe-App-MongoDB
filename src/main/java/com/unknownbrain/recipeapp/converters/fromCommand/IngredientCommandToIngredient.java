package com.unknownbrain.recipeapp.converters.fromCommand;

import com.unknownbrain.recipeapp.commands.IngredientCommand;
import com.unknownbrain.recipeapp.commands.UnitOfMeasureCommand;
import com.unknownbrain.recipeapp.models.Ingredient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Override
    public Ingredient convert(IngredientCommand command) {
        Objects.requireNonNull(command);

        final Ingredient ingredient = new Ingredient();

        ingredient.setId(command.getId());
        ingredient.setDescription(command.getDescription());
        ingredient.setAmount(command.getAmount());
        UnitOfMeasureCommand unitOfMeasureCommand = command.getUom() != null ? command.getUom() : new UnitOfMeasureCommand();
        ingredient.setUom(uomConverter.convert(unitOfMeasureCommand));

        return ingredient;
    }
}
