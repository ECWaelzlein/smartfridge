package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class FoodInventoryController {
    private final FoodInventoryService foodInventoryService;
    @Autowired
    public FoodInventoryController(FoodInventoryService foodInventoryService) {
        this.foodInventoryService = foodInventoryService;
    }
    @GetMapping(path = {"/{id}", "/"})
    @ResponseBody
    public List<FoodInventory> getFoodInventory(@PathVariable Optional<Long> id, HttpServletResponse httpServletResponse)
    {
        List<FoodInventory> foodInventories = null;
        if(id.isPresent()) {
            foodInventories.addAll(foodInventoryService.getItem(id.get()));
            if(foodInventories.isEmpty())  {httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND); }
            else { httpServletResponse.setStatus(HttpServletResponse.SC_OK);}
        }
        else{
            foodInventories.addAll(foodInventoryService.getItem(-1));
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        return foodInventories;
    }
    @PostMapping("/")
    @ResponseBody
    public int saveFoodInventory(FoodInventory foodInventory,  HttpServletResponse httpServletResponse)
    {
       int responseId = foodInventoryService.saveItem(foodInventory);
       httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
       return responseId;
    }
    @DeleteMapping("/{id}")
    public void deleteFoodInventory(long id, HttpServletResponse httpServletResponse)
    {
        int response = foodInventoryService.deleteItem(id);
        httpServletResponse.setStatus(response);
    }
}
