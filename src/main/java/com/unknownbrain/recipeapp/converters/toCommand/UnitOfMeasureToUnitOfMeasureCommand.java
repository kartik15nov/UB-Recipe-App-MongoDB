package com.unknownbrain.recipeapp.converters.toCommand;

import com.unknownbrain.recipeapp.commands.UnitOfMeasureCommand;
import com.unknownbrain.recipeapp.models.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Synchronized
    @Override
    public UnitOfMeasureCommand convert(UnitOfMeasure unitOfMeasure) {
        Objects.requireNonNull(unitOfMeasure);

        final UnitOfMeasureCommand command = new UnitOfMeasureCommand();

        command.setId(unitOfMeasure.getId());
        command.setDescription(unitOfMeasure.getDescription());

        return command;
    }
}
