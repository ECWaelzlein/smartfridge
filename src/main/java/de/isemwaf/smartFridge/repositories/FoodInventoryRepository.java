package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.FoodInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FoodInventoryRepository extends JpaRepository<FoodInventory, Long> {
    List<FoodInventory> findAllByFridge_Id(long id);
    List<FoodInventory> findAllByExpirationDateLessThanEqual(Date expDate);
}
