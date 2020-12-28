package de.isemwaf.smartFridge.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class FoodInventory extends SuperEntity {
    private long Id;
    private Food food;
    @Id
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
    @ManyToOne
    @JoinColumn(name = "food_id") //muss geändert werden
    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
    @Column
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    private Date expirationDate;
    class Food{}

}
