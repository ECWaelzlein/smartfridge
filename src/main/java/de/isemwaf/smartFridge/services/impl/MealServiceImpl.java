package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Meal;
import de.isemwaf.smartFridge.model.json.MealModel;
import de.isemwaf.smartFridge.repositories.MealRepository;
import de.isemwaf.smartFridge.services.AccountService;
import de.isemwaf.smartFridge.services.MealService;
import de.isemwaf.smartFridge.services.RecipeService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final RecipeService recipeService;
    private final AccountService accountService;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository, RecipeService recipeService, AccountService accountService) {
        this.mealRepository = mealRepository;
        this.recipeService = recipeService;
        this.accountService = accountService;
    }



    @Override
    public Meal saveMeal(Meal meal) {
        return mealRepository.save(meal);
    }


    @Override
    public Optional<Meal> findMeal(long id) {
        return mealRepository.findById(id);
    }


    @Override
    public List<Meal> fetchAllMeals(long userId) {
        return mealRepository.findAllByAccount_Id(userId);
    }


    @Override
    public boolean deleteMeal(long id) {
        try {
            mealRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e)  {
            return false;
        }
    }

    @Override
    public List<Meal> fetchUpcomingMealsByUser(long userId, Date date) {
        return mealRepository.findAllByAccount_IdAndDateGreaterThanEqualOrderByDateAsc(userId, DateUtils.truncate(date, Calendar.DAY_OF_MONTH));
    }


    @Override
    public Meal createMeal(MealModel mealModel) {
        Meal meal = new Meal();
        meal.setDate(mealModel.getDate());
        meal.setAccount(accountService.getAccountById(mealModel.getUserId()));
        meal.setRecipe(recipeService.getRecipe(mealModel.getRecipeId()));
        return mealRepository.save(meal);
    }
}
