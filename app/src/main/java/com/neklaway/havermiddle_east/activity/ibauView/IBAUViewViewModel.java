package com.neklaway.havermiddle_east.activity.ibauView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.neklaway.havermiddle_east.roomDataBase.repository.IBAURepository;
import com.neklaway.havermiddle_east.entities.IBAUSO;

import java.util.List;

public class IBAUViewViewModel extends AndroidViewModel {

    private final IBAURepository ibauRepository;
    private final LiveData<List<IBAUSO>> allIBAUSO;

    public IBAUViewViewModel(@NonNull Application application) {
        super(application);
        ibauRepository = new IBAURepository(application);
        allIBAUSO = ibauRepository.getAll();
    }

    public LiveData<List<IBAUSO>> getAllIBAUSO(){
        return allIBAUSO;
    }

}
