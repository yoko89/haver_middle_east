package com.neklaway.havermiddle_east.roomDataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.neklaway.havermiddle_east.entities.HMECode;

import java.util.List;

@Dao
public interface HMECodeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(HMECode hmeCode);

    @Query("SELECT HME_CODE FROM HMECODE_TABLE WHERE CUSTOMER_NAME = :customerName")
    LiveData<List<String>> getHMECodeFromCustomerName (String customerName);

    @Query("SELECT * FROM HMECODE_TABLE WHERE CUSTOMER_NAME = :customerName")
    LiveData<List<HMECode>> getFromCustomerName(String customerName);

    @Query("SELECT * FROM HMECODE_TABLE WHERE HME_CODE = :hmeCode")
    LiveData<List<HMECode>> getObjectForHMECode(String hmeCode);

    @Update
    void update(HMECode hmeCode);

    @Delete
    void delete(HMECode hmeCode);

    @Query("SELECT * FROM hmecode_table ORDER BY HME_CODE")
    LiveData<List<HMECode>> getAll();

    @Query("UPDATE HMECODE_TABLE SET CUSTOMER_NAME = :newCustomerName WHERE CUSTOMER_NAME = :oldCustomerName")
    void updateCustomer(String newCustomerName, String oldCustomerName);

}
