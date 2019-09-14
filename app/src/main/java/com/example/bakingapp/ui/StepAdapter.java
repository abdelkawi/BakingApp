package com.example.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.R;
import com.example.bakingapp.data.StepsItem;
import com.example.bakingapp.ui.activity.RecipeDetailsActivity;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final List<StepsItem> mValues;
    private OnStepClicked onStepClicked;

    public StepAdapter(List<StepsItem> items,OnStepClicked onStepClicked) {
        mValues = items;
        this.onStepClicked=onStepClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mContentView.setText(mValues.get(position).getShortDescription());
        holder.itemView.setTag(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.content);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onStepClicked.onStepClicked(getAdapterPosition());
        }
    }
}