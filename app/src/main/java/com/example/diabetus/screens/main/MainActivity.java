package com.example.diabetus.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.diabetus.R;
import com.example.diabetus.screens.common.ViewMvcFactory;
import com.example.diabetus.screens.diary.DiaryActivity;
import com.example.diabetus.screens.signin.SignInActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements MainView.Listener {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private MainView mViewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewMvcFactory viewMvcFactory = new ViewMvcFactory(LayoutInflater.from(this));
        mViewMvc = viewMvcFactory.getMainViewMvc(null);
        setContentView(mViewMvc.getRootView());
        mViewMvc.registerListener(this);

        DrawerLayout drawerLayout = findViewById(R.id.main_drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = findViewById(R.id.nav_menu);
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case (R.id.action_diary):
                    goToDiaryActivity();
                    break;
                case (R.id.action_signin):
                    goToSignInActivity(null);
                    break;
            }
            return true;
        });

        Runnable runnable = () -> {
            ModelImpl model = new ModelImpl(this);
            ArrayList<MainView.MacroData> macroData = model.getMacroStatistics();
            ArrayList<MainView.PieChartData> pieChartData = model.getAverageBs();
            runOnUiThread(() -> {
                mViewMvc.showAverages(pieChartData);
                mViewMvc.showMacroStatistics(macroData);
            });
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToDiaryActivity() {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    public void goToSignInActivity(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void onDiaryClicked() {
        goToDiaryActivity();
    }
}