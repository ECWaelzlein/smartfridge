package de.isemwaf.smartFridge.repositories;
import de.isemwaf.smartFridge.model.FoodInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodInventoryRepository extends JpaRepository<FoodInventory, Long> {
}
