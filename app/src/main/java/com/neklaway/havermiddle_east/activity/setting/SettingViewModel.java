package com.neklaway.havermiddle_east.activity.setting;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

public class SettingViewModel extends AndroidViewModel {

    private final SharedPreferences settings;
    private final SharedPreferences.Editor editor;

    private final String Settings = "user_settings";
    private final String UserName = "user_name_key";
    private final String VisaLogReminder = "visa_log_reminder_key";
    private final String BreakDefaultTime = "break_default_key";
    private final String reminderTime = "reminder_time_key";
    private final String IBAUUser = "IBAU_user_key";
    private final String clearDate = "clear_date_key";
    private final String hideCompletedKey = "hide_completed_key";

    public SettingViewModel(@NonNull Application application) {
        super(application);
        settings = application.getSharedPreferences(Settings, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public void setSettings(String userName, int visaLogReminder,int breakDefaultTime, int visaReminder, boolean ibau,boolean clear, boolean hideCompleted){
        editor.putString(UserName,userName);
        editor.putInt(VisaLogReminder, visaLogReminder);
        editor.putInt(BreakDefaultTime, breakDefaultTime);
        editor.putInt(reminderTime, visaReminder);
        editor.putBoolean(IBAUUser, ibau);
        editor.putBoolean(clearDate, clear);
        editor.putBoolean(hideCompletedKey,hideCompleted);
        editor.apply();
    }

    public ArrayList<String> getSettings(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(settings.getString(UserName,""));
        arrayList.add(String.valueOf(settings.getInt(VisaLogReminder, 30)));
        arrayList.add(String.valueOf(settings.getInt(BreakDefaultTime, 2)));
        arrayList.add(String.valueOf(settings.getInt(reminderTime, 8)));
        arrayList.add(String.valueOf(settings.getBoolean(IBAUUser, false)));
        arrayList.add(String.valueOf(settings.getBoolean(clearDate, false)));
        arrayList.add(String.valueOf(settings.getBoolean(hideCompletedKey,false)));

        return arrayList;
    }

}

