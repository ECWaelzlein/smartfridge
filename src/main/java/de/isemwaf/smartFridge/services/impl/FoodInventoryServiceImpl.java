package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.repositories.FoodInventoryRepository;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FoodInventoryServiceImpl implements FoodInventoryService {

    private final FoodInventoryRepository foodInventoryRepository;
    @Autowired
    public FoodInventoryServiceImpl(FoodInventoryRepository foodInventoryRepository) {
        this.foodInventoryRepository = foodInventoryRepository;
    }

    @Override
    public Food saveItem(FoodInventory foodInventory) {
        return foodInventoryRepository.save(foodInventory).getFood();
    }

    @Override
    public List<FoodInventory> getItem(long id) {
        List<FoodInventory> foodInventoryList = new ArrayList<>();
        if(id >= 0)
        {
            Optional<FoodInventory> foodInventory = foodInventoryRepository.findById(id);
            foodInventory.ifPresent(foodInventoryList::add);
        }
        else
        {
            foodInventoryList.addAll(foodInventoryRepository.findAll());
        }
        return foodInventoryList;
    }

    @Override
    public long deleteItem(long id) {
        try {
            foodInventoryRepository.deleteById(id);
            return id;
        }
        catch(Exception e){
         return -1;
        }
    }
}
