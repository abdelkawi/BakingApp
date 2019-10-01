package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.ui.StepDetailFragment;
import com.example.bakingapp.ui.StepListActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, MyWidgetRemoteViewsService.class);
            // Add the app widget ID to the intent extras.

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baking_widget);


            rv.setRemoteAdapter(appWidgetId, R.id.appwidget_list, intent);

            // Trigger listview item click
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    StepDetailFragment.ARG_RECIPE_ID, MODE_PRIVATE);
            Intent clicked = new Intent(context, StepListActivity.class);
            clicked.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clicked.putExtra(StepDetailFragment.ARG_RECIPE_ID
                    , sharedPreferences.getInt(StepDetailFragment.ARG_RECIPE_ID, 0));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clicked, 0);
            rv.setOnClickPendingIntent(R.id.container, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, BakingWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.appwidget_list);
        }
        super.onReceive(context, intent);
    }
}

