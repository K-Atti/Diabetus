package com.example.diabetus.network.usda;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsdaService {
    //https://api.nal.usda.gov/fdc/v1/food/2116771?api_key=pXhrcWZoczw198u4kamKmIcFtEhTlQhvueRXxfYz&format=abridged&nutrients=204&nutrients=205&nutrients=203
    @GET("fdc/v1/food/{fdcID}?api_key=DEMO_KEY&format=abridged&nutrients=204&nutrients=205&nutrients=203&nutrients=208&nutrients=605&nutrients=606&nutrients=646&nutrients=291&nutrients=269.3")
    Call<UsdaData> getFood(@Path(value = "fdcID", encoded = true) String fdcID);

    @GET("fdc/v1/foods/search?api_key=DEMO_KEY&nutrients=204&nutrients=205&nutrients=203&nutrients=208&nutrients=605&nutrients=606&nutrients=646&nutrients=291&nutrients=269")
    Call<UsdaSearchResult> getFoodList(@Query("query") String text);
}
