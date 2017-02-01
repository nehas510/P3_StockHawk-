package com.udacity.stockhawk.ui;

/**
 * Created by neha on 1/31/17.
 */

public class StockDoesNotExist {

    private String text;
    private String symbol;

    public StockDoesNotExist(String text, String symbol) {
        this.text = text;
        this.symbol = symbol;
    }

    public String getText() {
        return text;
    }

    public void setText(String message) {
        this.text = message;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
