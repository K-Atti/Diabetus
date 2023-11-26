package com.example.diabetus.screens.main;

import com.example.diabetus.screens.common.ObservableView;

import java.util.ArrayList;

public interface MainView extends ObservableView<MainView.Listener> {

    interface Listener {
        void onDiaryClicked();
    }

    void showAverages(ArrayList<PieChartData> dataList);

    void showMacroStatistics(ArrayList<MacroData> dataList);

    class PieChartData {
        double Average;
        int TooLow;
        int Low;
        int Normal;
        int High;
        int TooHigh;

        public PieChartData(double average, int tooLow, int low, int normal, int high, int tooHigh) {
            Average = average;
            TooLow = tooLow;
            Low = low;
            Normal = normal;
            High = high;
            TooHigh = tooHigh;
        }
    }

    class MacroData {
        public double Cal;
        public double Carb;
        public double Protein;
        public double Fat;
        public double Fiber;

        public MacroData(double cal, double carb, double protein, double fat, double fiber) {
            Cal = cal;
            Carb = carb;
            Protein = protein;
            Fat = fat;
            Fiber = fiber;
        }
    }

}
