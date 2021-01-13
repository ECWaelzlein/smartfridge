package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findFirstByBarcode(String barcode);

}
