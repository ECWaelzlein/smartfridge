package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Recipe;
import de.isemwaf.smartFridge.repositories.RecipeRepository;
import de.isemwaf.smartFridge.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipe(long id) {
        return recipeRepository.findById(id).isPresent() ? recipeRepository.findById(id).get() : null;
    }
}
