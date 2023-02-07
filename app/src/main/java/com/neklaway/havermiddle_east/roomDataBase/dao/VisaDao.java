package com.neklaway.havermiddle_east.roomDataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.neklaway.havermiddle_east.entities.Visa;

import java.util.List;

@Dao
public interface VisaDao {

    @Insert
    void insert(Visa visa);

    @Update
    void update(Visa visa);

    @Query("SELECT * FROM VISA_LOG_TABLE ORDER BY DATE ASC")
    LiveData<List<Visa>> getAll();

    @Query("SELECT * FROM VISA_LOG_TABLE ORDER BY DATE ASC")
    List<Visa> getAllAsList();

    @Delete
    void delete(Visa visa);


}
