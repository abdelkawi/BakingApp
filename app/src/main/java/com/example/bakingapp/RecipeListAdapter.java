package com.example.bakingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.model.RecipeItem;
import com.example.bakingapp.interfaces.OnRecipeClicked;

public class RecipeListAdapter extends ListAdapter<RecipeItem, RecipeListAdapter.RecipeViewHolder> {
    OnRecipeClicked onRecipeClicked;
    public RecipeListAdapter(OnRecipeClicked onRecipeClicked) {
        super(DIFF_CALLBACK);
        this.onRecipeClicked= onRecipeClicked;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false),onRecipeClicked);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static final DiffUtil.ItemCallback<RecipeItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<RecipeItem>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull RecipeItem oldRecipe, @NonNull RecipeItem newRecipe) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldRecipe.getId() == newRecipe.getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull RecipeItem oldRecipe, @NonNull RecipeItem newRecipe) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldRecipe.getId() == newRecipe.getId();
                }
            };

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mRecipeNameTv;
        OnRecipeClicked onRecipeClicked;

        RecipeViewHolder(View inflate, OnRecipeClicked onRecipeClicked) {
            super(inflate);
            mRecipeNameTv = inflate.findViewById(R.id.tv_recipe_name_display);
            inflate.setOnClickListener(this);
            this.onRecipeClicked = onRecipeClicked;
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            RecipeItem clicked = getItem(getAdapterPosition());
            switch (v.getId()) {
                default:
                    onRecipeClicked.onRecipeClicked(clicked.getId());
            }
        }

        public void bind(RecipeItem item) {
            mRecipeNameTv.setText(item.getName());
        }
    }
}
