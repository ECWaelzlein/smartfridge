package de.isemwaf.smartFridge.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
public class FoodInventory extends SuperEntity{
    long Id;
    Food food;

    @Id
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
    @ManyToOne
    @JoinColumn(name = "food_id") //muss noch ersetzte werden
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

    Date expirationDate;
class Food{} //muss noch ersetzt werden
}
