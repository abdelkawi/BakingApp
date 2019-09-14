package com.example.bakingapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bakingapp.data.IngredientsItem;
import com.example.bakingapp.data.RecipeItem;
import com.example.bakingapp.data.StepsItem;
import com.example.bakingapp.network.BakingRepo;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private BakingRepo mBakingRepo;
    private MutableLiveData<List<RecipeItem>> recipesLiveData;

    static RecipeViewModel recipeViewModel;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        mBakingRepo = new BakingRepo();
    }

    public static RecipeViewModel getInstance(Application application) {
        if (recipeViewModel == null)
            recipeViewModel = new RecipeViewModel(application);
        return recipeViewModel;
    }

    public MutableLiveData<List<RecipeItem>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public List<IngredientsItem> getRecipeIngredients(int pos) {
        return recipesLiveData.getValue().get(pos).getIngredients();
    }
    public StepsItem getCurrentStep (int recipePos,int stepPos){
        return recipesLiveData.getValue().get(recipePos).getSteps().get(stepPos);
    }

    public RecipeItem getCurrentRecipe (int recipePos){
        return recipesLiveData.getValue().get(recipePos);
    }
    public void getRecipes() {
        recipesLiveData = mBakingRepo.getRecipes();
    }



}
