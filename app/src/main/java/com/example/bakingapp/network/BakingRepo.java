package com.example.bakingapp.network;

import androidx.lifecycle.MutableLiveData;

import com.example.bakingapp.model.RecipeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BakingRepo {

    private ApiInterface apiInterface;

    public BakingRepo() {
        apiInterface = NetworkUtils.getClient().create(ApiInterface.class);
    }

    public MutableLiveData<List<RecipeItem>> getRecipes() {
        final MutableLiveData<List<RecipeItem>> mutableLiveData = new MutableLiveData<>();
        apiInterface.getData().enqueue(new Callback<List<RecipeItem>>() {
            @Override
            public void onResponse(Call<List<RecipeItem>> call, Response<List<RecipeItem>> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<RecipeItem>> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }


}
