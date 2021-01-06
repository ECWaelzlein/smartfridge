package de.isemwaf.smartFridge.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Fridge extends SuperEntity {
    private long id;
    private List<FoodInventory> inventory;
    private Account account;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQUENCE_FRIDGE_ID"
    )
    @SequenceGenerator(
            name = "SEQUENCE_FRIDGE_ID",
            sequenceName = "SEQUENCE_FRIDGE_ID",
            allocationSize = 1
    )
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "fridge")
    public List<FoodInventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<FoodInventory> inventory) {
        this.inventory = inventory;
    }

    @OneToOne
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
