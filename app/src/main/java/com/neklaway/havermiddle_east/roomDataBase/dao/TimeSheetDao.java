package com.neklaway.havermiddle_east.roomDataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.neklaway.havermiddle_east.entities.TimeSheet;

import java.util.List;

@Dao
public interface TimeSheetDao {

    @Insert
    void insert(TimeSheet timeSheet);

    @Update
    void update(List<TimeSheet> timeSheet);

    @Query("SELECT * FROM TIME_SHEET_TABLE WHERE HME_CODE = :hmeCode ORDER BY DATE ASC")
    LiveData<List<TimeSheet>> getByHMECode(String hmeCode);

    @Query("SELECT * FROM TIME_SHEET_TABLE WHERE SERVICE_CODE = :IBAUSo ORDER BY DATE ASC")
    LiveData<List<TimeSheet>> getByIBAUSo(String IBAUSo);

    @Query("SELECT * FROM TIME_SHEET_TABLE WHERE HME_CODE = :hmeCode AND TIME_SHEET_CREATED = :created ORDER BY DATE ASC")
    LiveData<List<TimeSheet>> getCreatedByHMECode(String hmeCode, int created);

    @Query("SELECT * FROM TIME_SHEET_TABLE WHERE SERVICE_CODE = :ibauSO AND TIME_SHEET_CREATED = :created ORDER BY DATE ASC")
    LiveData<List<TimeSheet>> getCreatedByIBAUCode(String ibauSO, int created);

    @Delete
    void delete(TimeSheet timeSheet);

    @Query("SELECT * FROM TIME_SHEET_TABLE WHERE DATE like :date ORDER BY DATE ASC")
    LiveData<List<TimeSheet>> getByDate(String date);

    @Query("SELECT * FROM TIME_SHEET_TABLE ORDER BY DATE ASC")
    LiveData<List<TimeSheet>> getAll();

    @Query("UPDATE TIME_SHEET_TABLE SET CUSTOMER = :newCustomer WHERE CUSTOMER = :oldCustomer and TIME_SHEET_CREATED = 0")
    void updateCustomer(String newCustomer, String oldCustomer);

    @Query("UPDATE TIME_SHEET_TABLE SET HME_CODE = :hmeCode WHERE HME_CODE = :oldHMECode")
    void UpdateHME(String hmeCode, String oldHMECode);

}
