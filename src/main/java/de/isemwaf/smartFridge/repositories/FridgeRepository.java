package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {
}
