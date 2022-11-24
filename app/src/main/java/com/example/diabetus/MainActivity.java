package com.example.diabetus;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;

import com.example.diabetus.UI.PieChart;
import com.example.diabetus.database.DbHelper;
import com.example.diabetus.database.DiaryDbHelper;
import com.example.diabetus.databinding.ActivityMainBinding;
import com.example.diabetus.diary.DiaryActivity;
import com.example.diabetus.diary.DiaryEntry;
import com.example.diabetus.food.FoodActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private final MutableLiveData<List<DiaryEntry>> Entries = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.diabetus.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btn_Diary = findViewById(R.id.btn_Diary);
        btn_Diary.setOnClickListener(this::goToDiaryActivity);

        Button btn_Food = findViewById(R.id.btn_Food);
        btn_Food.setOnClickListener(this::goToFoodActivity);

        DrawerLayout drawerLayout = findViewById(R.id.main_drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = findViewById(R.id.nav_menu);
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case (R.id.action_diary):
                    goToDiaryActivity(null);
                    break;
                case (R.id.action_food):
                    goToFoodActivity(null);
                    break;
                case (R.id.action_signin):
                    goToSignInActivity(null);
                    break;
            }
            return true;
        });

        Entries.observe(this, diaryEntries -> {
            diaryEntries.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()) * -1);
            showAverages(diaryEntries);
            showMacroStatistics(diaryEntries);
        });
        getLast90Days();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToDiaryActivity(View view) {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    public void goToFoodActivity(View view) {
        Intent intent = new Intent(this, FoodActivity.class);
        startActivity(intent);
    }

    public void goToSignInActivity(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void showAverages (List<DiaryEntry> list) {
        int[] time = {7, 30, 90};
        int[] chart = {R.id.PieChart_7day, R.id.PieChart_30day, R.id.PieChart_90day};
        int[] normal = {0, 0, 0};
        int[] low = {0, 0, 0};
        int[] high = {0, 0, 0};
        int[] too_high = {0, 0, 0};
        int[] too_low = {0, 0, 0};
        double[] average = {0, 0, 0};

        Date currentDate = Calendar.getInstance().getTime();

        for (DiaryEntry entry : list) {
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


        ArrayList<Integer> color = new ArrayList<>();
        color.add(ContextCompat.getColor(this, R.color.purple_500));
        color.add(ContextCompat.getColor(this, R.color.purple_200));
        color.add(ContextCompat.getColor(this, R.color.green));
        color.add(ContextCompat.getColor(this, R.color.orange));
        color.add(ContextCompat.getColor(this, R.color.red));

        for (int i = 0; i < average.length; i++) {
            average[i] /= too_low[i] + low[i] + normal[i] + high[i] + too_high[i];

            ArrayList<Integer> tmp = new ArrayList<>();
            tmp.add(too_low[i]);
            tmp.add(low[i]);
            tmp.add(normal[i]);
            tmp.add(high[i]);
            tmp.add(too_high[i]);

            PieChart pie = findViewById(chart[i]);
            pie.set(tmp, color, String.format(Locale.getDefault(), "%.1f", average[i]));
        }
    }

    private void showMacroStatistics (List<DiaryEntry> list) {
        double cal = 0.0;
        double carb = 0.0;
        double protein = 0.0;
        double fat = 0.0;
        double fiber = 0.0;
        int[] time = {0, 7, 30, 90};
        boolean[] set = {false, false, false, false};

        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.HOUR_OF_DAY, 23 );
        calendar.set( Calendar.MINUTE, 59 );
        calendar.set( Calendar.SECOND, 59 );
        Date currentDate = calendar.getTime();

        if (list.size() > 0) {
            for (DiaryEntry entry : list) {
                long daysDiff = TimeUnit.DAYS.convert( currentDate.getTime() - entry.getDate().getTime(), TimeUnit.MILLISECONDS);

                if ((daysDiff > time[0]) && !set[0]) {
                    //today
                    set[0] = true;
                    TextView et_main_cal = findViewById(R.id.et_main_cal_today);
                    et_main_cal.setText(String.format(Locale.getDefault(), "%.1f", cal));
                    TextView et_main_carb = findViewById(R.id.et_main_carb_today);
                    et_main_carb.setText(String.format(Locale.getDefault(), "%.1f", carb));
                    TextView et_main_prot = findViewById(R.id.et_main_prot_today);
                    et_main_prot.setText(String.format(Locale.getDefault(), "%.1f", protein));
                    TextView et_main_fat = findViewById(R.id.et_main_fat_today);
                    et_main_fat.setText(String.format(Locale.getDefault(), "%.1f", fat));
                    TextView et_main_fiber = findViewById(R.id.et_main_fiber_today);
                    et_main_fiber.setText(String.format(Locale.getDefault(), "%.1f", fiber));
                }

                if ((daysDiff > time[1]) && !set[1]) {
                    //7 day
                    set[1] = true;
                    TextView et_main_cal = findViewById(R.id.et_main_cal_7day);
                    et_main_cal.setText(String.format(Locale.getDefault(), "%.1f", cal/time[1]));
                    TextView et_main_carb = findViewById(R.id.et_main_carb_7day);
                    et_main_carb.setText(String.format(Locale.getDefault(), "%.1f", carb/time[1]));
                    TextView et_main_prot = findViewById(R.id.et_main_prot_7day);
                    et_main_prot.setText(String.format(Locale.getDefault(), "%.1f", protein/time[1]));
                    TextView et_main_fat = findViewById(R.id.et_main_fat_7day);
                    et_main_fat.setText(String.format(Locale.getDefault(), "%.1f", fat/time[1]));
                    TextView et_main_fiber = findViewById(R.id.et_main_fiber_7day);
                    et_main_fiber.setText(String.format(Locale.getDefault(), "%.1f", fiber/time[1]));
                }

                if ((daysDiff > time[2]) && !set[2]) {
                    //30 day
                    set[2] = true;
                    TextView et_main_cal = findViewById(R.id.et_main_cal_30day);
                    et_main_cal.setText(String.format(Locale.getDefault(), "%.1f", cal/time[2]));
                    TextView et_main_carb = findViewById(R.id.et_main_carb_30day);
                    et_main_carb.setText(String.format(Locale.getDefault(), "%.1f", carb/time[2]));
                    TextView et_main_prot = findViewById(R.id.et_main_prot_30day);
                    et_main_prot.setText(String.format(Locale.getDefault(), "%.1f", protein/time[2]));
                    TextView et_main_fat = findViewById(R.id.et_main_fat_30day);
                    et_main_fat.setText(String.format(Locale.getDefault(), "%.1f", fat/time[2]));
                    TextView et_main_fiber = findViewById(R.id.et_main_fiber_30day);
                    et_main_fiber.setText(String.format(Locale.getDefault(), "%.1f", fiber/time[2]));
                }

                if ((daysDiff > time[3]) && !set[3]) {
                    //90 day
                    set[3] = true;
                    TextView et_main_cal = findViewById(R.id.et_main_cal_90day);
                    et_main_cal.setText(String.format(Locale.getDefault(), "%.1f", cal/time[3]));
                    TextView et_main_carb = findViewById(R.id.et_main_carb_90day);
                    et_main_carb.setText(String.format(Locale.getDefault(), "%.1f", carb/time[3]));
                    TextView et_main_prot = findViewById(R.id.et_main_prot_90day);
                    et_main_prot.setText(String.format(Locale.getDefault(), "%.1f", protein/time[3]));
                    TextView et_main_fat = findViewById(R.id.et_main_fat_90day);
                    et_main_fat.setText(String.format(Locale.getDefault(), "%.1f", fat/time[3]));
                    TextView et_main_fiber = findViewById(R.id.et_main_fiber_90day);
                    et_main_fiber.setText(String.format(Locale.getDefault(), "%.1f", fiber/time[3]));
                }

                cal += entry.getMeal().getCalorie();
                carb += entry.getMeal().getCarb();
                protein += entry.getMeal().getProtein();
                fat += entry.getMeal().getFat();
                fiber += entry.getMeal().getFiber();
            }

        }
    }

    private void getLast90Days()
    {
        Runnable runnable = () -> {
            DbHelper dbh = new DbHelper(getBaseContext(), null, 1);
            Entries.postValue(dbh.getDiaryEntryWhere(" " +
                    "WHERE " +
                    "substr(REPLACE( + " + DiaryDbHelper.COLUMN_DIARY_TIMEDATE + ",'.','-'),1,10)" +
                    "BETWEEN date('now','localtime','-90 days') AND date('now','localtime')"));
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}