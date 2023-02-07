package com.neklaway.havermiddle_east.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "CUSTOMER_TABLE",indices = @Index(value = {"CUSTOMER_NAME"},unique = true))
public class Customer {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "CUSTOMER_NAME")
    private final String customerName;

    @ColumnInfo(name = "CUSTOMER_CITY")
    private final String customerCity;

    @ColumnInfo(name = "CUSTOMER_COUNTRY")
    private final String customerCountry;


    public Customer(String customerName, String customerCity, String customerCountry) {
        this.customerName = customerName;
        this.customerCity = customerCity;
        this.customerCountry = customerCountry;
    }


    @Override
    public String toString() {
        return customerName +
                "\n" + customerCity +
                ", " + customerCountry;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }


}
