package com.unknownbrain.recipeapp.converters.fromCommand;

import com.unknownbrain.recipeapp.commands.UnitOfMeasureCommand;
import com.unknownbrain.recipeapp.models.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

    @Synchronized
    @Override
    public UnitOfMeasure convert(UnitOfMeasureCommand command) {
        Objects.requireNonNull(command);

        final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();

        unitOfMeasure.setId(command.getId());
        unitOfMeasure.setDescription(command.getDescription());

        return unitOfMeasure;
    }
}
