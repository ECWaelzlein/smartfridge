package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Recipe;
import de.isemwaf.smartFridge.model.json.IngredientList;
import de.isemwaf.smartFridge.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(path = "/{id}")
    public Recipe getRecipe(@PathVariable String id, HttpServletResponse httpServletResponse) {
        Recipe recipe = recipeService.getRecipe(Long.parseLong(id));
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        if (recipe == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return recipe;
    }

    @PostMapping(path = "/")
    public Recipe createRecipeBasedOnIngredients(@RequestBody IngredientList ingredientList, HttpServletResponse httpServletResponse) {
        //search for recipes based on the ingredients and return 1 recipe
        Recipe recipe = new Recipe();
        recipe = recipeService.saveRecipe(recipe);

        httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        return recipe;
    }

    @PostMapping(path = "/random")
    public Recipe createRecipeBasedOnIngredients(@RequestParam List<String> tags, HttpServletResponse httpServletResponse) {
        //search for a random recipe based on the tags and return 1 recipe
        Recipe recipe = new Recipe();
        recipe = recipeService.saveRecipe(recipe);

        httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        return recipe;
    }
}
