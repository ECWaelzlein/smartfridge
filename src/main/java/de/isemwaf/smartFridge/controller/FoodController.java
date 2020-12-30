package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.services.FoodService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class FoodController {

    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    // TODO BindingResult als Parameter übergeben?
    @PostMapping(path = {"/api/food"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Food createFoodWithBarcode(@RequestBody String barcode, HttpServletResponse httpServletResponse) {
        String foodInformation = foodService.getFoodInformation(barcode);

        String name = Utility.getProductName(foodInformation);
        String quantity = Utility.getQuantity(foodInformation);

        Food food = new Food();
        food.setBarcode(barcode);
        food.setName(name);
        food.setQuantity(quantity);

        food = foodService.createFood(food);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        return food;
    }

    @GetMapping(path = {"/api/food/{id}", "/api/food"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Food> getFoodWithId(@PathVariable Optional<Long> id, HttpServletResponse httpServletResponse) {

        long id_food = id.isPresent() ? id.get() : -1;
        List<Food> foodList = foodService.getFood(id_food);
        if (foodList.isEmpty()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }

        return foodList;
    }

    @DeleteMapping(path = {"/api/food/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteFoodWithId(@PathVariable Long id, HttpServletResponse httpServletResponse) {

        int response = foodService.deleteFood(id);
        httpServletResponse.setStatus(response);
    }
}
