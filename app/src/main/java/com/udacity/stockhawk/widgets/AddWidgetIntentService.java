package com.udacity.stockhawk.widgets;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.ui.MainActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by neha on 2/1/17.
 */

public class AddWidgetIntentService extends IntentService {


    private final DecimalFormat dollarPlus;
    private final DecimalFormat dollar;
    private final DecimalFormat percentagePlus;

    {
        dollar = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarPlus.setPositivePrefix("+$");
        percentagePlus = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentagePlus.setMaximumFractionDigits(2);
        percentagePlus.setMinimumFractionDigits(2);
        percentagePlus.setPositivePrefix("+");
    }


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public AddWidgetIntentService() {
        super("AddWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (QuoteSyncJob.ACTION_DATA_UPDATED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            getDataFromContentProvider(this, appWidgetManager,
                    appWidgetManager.getAppWidgetIds(new ComponentName(this, AddStockWidget.class)));
        }
    }
    private void getDataFromContentProvider(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Cursor cursor = context.getContentResolver().query(
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS,
                null,
                null,
                Contract.Quote.COLUMN_SYMBOL);

        if (cursor == null || cursor.getCount() < 1 || !cursor.moveToFirst()) {
            return;
        }

        cursor.moveToPosition(0);


        for (int appWidgetID : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.add_stock_widget);

            remoteViews.setTextViewText(R.id.symbol, cursor.getString(Contract.Quote.POSITION_SYMBOL));
            remoteViews.setTextViewText(R.id.price, dollar.format(cursor.getFloat(Contract.Quote.POSITION_PRICE)));


            float absoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

            if (absoluteChange > 0) {
                remoteViews.setInt(R.id.change,"setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }

            String change = dollarPlus.format(absoluteChange);
            String percentage = percentagePlus.format(percentageChange / 100);

            if (PrefUtils.getDisplayMode(context)
                    .equals(context.getString(R.string.pref_display_mode_absolute_key))) {
                remoteViews.setTextViewText(R.id.change, change);
            } else {
                remoteViews.setTextViewText(R.id.change, percentage);
            }

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.single_widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
        }


    }
}

