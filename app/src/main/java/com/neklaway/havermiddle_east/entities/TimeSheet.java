package com.neklaway.havermiddle_east.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TIME_SHEET_TABLE")
public class TimeSheet {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "DATE")
    private final String date;

    @ColumnInfo(name = "TRAVEL_START")
    private final String travelStartTime;

    @ColumnInfo(name = "WORK_START")
    private final String workStartTime;

    @ColumnInfo(name = "WORK_END")
    private final String workEndTime;

    @ColumnInfo(name = "TRAVEL_END")
    private final String travelEndTime;

    @ColumnInfo(name = "BREAK_TIME")
    private final String breakHour;

    @ColumnInfo(name = "TRAVEL_DISTANCE")
    private final int travelDistance;

    @ColumnInfo(name = "CUSTOMER")
    private String customer;

    @ColumnInfo(name = "HME_CODE")
    private String HMECode;

    @ColumnInfo(name = "TIME_SHEET_CREATED")
    private boolean timeSheetCreated;

    @ColumnInfo(name = "WEEKEND")
    private boolean weekEnd;

    @ColumnInfo(name = "SERVICE_CODE")
    private String IBAUSO;

    @ColumnInfo(name = "OVER_TIME", defaultValue = "0")
    private boolean overTime;

    public TimeSheet( String date, String travelStartTime, String workStartTime, String workEndTime, String travelEndTime, String breakHour, int travelDistance, String customer, String HMECode, boolean timeSheetCreated, boolean weekEnd, String IBAUSO, boolean overTime) {
        this.date = date;
        this.travelStartTime = travelStartTime;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.travelEndTime = travelEndTime;
        this.breakHour = breakHour;
        this.travelDistance = travelDistance;
        this.customer = customer;
        this.HMECode = HMECode;
        this.timeSheetCreated = timeSheetCreated;
        this.weekEnd = weekEnd;
        this.IBAUSO = IBAUSO;
        this.overTime = overTime;
    }

    @Override
    public String toString() {
        return "Date= " + date + '\n' +
                "Travel = " + travelStartTime + '\n' +
                "Work from " + workStartTime + " to " + workEndTime + '\n' +
                "Return = " + travelEndTime + '\n' +
                "Break Duration= " + breakHour + '\n' +
                "Traveled Distance= " + travelDistance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTimeSheetCreated() {
        return timeSheetCreated;
    }

    public boolean isWeekEnd() {
        return weekEnd;
    }

    public String getIBAUSO() {
        return IBAUSO;
    }

    public int getTravelDistance() {
        return travelDistance;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTravelStartTime() {
        return travelStartTime;
    }

    public String getWorkStartTime() {
        return workStartTime;
    }

    public String getWorkEndTime() {
        return workEndTime;
    }

    public String getTravelEndTime() {
        return travelEndTime;
    }

    public String getBreakHour() {
        return breakHour;
    }

    public String getCustomer() {
        return customer;
    }

    public String getHMECode() {
        return HMECode;
    }

    public void setTimeSheetCreated(boolean timeSheetCreated) {
        this.timeSheetCreated = timeSheetCreated;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setHMECode(String HMECode) {
        this.HMECode = HMECode;
    }

    public void setWeekEnd(boolean weekEnd) {
        this.weekEnd = weekEnd;
    }

    public void setIBAUSO(String IBAUSO) {
        this.IBAUSO = IBAUSO;
    }

    public boolean isOverTime() {
        return overTime;
    }

    public void setOverTime(boolean overTime) {
        this.overTime = overTime;
    }
}