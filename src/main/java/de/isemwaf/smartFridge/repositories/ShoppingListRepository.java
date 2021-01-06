package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.Food;
import de.isemwaf.smartFridge.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
}
