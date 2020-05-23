package com.unknownbrain.recipeapp.converters.toCommand;

import com.unknownbrain.recipeapp.commands.NotesCommand;
import com.unknownbrain.recipeapp.models.Notes;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NotesToNotesCommand implements Converter<Notes, NotesCommand> {

    @Override
    public NotesCommand convert(Notes notes) {
        Objects.requireNonNull(notes);

        final NotesCommand command = new NotesCommand();

        command.setId(notes.getId());
        command.setRecipeNotes(notes.getRecipeNotes());

        return command;
    }
}
