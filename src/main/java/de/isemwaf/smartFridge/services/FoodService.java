package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Food;

import java.util.List;
import java.util.Optional;

public interface FoodService {
    Food createFood(Food food);

    Food getFood(long id);

    Optional<Food> searchForBarcode(String barcode);

    List<Food> getAllFood();

    void deleteFood(long id);

    String getFoodInformation(String barcode);
}
