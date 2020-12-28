package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.FoodInventory;

import java.util.List;
import java.util.Optional;

public interface FoodInventoryService {
    int saveItem(FoodInventory foodInventory);
    List<FoodInventory> getItem(int id);
    int deleteItem(int id);
}
