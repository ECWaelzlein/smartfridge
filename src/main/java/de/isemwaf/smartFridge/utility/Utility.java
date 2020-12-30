package de.isemwaf.smartFridge.utility;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class Utility {

    public static String getProductName(String productJson) {

        try {
            JSONObject jsonObject = new JSONObject(productJson).getJSONObject("product");
            return jsonObject.getString("product_name_de"); //TODO If Abfrage, ob der name nicht gefunden wurde -> 'brands' verwenden
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Not Found";
    }

    public static String getQuantity(String productJson) {

        try {
            JSONObject jsonObject = new JSONObject(productJson).getJSONObject("product");
            return jsonObject.getString("quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Not Found";
    }
}
