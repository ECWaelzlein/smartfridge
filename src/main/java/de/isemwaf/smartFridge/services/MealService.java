package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Meal;
import de.isemwaf.smartFridge.model.json.MealModel;

import java.util.List;
import java.util.Optional;

public interface MealService {

    /**
     * Speichert oder ändert ein Meal
     * @param meal zu speicherndes Meal
     * @return gibt die ID des gespeicherten Meals zurück
     */
    Meal saveMeal(Meal meal);

    /**
     * Sucht ein Meal mit einer bestimmten Id heraus
     * @param id Id des Meals
     * @return gibt ein optional mit einem Meal zurück.
     */
    Optional<Meal> findMeal(long id);

    /**
     * Gibt alle Meals der Person als Liste zurück.
     * @return Liste aller Meals
     */
    List<Meal> fetchAllMeals(long userId);

    /**
     * Löscht ein Meal mit der entsprechenden ID
     * @param id ID des Meals
     * @return Gibt true zurück, falls die ID nicht null ist.
     */
    boolean deleteMeal(long id);

    /**
     * Erstellt auf Basis eines mealModels ein Meal, speichert dies in der Datenbank und gibt das Meal zurück.
     * @param mealModel MealModel zur Erstellung des Meals
     * @return gespeichertes Meal
     */
    Meal createMeal(MealModel mealModel);
}
