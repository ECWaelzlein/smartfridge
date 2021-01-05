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

    public static long getFirstRecipeId(String json) throws JSONException {

        JSONObject jsonObject = new JSONArray(json).getJSONObject(0);

        return jsonObject.getLong("id");

    }

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
        }
        return recipe;

    }
}
