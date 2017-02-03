package com.udacity.stockhawk.ui.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by neha on 2/3/17.
 */

public class XValueFormater implements IAxisValueFormatter {

    private final SimpleDateFormat dateFormat;
    private final Date date;
    private final Float time;

    public XValueFormater(String dateFormat, Float time) {
        this.dateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        this.date = new Date();
        this.time = time;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        date.setTime((long) (value + time));
        return dateFormat.format(date);
    }
}
