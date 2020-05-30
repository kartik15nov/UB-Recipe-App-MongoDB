package com.unknownbrain.recipeapp.repositories.reactive;

import com.unknownbrain.recipeapp.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class CategoryReactiveRepositoryIT {

    public static final String DESCRIPTION = "Blah";

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @BeforeEach
    void setUp() {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    void save() {
        //given
        Category category = new Category();
        category.setDescription(DESCRIPTION);

        //when
        categoryReactiveRepository.save(category).block();

        //then
        Long count = categoryReactiveRepository.count().block();
        assertEquals(1L, count);
    }

    @Test
    void findByDescription() {
        //given
        Category category = new Category();
        category.setDescription(DESCRIPTION);
        categoryReactiveRepository.save(category).block();

        //when
        Category fetchedCategory = categoryReactiveRepository.findByDescription(DESCRIPTION).block();

        //then
        assertNotNull(fetchedCategory);
        assertNotNull(fetchedCategory.getId());
    }
}