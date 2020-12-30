package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Recipe;

import java.util.Optional;

public interface RecipeService {
    Recipe saveRecipe(Recipe recipe);

    Recipe getRecipe(long id);
}
