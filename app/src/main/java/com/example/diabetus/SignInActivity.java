package com.example.diabetus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.diabetus.databinding.ActivitySigninBinding;
import com.example.diabetus.diary.DiaryActivity;
import com.example.diabetus.food.FoodActivity;
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

public class SignInActivity extends AppCompatActivity {
    private DriveServiceHelper driveServiceHelper;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private final GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
            .build();

    TextView text_signIn;
    Button btn_upload, btn_sign_in, btn_sign_out;

    ActivityResultLauncher<Intent> signInResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    handleSignInIntent(result.getData());
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.diabetus.databinding.ActivitySigninBinding binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(this::uploadDatabase);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(v -> requestSignIn());

        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(v -> signOut());

        text_signIn = findViewById(R.id.signin_welcome);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            text_signIn.setText(String.format(getResources().getString(R.string.hello), "please sign in"));
            btn_sign_in.setVisibility(View.VISIBLE);
            btn_sign_out.setVisibility(View.INVISIBLE);
            btn_upload.setVisibility(View.INVISIBLE);
        }
        else {
            text_signIn.setText(String.format(getResources().getString(R.string.hello), account.getDisplayName()));
            btn_sign_in.setVisibility(View.INVISIBLE);
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

    private void requestSignIn() {
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);
        signInResultLauncher.launch(client.getSignInIntent());
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

    public void uploadDatabase(View v) {
        ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Uploading database");
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        driveServiceHelper.createFolder("Diabetus")
                .addOnSuccessListener(folderId -> {
                    String filePath = getFilesDir().getParent()+"/databases/Diary";
                    driveServiceHelper.UploadDatabase(filePath, folderId)
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Diary upload failed", Toast.LENGTH_SHORT).show());
                    filePath = getFilesDir().getParent()+"/databases/Meal";
                    driveServiceHelper.UploadDatabase(filePath, folderId)
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Meal upload failed", Toast.LENGTH_SHORT).show());

                    filePath = getFilesDir().getParent()+"/databases/Food";
                    driveServiceHelper.UploadDatabase(filePath, folderId)
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Food upload failed", Toast.LENGTH_SHORT).show());

                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Folder could not be created", Toast.LENGTH_SHORT).show();
                });
    }

    private void signOut() {
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);
        client.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
            text_signIn.setText(String.format(getResources().getString(R.string.hello), "please sign in"));

            btn_sign_in.setVisibility(View.VISIBLE);
            btn_sign_out.setVisibility(View.INVISIBLE);
            btn_upload.setVisibility(View.INVISIBLE);
        });

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
}
