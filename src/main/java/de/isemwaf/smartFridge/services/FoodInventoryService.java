package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.model.FoodInventory;

import java.util.List;

public interface FoodInventoryService {
    Food saveItem(FoodInventory foodInventory);
    List<FoodInventory> getItem(long id);
    long deleteItem(long id);
}
