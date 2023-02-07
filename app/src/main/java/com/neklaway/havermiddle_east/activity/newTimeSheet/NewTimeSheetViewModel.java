package com.neklaway.havermiddle_east.activity.newTimeSheet;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.neklaway.havermiddle_east.entities.Customer;
import com.neklaway.havermiddle_east.entities.HMECode;
import com.neklaway.havermiddle_east.entities.TimeSheet;
import com.neklaway.havermiddle_east.roomDataBase.repository.CustomerRepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.HMECODERepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.IBAURepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.TimeSheetRepository;

import java.util.List;

public class NewTimeSheetViewModel extends AndroidViewModel {

    private final TimeSheetRepository timeSheetRepository;
    private final CustomerRepository customerRepository;
    private final HMECODERepository hmecodeRepository;
    private final IBAURepository ibauRepository;
    private final SharedPreferences settings;

    private final MutableLiveData<String> customerSet = new MutableLiveData<>();
    private final MutableLiveData<String> hmeSet = new MutableLiveData<>();
    private final MutableLiveData<String> ibauSet = new MutableLiveData<>();

    private final String Settings = "user_settings";
    private final String hideCompletedKey = "hide_completed_key";

    public NewTimeSheetViewModel(@NonNull Application application) {
        super(application);
        timeSheetRepository = new TimeSheetRepository(application);
        customerRepository = new CustomerRepository(application);
        hmecodeRepository = new HMECODERepository(application);
        ibauRepository = new IBAURepository(application);
        settings = application.getSharedPreferences(Settings, Context.MODE_PRIVATE);
    }


    LiveData<List<String>> allCustomers() {
        return customerRepository.getCustomersName();
    }

    LiveData<List<String>> getHME() {
        return Transformations.switchMap(customerSet, customer -> hmecodeRepository.getHMECodeFromCustomerName(customer));
    }

    void setCustomer(String customer) {
        customerSet.setValue(customer);
    }

    LiveData<List<String>> getIBAU() {
        return Transformations.switchMap(hmeSet, hmeCode -> ibauRepository.getListFromHME(hmeCode));
    }

    void setHME(String hmeCode) {
        hmeSet.setValue(hmeCode);
    }


    LiveData<List<TimeSheet>> getTimeSheetFromHME() {
        if (!settings.getBoolean(hideCompletedKey, false)) {
            return Transformations.switchMap(hmeSet, hmeCode -> timeSheetRepository.getByHMECode(hmeCode));
        } else {
            return getUnCreatedDatesByHMECode();
        }
    }


    void setIBAU(String ibauSo) {
        ibauSet.setValue(ibauSo);
    }

    LiveData<List<TimeSheet>> getTimeSheetFromIBAU() {
        if (!settings.getBoolean(hideCompletedKey, false)) {
            return Transformations.switchMap(ibauSet, ibauSo -> timeSheetRepository.getByIBAUSo(ibauSo));
        }else {
            return getUnCreatedDatesByIBAUCode();
        }

    }

    void delete(TimeSheet timeSheet) {
        timeSheetRepository.delete(timeSheet);
    }

    LiveData<List<TimeSheet>> getUnCreatedDatesByHMECode() {
        return Transformations.switchMap(hmeSet, hmeCode -> timeSheetRepository.getUnCreatedDatesByHMECode(hmeCode));
    }

    LiveData<List<TimeSheet>> getUnCreatedDatesByIBAUCode() {
        return Transformations.switchMap(ibauSet, ibauCode -> timeSheetRepository.getUnCreatedDatesByIBAUCode(ibauCode));
    }

    public LiveData<List<HMECode>> getHMEObjectForHMECode() {
        return Transformations.switchMap(hmeSet, hmeCode -> hmecodeRepository.getObjectForHMECode(hmeCode));
    }

    public LiveData<List<Customer>> getCustomerObjectForCustomer() {
        return Transformations.switchMap(customerSet, customer ->
                customerRepository.getObjectForCustomer(customer)
        );
    }


}
