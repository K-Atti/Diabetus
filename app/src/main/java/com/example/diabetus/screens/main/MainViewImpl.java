package com.example.diabetus.screens.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.diabetus.R;
import com.example.diabetus.UI.PieChart;
import com.example.diabetus.screens.common.BaseObservableView;

import java.util.ArrayList;
import java.util.Locale;

import javax.annotation.Nullable;

public class MainViewImpl extends BaseObservableView<MainView.Listener> implements MainView {

    public MainViewImpl(LayoutInflater layoutInflater,
                        @Nullable ViewGroup parent) {
        setRootView(layoutInflater.inflate(R.layout.activity_main, parent, false));

        Button btn_Diary = findViewById(R.id.btn_Diary);
        btn_Diary.setOnClickListener(v -> onDiaryClicked());
    }

    public void onDiaryClicked() {
        for (Listener listener : getListeners()) {
            listener.onDiaryClicked();
        }
    }

    @Override
    public void showAverages(ArrayList<PieChartData> dataList) {
        if (dataList.size() == 3) {
            int[] chart = {R.id.PieChart_7day, R.id.PieChart_30day, R.id.PieChart_90day};

            ArrayList<Integer> color = new ArrayList<>();
            color.add(ContextCompat.getColor(getContext(), R.color.purple_500));
            color.add(ContextCompat.getColor(getContext(), R.color.purple_200));
            color.add(ContextCompat.getColor(getContext(), R.color.green));
            color.add(ContextCompat.getColor(getContext(), R.color.orange));
            color.add(ContextCompat.getColor(getContext(), R.color.red));

            for (int i = 0; i < dataList.size(); i++) {

                ArrayList<Integer> tmp = new ArrayList<>();
                tmp.add(dataList.get(i).TooLow);
                tmp.add(dataList.get(i).Low);
                tmp.add(dataList.get(i).Normal);
                tmp.add(dataList.get(i).High);
                tmp.add(dataList.get(i).TooHigh);

                PieChart pie = findViewById(chart[i]);
                pie.set(tmp, color, String.format(Locale.getDefault(), "%.1f", dataList.get(i).Average));
            }
        }
    }

    @Override
    public void showMacroStatistics(ArrayList<MacroData> dataList) {
        int[] calIds = {R.id.et_main_cal_today, R.id.et_main_cal_7day, R.id.et_main_cal_30day, R.id.et_main_cal_90day};
        int[] carbIds = {R.id.et_main_carb_today, R.id.et_main_carb_7day, R.id.et_main_carb_30day, R.id.et_main_carb_90day};
        int[] protIds = {R.id.et_main_prot_today, R.id.et_main_prot_7day, R.id.et_main_prot_30day, R.id.et_main_prot_90day};
        int[] fatIds = {R.id.et_main_fat_today, R.id.et_main_fat_7day, R.id.et_main_fat_30day, R.id.et_main_fat_90day};
        int[] fiberIds = {R.id.et_main_fiber_today, R.id.et_main_fiber_7day, R.id.et_main_fiber_30day, R.id.et_main_fiber_90day};

        if (dataList.size() == 4) {
            for (int i = 0; i < 4; i++) {
                MacroData data = dataList.get(i);
                TextView et_main_cal = findViewById(calIds[i]);
                et_main_cal.setText(String.format(Locale.getDefault(), "%.1f", data.Cal));
                TextView et_main_carb = findViewById(carbIds[i]);
                et_main_carb.setText(String.format(Locale.getDefault(), "%.1f", data.Carb));
                TextView et_main_prot = findViewById(protIds[i]);
                et_main_prot.setText(String.format(Locale.getDefault(), "%.1f", data.Protein));
                TextView et_main_fat = findViewById(fatIds[i]);
                et_main_fat.setText(String.format(Locale.getDefault(), "%.1f", data.Fat));
                TextView et_main_fiber = findViewById(fiberIds[i]);
                et_main_fiber.setText(String.format(Locale.getDefault(), "%.1f", data.Fiber));
            }
        }
    }
}
