package com.neklaway.havermiddle_east.roomDataBase.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.neklaway.havermiddle_east.roomDataBase.MainDataBase;
import com.neklaway.havermiddle_east.roomDataBase.dao.HMECodeDao;
import com.neklaway.havermiddle_east.roomDataBase.dao.IBAUSOdao;
import com.neklaway.havermiddle_east.roomDataBase.dao.TimeSheetDao;
import com.neklaway.havermiddle_east.entities.HMECode;

import java.util.List;
import java.util.concurrent.Executors;

public class HMECODERepository {
    private final HMECodeDao hmeCodeDao;
    private final TimeSheetDao timeSheetDao;
    private final IBAUSOdao ibausOdao;
    final LiveData<List<HMECode>> hmeCodes;


    public HMECODERepository(Application application) {
        MainDataBase dataBase = MainDataBase.getInstance(application);
        hmeCodeDao = dataBase.hmeCodeDao();
        timeSheetDao = dataBase.timeSheetDao();
        ibausOdao = dataBase.ibauSOdao();
        hmeCodes = hmeCodeDao.getAll();

    }


    public LiveData<Long> insert(HMECode hmeCode) {
        MutableLiveData<Long> value = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> value.postValue(hmeCodeDao.insert(hmeCode)));
        return value;
    }

    public void delete(HMECode hmeCode) {
        Executors.newSingleThreadExecutor().execute(() -> hmeCodeDao.delete(hmeCode));
    }

    public LiveData<List<String>> getHMECodeFromCustomerName(String customerName) {
        return hmeCodeDao.getHMECodeFromCustomerName(customerName);
    }


    public void update(HMECode hmeCode, String oldHMECode) {
        Executors.newSingleThreadExecutor().execute(() -> {
            hmeCodeDao.update(hmeCode);
            timeSheetDao.UpdateHME(hmeCode.getHMECode(), oldHMECode);
            ibausOdao.updateHME(hmeCode.getHMECode(), oldHMECode);
        });
    }

    public void update(HMECode hmeCode) {
        Executors.newSingleThreadExecutor().execute(() -> hmeCodeDao.update(hmeCode));
    }

    public LiveData<List<HMECode>> getAll() {
        return hmeCodes;
    }

    public LiveData<List<HMECode>> getFromCustomerName(String customer) {
        return hmeCodeDao.getFromCustomerName(customer);
    }

    public LiveData<List<HMECode>> getObjectForHMECode(String hmeCode) {
        return hmeCodeDao.getObjectForHMECode(hmeCode);
    }
}
