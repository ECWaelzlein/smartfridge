package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.services.FoodService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Food> createFoodWithBarcode(@RequestBody String barcode, HttpServletResponse httpServletResponse) {
        String foodInformation = foodService.getFoodInformation(barcode);

        String name = Utility.getProductName(foodInformation);
        String quantity = Utility.getProductQuantity(foodInformation);

        Food food = new Food();
        food.setBarcode(barcode);
        food.setName(name);
        food.setQuantity(quantity);

        food = foodService.createFood(food);

        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @GetMapping(path = {"/api/food/{id}", "/api/food"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Food>> getFoodWithId(@PathVariable Optional<Long> id, HttpServletResponse httpServletResponse) {

        long id_food = id.isPresent() ? id.get() : -1;
        List<Food> foodList = foodService.getFood(id_food);
        if (foodList.isEmpty()) {
            return new ResponseEntity<>(foodList, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(foodList, HttpStatus.OK);
    }

    @DeleteMapping(path = {"/api/food/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpServletResponse> deleteFoodWithId(@PathVariable Long id, HttpServletResponse httpServletResponse) {

        int response = foodService.deleteFood(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
