package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

}
