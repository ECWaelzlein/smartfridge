package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController("/api/foodinventory")
public class FoodInventoryController {
    private final FoodInventoryService foodInventoryService;
    @Autowired
    public FoodInventoryController(FoodInventoryService foodInventoryService) {
        this.foodInventoryService = foodInventoryService;
    }
    @GetMapping(path = {"/{id}", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FoodInventory>> getFoodInventory(@PathVariable Optional<Long> id)
    {
        List<FoodInventory> foodInventories = new ArrayList<>();
        if(id.isPresent()) {
            foodInventories.addAll(foodInventoryService.getItem(id.get()));
            if(foodInventories.isEmpty())  { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            else { return new ResponseEntity<>(foodInventories,HttpStatus.OK);}
        }
        else{
            foodInventories.addAll(foodInventoryService.getItem(-1));
            return new ResponseEntity<>(foodInventories, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Food> saveFoodInventory(@Valid FoodInventory foodInventory, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
       Food createdFood = foodInventoryService.saveItem(foodInventory);
       return new ResponseEntity<>(createdFood, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> deleteFoodInventory(@PathVariable long id)
    {
        long response = foodInventoryService.deleteItem(id);
        return response > 0 ? new ResponseEntity<>(id, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
