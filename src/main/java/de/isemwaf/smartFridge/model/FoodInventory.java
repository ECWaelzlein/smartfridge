package de.isemwaf.smartFridge.model;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Audited(withModifiedFlag = true)
public class FoodInventory extends SuperEntity {
    private long Id;
    private Food food;
    private Fridge fridge;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQUENCE_FOODINVENTORY_ID"
    )
    @SequenceGenerator(
            name = "SEQUENCE_FOODINVENTORY_ID",
            sequenceName = "SEQUENCE_FOODINVENTORY_ID",
            allocationSize = 1
    )
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    @ManyToOne
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

    @ManyToOne
    @NotAudited
    public Fridge getFridge() {
        return fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }
}
