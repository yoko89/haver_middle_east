package com.neklaway.havermiddle_east.activity.visaLog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.neklaway.havermiddle_east.roomDataBase.repository.VisaRepository;
import com.neklaway.havermiddle_east.entities.Visa;

import java.util.List;

public class VisaLogViewModel extends AndroidViewModel {
    private final VisaRepository visaRepository;
    private final LiveData<List<Visa>> allVisas;

    public VisaLogViewModel(@NonNull Application application) {
        super(application);
        visaRepository = new VisaRepository(application);
        allVisas = visaRepository.getAll();
    }

    public LiveData<List<Visa>> getAll(){
        return allVisas;
    }

    public void insert(Visa visa){
        visaRepository.insert(visa);
    }

    public void delete(Visa visa){

            visaRepository.delete(visa);
    }

    public void update(Visa visa){
        visaRepository.update(visa);
    }
}
