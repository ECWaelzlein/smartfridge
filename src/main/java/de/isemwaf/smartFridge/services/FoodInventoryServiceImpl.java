package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.repositories.FoodInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class FoodInventoryServiceImpl implements FoodInventoryService {

    private final FoodInventoryRepository foodInventoryRepository;
    @Autowired
    public FoodInventoryServiceImpl(FoodInventoryRepository foodInventoryRepository) {
        this.foodInventoryRepository = foodInventoryRepository;
    }

    @Override
    public int saveItem(FoodInventory foodInventory) {
        return ((int) foodInventoryRepository.save(foodInventory).getId());
    }

    @Override
    public List<FoodInventory> getItem(long id) {
        List<FoodInventory> foodInventoryList = null;
        if(id >= 0)
        {
            foodInventoryList.add(foodInventoryRepository.findById(id).get());
        }
        else
        {
            foodInventoryList.addAll(foodInventoryRepository.findAll());
        }
        return foodInventoryList;
    }

    @Override
    public int deleteItem(long id) {
        foodInventoryRepository.deleteById(id);
        return HttpServletResponse.SC_OK;
    }
}
