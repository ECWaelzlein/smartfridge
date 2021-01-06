package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.model.json.FoodInventoryModel;

import java.util.List;

public interface FoodInventoryService {
    FoodInventory saveItem(FoodInventoryModel foodInventoryModel);
    FoodInventory getItem(long id);
    void deleteItem(long id);
    List<FoodInventory> getAllItems(long userId);
}
