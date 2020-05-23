package com.unknownbrain.recipeapp.converters.toCommand;

import com.unknownbrain.recipeapp.commands.CategoryCommand;
import com.unknownbrain.recipeapp.models.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {

    @Synchronized
    @Override
    public CategoryCommand convert(Category category) {
        Objects.requireNonNull(category);

        final CategoryCommand command = new CategoryCommand();

        command.setId(category.getId());
        command.setDescription(category.getDescription());

        return command;
    }
}
