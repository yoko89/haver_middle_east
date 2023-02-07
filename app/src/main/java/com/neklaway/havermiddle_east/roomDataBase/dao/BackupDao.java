package com.neklaway.havermiddle_east.roomDataBase.dao;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public interface BackupDao {

    @RawQuery
    int checkpoint(SupportSQLiteQuery supportSQLiteQuery);
}
