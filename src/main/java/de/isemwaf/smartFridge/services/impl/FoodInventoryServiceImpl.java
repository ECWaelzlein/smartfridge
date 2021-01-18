package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.model.json.FoodInventoryModel;
import de.isemwaf.smartFridge.repositories.FoodInventoryRepository;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import de.isemwaf.smartFridge.services.FoodService;
import de.isemwaf.smartFridge.services.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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

    @Override
    public List<FoodInventory> getSoonExpiringFood(int days, long userId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return foodInventoryRepository.findAllByExpirationDateLessThanEqualAndFridge_Account_Id(cal.getTime(), userId);
    }
}
