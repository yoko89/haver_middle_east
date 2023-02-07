package com.neklaway.havermiddle_east.roomDataBase.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.neklaway.havermiddle_east.entities.Customer;
import com.neklaway.havermiddle_east.roomDataBase.MainDataBase;
import com.neklaway.havermiddle_east.roomDataBase.dao.CustomerDao;
import com.neklaway.havermiddle_east.roomDataBase.dao.HMECodeDao;
import com.neklaway.havermiddle_east.roomDataBase.dao.TimeSheetDao;

import java.util.List;
import java.util.concurrent.Executors;

public class CustomerRepository {
    private final CustomerDao customerDao;
    private final TimeSheetDao timeSheetDao;
    private final HMECodeDao hmeCodeDao;
    final LiveData<List<Customer>> customers;

    public CustomerRepository(Application application) {
        MainDataBase dataBase = MainDataBase.getInstance(application);
        customerDao = dataBase.customerDao();
        timeSheetDao = dataBase.timeSheetDao();
        hmeCodeDao = dataBase.hmeCodeDao();
        customers = customerDao.getAll();
    }

    public LiveData<List<Customer>> getAll() {
        return customers;
    }

    public LiveData<List<String>> getCustomersName() {
        return customerDao.getCustomersName();
    }

    public LiveData<Long> insert(Customer customer) {
        MutableLiveData<Long> value = new MutableLiveData<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            value.postValue(customerDao.insert(customer));
        });
        return value;
    }

    public void update(Customer customer, String oldCustomerName) {
        Executors.newSingleThreadExecutor().execute(() -> {
            customerDao.update(customer);
            timeSheetDao.updateCustomer(customer.getCustomerName(), oldCustomerName);
            hmeCodeDao.updateCustomer(customer.getCustomerName(), oldCustomerName);
        });

    }


    public LiveData<List<Customer>> getObjectForCustomer(String customer) {
        return customerDao.getByObjectByCustomerName(customer);
    }
}
