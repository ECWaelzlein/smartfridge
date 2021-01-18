package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.model.json.FoodInventoryModel;
import de.isemwaf.smartFridge.services.AccountService;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FoodInventoryController {
    private final FoodInventoryService foodInventoryService;

    @Autowired
    public FoodInventoryController(FoodInventoryService foodInventoryService) {
        this.foodInventoryService = foodInventoryService;
    }

    @GetMapping(path = {"/api/food-inventory/{id}", "/api/food-inventory"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FoodInventory>> getFoodInventory(@PathVariable Optional<Long> id, @RequestParam String userId)
    {
        List<FoodInventory> foodInventories = new ArrayList<>();
        if(id.isPresent()) {
            foodInventories.add(foodInventoryService.getItem(id.get()));
            if(foodInventories.isEmpty())  { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            else { return new ResponseEntity<>(foodInventories,HttpStatus.OK);}
        }
        else if (!userId.isEmpty()){
            foodInventories.addAll(foodInventoryService.getAllItems(Long.parseLong(userId)));
            return new ResponseEntity<>(foodInventories, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/api/food-inventory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodInventory> saveFoodInventory(@RequestBody @Valid FoodInventoryModel foodInventoryModel, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        FoodInventory createdFoodInventory = foodInventoryService.saveItem(foodInventoryModel);
        return new ResponseEntity<>(createdFoodInventory, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/api/food-inventory/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodInventory> deleteFoodInventory(@PathVariable long id)
    {
        foodInventoryService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/api/food-inventory/expire")
    public ResponseEntity<List<FoodInventory>> getExpiringFoods(@RequestParam int days){
        if(days < 0)
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<>(foodInventoryService.getSoonExpiringFood(days, Utility.getAccountFromSecurity().getAccountId()), HttpStatus.OK);
    }
}
