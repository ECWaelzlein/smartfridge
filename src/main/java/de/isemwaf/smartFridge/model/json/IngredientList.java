package de.isemwaf.smartFridge.model.json;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientList {
    private List<Ingredient> ingredientList;

    @NotBlank
    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<String> getStringIngredientList(){
        List<String> ingredientsStringList = new ArrayList<>();
        if (!ingredientList.isEmpty()) {
            ingredientsStringList = ingredientList.stream().map(Ingredient::getIngredient).collect(Collectors.toList());
        }
        return ingredientsStringList;
    }
}
