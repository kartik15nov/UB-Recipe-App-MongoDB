package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.RecipeCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.RecipeCommandToRecipe;
import com.unknownbrain.recipeapp.converters.toCommand.RecipeToRecipeCommand;
import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("I am in the Service!");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {
        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return recipeReactiveRepository
                .findById(id)
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        Objects.requireNonNull(recipeCommand);

        return recipeReactiveRepository.
                save(Objects.requireNonNull(recipeCommandToRecipe.convert(recipeCommand)))
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return recipeReactiveRepository.deleteById(id);
    }
}
