package com.example.diabetus.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.diabetus.MainActivity;
import com.example.diabetus.R;
import com.example.diabetus.SignInActivity;
import com.example.diabetus.databinding.ActivityFoodBinding;
import com.example.diabetus.diary.DiaryActivity;
import com.google.android.material.navigation.NavigationView;

public class FoodActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.diabetus.databinding.ActivityFoodBinding binding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawerLayout = findViewById(R.id.food_drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = findViewById(R.id.nav_menu);
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()){
                case(R.id.action_home):
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                case(R.id.action_diary):
                    intent = new Intent(this, DiaryActivity.class);
                    startActivity(intent);
                    break;
                case(R.id.action_signin):
                    intent = new Intent(this, SignInActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}