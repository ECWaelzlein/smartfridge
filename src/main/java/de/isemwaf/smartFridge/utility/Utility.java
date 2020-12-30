package de.isemwaf.smartFridge.utility;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class Utility {

    public static String getProductName(String productJson) {

        try {
            JSONObject jsonObject = new JSONObject(productJson);
            return jsonObject.getString("agribalyse_food_name_en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Not Found";
    }
}
