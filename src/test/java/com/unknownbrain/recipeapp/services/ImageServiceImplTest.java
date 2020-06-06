package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImageServiceImplTest {

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeReactiveRepository);
    }

    @Test
    void saveImageFile() throws IOException {

        //given
        String id = "10";

        Recipe recipe = new Recipe();
        recipe.setId(id);

        MultipartFile file = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                "Spring Framework Guru".getBytes());

        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        imageService.saveImageFile(id, file);

        //then
        verify(recipeReactiveRepository).save(argumentCaptor.capture());

        Recipe savedRecipe = argumentCaptor.getValue();

        assertEquals(file.getBytes().length, savedRecipe.getImage().length);
    }
}