package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.UnitOfMeasureCommand;

import java.util.List;

public interface UnitOfMeasureService {
    List<UnitOfMeasureCommand> listAllUoms();
}
