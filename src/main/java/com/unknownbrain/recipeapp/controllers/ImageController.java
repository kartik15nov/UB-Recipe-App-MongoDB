package com.unknownbrain.recipeapp.controllers;

import com.unknownbrain.recipeapp.commands.RecipeCommand;
import com.unknownbrain.recipeapp.services.ImageService;
import com.unknownbrain.recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    @GetMapping("/recipe/{recipeId}/image")
    public String getImageForm(@PathVariable String recipeId, Model model) {

        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();

        model.addAttribute("recipe", recipeCommand);

        return "recipe/imageuploadform";
    }

    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {

        imageService.saveImageFile(id, file);

        return String.format("redirect:/recipe/%s/view", id);
    }

//    @GetMapping("/recipe/{recipeId}/recipeimage")
//    public void renderImageFromDB(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
//
//        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();
//
//        if (recipeCommand.getImage() != null) {
//            byte[] bytes = new byte[recipeCommand.getImage().length];
//
//            int i = 0;
//            for (byte b : recipeCommand.getImage()) {
//                bytes[i++] = b;
//            }
//
//            response.setContentType("image/jpeg");
//
//            InputStream is = new ByteArrayInputStream(bytes);
//            IOUtils.copy(is, response.getOutputStream());
//        }
//    }
}
