package com.neklaway.havermiddle_east.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateFormatter {



    @SuppressLint("DefaultLocale")
    public static String formatDateForDataBase(Calendar date) {
        return String.format("%4d/%02d/%02d",date.get(Calendar.YEAR),(date.get(Calendar.MONTH) + 1),date.get(Calendar.DAY_OF_MONTH));
    }

    public static Calendar formatDateToCalendar(String dateString) {
        if (dateString.equals("")) {
            return null;
        } else {
            String[] dateSplit = dateString.split("/");
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]));
            return dateCalendar;
        }
    }

    public static String dateFormatToLocaleString(Calendar calendar){
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

}
