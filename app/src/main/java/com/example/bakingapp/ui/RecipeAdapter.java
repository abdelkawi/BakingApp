package com.example.bakingapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.RecipeItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeItemViewHolder>{
    List<RecipeItem> recipeItems = new ArrayList<>();
    OnRecipeClicked onRecipeClicked;
    public RecipeAdapter(OnRecipeClicked onRecipeClicked){
        this.onRecipeClicked = onRecipeClicked;
    }
    public void setRecipeItems(List<RecipeItem> recipeItems){
        this.recipeItems=recipeItems;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recipe,parent,false);
        return new RecipeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        holder.mRecipeNameTV.setText(recipeItems.get(position).getName());
        if(!recipeItems.get(position).getImage().isEmpty())
        Picasso.get().load(recipeItems.get(position).getImage()).into(holder.mRecipeIV);
    }

    @Override
    public int getItemCount() {
        return recipeItems.size();
    }

    class RecipeItemViewHolder extends RecyclerView.ViewHolder{
        TextView mRecipeNameTV;
        ImageView mRecipeIV;
        public RecipeItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeNameTV = itemView.findViewById(R.id.tv_recipe_name_display);
            mRecipeIV = itemView.findViewById(R.id.iv_recipe_display);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecipeClicked.onRecipeClicked(getAdapterPosition());
                }
            });
        }
    }
}
