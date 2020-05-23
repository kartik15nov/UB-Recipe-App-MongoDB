package com.unknownbrain.recipeapp.converters.fromCommand;

import com.unknownbrain.recipeapp.commands.CategoryCommand;
import com.unknownbrain.recipeapp.models.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {

    @Synchronized
    @Override
    public Category convert(CategoryCommand command) {
        Objects.requireNonNull(command);

        final Category category = new Category();

        category.setId(command.getId());
        category.setDescription(command.getDescription());

        return category;
    }
}
