package com.udacity.stockhawk.ui;

/**
 * Created by neha on 1/30/17.
 */

public class HistoryData {

    private  float history;
    private  float values;
    private String date;


    public String getDate(){ return date;}

    public void setDate(String date){ this.date = date;}

    public float getHistory() {
        return history;
    }

    public void setHistory(float history) {
        this.history = history;
    }


    public float getValues() {
        return values;
    }

    public void setValues(float values) {
        this.values = values;
    }
}
