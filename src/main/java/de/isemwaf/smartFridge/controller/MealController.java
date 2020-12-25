package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Meal;
import de.isemwaf.smartFridge.services.impl.MealServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MealController {
    private final MealServiceImpl mealService;

    @Autowired
    public MealController(MealServiceImpl mealService) {
        this.mealService = mealService;
    }


    /**
     * Gibt ein Json-Array zurück, indem kein, ein oder mehrere Meals sind.
     * @param id Id des Meals. Wenn nicht angegeben, werden alle Meals zurückgegeben.
     * @return Gibt die Liste als Json zurück.
     */
    @GetMapping(value = "api/meal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Meal> getMeals(@PathVariable Optional<Long> id){
        if(id.isEmpty()) {
            return mealService.fetchAllMeals();

        }
        else {
            List<Meal> list = new ArrayList<>();
            mealService.findMeal(id.get()).ifPresent(list::add);
            return list;
        }
    }

    @DeleteMapping("/api/meal/{id}")
    @ResponseBody
    public ResponseEntity deleteMeal(@PathVariable long id){
        boolean deleted = mealService.deleteMeal(id);
        if(deleted){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/api/meal/")
    @ResponseBody
    public Meal addMeal(@RequestBody Meal meal, BindingResult bindingResult){
        //TODO check bindingResult and return meal
        return null;
    }
}
