package com.neklaway.havermiddle_east.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "IBAUCODE_TABLE")
public class IBAUSO {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "HME_CODE")
    private final String HMECode;

    @ColumnInfo(name = "SERVICE_CODE")
    private String IBAUSO;


    public IBAUSO(String HMECode, String IBAUSO) {
        this.HMECode = HMECode;
        this.IBAUSO = IBAUSO;
    }

    public static String toString(com.neklaway.havermiddle_east.entities.IBAUSO ibauso) {
        return ibauso.IBAUSO;
    }

    @Override
    public String toString() {
        return IBAUSO ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getHMECode() {
        return HMECode;
    }

    public String getIBAUSO() {
        return IBAUSO;
    }

}
