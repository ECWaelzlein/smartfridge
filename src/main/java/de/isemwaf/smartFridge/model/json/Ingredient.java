package de.isemwaf.smartFridge.model.json;

import javax.validation.constraints.NotBlank;

public class Ingredient {
    private String ingredient;

    @NotBlank(message = "Ingredient darf nicht leer sein")
    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
