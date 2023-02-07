package com.neklaway.havermiddle_east.roomDataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.neklaway.havermiddle_east.entities.Customer;

import java.util.List;

@Dao
public interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Customer customer);

    @Update
    void update(Customer customer);

    @Query("SELECT * FROM CUSTOMER_TABLE ORDER BY CUSTOMER_NAME ASC , CUSTOMER_COUNTRY ASC")
    LiveData<List<Customer>> getAll();


    @Query("SELECT CUSTOMER_NAME FROM CUSTOMER_TABLE")
    LiveData<List<String>> getCustomersName();

    @Query("SELECT * FROM CUSTOMER_TABLE WHERE CUSTOMER_NAME = :customer")
    LiveData<List<Customer>> getByObjectByCustomerName(String customer);
}
