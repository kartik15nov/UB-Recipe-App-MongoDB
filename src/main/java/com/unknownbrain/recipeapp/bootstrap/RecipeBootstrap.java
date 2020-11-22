package com.unknownbrain.recipeapp.bootstrap;

import com.unknownbrain.recipeapp.domain.*;
import com.unknownbrain.recipeapp.repositories.CategoryRepository;
import com.unknownbrain.recipeapp.repositories.RecipeRepository;
import com.unknownbrain.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        log.debug("Loading Bootstrap Data..");

        if (categoryRepository.count() == 0L) {
            log.debug("Loading Categories");
            loadCategories();
        }

        if (unitOfMeasureRepository.count() == 0L) {
            log.debug("Loading UOMs");
            loadUom();
        }

        if (recipeRepository.count() == 0L) {
            log.debug("Loading Recipes");
            recipeRepository.saveAll(getRecipes());
        }
    }

    private void loadCategories() {
        Category cat1 = new Category();
        cat1.setDescription("American");
        categoryRepository.save(cat1);

        Category cat2 = new Category();
        cat2.setDescription("Italian");
        categoryRepository.save(cat2);

        Category cat3 = new Category();
        cat3.setDescription("Mexican");
        categoryRepository.save(cat3);

        Category cat4 = new Category();
        cat4.setDescription("Fast Food");
        categoryRepository.save(cat4);
    }

    private void loadUom() {
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setDescription("TeaSpoon");
        unitOfMeasureRepository.save(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setDescription("TableSpoon");
        unitOfMeasureRepository.save(uom2);

        UnitOfMeasure uom3 = new UnitOfMeasure();
        uom3.setDescription("Cup");
        unitOfMeasureRepository.save(uom3);

        UnitOfMeasure uom4 = new UnitOfMeasure();
        uom4.setDescription("Pinch");
        unitOfMeasureRepository.save(uom4);

        UnitOfMeasure uom5 = new UnitOfMeasure();
        uom5.setDescription("Ounce");
        unitOfMeasureRepository.save(uom5);

        UnitOfMeasure uom6 = new UnitOfMeasure();
        uom6.setDescription("Each");
        unitOfMeasureRepository.save(uom6);

        UnitOfMeasure uom7 = new UnitOfMeasure();
        uom7.setDescription("Pint");
        unitOfMeasureRepository.save(uom7);

        UnitOfMeasure uom8 = new UnitOfMeasure();
        uom8.setDescription("Dash");
        unitOfMeasureRepository.save(uom8);
    }

    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>(2);

        Recipe guacamoleRecipe = new Recipe();
        Recipe tacosRecipe = new Recipe();

        // Prepare the Guacamole recipe
        guacamoleRecipe.setDescription("Perfect Guacamole");
        guacamoleRecipe.setPrepTime(10);
        guacamoleRecipe.setCookTime(50);
        guacamoleRecipe.setDifficulty(Difficulty.EASY);

        guacamoleRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), getUOM("Each")));
        guacamoleRecipe.addIngredient(new Ingredient("Kosher salt", new BigDecimal(".5"), getUOM("TeaSpoon")));
        guacamoleRecipe.addIngredient(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(2), getUOM("TableSpoon")));
        guacamoleRecipe.addIngredient(new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal(2), getUOM("TableSpoon")));
        guacamoleRecipe.addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(2), getUOM("TableSpoon")));
        guacamoleRecipe.addIngredient(new Ingredient("Cilantro", new BigDecimal(2), getUOM("TableSpoon")));
        guacamoleRecipe.addIngredient(new Ingredient("freshly grated black pepper", new BigDecimal(2), getUOM("Dash")));
        guacamoleRecipe.addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal(".5"), getUOM("Each")));

        guacamoleRecipe.getCategories().add(getCategory("American"));
        guacamoleRecipe.getCategories().add(getCategory("Mexican"));

        guacamoleRecipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air rEaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvpiV9Sd");
        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes("For a very quick guacamole just take a 1/4 Cup of salsa and mix it in with your mashed avocados.\n" +
                "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of pEaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries.\n" +
                "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability of other ingredients stop you from making guacamole.\n" +
                "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvoun5ws");
        guacamoleRecipe.setNotes(guacamoleNotes);
        recipes.add(guacamoleRecipe);


        // Prepare the Tacos recipe
        tacosRecipe.setDescription("Spicy Grilled Chicken Taco");
        tacosRecipe.setCookTime(9);
        tacosRecipe.setPrepTime(20);
        tacosRecipe.setDifficulty(Difficulty.MODERATE);

        tacosRecipe.addIngredient(new Ingredient("Ancho Chili Powder", new BigDecimal(2), getUOM("TableSpoon")));
        tacosRecipe.addIngredient(new Ingredient("Dried Oregano", new BigDecimal(1), getUOM("TeaSpoon")));
        tacosRecipe.addIngredient(new Ingredient("Dried Cumin", new BigDecimal(1), getUOM("TeaSpoon")));
        tacosRecipe.addIngredient(new Ingredient("Sugar", new BigDecimal(1), getUOM("TeaSpoon")));
        tacosRecipe.addIngredient(new Ingredient("Salt", new BigDecimal(".5"), getUOM("TeaSpoon")));
        tacosRecipe.addIngredient(new Ingredient("Clove of Garlic, Choppedr", new BigDecimal(1), getUOM("Each")));
        tacosRecipe.addIngredient(new Ingredient("finely grated orange zestr", new BigDecimal(1), getUOM("TableSpoon")));
        tacosRecipe.addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), getUOM("TableSpoon")));
        tacosRecipe.addIngredient(new Ingredient("Olive Oil", new BigDecimal(2), getUOM("TableSpoon")));
        tacosRecipe.addIngredient(new Ingredient("boneless chicken thighs", new BigDecimal(4), getUOM("TableSpoon")));
        tacosRecipe.addIngredient(new Ingredient("small corn tortillasr", new BigDecimal(8), getUOM("Each")));
        tacosRecipe.addIngredient(new Ingredient("packed baby arugula", new BigDecimal(3), getUOM("Cup")));
        tacosRecipe.addIngredient(new Ingredient("medium ripe avocados, slic", new BigDecimal(2), getUOM("Each")));
        tacosRecipe.addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4), getUOM("Each")));
        tacosRecipe.addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(".5"), getUOM("Pint")));
        tacosRecipe.addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(".25"), getUOM("Each")));
        tacosRecipe.addIngredient(new Ingredient("Roughly chopped cilantro", new BigDecimal(4), getUOM("Each")));
        tacosRecipe.addIngredient(new Ingredient("Cup sour cream thinned with 1/4 Cup milk", new BigDecimal(4), getUOM("Cup")));
        tacosRecipe.addIngredient(new Ingredient("lime, cut into wedges", new BigDecimal(4), getUOM("Each")));

        tacosRecipe.getCategories().add(getCategory("American"));
        tacosRecipe.getCategories().add(getCategory("Mexican"));

        tacosRecipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "\n" +
                "\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "4 Warm the tortillas: Place Each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On Each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvtrAnNm");

        Notes tacoNotes = new Notes();
        tacoNotes.setRecipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                "\n" +
                "\n" +
                "Read more: http://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvu7Q0MJ");
        tacosRecipe.setNotes(tacoNotes);
        recipes.add(tacosRecipe);

        return recipes;
    }

    private UnitOfMeasure getUOM(String description) {
        Objects.requireNonNull(description);

        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription(description);

        return unitOfMeasureOptional.orElseThrow(RuntimeException::new);
    }

    private Category getCategory(String description) {
        Objects.requireNonNull(description);

        Optional<Category> categoryOptional = categoryRepository.findByDescription(description);

        return categoryOptional.orElseThrow(RuntimeException::new);
    }
}
