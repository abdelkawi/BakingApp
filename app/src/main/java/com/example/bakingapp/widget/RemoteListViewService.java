package com.example.bakingapp.widget;

import android.content.Context;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.model.IngredientsItem;

import java.util.List;

public class RemoteListViewService implements RemoteViewsService.RemoteViewsFactory {

    List<IngredientsItem> ingredientsItems;
    Context mContext;

    public RemoteListViewService(List<IngredientsItem> ingredientsItems, Context mContext) {
        this.ingredientsItems = ingredientsItems;
        this.mContext = mContext;

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {

        ingredientsItems.clear();
    }

    @Override
    public int getCount() {
        return ingredientsItems.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient);
        rv.setTextViewText(R.id.tv_Ingredient, ingredientsItems.get(i).getIngredient());
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
