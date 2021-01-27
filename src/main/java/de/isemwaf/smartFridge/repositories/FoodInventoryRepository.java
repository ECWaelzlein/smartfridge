package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.FoodInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface FoodInventoryRepository extends JpaRepository<FoodInventory, Long> {
    List<FoodInventory> findAllByFridge_Id(long id);
    List<FoodInventory> findAllByExpirationDateLessThanEqualAndFridge_Account_Id(Date expDate, long userId);
}
