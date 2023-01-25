package com.example.diabetus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.diabetus.database.diary.DiaryEntry;
import com.example.diabetus.database.food.FoodEntry;
import com.example.diabetus.database.food.Meal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryDbHelper extends SQLiteOpenHelper {

    public static final String DIARY_TABLE = "DIARY_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DIARY_TIMEDATE = "DIARY_TIMEDATE";
    public static final String COLUMN_DIARY_BLOODSUGAR = "DIARY_BLOODSUGAR";
    public static final String COLUMN_DIARY_RAPID_INSULIN = "RAPID_INSULIN";
    public static final String COLUMN_DIARY_BASAL_INSULIN = "BASAL_INSULIN";
    public static final String COLUMN_DIARY_WEIGHT = "WEIGHT";
    public static final String COLUMN_DIARY_CATEGORY = "CATEGORY";
    public static final String COLUMN_DIARY_MEAL = "MEAL";

    public DiaryDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + DIARY_TABLE + "(" +
                COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DIARY_TIMEDATE + " TEXT, " +
                COLUMN_DIARY_BLOODSUGAR + " REAL, "+
                COLUMN_DIARY_RAPID_INSULIN + " INTEGER, " +
                COLUMN_DIARY_BASAL_INSULIN + " INTEGER, " +
                COLUMN_DIARY_WEIGHT + " REAL, "+
                COLUMN_DIARY_CATEGORY + " TEXT, " +
                COLUMN_DIARY_MEAL + " TEXT" +
                ")";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public boolean addEntry(DiaryEntry entry) {
        long insert;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DIARY_TIMEDATE,entry.getStringTimeDate());
        cv.put(COLUMN_DIARY_BLOODSUGAR,entry.getBs());
        cv.put(COLUMN_DIARY_RAPID_INSULIN,entry.getRapid_insulin());
        cv.put(COLUMN_DIARY_BASAL_INSULIN,entry.getBasal_insulin());
        cv.put(COLUMN_DIARY_WEIGHT,entry.getWeight());
        cv.put(COLUMN_DIARY_CATEGORY,entry.getCategory());
        cv.put(COLUMN_DIARY_MEAL,entry.getMeal().getJSON());

        if(entry.getId() != -1) {
            // Entry already exists in database
            insert = db.update(DIARY_TABLE, cv, "id = " + entry.getId(),null);
        }
        else {
            insert = db.insert(DIARY_TABLE, null, cv);
        }

        //clean up
        db.close();

        return insert!=-1;
    }

    public List<DiaryEntry> getAllEntry(List<FoodEntry> food) {
        List<DiaryEntry> returnList = new ArrayList<>();
        SQLiteDatabase db;
        Cursor cursor;

        // get data from database
        String queryString = "SELECT * FROM " + DIARY_TABLE;

        db = this.getReadableDatabase();
        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                DiaryEntry newEntry = convertToObject(cursor, food);
                returnList.add(newEntry);
            } while (cursor.moveToNext());
        }

        // clean up
        cursor.close();
        db.close();

        return returnList;
    }

    public List<DiaryEntry> getEntryWhere(String condition, List<FoodEntry> food) {
        List<DiaryEntry> returnList = new ArrayList<>();
        SQLiteDatabase db;
        Cursor cursor;

        // get data from database
        String queryString = "SELECT * FROM " + DIARY_TABLE + " " + condition;

        db = this.getReadableDatabase();
        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                DiaryEntry newEntry = convertToObject(cursor, food);
                returnList.add(newEntry);
            } while (cursor.moveToNext());
        }

        // clean up
        cursor.close();
        db.close();

        return returnList;
    }

    public void deleteEntry(DiaryEntry entry) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DIARY_TABLE, "id =" + entry.getId(), null);

        db.close();
    }

    private DiaryEntry convertToObject(Cursor cursor, List<FoodEntry> food) {
        int entryID = cursor.getInt(0);
        Date entryDate;
        try {
            String debug = cursor.getString(1);
            entryDate = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault()).parse(debug);
        }
        catch (ParseException e)
        {
            entryDate = new Date(0,0,0,0,0,0);
        }
        double entryBS = cursor.getDouble(2);
        int entryRapid_Insulin = cursor.getInt(3);
        int entryBasal_Insulin = cursor.getInt(4);
        double entryWeight = cursor.getDouble(5);
        String entryCategory = cursor.getString(6);
        Meal meal = new Meal(cursor.getString(7), food);

        return new DiaryEntry(entryID, entryDate, entryBS,
                entryRapid_Insulin, entryBasal_Insulin, entryWeight, entryCategory, meal);
    }
}
