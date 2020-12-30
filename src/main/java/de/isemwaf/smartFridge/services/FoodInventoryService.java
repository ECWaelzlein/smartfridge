package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.FoodInventory;

import java.util.List;

public interface FoodInventoryService {
    int saveItem(FoodInventory foodInventory);
    List<FoodInventory> getItem(long id);
    int deleteItem(long id);
}
