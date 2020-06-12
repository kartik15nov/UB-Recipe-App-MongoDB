package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.commands.IngredientCommand;
import com.unknownbrain.recipeapp.converters.fromCommand.IngredientCommandToIngredient;
import com.unknownbrain.recipeapp.converters.toCommand.IngredientToIngredientCommand;
import com.unknownbrain.recipeapp.domain.Ingredient;
import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.repositories.reactive.RecipeReactiveRepository;
import com.unknownbrain.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Service
public class IngredientServiceImpl implements IngredientService {

    RecipeReactiveRepository recipeReactiveRepository;
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    IngredientToIngredientCommand ingredientToIngredientCommand;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {

        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;

        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    assert command != null;
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        Objects.requireNonNull(command);

        AtomicReference<String> ingredientId = new AtomicReference<>();
        return recipeReactiveRepository.findById(command.getRecipeId())
                .map(recipe -> {
                    recipe.getIngredients()
                            .stream()
                            .filter(ingredient -> ingredient.getId().equalsIgnoreCase(command.getId()))
                            .findFirst()
                            .map(ingredient -> {
                                ingredientId.set(command.getId());
                                ingredient.setDescription(command.getDescription());
                                ingredient.setAmount(command.getAmount());
                                return recipe;
                            })
                            .orElseGet(() -> {
                                Ingredient newIngredient = ingredientCommandToIngredient.convert(command);
                                ingredientId.set(Objects.requireNonNull(newIngredient).getId());
                                unitOfMeasureReactiveRepository
                                        .findById(command.getUom().getId())
                                        .flatMap(unitOfMeasure -> {
                                            newIngredient.setUom(unitOfMeasure);
                                            return Mono.just(unitOfMeasure);
                                        }).subscribe();
                                recipe.addIngredient(newIngredient);
                                return recipe;
                            });
//                            .ifPresentOrElse(ingredient -> {
//                                ingredientId.set(command.getId());
//                                ingredient.setDescription(command.getDescription());
//                                ingredient.setAmount(command.getAmount());
//                            }, () -> {
//
//                                Ingredient newIngredient = ingredientCommandToIngredient.convert(command);
//                                ingredientId.set(Objects.requireNonNull(newIngredient).getId());
//                                unitOfMeasureReactiveRepository
//                                        .findById(command.getUom().getId())
//                                        .flatMap(unitOfMeasure -> {
//                                            newIngredient.setUom(unitOfMeasure);
//                                            return Mono.just(unitOfMeasure);
//                                        }).subscribe();
//                                recipe.addIngredient(newIngredient);
//                            });
                    return recipe;
                })
                .flatMap(recipe -> recipeReactiveRepository.save(recipe).then(Mono.just(recipe)))
                .flatMapIterable(Recipe::getIngredients)
                .filter(savedIngredient -> savedIngredient.getId().equalsIgnoreCase(ingredientId.get()))
                .flatMap(savedIngredient -> Mono.justOrEmpty(ingredientToIngredientCommand.convert(savedIngredient)))
                .single();
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe != null) {
            Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (optionalIngredient.isPresent()) {
                log.debug("Deleting ingredient :{} from recipe :{}", ingredientId, recipeId);
                recipe.getIngredients().remove(optionalIngredient.get());
                recipeReactiveRepository.save(recipe).block();
            } else
                log.debug("Ingredient Id Not found. Id:" + ingredientId);

        } else
            log.debug("Recipe Id Not found. Id:" + recipeId);

        return Mono.empty();
    }
}
