package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Recipe;
import de.isemwaf.smartFridge.model.json.IngredientList;
import de.isemwaf.smartFridge.repositories.RecipeRepository;
import de.isemwaf.smartFridge.services.RecipeService;
import de.isemwaf.smartFridge.utility.Utility;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    public static final String spoonacularRecipeURL = "https://api.spoonacular.com/recipes/";
    public static final String spoonacularApiPassphrase = "a1762976c3ac4015aedb6285143cee50";

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
    @Bulkhead(name = "recipeService", fallbackMethod = "getDefaultRecipeBasedOnIngredients")
    public Recipe getRecipeBasedOnIngredients(IngredientList ingredientList) {
        String spectacularQuery = spoonacularRecipeURL +
                "findByIngredients?ingredients=" +
                String.join(",", ingredientList.getStringIngredientList()) +
                "&number=1&apiKey=" +
                spoonacularApiPassphrase;
        try {
            String json = Utility.getJsonAnswer(spectacularQuery);
            long recipeID = Utility.getFirstRecipeId(json);
            String json2 = Utility.getJsonAnswer(spoonacularRecipeURL +recipeID+"/information?includeNutrition=true&apiKey="+ spoonacularApiPassphrase);
            return Utility.getRecipeInformationFromJson(json2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Recipe getDefaultRecipeBasedOnIngredients(IngredientList ingredientList, Throwable throwable) {
        Recipe recipe = new Recipe();

        recipe.setName("No recipe found!");

        return recipe;
    }

    private Recipe getDefaultRecipe(String tags, Throwable throwable) {
        Recipe recipe = new Recipe();

        recipe.setName("No recipe found!");

        return recipe;
    }

    @Override
    @Bulkhead(name = "recipeService", fallbackMethod = "getDefaultRecipe")
    public Recipe getRandomRecipe(String tags) {
        try {
            String json = Utility.getJsonAnswer(spoonacularRecipeURL+"random?number=1&tags="+tags+"&apiKey="+ spoonacularApiPassphrase);
            long recipeID = Utility.getFirstRandomRecipeId(json);
            String json2 = Utility.getJsonAnswer(spoonacularRecipeURL +recipeID+"/information?includeNutrition=true&apiKey="+ spoonacularApiPassphrase);
            return Utility.getRecipeInformationFromJson(json2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*private Recipe composeRecipeByID(long recipeID) throws JSONException, IOException {
        String json = Utility.getJsonAnswer(spoonacularRecipeURL +recipeID+"/information?includeNutrition=true&apiKey="+ spoonacularApiPassphrase);
        return Utility.getRecipeInformationFromJson(json);
    }*/
}
