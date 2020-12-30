package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.repositories.FoodInventoryRepository;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
    public int saveItem(FoodInventory foodInventory) {
        return ((int) foodInventoryRepository.save(foodInventory).getId());
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
    public int deleteItem(long id) {
        foodInventoryRepository.deleteById(id);
        return HttpServletResponse.SC_OK;
    }
}
