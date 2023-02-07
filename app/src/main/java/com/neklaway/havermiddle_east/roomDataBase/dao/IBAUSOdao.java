package com.neklaway.havermiddle_east.roomDataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.neklaway.havermiddle_east.entities.IBAUSO;

import java.util.List;

@Dao
public interface IBAUSOdao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(IBAUSO ibauso);

    @Query("SELECT * FROM IBAUCODE_TABLE WHERE HME_CODE = :hmeCode")
    LiveData<List<IBAUSO>> getSOsFromHMECode (String hmeCode);

    @Update
    void update(IBAUSO ibauso);

    @Query("SELECT * FROM IBAUCODE_TABLE ORDER BY SERVICE_CODE")
    LiveData<List<IBAUSO>> getAll ();

    @Query("UPDATE IBAUCODE_TABLE SET HME_CODE = :hmeCode WHERE HME_CODE = :oldHMECode")
    void updateHME(String hmeCode, String oldHMECode);

    @Query("SELECT SERVICE_CODE FROM IBAUCODE_TABLE WHERE HME_CODE = :hmeCode ORDER BY SERVICE_CODE")
    LiveData<List<String>> getListFromHME(String hmeCode);
}
