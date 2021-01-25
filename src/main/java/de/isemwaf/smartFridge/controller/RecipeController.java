package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Recipe;
import de.isemwaf.smartFridge.model.json.IngredientList;
import de.isemwaf.smartFridge.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping(path = "/api/recipe/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> getRecipe(@PathVariable String id) {
        Recipe recipe = recipeService.getRecipe(Long.parseLong(id));

        if (recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @PostMapping(path = "/api/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> createRecipeBasedOnIngredients(@RequestBody IngredientList ingredientList) {
        //search for recipes based on the ingredients and return 1 recipe

        Recipe recipe = recipeService.getRecipeBasedOnIngredients(ingredientList);
        if(recipe == null){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        recipe = recipeService.saveRecipe(recipe);

        return new ResponseEntity<>(recipe, HttpStatus.CREATED);
    }

    @PostMapping(path = "/api/recipe/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> createRecipeBasedOnIngredients(@RequestParam List<String> tags) {
        //search for a random recipe based on the tags and return 1 recipe
        Recipe recipe =  recipeService.getRandomRecipe(String.join(",", tags));
        if(recipe == null){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        recipe = recipeService.saveRecipe(recipe);

        return new ResponseEntity<>(recipe, HttpStatus.CREATED);
    }
}
