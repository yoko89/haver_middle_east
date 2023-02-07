package com.neklaway.havermiddle_east.activity.oldDate;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.neklaway.havermiddle_east.roomDataBase.repository.CustomerRepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.HMECODERepository;

import java.util.List;

public class OldDateViewModel extends AndroidViewModel {

    private final CustomerRepository customerRepository;
    private final HMECODERepository hmecodeRepository;
    private final MutableLiveData<String> selectedCustomer;

    public OldDateViewModel(@NonNull Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
        hmecodeRepository = new HMECODERepository(application);
        selectedCustomer = new MutableLiveData<>();
    }

    public LiveData<List<String>> getCustomerName() {
        return customerRepository.getCustomersName();
    }

    public void setSelectedCustomer(String customer) {
        selectedCustomer.setValue(customer);
    }

    public LiveData<List<String>> getCustomerHMEs() {
        return Transformations.switchMap(selectedCustomer, customerName -> hmecodeRepository.getHMECodeFromCustomerName(customerName));
    }

}
