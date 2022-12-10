package com.example.diabetus.food;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.diabetus.usda.UsdaData;

public class FoodEntry implements Parcelable, Eatable {
    private String name;
    private String brand;
    private double calorie;
    private double trans_fat;
    private double saturated_fat;
    private double unsaturated_fat;
    private double sodium;
    private double carb;
    private double sugar;
    private double fiber;
    private double protein;
    private double weight;

    public FoodEntry(String name, String brand, double calorie, double trans_fat, double saturated_fat, double unsaturated_fat, double sodium, double carb, double sugar, double fiber, double protein, double weight) {
        this.name = name;
        this.brand = brand;
        this.calorie = calorie;
        this.trans_fat = trans_fat;
        this.saturated_fat = saturated_fat;
        this.unsaturated_fat = unsaturated_fat;
        this.sodium = sodium;
        this.carb = carb;
        this.sugar = sugar;
        this.fiber = fiber;
        this.protein = protein;
        this.weight = weight;
    }

    public FoodEntry() {
        this.name = "";
        this.brand = "";
        this.calorie = 0;
        this.trans_fat = 0;
        this.saturated_fat = 0;
        this.unsaturated_fat = 0;
        this.sodium = 0;
        this.carb = 0;
        this.sugar = 0;
        this.fiber = 0;
        this.protein = 0;
        this.weight = 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "FoodEntry{" +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", calorie=" + calorie +
                ", trans_fat=" + trans_fat +
                ", saturated_fat=" + saturated_fat +
                ", unsaturated_fat=" + unsaturated_fat +
                ", sodium=" + sodium +
                ", carb=" + carb +
                ", sugar=" + sugar +
                ", fiber=" + fiber +
                ", protein=" + protein +
                ", weight=" + weight +
                '}';
    }

    public FoodEntry(UsdaData usda) {
        name = usda.getDescription();
        brand = "";
        calorie = usda.getCalorie();
        trans_fat = usda.getTransFat();
        saturated_fat = usda.getSaturatedFat();
        unsaturated_fat = usda.getUnSaturatedFat();
        carb = usda.getCarb();
        sugar = usda.getSugar();
        fiber = usda.getFiber();
        protein = usda.getProtein();
        weight = usda.getServingSize();
        sodium = 0;
    }

    private FoodEntry(Parcel in) {
        name = in.readString();
        brand = in.readString();
        calorie = in.readDouble();
        trans_fat = in.readInt();
        saturated_fat = in.readInt();
        unsaturated_fat = in.readDouble();
        sodium = in.readDouble();
        carb = in.readDouble();
        sugar = in.readDouble();
        fiber = in.readDouble();
        protein = in.readDouble();
        weight = in.readDouble();
    }

    public static final Creator<FoodEntry> CREATOR = new Creator<FoodEntry>() {
        @Override
        public FoodEntry createFromParcel(Parcel in) {
            return new FoodEntry(in);
        }

        @Override
        public FoodEntry[] newArray(int size) {
            return new FoodEntry[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public double getTrans_fat() {
        return trans_fat;
    }

    public void setTrans_fat(double trans_fat) {
        this.trans_fat = trans_fat;
    }

    public double getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(double saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public double getUnsaturated_fat() {
        return unsaturated_fat;
    }

    public void setUnsaturated_fat(double unsaturated_fat) {
        this.unsaturated_fat = unsaturated_fat;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getCarb() {
        return carb;
    }

    @Override
    public double getFat() {
        return saturated_fat + unsaturated_fat + trans_fat;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(brand);
        parcel.writeDouble(calorie);
        parcel.writeDouble(trans_fat);
        parcel.writeDouble(saturated_fat);
        parcel.writeDouble(unsaturated_fat);
        parcel.writeDouble(sodium);
        parcel.writeDouble(carb);
        parcel.writeDouble(sugar);
        parcel.writeDouble(fiber);
        parcel.writeDouble(protein);
        parcel.writeDouble(weight);
    }

    public String getPresentableName() {
        String retVal = name;
        if (!brand.isEmpty()) {
            retVal += (" (" + brand + ")");
        }
        return retVal;
    }

    public int getType() { return Eatable.EATABLE_FOODENTRY; }

}
