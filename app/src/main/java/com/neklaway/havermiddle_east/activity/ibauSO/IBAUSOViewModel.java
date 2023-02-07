package com.neklaway.havermiddle_east.activity.ibauSO;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.neklaway.havermiddle_east.roomDataBase.repository.CustomerRepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.HMECODERepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.IBAURepository;
import com.neklaway.havermiddle_east.entities.IBAUSO;

import java.util.List;

public class IBAUSOViewModel extends AndroidViewModel {

    private final IBAURepository ibauRepository;
    private final CustomerRepository customerRepository;
    private final HMECODERepository hmecodeRepository;
    private final LiveData<List<String>> customerList;
    private final MutableLiveData<String> selectedCustomer;
    private final MutableLiveData<String> selectedHME;


    public IBAUSOViewModel(@NonNull Application application) {
        super(application);
        ibauRepository = new IBAURepository(application);
        customerRepository = new CustomerRepository(application);
        hmecodeRepository = new HMECODERepository(application);

        customerList = customerRepository.getCustomersName();
        selectedCustomer = new MutableLiveData<>();
        selectedHME = new MutableLiveData<>();
    }

    public LiveData<List<String>> getCustomerList(){
        return customerList;
    }

    public LiveData<List<IBAUSO>> getIBAU(){
        return Transformations.switchMap(selectedHME,hmeCode->ibauRepository.getFromHME(hmeCode));
    }

    public void setHME (String hmeCode){
        selectedHME.setValue(hmeCode);
    }

    public LiveData<List<String>> getCustomerHMEs(){
        return Transformations.switchMap(selectedCustomer,customerName-> hmecodeRepository.getHMECodeFromCustomerName(customerName));
    }

    public void setCustomer(String customer){
        selectedCustomer.setValue(customer);
    }

    public void insert(IBAUSO new_data) {
    ibauRepository.insert(new_data);
    }

    public void update(IBAUSO ibausoUpdated) {
    ibauRepository.update(ibausoUpdated);
    }
}
