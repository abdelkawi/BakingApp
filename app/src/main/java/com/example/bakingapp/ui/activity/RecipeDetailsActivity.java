package com.example.bakingapp.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.IngredientsItem;
import com.example.bakingapp.data.RecipeItem;
import com.example.bakingapp.ui.OnStepClicked;
import com.example.bakingapp.ui.RecipeDetailFragment;
import com.example.bakingapp.ui.StepAdapter;
import com.example.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements OnStepClicked {
    private RecyclerView mRecipeRv;
    private TextView mIngredientTv;
    private int recipePos = 0;
    private RecipeItem mCurrentRecipe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        mRecipeRv = findViewById(R.id.rv_recipe_step_list);
        mIngredientTv = findViewById(R.id.tv_Ingredient);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();

        recipePos = getIntent().getIntExtra(RecipeDetailFragment.ARG_RECIPE, 0);
        mCurrentRecipe = RecipeViewModel.getInstance(getApplication()).getCurrentRecipe(recipePos);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mCurrentRecipe.getName());
        }
        mRecipeRv.setAdapter(new StepAdapter(
                mCurrentRecipe.getSteps(), this));
        mIngredientTv.setText(getIngredientsText(mCurrentRecipe.getIngredients()));

    }

    String getIngredientsText(List<IngredientsItem> ingredientsItems) {
        String ingredientsTxt = getString(R.string.ingredients) + "\n";
        for (IngredientsItem ingredientsItem : ingredientsItems)
            ingredientsTxt += ingredientsItem.getQuantity() + " " + ingredientsItem.getMeasure() + " " + ingredientsItem.getIngredient() + "\n";
        return ingredientsTxt;
    }


    @Override
    public void onStepClicked(int stepPos) {
        Bundle arguments = new Bundle();
        arguments.putInt(RecipeDetailFragment.ARG_STEP, stepPos);
        arguments.putInt(RecipeDetailFragment.ARG_RECIPE, recipePos);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(arguments);

        if (findViewById(R.id.item_detail_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        }
    }
}
