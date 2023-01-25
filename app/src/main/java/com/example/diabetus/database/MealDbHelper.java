package com.example.diabetus.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.diabetus.database.food.FoodEntry;
import com.example.diabetus.database.food.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealDbHelper extends SQLiteOpenHelper {

    public static final String MEAL_TABLE = "MEAL_TABLE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_FOOD = "FOOD_LIST";

    public MealDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + MEAL_TABLE + "(" +
                COLUMN_NAME + " STRING, " +
                COLUMN_FOOD + " STRING, " +
                "PRIMARY KEY(" + COLUMN_NAME + ")" +
                ")";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean addEntry(Meal meal) {
        long insert;
        boolean exists = false;
        SQLiteDatabase db;
        Cursor cursor;


        // get data from database
        String queryString = "SELECT * FROM " + MEAL_TABLE + " WHERE "+ COLUMN_NAME + "=\"" + meal.getName() + "\"";

        db = this.getReadableDatabase();
        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            exists = true;
        }

        cursor.close();
        db.close();

        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, meal.getName());
        cv.put(COLUMN_FOOD, meal.getJSON());

        if (exists) {
            insert = db.update(MEAL_TABLE, cv, COLUMN_NAME + "=\"" + meal.getName() + "\"",null);
        }
        else {
            insert = db.insert(MEAL_TABLE, null, cv);
        }

        //clean up
        db.close();

        return insert != -1;
    }

    public List<Meal> getAllEntry(List<FoodEntry> foodEntryList) {
        List<Meal> returnList = new ArrayList<>();
        SQLiteDatabase db;
        Cursor cursor;

        // get data from database
        String queryString = "SELECT * FROM " + MEAL_TABLE;

        db = this.getReadableDatabase();
        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                Meal newEntry = new Meal(cursor.getString(0),
                        cursor.getString(1), foodEntryList);
                returnList.add(newEntry);

            } while (cursor.moveToNext());
        }

        // clean up
        cursor.close();
        db.close();

        return returnList;
    }

    public void deleteEntry(Meal entry) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MEAL_TABLE, COLUMN_NAME + "=\"" + entry.getName() + "\"", null);

        db.close();
    }
}
