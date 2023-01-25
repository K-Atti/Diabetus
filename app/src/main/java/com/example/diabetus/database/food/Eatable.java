package com.example.diabetus.database.food;

public interface Eatable {

    int EATABLE_FOOD = 1;
    int EATABLE_FOODENTRY = 2;
    int EATABLE_MEAL = 3;


    double getCalorie();
    double getProtein();
    double getCarb();
    double getFat();
    String getPresentableName();
    int getType();
}
