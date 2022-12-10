package com.example.diabetus.usda;

import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class USDAApi {
    static final String baseURL = "https://api.nal.usda.gov/";
    private static Retrofit retrofit = null;

    public void getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl("baseURL")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getFood(String text, Callback<UsdaData> callback) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsdaService usdaService = retrofit.create(UsdaService.class);
        Call<UsdaData> call = usdaService.getFood(text);
        call.enqueue(callback);
    }

    public void getFoodList(String text, Callback<UsdaSearchResult> callback) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsdaService usdaService = retrofit.create(UsdaService.class);
        Call<UsdaSearchResult> call = usdaService.getFoodList(text);
        call.enqueue(callback);
    }
}
