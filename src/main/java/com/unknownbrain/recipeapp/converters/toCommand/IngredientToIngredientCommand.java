package com.unknownbrain.recipeapp.converters.toCommand;

import com.unknownbrain.recipeapp.commands.IngredientCommand;
import com.unknownbrain.recipeapp.domain.Ingredient;
import com.unknownbrain.recipeapp.domain.UnitOfMeasure;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    public IngredientToIngredientCommand(UnitOfMeasureToUnitOfMeasureCommand uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Override
    public IngredientCommand convert(Ingredient ingredient) {
        Objects.requireNonNull(ingredient);

        IngredientCommand command = new IngredientCommand();

        command.setId(ingredient.getId());
        command.setDescription(ingredient.getDescription());
        command.setAmount(ingredient.getAmount());
        UnitOfMeasure unitOfMeasure = ingredient.getUom() != null ? ingredient.getUom() : new UnitOfMeasure();
        command.setUom(uomConverter.convert(unitOfMeasure));

        return command;
    }
}
