package com.unknownbrain.recipeapp.repositories.reactive;

import com.unknownbrain.recipeapp.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class RecipeReactiveRepositoryIT {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @BeforeEach
    void setUp() {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    void save() {
        //given
        Recipe recipe = new Recipe();

        //when
        recipeReactiveRepository.save(recipe).block();

        //then
        Long count = recipeReactiveRepository.count().block();
        assertEquals(1L, count);
    }
}