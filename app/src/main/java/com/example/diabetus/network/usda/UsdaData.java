package com.example.diabetus.network.usda;

import java.util.ArrayList;

public class UsdaData {
    private String fdcId;
    private String description;
    private float servingSize;
    private String servingSizeUnit;
    private ArrayList<FoodNutrient> foodNutrients;

    public String getFdcId() {
        return fdcId;
    }

    public void setFdcId(String fdcId) {
        this.fdcId = fdcId;
    }

    public ArrayList<FoodNutrient> getFoodNutrients() {
        return foodNutrients;
    }

    public void setFoodNutrients(ArrayList<FoodNutrient> foodNutrients) {
        this.foodNutrients = foodNutrients;
    }

    public float getServingSize() {
        return servingSize;
    }

    public void setServingSize(float servingSize) {
        this.servingSize = servingSize;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setServingSizeUnit(String servingSizeUnit) {
        this.servingSizeUnit = servingSizeUnit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCalorie() {
        return getNutrient(FoodNutrient.CALORIE);
    }

    public float getProtein() {
        return getNutrient(FoodNutrient.PROTEIN);
    }

    public float getCarb() {
        return getNutrient(FoodNutrient.CARB) - getNutrient(FoodNutrient.FIBER);
    }

    public float getSaturatedFat() {
        return getNutrient(FoodNutrient.SATURATED);
    }

    public float getUnSaturatedFat() {
        return getNutrient(FoodNutrient.UNSATURATED);
    }

    public float getTransFat() {
        return getNutrient(FoodNutrient.TRANS_FAT);
    }

    public float getSugar() {
        return getNutrient(FoodNutrient.SUGAR);
    }

    public float getFiber() {
        return getNutrient(FoodNutrient.FIBER);
    }

    private float getNutrient(float id) {
        float retValue = 0.0f;
        for (FoodNutrient item : foodNutrients) {
            if (item.getNutrientNumber() == id) {
                retValue = item.getValue();
                break;
            }
        }
         return retValue;
    }
}
