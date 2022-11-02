package com.example.diabetus.food;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Meal implements Parcelable, Eatable {
    private String name;
    private List<Food> foodList = new ArrayList<Food>();

    public Meal() {
        name = "";
    }

    private Meal(Parcel in) {
        name = in.readString();
        in.readTypedList(foodList, Food.CREATOR);
    }

    public Meal(String in, List<FoodEntry> knownFood) {
        try {
            JSONObject json = new JSONObject(in);
            name = json.getString("name");
            processFoodListJSON(in,knownFood);

        }
        catch (JSONException e) {
            // Meal cannot be set
        }
    }

    public Meal(String db_name, String in, List<FoodEntry> knownFood) {
        name = db_name;
        this.processFoodListJSON(in,knownFood);
    }

    public static final Parcelable.Creator<Meal> CREATOR
            = new Parcelable.Creator<Meal>() {
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public void addFood(FoodEntry food, double quantity, int quantityType) {
        Food tmp = new Food(food, quantity, quantityType);
        foodList.add(tmp);
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public void removeFood(FoodEntry food, double quantity, int quantityType) {
        Food tmp = new Food(food, quantity, quantityType);
        foodList.remove(tmp);
    }

    public String getName() {
        return name;
    }

    public String getPresentableName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalorie() {
        return foodList.stream().mapToDouble(f -> f.getCalorie()).sum();
    }

    public double getProtein() {
        return foodList.stream().mapToDouble(f -> f.getProtein()).sum();
    }

    public double getCarb() {
        return foodList.stream().mapToDouble(f -> f.getCarb()).sum();
    }

    public double getFat() {
        return foodList.stream().mapToDouble(f -> f.getFat()).sum();
    }

    public double getFiber() {
        return foodList.stream().mapToDouble(f -> f.getFiber()).sum();
    }
    public List<Food> getFoodList() { return foodList; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedList(foodList);
    }

    public String getJSON() {
        String returnValue = "{\"name\": \"" + name + "\",";
        returnValue += "\"foodList\":[";
        for (Food food : foodList) {
            returnValue += "{";
            returnValue += "\"name\": \"" + food.getFood().getName() + "\",";
            returnValue += "\"brand\": \"" + food.getFood().getBrand() + "\",";
            returnValue += "\"quantity\": \"" + food.getQuantity() + "\",";
            returnValue += "\"quantityType\": \"" + food.getQuantityType() + "\"";
            returnValue += "},";
        }
        returnValue = returnValue.substring(0, returnValue.length() - 1);
        returnValue += "]}";

        return returnValue;
    }

    @Override
    public int getType() {
        return Eatable.EATABLE_MEAL;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", foodList=" + foodList +
                '}';
    }

    public String getContent() {
        String retVal = "";
        for(Food food : foodList) {
            retVal += "•" + food.getFood().getPresentableName() + " " + food.getQuantity();
            if (food.getQuantityType() == Food.QUANTITY_100G) {
                retVal += "g";
            }
            else {
                retVal += "piece";
            }
            retVal += " ";
        }
        return retVal;
    }

    public String getMacros() {
        return "Cal: " + String.format("%.2f", this.getCalorie()) +
                " Carb: " + String.format("%.2f", this.getCarb()) +
                " Prot: " + String.format("%.2f", this.getProtein()) +
                " Fat: " + String.format("%.2f", this.getFat());
    }

    private void processFoodListJSON(String in, List<FoodEntry> knownFood) {
        try {
            JSONObject json = new JSONObject(in);
            JSONArray jsonArray = json.getJSONArray("foodList");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                try {
                    JSONObject foodObject = jsonArray.getJSONObject(i);
                    String name = foodObject.getString("name");
                    String brand = foodObject.getString("brand");
                    Double quantity = foodObject.getDouble("quantity");
                    int quantityType = foodObject.getInt("quantityType");

                    //Find food
                    for (FoodEntry food: knownFood) {
                        if (food.getName().equals(name) && food.getBrand().equals(brand)) {
                            foodList.add(new Food(food, quantity, quantityType));
                            break;
                        }
                    }
                }
                catch (JSONException e) {
                    // Food cannot be set
                }
            }

        }
        catch (JSONException e) {
            // Meal cannot be set
        }
    }
}
