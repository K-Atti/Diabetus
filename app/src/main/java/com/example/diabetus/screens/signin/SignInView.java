package com.example.diabetus.screens.signin;

import com.example.diabetus.screens.common.ObservableView;

public interface SignInView extends ObservableView<SignInView.Listener> {

    interface Listener {
        void onUploadClicked();
        void onSignInClicked();
        void onSignOutClicked();
    }

    void userLoggedIn(String name);

    void userLoggedOut();

    void uploadFailed(String error);
}
