package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.formatter.XValueFormater;
import com.udacity.stockhawk.ui.formatter.YValueFormatter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.stock_chart)
    LineChart stockGraphView;

    private Float refTime;
   private  boolean firstData = true;
    private String rawData;
    private  List<HistoryData> historyData = new ArrayList<>();

private boolean fetchDataFirst = false;



    public void setData(List<HistoryData> dataEntry) {


        ArrayList<Entry> values = new ArrayList<Entry>();

        for (HistoryData data : dataEntry) {

            values.add(new Entry((data.getHistory()) - refTime, data.getValues()));
        }

        LineDataSet dataSet = new LineDataSet(values, "Dataset1");
        dataSet.setColor(R.color.colorPrimary);
        dataSet.setLineWidth(2f);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setCircleColor(R.color.colorPrimary);
        dataSet.setHighLightColor(R.color.colorPrimary);
        dataSet.setDrawValues(false);



        XAxis xAxis = stockGraphView.getXAxis();
        xAxis.setValueFormatter(new XValueFormater("MMM/yy", refTime));
        xAxis.setDrawGridLines(true);
        xAxis.setAxisLineColor(R.color.colorPrimary);
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setTextColor(R.color.colorPrimary);
        xAxis.setTextSize(12f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisRight = stockGraphView.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxis = stockGraphView.getAxisLeft();
        yAxis.setValueFormatter(new YValueFormatter());
        yAxis.setDrawGridLines(true);
        yAxis.setAxisLineColor(R.color.colorPrimary);
        yAxis.setAxisLineWidth(1.5f);
        yAxis.setTextColor(R.color.colorPrimary);
        yAxis.setTextSize(12f);


        LineData lineData = new LineData(dataSet);
        stockGraphView.setData(lineData);

        stockGraphView.getLegend().setEnabled(false);

        stockGraphView.animateXY(2000, 2000);

        stockGraphView.invalidate();



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);
   if(rawData!=null&&(!historyData.isEmpty()))
        setData(historyData);
        getSupportLoaderManager().initLoader(0,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this,
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS,
                Contract.Quote.COLUMN_SYMBOL + " = ?",
                new String[]{getIntent().getStringExtra("symbol")},
                Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();

        rawData = data.getString(
                data.getColumnIndexOrThrow(Contract.Quote.COLUMN_HISTORY)
        );        CSVReader csvReader = new CSVReader(new StringReader(rawData), ',');

         String[] check = null;

        try {
            while ((check = csvReader.readNext()) != null) {

                HistoryData histPriceModel = new HistoryData();
                histPriceModel.setHistory(Float.valueOf(check[0]));
                histPriceModel.setValues(Float.valueOf(check[1]));
                historyData.add(histPriceModel);
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.reverse(historyData);
        if(firstData)
        {
            this.refTime = Float.valueOf(historyData.get(0).getHistory());
            firstData = false;
        }

        System.out.print(historyData);

        Log.e("llllist", historyData.toString());

        if(!fetchDataFirst) {
            setData(historyData);
            fetchDataFirst = true;
            // supportStartPostponedEnterTransition();
        }


        Timber.d("Data : ",rawData);


      //  Toast.makeText(this, "This is the symbol : " + rawData, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
