package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.repositories.FoodInventoryRepository;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodInventoryServiceImpl implements FoodInventoryService {
    private final FoodInventoryRepository foodInventoryRepository;

    @Autowired
    public FoodInventoryServiceImpl(FoodInventoryRepository foodInventoryRepository)
    {
        this.foodInventoryRepository = foodInventoryRepository;
    }

    @Override
    public int saveItem(FoodInventory foodInventory) {
        return 0;
    }
    @Override
    public List<FoodInventory> getItem(int id) {
        if(id == -1)
        {

        }
        else {

        }
        return null;
    }

    @Override
    public int deleteItem(int id) {
       return 0;
    }
}
