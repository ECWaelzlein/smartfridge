package de.isemwaf.smartFridge.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Meal extends SuperEntity{
    long id;
    Date date;
    Recipe recipe;
    class Recipe{}


    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQUENCE_MEAL_ID"
    )
    @SequenceGenerator(
            name = "SEQUENCE_MEAL_ID",
            sequenceName = "SEQUENCE_MEAL_ID",
            allocationSize = 1
    )
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    @NotNull
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //Ist ManyToOne die richtige Beziehung? (Jedes Meal hat genau ein Rezept)
    //@ManyToOne
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
