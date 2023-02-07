package com.neklaway.havermiddle_east.activity.calendar;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.neklaway.havermiddle_east.roomDataBase.repository.TimeSheetRepository;
import com.neklaway.havermiddle_east.entities.TimeSheet;

import java.util.Calendar;
import java.util.List;

public class CalendarViewModel extends AndroidViewModel {

    private final TimeSheetRepository timeSheetRepository;
    private final MutableLiveData<Calendar> calendarSelected;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        timeSheetRepository = new TimeSheetRepository(application);
        calendarSelected = new MutableLiveData<>();
    }

    public void setCalender (Calendar calendar){
        calendarSelected.setValue(calendar);
    }

    public LiveData<List<TimeSheet>> getTimeSheet(){

        return Transformations.switchMap(calendarSelected, calendar -> {
            // current year
            int year = calendar.get(Calendar.YEAR);
            // current month
            int monthOfYear = calendar.get(Calendar.MONTH);
            // current day
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            @SuppressLint("DefaultLocale") String qualifier =  String.format("%4d/%02d",year,monthOfYear +1)+ "%";

            return timeSheetRepository.getByDate(qualifier);
        });
    }

}
