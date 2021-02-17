package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Meal;
import de.isemwaf.smartFridge.services.MealService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShoppingListController {

    private final MealService mealService;

    @Autowired
    public ShoppingListController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping(path = {"/api/shopping-list"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(cacheNames = "cache", cacheManager = "cacheManager")
    public ResponseEntity<List<String>> getShoppingList() {
        long userId = Utility.getAccountFromSecurity().getAccountId();
        List<Meal> mealList = mealService.fetchAllMeals(userId);
        List<String> shoppingList = new ArrayList<>();
        for (Meal meal : mealList) {
            shoppingList.add(meal.getRecipe().getIngredients());
        }
        return new ResponseEntity<>(shoppingList, HttpStatus.OK);
    }

}
