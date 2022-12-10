package com.example.diabetus.usda;

import java.util.List;

public class UsdaSearchResult {
    public List<UsdaData> getFoods() {
        return foods;
    }

    public void setFoods(List<UsdaData> foods) {
        this.foods = foods;
    }

    private List<UsdaData> foods;
}
