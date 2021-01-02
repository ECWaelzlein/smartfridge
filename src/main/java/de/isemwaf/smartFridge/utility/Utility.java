package de.isemwaf.smartFridge.utility;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

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
}
