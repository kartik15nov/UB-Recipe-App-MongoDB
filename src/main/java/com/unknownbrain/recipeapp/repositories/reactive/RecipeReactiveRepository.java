package com.unknownbrain.recipeapp.repositories.reactive;

import com.unknownbrain.recipeapp.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
