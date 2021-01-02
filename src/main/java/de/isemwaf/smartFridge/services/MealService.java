package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Meal;

import java.util.List;
import java.util.Optional;

public interface MealService {
    Meal saveMeal(Meal meal);

    Optional<Meal> findMeal(long id);
    List<Meal> fetchAllMeals();
    boolean deleteMeal(long id);
}
