package com.unknownbrain.recipeapp.services;

import com.unknownbrain.recipeapp.models.Recipe;
import com.unknownbrain.recipeapp.repositories.RecipeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Service
public class ImageServiceImpl implements ImageService {

    RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(String recipeId, MultipartFile file) {
        log.debug("Received a file");

        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        optionalRecipe.ifPresent(recipe -> {
            try {
                Byte[] bytes = new Byte[file.getBytes().length];

                int i = 0;
                for (byte b : file.getBytes()) {
                    bytes[i++] = b;
                }

                recipe.setImage(bytes);

                recipeRepository.save(recipe);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
