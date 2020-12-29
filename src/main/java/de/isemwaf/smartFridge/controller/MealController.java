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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @GetMapping(path = "api/meal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Meal>> getMeals(@PathVariable Optional<Long> id){
        if(id.isEmpty()) {
            return new ResponseEntity<>(mealService.fetchAllMeals(),HttpStatus.OK);
        }
        else {
            List<Meal> mealList= mealService.findMeal(id.get()).stream().collect(Collectors.toList());
            return new ResponseEntity<>(mealList,HttpStatus.OK);
        }
    }

    @DeleteMapping(path ="/api/meal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteMeal(@PathVariable long id){
        boolean deleted = mealService.deleteMeal(id);
        if(deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(path ="/api/meal/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Meal> addMeal(@RequestBody Meal meal, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(null,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        mealService.saveMeal(meal);
        //Hier wird noch nicht die gespeicherte Entity zurückgegeben. SaveMeal gibt nur die ID zurück.
        return new ResponseEntity<>(meal,HttpStatus.OK);
    }

    @PostMapping(path ="/api/meal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Meal> changeMeal(@RequestBody Date newDate, @PathVariable long id){
        Optional<Meal> mealOptional = mealService.findMeal(id);
        if(mealOptional.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Meal meal= mealOptional.get();
        meal.setDate(newDate);
        mealService.saveMeal(meal);
        //Hier wird noch nicht die gespeicherte Entity zurückgegeben. SaveMeal gibt nur die ID zurück.
        return new ResponseEntity<>(meal,HttpStatus.OK);
    }
}
