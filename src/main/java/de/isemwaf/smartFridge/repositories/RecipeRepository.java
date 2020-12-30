package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
