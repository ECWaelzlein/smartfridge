package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Meal;
import de.isemwaf.smartFridge.repositories.MealRepository;
import de.isemwaf.smartFridge.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }


    /**
     * Speichert oder ändert ein Meal
     * @param meal zu speicherndes Meal
     * @return gibt die ID des gespeicherten Meals zurück
     */
    @Override
    public Long saveMeal(Meal meal) {
        return mealRepository.save(meal).getId();
    }

    /**
     * Sucht ein Meal mit einer bestimmten Id heraus
     * @param id Id des Meals
     * @return gibt ein optional mit einem Meal zurück.
     */
    @Override
    public Optional<Meal> findMeal(long id) {
        return mealRepository.findById(id);
    }

    /**
     * Gibt alle Meals der Person als Liste zurück.
     * @return Liste aller Meals
     */
    @Override
    public List<Meal> fetchAllMeals() {
        /*
         * An dieser Stelle muss vielleicht auch die accountID übergeben werden,
         * damit nur die Meals der Person angezeigt werden
         */
        return mealRepository.findAll();
    }

    /**
     * Löscht ein Meal mit der entsprechenden ID
     * @param id ID des Meals
     * @return Gibt true zurück, falls die ID gefunden und das Meal gelöscht wurde.
     */
    @Override
    public boolean deleteMeal(long id) {
        try {
            mealRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
