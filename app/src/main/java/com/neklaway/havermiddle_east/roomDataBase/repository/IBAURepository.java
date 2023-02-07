package com.neklaway.havermiddle_east.roomDataBase.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.neklaway.havermiddle_east.roomDataBase.MainDataBase;
import com.neklaway.havermiddle_east.roomDataBase.dao.IBAUSOdao;
import com.neklaway.havermiddle_east.entities.IBAUSO;

import java.util.List;
import java.util.concurrent.Executors;

public class IBAURepository {

    private final IBAUSOdao ibauS0Dao;
    final LiveData<List<IBAUSO>> ibauSos;


    public IBAURepository(Application application) {
        ibauS0Dao = MainDataBase.getInstance(application).ibauSOdao();
        ibauSos = ibauS0Dao.getAll();
    }

    public LiveData<List<IBAUSO>> getAll(){
        return ibauSos;
    }

    public LiveData<List<IBAUSO>> getFromHME(String hmeCode) {return ibauS0Dao.getSOsFromHMECode(hmeCode);}

    public void insert(IBAUSO ibauso){
        Executors.newSingleThreadExecutor().execute(() -> ibauS0Dao.insert(ibauso));
    }

    public void update(IBAUSO ibauso){
        Executors.newSingleThreadExecutor().execute(() -> ibauS0Dao.update(ibauso));
    }


    public LiveData<List<String>> getListFromHME(String hmeCode) {
        return ibauS0Dao.getListFromHME(hmeCode);
    }

}
