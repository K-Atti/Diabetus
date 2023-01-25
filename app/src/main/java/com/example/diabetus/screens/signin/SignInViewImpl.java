package com.example.diabetus.screens.signin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diabetus.R;
import com.example.diabetus.screens.common.BaseObservableView;

import javax.annotation.Nullable;

public class SignInViewImpl extends BaseObservableView<SignInView.Listener> implements SignInView {

    String mUserName;
    Button btn_upload, btn_sign_in, btn_sign_out;
    TextView text_signIn;

    public SignInViewImpl(LayoutInflater layoutInflater,
                          @Nullable ViewGroup parent) {
        setRootView(layoutInflater.inflate(R.layout.activity_signin, parent, false));
        mUserName ="";

        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(v -> onUploadClicked());

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(v -> onSignInClicked());

        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(v -> onSignOutClicked());

        text_signIn = findViewById(R.id.signin_welcome);
        text_signIn.setText(String.format(getString(R.string.hello), "please sign in"));
    }

    private void onSignOutClicked() {
        for(Listener listener : getListeners()) {
            listener.onSignOutClicked();
        }
    }

    private void onSignInClicked() {
        for(Listener listener : getListeners()) {
            listener.onSignInClicked();
        }
    }

    private void onUploadClicked() {
        for(Listener listener : getListeners()) {
            listener.onUploadClicked();
        }
    }

    @Override
    public void userLoggedIn(String name) {
        mUserName = name;
        userLoggedIn();
    }

    @Override
    public void userLoggedOut() {
        mUserName = "";
        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        noOneLoggedIn();
    }

    @Override
    public void uploadFailed(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void noOneLoggedIn() {
        text_signIn.setText(String.format(getString(R.string.hello), "please sign in"));
        btn_upload.setVisibility(View.INVISIBLE);
        btn_sign_in.setVisibility(View.VISIBLE);
        btn_sign_out.setVisibility(View.INVISIBLE);
    }

    private void userLoggedIn() {
        text_signIn.setText(String.format(getString(R.string.hello), mUserName));
        btn_upload.setVisibility(View.VISIBLE);
        btn_sign_in.setVisibility(View.INVISIBLE);
        btn_sign_out.setVisibility(View.VISIBLE);
    }
}
