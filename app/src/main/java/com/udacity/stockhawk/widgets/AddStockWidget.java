package com.udacity.stockhawk.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.udacity.stockhawk.sync.QuoteSyncJob;

/**
 * Implementation of App Widget functionality.
 */
public class AddStockWidget extends AppWidgetProvider {



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Intent intent = new Intent(context, AddWidgetIntentService.class);
        intent.setAction(QuoteSyncJob.ACTION_DATA_UPDATED);
        context.startService(intent);
    }


    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (QuoteSyncJob.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            intent = new Intent(context, AddWidgetIntentService.class);
            intent.setAction(QuoteSyncJob.ACTION_DATA_UPDATED);
            context.startService(intent);
        }
    }
}

