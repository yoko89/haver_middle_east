package com.neklaway.havermiddle_east.activity.newHMECode;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.neklaway.havermiddle_east.entities.HMECode;
import com.neklaway.havermiddle_east.roomDataBase.repository.CustomerRepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.HMECODERepository;

import java.util.List;

public class NewHMECodeViewModel extends AndroidViewModel {
    private final HMECODERepository hmecodeRepository;
    private final CustomerRepository customerRepository;

    final MutableLiveData<String> customerSelected;

    public NewHMECodeViewModel(@NonNull Application application) {
        super(application);
        hmecodeRepository = new HMECODERepository(application);
        customerRepository = new CustomerRepository(application);
        customerSelected = new MutableLiveData<>();
    }

    void setCustomer(String customer) {
        customerSelected.setValue(customer);
    }

    LiveData<List<HMECode>> getHME() {
        return Transformations.switchMap(customerSelected, customer -> hmecodeRepository.getFromCustomerName(customer));
    }

    LiveData<List<String>> getAllCustomers() {
        return customerRepository.getCustomersName();
    }

    void update(HMECode hmeCode, String oldHMECode) {
        hmecodeRepository.update(hmeCode, oldHMECode);
    }

    LiveData<Long> insert(HMECode hmeCode) {
        return hmecodeRepository.insert(hmeCode);
    }

    void delete(HMECode hmeCode) {
        hmecodeRepository.delete(hmeCode);
    }
}
