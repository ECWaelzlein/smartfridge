package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.services.FoodService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FoodController {

    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping(path = {"/api/food"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Food> createFoodWithBarcode(@RequestBody String barcode) {
        Food food;
        Optional<Food> foodOptional = foodService.searchForBarcode(barcode);
        if (foodOptional.isPresent()) {
            food = foodOptional.get();
        } else {
            String foodInformation = foodService.getFoodInformation(barcode);

            String name = Utility.getProductName(foodInformation);
            String quantity = Utility.getProductQuantity(foodInformation);
            String imageURL = Utility.getProductImage(foodInformation);
            food = new Food();
            if (name != null && quantity != null) {
                food.setBarcode(barcode);
                food.setName(name);
                food.setQuantity(quantity);
                food.setImageURL(imageURL);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            food = foodService.createFood(food);
        }
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @GetMapping(path = {"/api/food/{id}", "/api/food"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Food>> getFoodWithId(@PathVariable Optional<Long> id) {
        List<Food> foodList = new ArrayList<>();

        if (id.isEmpty()) {
            foodList.addAll(foodService.getAllFood());
        }

        id.ifPresent(foodId -> foodList.add(foodService.getFood(foodId)));

        if(foodList.isEmpty()) {
            return new ResponseEntity<>(foodList, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(foodList, HttpStatus.OK);
    }

    @DeleteMapping(path = {"/api/food/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Food> deleteFoodWithId(@PathVariable Long id) {
        foodService.deleteFood(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
