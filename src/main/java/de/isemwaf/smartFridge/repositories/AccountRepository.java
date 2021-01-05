package de.isemwaf.smartFridge.repositories;

import de.isemwaf.smartFridge.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
