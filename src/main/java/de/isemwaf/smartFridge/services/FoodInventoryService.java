package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.FoodInventory;

import java.util.Optional;

public interface IFoodInventoryService {
    int saveItem(FoodInventory foodInventory);
    FoodInventory getItem(Optional<Integer> id);
    int deleteItem(int id);
}
