package com.unknownbrain.recipeapp.controllers;

import com.unknownbrain.recipeapp.commands.RecipeCommand;
import com.unknownbrain.recipeapp.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequiredArgsConstructor
public class RecipeController {

    private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";

    private final RecipeService recipeService;

    private WebDataBinder webDataBinder;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe/{id}/view")
    public String showById(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));

        return "recipe/view";
    }

    @GetMapping("/recipe/new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_RECIPEFORM_URL;
    }

    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));

        return RECIPE_RECIPEFORM_URL;
    }

    @PostMapping("/recipe")
    public String saveOrUpdate(@ModelAttribute("recipe") RecipeCommand recipeCommand) {
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(log::debug);

            return RECIPE_RECIPEFORM_URL;
        }

        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand).block();

        return "redirect:/recipe/" + savedRecipeCommand.getId() + "/view";
    }

    @GetMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id) {
        log.debug("Deleting id : " + id);

        recipeService.deleteById(id).block();

        return "redirect:/";
    }
}
