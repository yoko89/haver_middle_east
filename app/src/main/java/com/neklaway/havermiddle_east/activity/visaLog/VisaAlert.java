package com.neklaway.havermiddle_east.activity.visaLog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.neklaway.havermiddle_east.entities.Visa;
import com.neklaway.havermiddle_east.notification.NotificationCreator;
import com.neklaway.havermiddle_east.roomDataBase.repository.VisaRepository;
import com.neklaway.havermiddle_east.utils.DateFormatter;

import java.util.Calendar;
import java.util.List;

public class VisaAlert extends JobIntentService {

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        final String Settings = "user_settings";
        final String VisaLogReminder = "visa_log_reminder_key";
        long remainingDays;

        Calendar currentDate = Calendar.getInstance();
        Calendar visaDate = Calendar.getInstance();
        SharedPreferences settings;

        VisaRepository visaRepository = new VisaRepository(getApplication());
        List<Visa> visaList = visaRepository.getAllAsList();
        settings = getSharedPreferences(Settings, Context.MODE_PRIVATE);
        for (int position = 0; position < visaList.size(); position++) {
            if (visaList.get(position).isEnabled()) {
                visaDate.setTime(DateFormatter.formatDateToCalendar(visaList.get(position).getDate()).getTime());

                remainingDays = (visaDate.getTimeInMillis() - currentDate.getTimeInMillis()) / (1000 * 60 * 60 * 24);

                if (remainingDays <= 0) {
                    new NotificationCreator(this).CreateVisaNotification("Visa Reminder", visaList.get(position).getCountry() + " visa has expired on " + DateFormatter.dateFormatToLocaleString(DateFormatter.formatDateToCalendar(visaList.get(position).getDate())), visaList.get(position).getId());
                } else if (remainingDays <= settings.getInt(VisaLogReminder, 0)) {
                    new NotificationCreator(this).CreateVisaNotification("Visa Reminder", visaList.get(position).getCountry() + " visa will expire on " + DateFormatter.dateFormatToLocaleString(DateFormatter.formatDateToCalendar(visaList.get(position).getDate())), visaList.get(position).getId());
                }
            }
        }
    }
}
