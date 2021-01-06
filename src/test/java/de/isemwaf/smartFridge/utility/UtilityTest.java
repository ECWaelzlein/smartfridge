package de.isemwaf.smartFridge.utility;

import de.isemwaf.smartFridge.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @BeforeEach
    void setUp() {
    }
    /*
    @Test
    void getProductName() {
    }

    @Test
    void getProductQuantity() {
    }

    @Test
    void getJsonAnswer() {
    }

    @Test
    void getFirstRecipeId() {
    }
    */

    /**
     * Testet ob der json-String richtig in ein Rezept umgewandelt wird.
     */
    @Test
    void getRecipeInformationFromJson() {
        Path path = Paths.get("src/test/resources/jsonSpoonacularAnswer.txt");
        try {
            String read = Files.lines(path).collect(Collectors.joining());
            Recipe recipe = Utility.getRecipeInformationFromJson(read);
            //Delta gibt wie stark sich die Werte unterscheiden können.
            assertEquals(recipe.getFats(), 19.83, 0.01);
            assertEquals(recipe.getCalories(), 584.46, 0.01);
            assertEquals(recipe.getCarbs(), 83.71, 0.01);
            assertEquals(recipe.getProteins(), 18.98, 0.01);
            assertEquals(recipe.getName(), "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs");
        }catch (IOException | JSONException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }

    }
}