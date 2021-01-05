package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Recipe;
import de.isemwaf.smartFridge.model.json.IngredientList;
import de.isemwaf.smartFridge.repositories.RecipeRepository;
import de.isemwaf.smartFridge.services.RecipeService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URL;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    public static final String sponacularRecipeURL = "https://api.spoonacular.com/recipes/";
    public static final String sponacularAPIKey = "a1762976c3ac4015aedb6285143cee50";

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
        StringBuilder sb = new StringBuilder();
        sb.append(sponacularRecipeURL);
        sb.append("findByIngredients?ingredients=");
        sb.append(ingredientList.getIngredientList().get(0).getIngredient());
        ingredientList.getIngredientList().remove(0);
        if (!ingredientList.getIngredientList().isEmpty()){
            ingredientList.getIngredientList().forEach(s -> sb.append(s.getIngredient()));
        }
        sb.append("&number=1&apiKey=");
        sb.append(sponacularAPIKey);
        String spectacularQuery = sb.toString();
        try {
            String json = Utility.getJsonAnswer(new URL(spectacularQuery));
            long recipeID = Utility.getFirstRecipeId(json);
            json = Utility.getJsonAnswer(new URL(sponacularRecipeURL+recipeID+"/information?includeNutrition=true&apiKey="+sponacularAPIKey));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
