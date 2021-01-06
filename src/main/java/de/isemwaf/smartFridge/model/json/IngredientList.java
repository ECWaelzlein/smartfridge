package de.isemwaf.smartFridge.model.json;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class IngredientList {
    private List<Ingredient> ingredientList;

    @NotBlank
    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
