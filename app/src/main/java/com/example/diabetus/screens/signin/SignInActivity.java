package com.example.diabetus.screens.signin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.diabetus.R;
import com.example.diabetus.screens.common.ViewMvcFactory;
import com.example.diabetus.screens.diary.DiaryActivity;
import com.example.diabetus.database.food.FoodActivity;
import com.example.diabetus.network.google.DriveServiceHelper;
import com.example.diabetus.screens.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.material.navigation.NavigationView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity implements SignInView.Listener {
    private DriveServiceHelper driveServiceHelper;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private final GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
            .build();

    SignInView mView;

    ActivityResultLauncher<Intent> signInResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    handleSignInIntent(result.getData());
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = new ViewMvcFactory(getLayoutInflater()).getSignInViewMvc(null);
        mView.registerListener(this);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            mView.userLoggedOut();
        }
        else {
            mView.userLoggedIn(account.getDisplayName());
            connectToDrive(account);
        }

        DrawerLayout drawerLayout = findViewById(R.id.signin_drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView mNavigationView = findViewById(R.id.nav_menu);
        mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()){
                case(R.id.action_home):
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                case(R.id.action_food):
                    intent = new Intent(this, FoodActivity.class);
                    startActivity(intent);
                    break;
                case(R.id.action_diary):
                    intent = new Intent(this, DiaryActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(googleSignInAccount -> {
                    connectToDrive(googleSignInAccount);
                    Intent intent = getIntent();
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    private void connectToDrive(GoogleSignInAccount googleSignInAccount) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(SignInActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));

        credential.setSelectedAccount(googleSignInAccount.getAccount());

        Drive googleDriveService = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName(getString(R.string.app_name)).build();

        driveServiceHelper = new DriveServiceHelper(googleDriveService);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUploadClicked() {
        ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Uploading database");
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        driveServiceHelper.createFolder("Diabetus")
                .addOnSuccessListener(folderId -> {
                    String filePath = getFilesDir().getParent()+"/databases/Diary";
                    driveServiceHelper.UploadDatabase(filePath, folderId)
                            .addOnFailureListener(e -> mView.uploadFailed("Diary upload failed"));
                    filePath = getFilesDir().getParent()+"/databases/Meal";
                    driveServiceHelper.UploadDatabase(filePath, folderId)
                            .addOnFailureListener(e -> mView.uploadFailed("Meal upload failed"));

                    filePath = getFilesDir().getParent()+"/databases/Food";
                    driveServiceHelper.UploadDatabase(filePath, folderId)
                            .addOnFailureListener(e -> mView.uploadFailed("Food upload failed"));

                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    mView.uploadFailed("Folder could not be created");
                });
    }

    @Override
    public void onSignInClicked() {
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);
        signInResultLauncher.launch(client.getSignInIntent());
    }

    @Override
    public void onSignOutClicked() {
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);
        client.signOut().addOnCompleteListener(this, task -> mView.userLoggedOut());
    }
}
