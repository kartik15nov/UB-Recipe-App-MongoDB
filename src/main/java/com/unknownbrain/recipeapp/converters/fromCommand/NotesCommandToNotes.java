package com.unknownbrain.recipeapp.converters.fromCommand;

import com.unknownbrain.recipeapp.commands.NotesCommand;
import com.unknownbrain.recipeapp.models.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Override
    public Notes convert(NotesCommand command) {
        Objects.requireNonNull(command);

        final Notes notes = new Notes();

        notes.setId(command.getId());
        notes.setRecipeNotes(command.getRecipeNotes());

        return notes;
    }
}
