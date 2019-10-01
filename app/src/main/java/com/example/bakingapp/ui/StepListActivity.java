package com.example.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.bakingapp.R;
import com.example.bakingapp.viewmodel.RecipesViewModel;
import com.example.bakingapp.model.IngredientsItem;
import com.example.bakingapp.model.RecipeItem;
import com.example.bakingapp.model.StepsItem;
import com.example.bakingapp.widget.BakingWidget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private static RecipeItem mRecipeItem;
    private RecipesViewModel recipesViewModel;
    private static int sRecipeId = 0;
    private TextView mIngredientsTV;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mIngredientsTV = findViewById(R.id.tv_Ingredient);
       sharedPreferences = getSharedPreferences(StepDetailFragment.ARG_RECIPE_ID,MODE_PRIVATE);
       editor = sharedPreferences.edit();
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        if (getIntent().getIntExtra(StepDetailFragment.ARG_RECIPE_ID, 0) != 0) {
            sRecipeId = getIntent().getIntExtra(StepDetailFragment.ARG_RECIPE_ID, 0);
        }
        recipesViewModel = RecipesViewModel.getInstance(getApplication());
        mRecipeItem = recipesViewModel.getRecipe(sRecipeId);
        mIngredientsTV.setText(getIngredientsText(mRecipeItem.getIngredients()));
        editor.putInt(StepDetailFragment.ARG_RECIPE_ID,sRecipeId);
        editor.commit();
        updateWidget();

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    String getIngredientsText(List<IngredientsItem> ingredientsItems) {
        String ingredientsTxt = getString(R.string.ingredients) + "\n";
        for (IngredientsItem ingredientsItem : ingredientsItems)
            ingredientsTxt += ingredientsItem.getQuantity() + " " + ingredientsItem.getMeasure() + " " + ingredientsItem.getIngredient() + "\n";
        return ingredientsTxt;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mRecipeItem.getSteps(), mTwoPane));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private final List<StepsItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepsItem item = (StepsItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(StepDetailFragment.ARG_RECIPE_ID, mRecipeItem.getId());
                    arguments.putInt(StepDetailFragment.ARG_STEP_ID, item.getId());
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(StepDetailFragment.ARG_RECIPE_ID, mRecipeItem.getId());
                    intent.putExtra(StepDetailFragment.ARG_STEP_ID, item.getId());
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(StepListActivity parent,
                                      List<StepsItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).getShortDescription());
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
            if (mTwoPane && position == 0) {
                holder.itemView.performClick();
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    void updateWidget() {
        Intent intent = new Intent(this, BakingWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication()
                , BakingWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

    }


}
