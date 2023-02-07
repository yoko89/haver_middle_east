package com.neklaway.havermiddle_east.activity.hmeView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.neklaway.havermiddle_east.roomDataBase.repository.HMECODERepository;
import com.neklaway.havermiddle_east.entities.HMECode;

import java.util.List;

public class HMEViewModel extends AndroidViewModel {
    private final HMECODERepository hmecodeRepository;
    private final LiveData<List<HMECode>> allHME;

    public HMEViewModel(@NonNull Application application) {
        super(application);
        hmecodeRepository = new HMECODERepository(application);
        allHME = hmecodeRepository.getAll();
        }

        public LiveData<List<HMECode>> getAllHME(){
            return allHME;
        }
}
