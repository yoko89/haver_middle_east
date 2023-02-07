package com.neklaway.havermiddle_east.activity.newDate;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.neklaway.havermiddle_east.roomDataBase.repository.CustomerRepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.HMECODERepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.IBAURepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.TimeSheetRepository;
import com.neklaway.havermiddle_east.entities.TimeSheet;

import java.util.List;

public class NewDateViewModel extends AndroidViewModel {

    private final TimeSheetRepository timeSheetRepository;
    private final CustomerRepository customerRepository;
    private final HMECODERepository hmecodeRepository;
    private final IBAURepository ibauRepository;
    private final MutableLiveData<String> selectedCustomer;
    private final MutableLiveData<String> selectedHME;

    public NewDateViewModel(@NonNull Application application) {
        super(application);
        timeSheetRepository = new TimeSheetRepository(application);
        customerRepository = new CustomerRepository(application);
        hmecodeRepository = new HMECODERepository(application);
        ibauRepository = new IBAURepository(application);
        selectedCustomer = new MutableLiveData<>();
        selectedHME = new MutableLiveData<>();
    }

    public void insert(TimeSheet timeSheet) {
        timeSheetRepository.insert(timeSheet);
    }

    public LiveData<List<String>> getAllCustomers() {
        return customerRepository.getCustomersName();
    }

    public void setCustomer(String customerName) {
        selectedCustomer.setValue(customerName);
    }

    public LiveData<List<String>> getHME() {
        return Transformations.switchMap(selectedCustomer, customerName -> hmecodeRepository.getHMECodeFromCustomerName(customerName));
    }

    public LiveData<List<String>> getIBAU() {
        return Transformations.switchMap(selectedHME,hmeCode ->ibauRepository.getListFromHME(hmeCode));
    }

    public void setHME(String hme) {
        selectedHME.setValue(hme);
    }
}
