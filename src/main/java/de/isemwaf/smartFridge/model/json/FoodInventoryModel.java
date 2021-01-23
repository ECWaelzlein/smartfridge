package de.isemwaf.smartFridge.model.json;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class FoodInventoryModel {
    String userId;
    String foodId;
    Date expirationDate;

    @NotBlank(message = "Userid darf nicht leer sein")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NotBlank(message = "FoodId darf nicht leer sein")
    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    @NotNull(message = "Das Verfallsdatum darf nicht leer sein")
    @Temporal(TemporalType.DATE)
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
