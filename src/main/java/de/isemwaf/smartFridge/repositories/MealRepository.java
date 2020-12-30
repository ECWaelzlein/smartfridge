package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
