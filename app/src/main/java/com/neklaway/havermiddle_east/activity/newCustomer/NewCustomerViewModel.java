package com.neklaway.havermiddle_east.activity.newCustomer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.neklaway.havermiddle_east.roomDataBase.repository.CustomerRepository;
import com.neklaway.havermiddle_east.entities.Customer;

import java.util.List;

public class NewCustomerViewModel extends AndroidViewModel {
    private final CustomerRepository customerRepository;
    private final LiveData<List<Customer>> allCustomers;

    public NewCustomerViewModel(@NonNull Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
        allCustomers = customerRepository.getAll();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public LiveData<Long> insert(Customer customer) {
        return customerRepository.insert(customer);
    }

    public void update(Customer customer , String oldCustomer) {
        customerRepository.update(customer, oldCustomer);
    }

}
