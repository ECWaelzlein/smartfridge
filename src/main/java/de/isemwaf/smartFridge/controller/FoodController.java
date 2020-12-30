package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.enums.Category;
import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.services.FoodService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class FoodController {

    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping(path = {"/api/food"})
    @ResponseBody
    public long createFoodWithBarcode(String barcode, HttpServletResponse httpServletResponse) {
        String foodInformation = foodService.getFoodInformation(barcode);

        String name = Utility.getProductName(foodInformation);
        String quantity = "0"; //TODO
        Category category = Category.MEAT; //TODO

        Food food = new Food();
        food.setBarcode(barcode);
        food.setName(name);
        food.setQuantity(quantity);
        food.setCategory(category);

        foodService.createFood(food);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return food.getId(); //TODO
    }

    @GetMapping(path = {"/api/food/{id}"})
    @ResponseBody
    public List<Food> getFoodWithId(@PathVariable Long id, HttpServletResponse httpServletResponse) {


        List<Food> foodList = foodService.getFood(id);
        if (foodList.isEmpty()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }

        return foodList;
    }

    @GetMapping(path = {"/api/food/{id}"})
    @ResponseBody
    public void deleteFoodWithId(@PathVariable Long id, HttpServletResponse httpServletResponse) {

        int response = foodService.deleteFood(id);
        httpServletResponse.setStatus(response);
    }
}
