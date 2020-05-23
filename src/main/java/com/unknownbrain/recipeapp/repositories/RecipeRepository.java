package com.unknownbrain.recipeapp.repositories;

import com.unknownbrain.recipeapp.models.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
