package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.interfaces.OnRecipeClicked;
import com.example.bakingapp.network.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements OnRecipeClicked {

    @BindView(R.id.rv_recipes_display)
    RecyclerView mRecyclerView;
    RecipesViewModel recipesViewModel;

    final String CURRENT_LIST_POSITION = "list_pos";
    @BindView(R.id.tv_retry_display)
    TextView mRetryTV;
    @BindView(R.id.pb_loading_display)
    ProgressBar mLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(getResources().getBoolean(R.bool.isTablet))
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        loadData();
    }

    private void loadData() {
        showLoading();
        recipesViewModel = RecipesViewModel.getInstance(getApplication());
        RecipeListAdapter adapter = new RecipeListAdapter(this);
        if (NetworkUtils.isConnect(this)) {
            recipesViewModel.getRecipesLiveData().observe(this, list -> {
                        if (list != null) {
                            Log.wtf("xxxxxx",list.toString());
                            adapter.submitList(list);
                            recipesViewModel.insertRecipesToDb();
                            showData();
                        } else {

                            showError();
                        }
                    }
            );
        } else {
            recipesViewModel.getRecipesFromDb().observe(this, list -> {
                adapter.submitList(list);
                showData();
           //     showError();

            });
        }
        mRecyclerView.setAdapter(adapter);
    }

    void showData() {
        mLoading.setVisibility(View.GONE);
        mRetryTV.setVisibility(View.GONE);
    }

    void showError() {
        mLoading.setVisibility(View.GONE);
        mRetryTV.setVisibility(View.VISIBLE);
        Snackbar mySnackbar = Snackbar.make(mRetryTV,
                R.string.error_message, Snackbar.LENGTH_INDEFINITE);
        mySnackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
        mySnackbar.show();
    }

    void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
        mRetryTV.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeClicked(int recipeId) {
        Intent intent = new Intent(this, StepListActivity.class);
        intent.putExtra(StepDetailFragment.ARG_RECIPE_ID, recipeId);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_LIST_POSITION, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState);
    }


}
