package de.isemwaf.smartFridge.utility;

import de.isemwaf.smartFridge.model.Recipe;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utility {

    public static String getProductName(String productJson) {

        try {
            JSONObject jsonObject = new JSONObject(productJson).getJSONObject("product");

            String productName = jsonObject.getString("product_name_de");

            if (productName.isEmpty()) {
                productName = jsonObject.getString("brands");
            }

            return productName;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Not Found";
    }

    public static String getProductQuantity(String productJson) {

        try {
            JSONObject jsonObject = new JSONObject(productJson).getJSONObject("product");
            return jsonObject.getString("quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Not Found";
    }

    /**
     * Ruf eine Rest-API (nur GET) auf und gibt den json-String zurück.
     * @param apiCall Endpunkt, der angesprochen wird (ganze URL).
     * @return json-String (Rest-Antwort)
     * @throws IOException falls beim Verbindungsaufbau etas nicht funktioniert
     */
    public static String getJsonAnswer(URL apiCall) throws IOException {
        HttpURLConnection con = (HttpURLConnection) apiCall.openConnection();
        con.setRequestMethod("GET");

        if (con.getResponseCode() != HttpServletResponse.SC_NOT_FOUND) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            return content.toString();
        }
        return null;
    }

    /**
     * Sucht aus der spoonacular Antwort die erste Recipe-ID heraus
     * @param json spoonacular Antwort
     * @return erste Recipe-ID
     * @throws JSONException falls das parsen nicht funktioniert
     */
    public static long getFirstRecipeId(String json) throws JSONException {

        JSONObject jsonObject = new JSONArray(json).getJSONObject(0);

        return jsonObject.getLong("id");

    }

    /**
     * Sucht aus der spoonacular GETRandom-Antwort die erste Recipe-ID heraus
     * @param json spoonacular Antwort
     * @return erste Recipe-ID
     * @throws JSONException falls das parsen nicht funktioniert
     */
    public static long getFirstRandomRecipeId(String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json).getJSONArray("recipes").getJSONObject(0);

        return jsonObject.getLong("id");

    }

    /**
     * Erstellt aus der json-Antwort von spoonacular die richtigen Informationen für ein Recipe heraus.
     * @param jsonString json-Antwort von spoonacular
     * @return ein neues Recipe
     * @throws JSONException falls das parsen nicht funktioniert
     */
    public static Recipe getRecipeInformationFromJson(String jsonString) throws JSONException{
        JSONObject recipeJSON = new JSONObject(jsonString);
        JSONObject nutrition = recipeJSON.getJSONObject("nutrition");
        JSONArray nutrients = nutrition.getJSONArray("nutrients");
        Recipe recipe = new Recipe();
        for(int i = 0; i< nutrients.length();i++){
            JSONObject jsonObject = nutrients.getJSONObject(i);
            switch (jsonObject.getString("title")) {
                case "Calories" -> recipe.setCalories((float) jsonObject.getDouble("amount"));
                case "Carbohydrates" -> recipe.setCarbs((float) jsonObject.getDouble("amount"));
                case "Protein" -> recipe.setProteins((float) jsonObject.getDouble("amount"));
                case "Fat" -> recipe.setFats((float) jsonObject.getDouble("amount"));
            }
        }
        recipe.setServings((float) recipeJSON.getDouble("servings"));
        recipe.setName(recipeJSON.getString("title"));
        JSONArray extendedIngredients = recipeJSON.getJSONArray("extendedIngredients");
        JSONArray ingredientsToSave = new JSONArray();
        for (int i = 0; i < extendedIngredients.length(); i++) {
            JSONObject jsonObject = extendedIngredients.getJSONObject(i);
            JSONObject ingredient = new JSONObject();
            ingredient.put("name", jsonObject.getString("name"));
            JSONObject measures = jsonObject.getJSONObject("measures").getJSONObject("metric");
            ingredient.put("amount", measures.getDouble("amount"));
            ingredient.put("unit", measures.getString("unitShort"));
            ingredientsToSave.put(ingredient);
        }
        recipe.setIngredients(ingredientsToSave.toString());
        return recipe;

    }

    /**
     * Füllt die steps in recipe mit denen aus dem json-String
     * @param json Json, der von spoonacular zurückgegeben wurde
     * @param recipe Recipe, dass bearbeitet werden soll
     * @return gibt das bearbeitete Recipe zurück
     * @throws JSONException falls das parsen nicht funktioniert
     */
    public static Recipe addStepsToRecipe(String json, Recipe recipe) throws JSONException{
        if(recipe != null) recipe.setSteps(json);
        return recipe;
    }
}
