package com.example.diabetus.screens.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.diabetus.screens.main.MainView;
import com.example.diabetus.screens.main.MainViewImpl;
import com.example.diabetus.screens.signin.SignInView;
import com.example.diabetus.screens.signin.SignInViewImpl;

public class ViewMvcFactory {

    private final LayoutInflater mLayoutInflater;

    public ViewMvcFactory(LayoutInflater layoutInflater) {
        mLayoutInflater = layoutInflater;
    }

    public MainView getMainViewMvc (ViewGroup parent) {
        return new MainViewImpl(mLayoutInflater, parent);
    }

    public SignInView getSignInViewMvc (ViewGroup parent) {
        return new SignInViewImpl(mLayoutInflater, parent);
    }

}
