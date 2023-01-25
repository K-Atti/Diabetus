package com.example.diabetus.database.food;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Food implements Parcelable, Eatable {

    public static final int QUANTITY_100G = 1;
    public static final int QUANTITY_PIECE = 2;

    FoodEntry food;
    double quantity;
    int quantityType;

    public Food(FoodEntry food, double quantity, int quantityType) {
        this.food = food;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    private Food(Parcel in) {
        food = in.readParcelable(FoodEntry.class.getClassLoader());
        quantity = in.readDouble();
        quantityType = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    //getters and setters
    public FoodEntry getFood() {
        return food;
    }

    public void setFood(FoodEntry food) {
        this.food = food;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(int quantityType) {
        this.quantityType = quantityType;
    }

    @NonNull
    @Override
    public String toString() {
        return "Food{" +
                " Food: " + food.getName() +
                " Quantity: " + quantity +
                "}";
    }

    public double getCalorie() {
        double retValue = food.getCalorie() * quantity / 100;
        if (quantityType == QUANTITY_PIECE) {
            retValue *= food.getWeight();
        }
        return retValue;
    }
    public double getProtein() {
        double retValue = food.getProtein() * quantity / 100;
        if (quantityType == QUANTITY_PIECE) {
            retValue *= food.getWeight();
        }
        return retValue;
    }

    public double getCarb() {
        double retValue = food.getCarb() * quantity / 100;
        if (quantityType == QUANTITY_PIECE) {
            retValue *= food.getWeight();
        }
        return retValue;
    }

    public double getFat() {
        double retValue = (food.getFat()) * quantity / 100;
        if (quantityType == QUANTITY_PIECE) {
            retValue *= food.getWeight();
        }
        return retValue;
    }

    public double getFiber() {
        double retValue = (food.getFiber()) * quantity / 100;
        if (quantityType == QUANTITY_PIECE) {
            retValue *= food.getWeight();
        }
        return retValue;
    }

    public String getPresentableName() {
        return food.getPresentableName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(food, i);
        parcel.writeDouble(quantity);
        parcel.writeInt(quantityType);
    }

    @Override
    public int getType() {
        return Eatable.EATABLE_FOOD;
    }
}
