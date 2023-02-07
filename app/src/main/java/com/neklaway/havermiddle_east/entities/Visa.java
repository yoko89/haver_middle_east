package com.neklaway.havermiddle_east.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "VISA_LOG_TABLE")
public class Visa {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "DATE")
    private final String date;

    @ColumnInfo(name = "COUNTRY")
    private final String country;

    @ColumnInfo(name = "ENABLED")
    private boolean enabled;


    public Visa(String country, String date,boolean enabled) {
        this.date = date;
        this.country = country;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return country +
                ", Expire date= " + date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
