package com.example.bakingapp.widget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.example.bakingapp.ui.StepDetailFragment;
import com.example.bakingapp.viewmodel.RecipesViewModel;

public class MyWidgetRemoteViewsService extends RemoteViewsService {
SharedPreferences sharedPreferences;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        sharedPreferences = getSharedPreferences(StepDetailFragment.ARG_RECIPE_ID,MODE_PRIVATE);
        int recipeId= sharedPreferences.getInt(StepDetailFragment.ARG_RECIPE_ID,0);
        return new RemoteListViewService(
                 RecipesViewModel.getInstance(getApplication()).getRecipe(recipeId).getIngredients() ,getApplicationContext());
    }
}
