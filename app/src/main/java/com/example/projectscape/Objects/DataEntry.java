package com.example.projectscape.Objects;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataEntry {

    private Date date;
    private int price;
    private int index;

    public DataEntry(long date, int price, int index) {
        this.date = new Date(date);
        this.price = price;
        this.index = index;
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public int getIndex() {
        return index;
    }

    @Override
    @NonNull
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return format.format(date) + " | " + price;
    }
}
