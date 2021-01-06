package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Meal;
import de.isemwaf.smartFridge.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MealController {
    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }


    /**
     * Gibt ein Json-Array zurück, indem kein, ein oder mehrere Meals sind.
     * @param id Id des Meals. Wenn nicht angegeben, werden alle Meals zurückgegeben.
     * @return Gibt die Liste als Json zurück.
     */
    @GetMapping(path = "api/meal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Meal>> getMeals(@PathVariable Optional<Long> id, @RequestParam Long userId){
        if(id.isEmpty()) {
            return new ResponseEntity<>(mealService.fetchAllMeals(userId),HttpStatus.OK);
        }
        else {
            List<Meal> mealList= mealService.findMeal(id.get()).stream().collect(Collectors.toList());
            return new ResponseEntity<>(mealList,HttpStatus.OK);
        }
    }

    /**
     * Löscht ein Meal mid der übergebenen ID.
     * @param id ID des Meals
     * @return HTTP-Code 204 für erfolg, HTTP-Code 422 für nicht gefunden.
     */
    @DeleteMapping(path ="/api/meal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteMeal(@PathVariable long id){
        boolean deleted = mealService.deleteMeal(id);
        if(deleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Speichert ein Meal in die Datenbank.
     * @param meal Meal, welches gespeichert werden soll
     * @param bindingResult überprüft ob param valide sind
     * @return gibt das Meal zurück, wenn erfolgreich, ansonsten HTTP-Code 422
     */
    @PostMapping(path ="/api/meal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> addMeal(@RequestBody @Valid Meal meal, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(null,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        meal = mealService.saveMeal(meal);
        return new ResponseEntity<>(meal,HttpStatus.OK);
    }

    /**
     * Ändert das datum eines Meals
     * @param newDate neues Datum des Meals
     * @param id ID des Meals, welches geändert werden soll
     * @return Gibt bei Erfolg das veränderte Meal zurück, sonst HTTP-Code 422
     */
    @PostMapping(path ="/api/meal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> changeMeal(@RequestBody @Valid Date newDate, @PathVariable long id){
        Optional<Meal> mealOptional = mealService.findMeal(id);
        if(mealOptional.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Meal meal= mealOptional.get();
        meal.setDate(newDate);
        meal = mealService.saveMeal(meal);
        return new ResponseEntity<>(meal,HttpStatus.OK);
    }
}
