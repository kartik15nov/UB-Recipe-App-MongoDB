package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.domain.Recipe;
import com.unknownbrain.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;

@Log4j2
@Service
public class ImageServiceImpl implements ImageService {

    RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {
        log.debug("Received a file");

        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    try {
                        Byte[] bytes = new Byte[file.getBytes().length];

                        int i = 0;
                        for (byte b : file.getBytes()) {
                            bytes[i++] = b;
                        }

                        recipe.setImage(bytes);
                    } catch (IOException e) {
                        log.error(e);
                    }
                    return recipe;
                });
        recipeReactiveRepository.save(Objects.requireNonNull(recipeMono.block())).block();
        return Mono.empty();
    }
}
