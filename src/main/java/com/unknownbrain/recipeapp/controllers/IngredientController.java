package com.unknownbrain.recipeapp.controllers;

import com.unknownbrain.recipeapp.commands.IngredientCommand;
import com.unknownbrain.recipeapp.commands.UnitOfMeasureCommand;
import com.unknownbrain.recipeapp.services.IngredientService;
import com.unknownbrain.recipeapp.services.RecipeService;
import com.unknownbrain.recipeapp.services.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Controller
@RequiredArgsConstructor
public class IngredientController {

    public static final String RECIPE_INGREDIENT_INGREDIENT_FORM = "recipe/ingredient/ingredientform";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    private WebDataBinder webDataBinder;

    @InitBinder("ingredient")
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model) {
        log.debug("Getting ingredient list for recipe id: " + id);
        model.addAttribute("recipe", recipeService.findCommandById(id));

        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/view")
    public String viewIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        log.debug("Getting ingredient list for recipe id: " + recipeId);

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        return "recipe/ingredient/view";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {

        //need to return back parent id for hidden form property
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);

        return RECIPE_INGREDIENT_INGREDIENT_FORM;
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public Mono<String> updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

        return Mono.just(RECIPE_INGREDIENT_INGREDIENT_FORM);
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public Mono<String> saveOrUpdate(@ModelAttribute("ingredient") IngredientCommand command, @PathVariable String recipeId) throws InterruptedException {

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(log::debug);
            return Mono.just(RECIPE_INGREDIENT_INGREDIENT_FORM);
        }

        return ingredientService.saveIngredientCommand(command)
                .flatMap(ingredientCommand -> Mono.just("redirect:/recipe/" + recipeId + "/ingredient/" + ingredientCommand.getId() + "/view"));
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {

        log.debug("Deleting the ingredient id : " + ingredientId);
        ingredientService.deleteById(recipeId, ingredientId).block();

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

    @ModelAttribute("uomList")
    public Flux<UnitOfMeasureCommand> populateUomList() {
        return unitOfMeasureService.listAllUoms();
    }
}
