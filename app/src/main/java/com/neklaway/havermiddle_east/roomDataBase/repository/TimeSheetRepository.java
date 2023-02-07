package com.neklaway.havermiddle_east.roomDataBase.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.neklaway.havermiddle_east.roomDataBase.MainDataBase;
import com.neklaway.havermiddle_east.roomDataBase.dao.TimeSheetDao;
import com.neklaway.havermiddle_east.entities.TimeSheet;

import java.util.List;
import java.util.concurrent.Executors;

public class TimeSheetRepository {

    private final TimeSheetDao timeSheetDao;
    private final LiveData<List<TimeSheet>> timeSheets;

    public TimeSheetRepository(Application application) {
        MainDataBase dataBase = MainDataBase.getInstance(application);
        timeSheetDao = dataBase.timeSheetDao();
        timeSheets = timeSheetDao.getAll();
    }

    public void delete(TimeSheet timeSheet) {
        Executors.newSingleThreadExecutor().submit(() -> {
            timeSheetDao.delete(timeSheet);
        });
    }


    public void insert(TimeSheet timeSheet){
        Executors.newSingleThreadExecutor().submit(() -> {
            timeSheetDao.insert(timeSheet);
        });
    }


    public void update(List<TimeSheet> timeSheet){
        Executors.newSingleThreadExecutor().submit(() -> {
            timeSheetDao.update(timeSheet);
        });
    }

    public LiveData<List<TimeSheet>> getByHMECode(String hmeCode){
        return timeSheetDao.getByHMECode(hmeCode);
    }

    public LiveData<List<TimeSheet>> getByIBAUSo(String IBAUSo){
        return timeSheetDao.getByIBAUSo(IBAUSo);
    }


    public LiveData<List<TimeSheet>> getByDate(String qualifier) {

        return timeSheetDao.getByDate(qualifier);
    }


    public LiveData<List<TimeSheet>> getUnCreatedDatesByHMECode(String hmeCode) {
        return timeSheetDao.getCreatedByHMECode(hmeCode,0);
    }

    public LiveData<List<TimeSheet>> getUnCreatedDatesByIBAUCode(String ibauCode) {
        return timeSheetDao.getCreatedByIBAUCode(ibauCode,0);
    }
}
