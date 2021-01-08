package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Recipe;
import de.isemwaf.smartFridge.model.json.IngredientList;
import de.isemwaf.smartFridge.repositories.RecipeRepository;
import de.isemwaf.smartFridge.services.RecipeService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    public static final String spoonacularRecipeURL = "https://api.spoonacular.com/recipes/";
    public static final String spoonacularAPIKey = "a1762976c3ac4015aedb6285143cee50";

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipe(long id) {
        return recipeRepository.findById(id).isPresent() ? recipeRepository.findById(id).get() : null;
    }

    @Override
    public Recipe getRecipeBasedOnIngredients(IngredientList ingredientList) {
        String spectacularQuery = spoonacularRecipeURL +
                "findByIngredients?ingredients=" +
                String.join(",", ingredientList.getStringIngredientList()) +
                "&number=1&apiKey=" +
                spoonacularAPIKey;
        try {
            String json = Utility.getJsonAnswer(spectacularQuery);
            long recipeID = Utility.getFirstRecipeId(json);
            return composeRecipeByID(recipeID);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Recipe getRandomRecipe(String tags) {
        try {
            String json = Utility.getJsonAnswer(spoonacularRecipeURL+"random?number=1&tags="+tags+"&apiKey="+spoonacularAPIKey);
            long recipeID = Utility.getFirstRandomRecipeId(json);
            return composeRecipeByID(recipeID);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Recipe composeRecipeByID(long recipeID) throws JSONException, IOException {
        String json = Utility.getJsonAnswer(spoonacularRecipeURL +recipeID+"/information?includeNutrition=true&apiKey="+ spoonacularAPIKey);
        return Utility.getRecipeInformationFromJson(json);
    }
}
