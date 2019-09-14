package com.example.bakingapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeItem;
import com.example.bakingapp.ui.OnRecipeClicked;
import com.example.bakingapp.ui.RecipeAdapter;
import com.example.bakingapp.ui.RecipeDetailFragment;
import com.example.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRecipeClicked {

    private RecipeAdapter recipeAdapter;
    private RecyclerView mRecipeRecyclerView;
    final String CURRENT_LIST_POSITION = "list_pos";
    private TextView mRetryTV;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecipeRecyclerView = findViewById(R.id.rv_recipes_display);
        mLoading = findViewById(R.id.pb_loading_display);
        mRetryTV = findViewById(R.id.tv_retry_display);
        if (getResources().getBoolean(R.bool.isTablet)) {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        } else {
            mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        recipeAdapter = new RecipeAdapter(this);
        mRecipeRecyclerView.setAdapter(recipeAdapter);
        getData();
    }

    void getData() {
        showLoading();
        RecipeViewModel.getInstance(getApplication()).getRecipes();
        RecipeViewModel.getInstance(getApplication()).getRecipesLiveData().observe(this, new Observer<List<RecipeItem>>() {
            @Override
            public void onChanged(List<RecipeItem> recipeItems) {
                if (recipeItems.size() > 0) {
                    showData();
                    recipeAdapter.setRecipeItems(recipeItems);
                } else {
                    showError();
                }
            }
        });
    }

    void showData() {
        mLoading.setVisibility(View.GONE);
        mRetryTV.setVisibility(View.GONE);
    }

    void showError() {
        mLoading.setVisibility(View.GONE);
        mRetryTV.setVisibility(View.VISIBLE);
    }

    void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
        mRetryTV.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeClicked(int pos) {
        Intent detailIntent = new Intent(this, RecipeDetailsActivity.class);
        detailIntent.putExtra(RecipeDetailFragment.ARG_RECIPE, pos);
        startActivity(detailIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_LIST_POSITION, mRecipeRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState);
    }
}
