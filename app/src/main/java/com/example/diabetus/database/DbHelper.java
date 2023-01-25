package com.example.diabetus.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.diabetus.database.diary.DiaryEntry;
import com.example.diabetus.database.food.Eatable;
import com.example.diabetus.database.food.FoodEntry;
import com.example.diabetus.database.food.Meal;

import java.util.ArrayList;
import java.util.List;

public class DbHelper {
    private final DiaryDbHelper ddbh;
    private final FoodDbHelper fdbh;
    private final MealDbHelper mdbh;

    public DbHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, int version) {

        ddbh = new DiaryDbHelper(context,"Diary",factory,version);
        fdbh = new FoodDbHelper(context,"Food",factory,version);
        mdbh = new MealDbHelper(context, "Meal",factory,version);
    }

    public List<DiaryEntry> getAllDiaryEntry() {
        return ddbh.getAllEntry((List<FoodEntry>)fdbh.getAllEntry());
    }

    public List<? extends Eatable> getAllEatableEntry() {
        List<FoodEntry> foodList = fdbh.getAllEntry();
        List<Meal> mealList = mdbh.getAllEntry((List<FoodEntry>)foodList);
        List<Object> retVal = new ArrayList<>();
        retVal.addAll(foodList);
        retVal.addAll(mealList);
        return (List<? extends Eatable>)(Object)retVal;
    }

    public boolean addDiaryEntry(DiaryEntry entry) {
        return ddbh.addEntry(entry);
    }

    public boolean addMealEntry(Meal entry) {
        return mdbh.addEntry(entry);
    }

    public boolean addFoodEntry(FoodEntry entry) {
        return fdbh.addEntry(entry);
    }

    public void deleteDiaryEntry(DiaryEntry entry) {
        ddbh.deleteEntry(entry);
    }

    public void deleteMealEntry(Meal entry) {
        mdbh.deleteEntry(entry);
    }

    public void deleteFoodEntry(FoodEntry entry) {
        fdbh.deleteEntry(entry);
    }

    public List<DiaryEntry> getLast90DaysEntries() {
        return getDiaryEntryWhere(" " +
                "WHERE " +
                "substr(REPLACE( + " + DiaryDbHelper.COLUMN_DIARY_TIMEDATE + ",'.','-'),1,10)" +
                "BETWEEN date('now','localtime','-90 days') AND date('now','localtime')");
    }

    public List<DiaryEntry> getDiaryEntryWhere(String condition) {
        return ddbh.getEntryWhere(condition,(List<FoodEntry>)fdbh.getAllEntry());
    }
}
