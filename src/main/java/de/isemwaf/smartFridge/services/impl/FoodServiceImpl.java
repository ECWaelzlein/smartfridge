package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.repositories.FoodRepository;
import de.isemwaf.smartFridge.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    public static final String OPEN_FOOD_FACTS_URL = "https://world.openfoodfacts.org/api/v0/product/";

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public Food createFood(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public List<Food> getAllFood() {
        return new ArrayList<>(foodRepository.findAll());
    }

    @Override
    public Food getFood(long id) {
        return foodRepository.findById(id).isPresent() ? foodRepository.findById(id).get() : null;
    }

    @Override
    public void deleteFood(long id) {
        foodRepository.deleteById(id);
    }

    @Override
    public String getFoodInformation(String barcode) {

        try {
            URL url = new URL(OPEN_FOOD_FACTS_URL + barcode + ".json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
