package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.FoodInventory;
import de.isemwaf.smartFridge.services.FoodInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class FoodInventoryController {
    public final FoodInventoryService foodInventoryServices;

    @Autowired
    public FoodInventoryController(FoodInventoryService foodInventoryServices) {
        this.foodInventoryServices = foodInventoryServices;
    }
    @GetMapping(path = {"/{id}", "/"})
    @ResponseBody
    public List<FoodInventory> getFoodInventory(@PathVariable Optional<Integer> id, HttpServletResponse httpServletResponse)
    {
        int paramID = id.isPresent() ? id.get() : -1;
        List<FoodInventory> foodInventories = foodInventoryServices.getItem(paramID);
        if(!foodInventories.isEmpty()){
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else { httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST); }
        return foodInventories;
    }
    @PostMapping(path = "/")
    @ResponseBody
    public int saveFoodInventory(@RequestBody FoodInventory foodInventory, HttpServletResponse httpServletResponse)
    {
       int id = foodInventoryServices.saveItem(foodInventory);
       if(id > 0) {
           httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
       }
       else { httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);}
       return id;
    }
    @DeleteMapping(path = "/{id}")
    public void deleteFoodInventory(@PathVariable int id, HttpServletResponse httpServletResponse)
    {
        int response = foodInventoryServices.deleteItem(id);
        httpServletResponse.setStatus(response);
    }
}
