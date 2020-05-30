package com.unknownbrain.recipeapp.repositories.reactive;

import com.unknownbrain.recipeapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
class UnitOfMeasureReactiveRepositoryIT {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @BeforeEach
    void setUp() {
        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    void save() {
        //given
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();

        //when
        unitOfMeasureReactiveRepository.save(unitOfMeasure).block();

        //then
        Long count = unitOfMeasureReactiveRepository.count().block();
        assertEquals(1L, count);
    }

    @Test
    void findByDescription() {
        //given
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription("Each");
        unitOfMeasureReactiveRepository.save(unitOfMeasure).block();

        //when
        UnitOfMeasure fetchedUnitOfMeasure = unitOfMeasureReactiveRepository.findByDescription("Each").block();

        //then
        assertNotNull(fetchedUnitOfMeasure);
        assertNotNull(fetchedUnitOfMeasure.getId());
    }
}