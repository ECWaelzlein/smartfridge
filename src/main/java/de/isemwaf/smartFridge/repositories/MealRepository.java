package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findAllByAccount_Id(long userId);
    List<Meal> findAllByAccount_IdAndDateGreaterThanEqual(long id, Date date);
}
