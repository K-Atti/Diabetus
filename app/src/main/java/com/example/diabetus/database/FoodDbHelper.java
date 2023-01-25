package com.example.diabetus.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.diabetus.database.food.FoodEntry;

import java.util.ArrayList;
import java.util.List;

public class FoodDbHelper extends SQLiteOpenHelper {

    public static final String FOOD_TABLE = "FOOD_TABLE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_BRAND = "BRAND";
    public static final String COLUMN_CALORIE = "CALORIE";
    public static final String COLUMN_TRANS_FAT = "TRANS_FAT";
    public static final String COLUMN_SATURATED_FAT = "SATURATED_FAT";
    public static final String COLUMN_UNSATURATED_FAT = "UNSATURATED_FAT";
    public static final String COLUMN_SODIUM = "SODIUM";
    public static final String COLUMN_CARB = "CARB";
    public static final String COLUMN_SUGAR = "SUGAR";
    public static final String COLUMN_FIBER = "FIBER";
    public static final String COLUMN_PROTEIN = "PROTEIN";
    public static final String COLUMN_WEIGHT = "WEIGHT";


    public FoodDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + FOOD_TABLE + "(" +
                COLUMN_NAME + " STRING, " +
                COLUMN_BRAND + " STRING, " +
                COLUMN_CALORIE + " REAL, " +
                COLUMN_TRANS_FAT + " REAL, " +
                COLUMN_SATURATED_FAT + " REAL, " +
                COLUMN_UNSATURATED_FAT + " REAL, " +
                COLUMN_SODIUM + " REAL, " +
                COLUMN_CARB + " REAL, " +
                COLUMN_SUGAR + " REAL, " +
                COLUMN_FIBER + " REAL, " +
                COLUMN_PROTEIN + " REAL, " +
                COLUMN_WEIGHT + " REAL, " +
                "PRIMARY KEY(" + COLUMN_NAME + "," + COLUMN_BRAND + ")" +
                ")";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addEntry(FoodEntry entry) {
        long insert;
        boolean exists = true;

        try {
            getEntry(entry.getName(), entry.getBrand());
        }
        catch (Exception e) {
            exists = false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME,entry.getName());
        cv.put(COLUMN_BRAND,entry.getBrand());
        cv.put(COLUMN_CALORIE,entry.getCalorie());
        cv.put(COLUMN_TRANS_FAT,entry.getTrans_fat());
        cv.put(COLUMN_SATURATED_FAT,entry.getSaturated_fat());
        cv.put(COLUMN_UNSATURATED_FAT,entry.getUnsaturated_fat());
        cv.put(COLUMN_SODIUM,entry.getSodium());
        cv.put(COLUMN_CARB,entry.getCarb());
        cv.put(COLUMN_SUGAR,entry.getSugar());
        cv.put(COLUMN_FIBER,entry.getFiber());
        cv.put(COLUMN_PROTEIN,entry.getProtein());
        cv.put(COLUMN_WEIGHT,entry.getWeight());

        if (exists) {
            insert = db.update(FOOD_TABLE, cv, "(" + COLUMN_NAME + "=\"" + entry.getName()
                            + "\") AND (" + COLUMN_BRAND + "=\"" + entry.getBrand() + "\")",null);
        }
        else {
            insert = db.insert(FOOD_TABLE, null, cv);
        }

        //clean up
        db.close();

        return insert!=-1;
    }

    public List<FoodEntry> getAllEntry() {
        List<FoodEntry> returnList = new ArrayList<>();
        SQLiteDatabase db;
        Cursor cursor;

        // get data from database
        String queryString = "SELECT * FROM " + FOOD_TABLE;

        db = this.getReadableDatabase();
        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String brand = cursor.getString(1);
                double calorie = cursor.getDouble(2);
                double trans_fat = cursor.getDouble(3);
                double saturated_fat = cursor.getDouble(4);
                double unsaturated_fat = cursor.getDouble(5);
                double sodium = cursor.getDouble(6);
                double carb = cursor.getDouble(7);
                double sugar = cursor.getDouble(8);
                double fiber = cursor.getDouble(9);
                double protein = cursor.getDouble(10);
                double weight = cursor.getDouble(11);

                FoodEntry newEntry = new FoodEntry(name, brand, calorie, trans_fat, saturated_fat,
                        unsaturated_fat, sodium, carb, sugar, fiber, protein, weight, true);
                returnList.add(newEntry);

            } while (cursor.moveToNext());
        }

        // clean up
        cursor.close();
        db.close();

        return returnList;
    }

    public FoodEntry getEntry(String name, String brand) throws Exception {
        FoodEntry returnValue = null;
        SQLiteDatabase db;
        Cursor cursor;

        // get data from database
        String queryString = "SELECT * FROM " + FOOD_TABLE + " WHERE " + COLUMN_NAME + "=\"" + name +
                "\" AND " + COLUMN_BRAND + "=\"" + brand + "\"";

        db = this.getReadableDatabase();
        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            double calorie = cursor.getDouble(2);
            double trans_fat = cursor.getDouble(3);
            double saturated_fat = cursor.getDouble(4);
            double unsaturated_fat = cursor.getDouble(5);
            double sodium = cursor.getDouble(6);
            double carb = cursor.getDouble(7);
            double sugar = cursor.getDouble(8);
            double fiber = cursor.getDouble(9);
            double protein = cursor.getDouble(10);
            double weight = cursor.getDouble(11);

            returnValue = new FoodEntry(name, brand, calorie, trans_fat, saturated_fat,
                    unsaturated_fat, sodium, carb, sugar, fiber, protein, weight, true);
        }

        // clean up
        cursor.close();
        db.close();

        if (returnValue == null) {
            throw new Exception("Food does not exist in database");
        }

        return returnValue;
    }

    public void deleteEntry(FoodEntry entry) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(FOOD_TABLE, COLUMN_NAME + "=\"" + entry.getName() +
                "\" AND " + COLUMN_BRAND + "=\"" + entry.getBrand() + "\"", null);

        db.close();
    }


}
