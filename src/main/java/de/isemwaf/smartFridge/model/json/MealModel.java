package de.isemwaf.smartFridge.model.json;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class MealModel {
    long recipeId;
    Date date;
    long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @NotNull(message = "Die RecipeId darf nicht leer sein")
    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @NotNull(message = "Das Datum darf nicht leer sein")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
