package com.example.diabetus.diary;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.diabetus.food.Food;
import com.example.diabetus.food.Meal;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryEntry implements Parcelable {
    private int id;
    private Date date;
    private double bs;
    private int rapid_insulin;
    private int basal_insulin;
    private double weight;
    private String category;
    private Meal meal;

    // constructors
    public DiaryEntry() {
        id = -1;
    }

    public DiaryEntry(int id, Date date, double bs, int rapid_insulin, int basal_insulin, double weight, String category, Meal meal) {
        this.id = id;
        this.date = date;
        this.bs = bs;
        this.rapid_insulin = rapid_insulin;
        this.basal_insulin = basal_insulin;
        this.weight = weight;
        this.category = category;
        this.meal = meal;
    }

    public DiaryEntry(Date date) {
        this.date = date;
    }

    protected DiaryEntry(Parcel in) {
        id = in.readInt();
        bs = in.readDouble();
        date = new Date(in.readLong());
        rapid_insulin = in.readInt();
        basal_insulin = in.readInt();
        weight = in.readDouble();
        category = in.readString();
        meal = in.readParcelable(Meal.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(bs);
        dest.writeLong(date.getTime());
        dest.writeInt(rapid_insulin);
        dest.writeInt(basal_insulin);
        dest.writeDouble(weight);
        dest.writeString(category);
        dest.writeParcelable(meal, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DiaryEntry> CREATOR = new Creator<DiaryEntry>() {
        @Override
        public DiaryEntry createFromParcel(Parcel in) {
            return new DiaryEntry(in);
        }

        @Override
        public DiaryEntry[] newArray(int size) {
            return new DiaryEntry[size];
        }
    };

    // getter functions
    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public  String getStringTimeDate() {
        SimpleDateFormat dateF = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return dateF.format(date);
    }
    public  String getStringTime() {
        SimpleDateFormat dateF = new SimpleDateFormat("HH:mm");
        return dateF.format(date);
    }

    public  String getStringDate() {
        SimpleDateFormat dateF = new SimpleDateFormat("yyyy.MM.dd");
        return dateF.format(date);
    }

    public double getBs() {
        return bs;
    }

    public int getRapid_insulin() {
        return rapid_insulin;
    }

    public int getBasal_insulin() {
        return basal_insulin;
    }

    public double getWeight() {
        return weight;
    }

    public String getCategory() {
        return category;
    }

    public Meal getMeal() {
        return meal;
    }

    // setter functions
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBs(double bs) {
        this.bs = bs;
    }

    public void setRapid_insulin(int rapid_insulin) {
        this.rapid_insulin = rapid_insulin;
    }

    public void setBasal_insulin(int basal_insulin) {
        this.basal_insulin = basal_insulin;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public void addMeal(Meal meal) {
        this.meal.setName(this.meal.getName() + " + " + meal.getName());
        for (Food food: meal.getFoodList()) {
            this.meal.addFood(food.getFood(), food.getQuantity(), food.getQuantityType());
        }
    }

    //toString() is necessary for debug reasons
    @Override
    public String toString() {
        return "DiaryEntry{" +
                "id=" + id +
                ", date=" + date +
                ", bs=" + bs +
                ", rapid_insulin=" + rapid_insulin +
                ", basal_insulin=" + basal_insulin +
                ", weight=" + weight +
                ", category=" + category +
                ", meal=" + meal +
                '}';
    }
}
