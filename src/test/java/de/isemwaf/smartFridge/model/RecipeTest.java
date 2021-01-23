package de.isemwaf.smartFridge.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


public class RecipeTest {
    @Test
    public void testRecipe() {
        Recipe recipe = new Recipe();

        long id = 1;
        float calories = 244;
        float servings = 1.5F;
        float carbs = 30;
        String ingredients = "\"ingredient\": \"apple\"";
        String name = "Apple";
        float proteins = 10;
        float fats = 2;

        recipe.setId(id);
        recipe.setCalories(calories);
        recipe.setCarbs(carbs);
        recipe.setIngredients(ingredients);
        recipe.setName(name);
        recipe.setProteins(proteins);
        recipe.setFats(fats);
        recipe.setServings(servings);

        Assertions.assertEquals(id, recipe.getId());
        Assertions.assertEquals(calories, recipe.getCalories());
        Assertions.assertEquals(servings, recipe.getServings());
        Assertions.assertEquals(carbs, recipe.getCarbs());
        Assertions.assertEquals(ingredients, recipe.getIngredients());
        Assertions.assertEquals(proteins, recipe.getProteins());
        Assertions.assertEquals(fats, recipe.getFats());
    }
}
