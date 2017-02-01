package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.stock_chart)
    BarChart stockGraphView;

private boolean fetchDataFirst = false;


    void setStockGraphView(List<HistoryData> data){

     //   stockGraphView.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);
stockGraphView.setDrawBarShadow(false);
        stockGraphView.setDrawValueAboveBar(true);

      //  stockGraphView.setViewPortOffsets(0, 0, 0, 0);

        stockGraphView.getDescription().setEnabled(false);
       stockGraphView.setMaxVisibleValueCount(60);


        stockGraphView.setPinchZoom(false);

        stockGraphView.setDrawGridBackground(false);
        stockGraphView.setMaxHighlightDistance(300);


        XAxis xAxis = stockGraphView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisLineColor(R.color.colorPrimary);
        xAxis.setTextColor(R.color.colorPrimary);
       // xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);


        YAxis yAxis = stockGraphView.getAxisLeft();
        yAxis.setLabelCount(8, false);
        yAxis.setTextColor(R.color.colorPrimary);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(true);
        yAxis.setAxisLineColor(R.color.colorPrimary);

        stockGraphView.getAxisRight().setEnabled(false);

        setData(data);

        stockGraphView.getLegend().setEnabled(false);

        stockGraphView.animateXY(2000, 2000);


        stockGraphView.invalidate();




    }

    public void setData(List<HistoryData> dataEntry) {


        ArrayList<BarEntry> values = new ArrayList<BarEntry>();

        for (int i = 0; i < dataEntry.size(); i++) {

            values.add(new BarEntry(dataEntry.get(i).getValues(), dataEntry.get(i).getHistory()));
        }

        BarDataSet set1;

        if (stockGraphView.getData() != null &&
                stockGraphView.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) stockGraphView.getData().getDataSetByIndex(0);
            set1.setColor(R.color.colorPrimary);
            set1.setValues(values);
            stockGraphView.getData().notifyDataChanged();
            stockGraphView.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new BarDataSet(values, "DataSet 1");

            // set the line to be drawn like this "- - - - - -"
            set1.setColor(Color.BLACK);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            stockGraphView.setData(data);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);

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

String rawData = data.getString(
        data.getColumnIndexOrThrow(Contract.Quote.COLUMN_HISTORY)
);
        CSVReader csvReader = new CSVReader(new StringReader(rawData), ',');
        List<HistoryData> historyData = new ArrayList<>();
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
if(!fetchDataFirst)


    setStockGraphView(historyData);


        Timber.d("Data : ",rawData);


      //  Toast.makeText(this, "This is the symbol : " + rawData, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
