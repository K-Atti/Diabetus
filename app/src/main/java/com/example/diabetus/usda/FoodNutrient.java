package com.example.diabetus.usda;

public class FoodNutrient {
    public static final float PROTEIN = 203;
    public static final float FAT = 204;
    public static final float CARB = 205;
    public static final float CALORIE = 208;
    public static final float TRANS_FAT = 605;
    public static final float SATURATED = 606;
    public static final float UNSATURATED = 646;
    public static final float FIBER = 291;
    public static final float SUGAR = 269;

    private float nutrientNumber;
    private float value;
    private String unitName;

    public float getNutrientNumber() {
        return nutrientNumber;
    }

    public void setNutrientNumber(float nutrientNumber) {
        this.nutrientNumber = nutrientNumber;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

}
