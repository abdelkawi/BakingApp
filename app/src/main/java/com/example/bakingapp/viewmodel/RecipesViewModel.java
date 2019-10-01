package com.example.bakingapp.viewmodel;

import android.app.Application;


import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.bakingapp.database.AppDatabase;
import com.example.bakingapp.database.AppExecutors;
import com.example.bakingapp.model.RecipeItem;
import com.example.bakingapp.network.BakingRepo;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {
    private BakingRepo bakingRepo;
    private LiveData<List<RecipeItem>> recipesLiveData = new MutableLiveData<List<RecipeItem>>();
    private static  RecipesViewModel recipesViewModel;
    private AppDatabase appDatabase ;

    public RecipesViewModel(@Nullable Application application) {
        super(application);
        bakingRepo = new BakingRepo();
        recipesLiveData = bakingRepo.getRecipes();
        appDatabase = AppDatabase.getInstance(application);
    }
    public static RecipesViewModel getInstance(Application application){
        if(recipesViewModel==null)
            recipesViewModel = new RecipesViewModel(application);
        return recipesViewModel;
    }

    public RecipeItem getRecipe(int id){
        return appDatabase.taskDAO().getRecipe(id);
    }
    public LiveData<List<RecipeItem>> getRecipesLiveData() {
        return recipesLiveData;
    }

    public LiveData<List<RecipeItem>> getRecipesFromDb(){
        return appDatabase.taskDAO().loadRecipes();
    }
    public void insertRecipesToDb(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for(RecipeItem recipeItem:recipesLiveData.getValue())
                    appDatabase.taskDAO().insertRecipe(recipeItem);
                }
            }
        );
    }




}
