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

 /*   static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, AddWidgetIntentService.class);
        intent.setAction(QuoteSyncJob.ACTION_DATA_UPDATED);
        context.startService(intent);
       CharSequence widgetText = context.getString(R.string.stock_hawk);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.add_stock_widget);
              Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.symbol_widget, pendingIntent);

        views.setTextViewText(R.id.symbol_widget, widgetText);
        views.setTextViewText(R.id.price, "20");
        views.setTextViewText(R.id.change, "change");
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }
    */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
           // updateAppWidget(context, appWidgetManager, appWidgetIds);

        Intent intent = new Intent(context, AddWidgetIntentService.class);
        intent.setAction(QuoteSyncJob.ACTION_DATA_UPDATED);
        context.startService(intent);
    }

 /*   @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
*/
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

