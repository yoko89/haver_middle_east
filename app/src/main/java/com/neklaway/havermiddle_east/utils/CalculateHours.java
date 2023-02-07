package com.neklaway.havermiddle_east.utils;

import android.annotation.SuppressLint;

public class CalculateHours {



    @SuppressLint("DefaultLocale")
    public String[] calculateHours(String travelStartTime, String workStartTime, String workEndTime, String travelEndTime, String breakTime,boolean overTime) {
        String[] travelStartArray = travelStartTime.split(":");
        int travelStartMinutes = (Integer.parseInt(travelStartArray[0])) * 60 + Integer.parseInt(travelStartArray[1]);

        String[] workStartArray = workStartTime.split(":");
        int workStartMinutes = (Integer.parseInt(workStartArray[0])) * 60 + Integer.parseInt(workStartArray[1]);

        String[] workEndArray = workEndTime.split(":");
        int workEndMinutes = (Integer.parseInt(workEndArray[0])) * 60 + Integer.parseInt(workEndArray[1]);

        String[] travelEndArray = travelEndTime.split(":");
        int travelEndMinutes = (Integer.parseInt(travelEndArray[0])) * 60 + Integer.parseInt(travelEndArray[1]);

        String[] breakTimeArray = breakTime.replace("H", "").split("\\.");

        int breakTimeMinutes;


        if (breakTimeArray.length == 2) {
            breakTimeMinutes = (Integer.parseInt(breakTimeArray[0])) * 60 + Integer.parseInt(breakTimeArray[1]) * 6;
        } else {
            breakTimeMinutes = (Integer.parseInt(breakTimeArray[0])) * 60;
        }

        int workTimeMinutes = 0;
        int overTimeMinutes = 0;
        if (overTime) {
            overTimeMinutes = workEndMinutes - workStartMinutes - breakTimeMinutes;
        } else {
            workTimeMinutes = workEndMinutes - workStartMinutes - breakTimeMinutes;
            if (workTimeMinutes >= 8 * 60) {
                overTimeMinutes = workTimeMinutes - (8 * 60);
                workTimeMinutes = 8 * 60;
            }
        }
        int travelTimeMinutes = travelEndMinutes - travelStartMinutes - workTimeMinutes - overTimeMinutes - breakTimeMinutes;


        float calWorkTime = (float) Math.round(workTimeMinutes / 60.0 * 100) / 100;
        float calTravelTime = (float) Math.round(travelTimeMinutes / 60.00 * 100) / 100;
        float calOverTime = (float) Math.round(overTimeMinutes / 60.00 * 100) / 100;



        String[] calculatedHours = new String[3];
        calculatedHours[0] = String.format("%.2f", calWorkTime);
        calculatedHours[1] = String.format("%.2f", calTravelTime);
        calculatedHours[2] = String.format("%.2f", calOverTime);


        return calculatedHours;

    }
}
