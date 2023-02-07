package com.neklaway.havermiddle_east.roomDataBase.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.neklaway.havermiddle_east.roomDataBase.MainDataBase;
import com.neklaway.havermiddle_east.roomDataBase.dao.VisaDao;
import com.neklaway.havermiddle_east.entities.Visa;

import java.util.List;
import java.util.concurrent.Executors;

public class VisaRepository {

    private final VisaDao visaDao;
    final LiveData<List<Visa>> visas;

    public VisaRepository(Application application) {
        MainDataBase dataBase = MainDataBase.getInstance(application);
        visaDao = dataBase.visaDao();
        visas = visaDao.getAll();
    }


    public void update(Visa visa) {
        Executors.newSingleThreadExecutor().execute(() -> visaDao.update(visa));
    }

    public LiveData<List<Visa>> getAll() {
        return visas;
    }

    public List<Visa> getAllAsList(){
        return visaDao.getAllAsList();
}

    public void delete(Visa visa) {
        Executors.newSingleThreadExecutor().execute(() -> visaDao.delete(visa));
    }

    public void insert(Visa visa) {
        Executors.newSingleThreadExecutor().execute(() -> visaDao.insert(visa));
    }



}
