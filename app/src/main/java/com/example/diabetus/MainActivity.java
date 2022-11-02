package com.example.diabetus;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.diabetus.database.DbHelper;
import com.example.diabetus.database.DiaryDbHelper;
import com.example.diabetus.databinding.ActivityMainBinding;
import com.example.diabetus.diary.DiaryActivity;
import com.example.diabetus.diary.DiaryEntry;
import com.example.diabetus.food.FoodActivity;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = findViewById(R.id.nav_menu);
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case(R.id.action_diary):
                    goToDiaryActivity(null);
                    break;
                case(R.id.action_food):
                    goToFoodActivity(null);
                    break;
                case(R.id.action_signin):
                    goToSignInActivity(null);
                    break;
            }
            return true;
        });

        // Collect today's entries
        {
            DbHelper dbh = new DbHelper(this, null, 1);
            SimpleDateFormat dateF = new SimpleDateFormat("yyyy.MM.dd");

            List<DiaryEntry> list = dbh.getDiaryEntryWhere(" WHERE " + DiaryDbHelper.COLUMN_DIARY_TIMEDATE + " LIKE \"%" + dateF.format(new Date()) + "%\"");

            if (list.size() > 0) {
                double cal = 0.0, carb= 0.0, protein= 0.0, fat= 0.0, fiber = 0.0;
                for(DiaryEntry entry : list) {
                    cal += entry.getMeal().getCalorie();
                    carb += entry.getMeal().getCarb();
                    protein += entry.getMeal().getProtein();
                    fat += entry.getMeal().getFat();
                    fiber += entry.getMeal().getFiber();
                }

                TextView et_main_cal = findViewById(R.id.et_main_cal);
                et_main_cal.setText(String.format("%.1f",cal));
                TextView et_main_carb = findViewById(R.id.et_main_carb);
                et_main_carb.setText(String.format("%.1f",carb));
                TextView et_main_prot = findViewById(R.id.et_main_prot);
                et_main_prot.setText(String.format("%.1f",protein));
                TextView et_main_fat = findViewById(R.id.et_main_fat);
                et_main_fat.setText(String.format("%.1f",fat));
                TextView et_main_fiber = findViewById(R.id.et_main_fiber);
                et_main_fiber.setText(String.format("%.1f",fiber));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.menu_main, menu);*/
        return true;
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
}