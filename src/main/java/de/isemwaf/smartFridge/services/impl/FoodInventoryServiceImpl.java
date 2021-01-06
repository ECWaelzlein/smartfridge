package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.model.json.FoodInventoryModel;
import de.isemwaf.smartFridge.repositories.FoodInventoryRepository;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import de.isemwaf.smartFridge.services.FoodService;
import de.isemwaf.smartFridge.services.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodInventoryServiceImpl implements FoodInventoryService {

    private final FoodInventoryRepository foodInventoryRepository;
    private final FridgeService fridgeService;
    private final FoodService foodService;

    @Autowired
    public FoodInventoryServiceImpl(FoodInventoryRepository foodInventoryRepository, FridgeService fridgeService, FoodService foodService) {
        this.foodInventoryRepository = foodInventoryRepository;
        this.fridgeService = fridgeService;
        this.foodService = foodService;
    }

    @Override
    public FoodInventory saveItem(FoodInventoryModel foodInventoryModel) {
        FoodInventory foodInventory = new FoodInventory();

        foodInventory.setFood(foodService.getFood(Long.parseLong(foodInventoryModel.getFoodId())));
        foodInventory.setFridge(fridgeService.getFridgeByAccountId(Long.parseLong(foodInventoryModel.getUserId())));
        foodInventory.setExpirationDate(foodInventoryModel.getExpirationDate());

        return foodInventoryRepository.save(foodInventory);
    }

    @Override
    public FoodInventory getItem(long id) {
        return foodInventoryRepository.findById(id).isPresent() ? foodInventoryRepository.findById(id).get() : null;
    }

    @Override
    public void deleteItem(long id) {
        foodInventoryRepository.deleteById(id);
    }

    @Override
    public List<FoodInventory> getAllItems(long userId) {
        return foodInventoryRepository.findAllByFridge_Id(fridgeService.getFridgeByAccountId(userId).getId());
    }
}
