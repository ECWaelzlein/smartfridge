package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Food;

import java.util.List;

public interface FoodService {
    Food createFood(Food food);

    Food getFood(long id);

    List<Food> getAllFood();

    void deleteFood(long id);

    String getFoodInformation(String barcode);
}
