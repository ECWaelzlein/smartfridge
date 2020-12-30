package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Food;

import java.util.List;

public interface FoodService {
    long createFood(Food food);

    List<Food> getFood(long id);

    int deleteFood(long id);

    String getFoodInformation(String barcode);
}
