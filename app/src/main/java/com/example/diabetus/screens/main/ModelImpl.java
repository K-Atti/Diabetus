package com.example.diabetus.screens.main;

import android.content.Context;

import com.example.diabetus.database.DbHelper;
import com.example.diabetus.database.diary.DiaryEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ModelImpl {

    private final List<DiaryEntry> mDiaryEntries;

    public ModelImpl(Context context) {
        DbHelper mDbHelper = new DbHelper(context, null, 1);
        mDiaryEntries = mDbHelper.getLast90DaysEntries();
        mDiaryEntries.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()) * -1);
    }

    public ArrayList<MainView.MacroData> getMacroStatistics() {
        double cal = 0.0;
        double carb = 0.0;
        double protein = 0.0;
        double fat = 0.0;
        double fiber = 0.0;
        int[] time = {0, 7, 30, 90};
        ArrayList<MainView.MacroData> macroData = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY, 23 );
        calendar.set( Calendar.MINUTE, 59 );
        calendar.set( Calendar.SECOND, 59 );
        Date currentDate = calendar.getTime();

        for (DiaryEntry entry : mDiaryEntries) {
            long daysDiff = TimeUnit.DAYS.convert(currentDate.getTime() - entry.getDate().getTime(), TimeUnit.MILLISECONDS);

            if ((daysDiff > time[0]) && (macroData.size() == 0)) {
                //today
                macroData.add(new MainView.MacroData(cal, carb, protein, fat, fiber));
            }

            if ((daysDiff > time[1]) && (macroData.size() == 1)) {
                //7 day
                macroData.add(new MainView.MacroData(cal / time[1], carb / time[1], protein / time[1], fat / time[1], fiber / time[1]));
            }

            if ((daysDiff > time[2]) && (macroData.size() == 2)) {
                //30 day
                macroData.add(new MainView.MacroData(cal / time[2], carb / time[2], protein / time[2], fat / time[2], fiber / time[2]));
            }

            if ((daysDiff > time[3]) && (macroData.size() == 3)) {
                //90 day
                macroData.add(new MainView.MacroData(cal / time[3], carb / time[3], protein / time[3], fat / time[3], fiber / time[3]));
            }

            cal += entry.getMeal().getCalorie();
            carb += entry.getMeal().getCarb();
            protein += entry.getMeal().getProtein();
            fat += entry.getMeal().getFat();
            fiber += entry.getMeal().getFiber();
        }

        for (int i = 0; i < (4 - macroData.size()); i++) {
            int x = macroData.size();
            macroData.add(new MainView.MacroData(cal / time[x], carb / time[x], protein / time[x], fat / time[x], fiber / time[x]));
        }
        return macroData;
    }

    public ArrayList<MainView.PieChartData> getAverageBs() {
        int[] time = {7, 30, 90};
        int[] normal = {0, 0, 0};
        int[] low = {0, 0, 0};
        int[] high = {0, 0, 0};
        int[] too_high = {0, 0, 0};
        int[] too_low = {0, 0, 0};
        double[] average = {0, 0, 0};
        ArrayList<MainView.PieChartData> pieChartData = new ArrayList<>();

        Date currentDate = Calendar.getInstance().getTime();

        for (DiaryEntry entry : mDiaryEntries) {
            if (entry.getBs() != 0) {
                long daysDiff = TimeUnit.DAYS.convert(currentDate.getTime() - entry.getDate().getTime(), TimeUnit.MILLISECONDS);
                int chartIndex;

                if (daysDiff < time[0]) {
                    //7 days
                    chartIndex = 0;
                } else if (daysDiff < time[1]) {
                    //30 days
                    chartIndex = 1;
                } else {
                    //90 days
                    chartIndex = 2;
                }

                if (entry.getBs() >= 10) {
                    too_high[chartIndex]++;
                } else if (entry.getBs() >= 6) {
                    high[chartIndex]++;
                } else if (entry.getBs() < 3) {
                    too_low[chartIndex]++;
                } else if (entry.getBs() < 4) {
                    low[chartIndex]++;
                } else {
                    normal[chartIndex]++;
                }

                average[chartIndex] += entry.getBs();
            }
        }

        too_low[1] += too_low[0];
        too_low[2] += too_low[1];
        low[1] += low[0];
        low[2] += low[1];
        normal[1] += normal[0];
        normal[2] += normal[1];
        high[1] += high[0];
        high[2] += high[1];
        too_high[1] += too_high[0];
        too_high[2] += too_high[1];
        average[1] += average[0];
        average[2] += average[1];

        for (int i = 0; i < average.length; i++) {
            average[i] /= too_low[i] + low[i] + normal[i] + high[i] + too_high[i];
            pieChartData.add(new MainView.PieChartData(average[i], too_low[i], low[i], normal[i], high[i], too_high[i]));
        }
        return pieChartData;
    }
}
