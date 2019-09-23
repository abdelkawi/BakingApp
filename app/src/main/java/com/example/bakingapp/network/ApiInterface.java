package com.example.bakingapp.network;

import com.example.bakingapp.model.RecipeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("baking.json")
    Call<List<RecipeItem>> getData();
}
