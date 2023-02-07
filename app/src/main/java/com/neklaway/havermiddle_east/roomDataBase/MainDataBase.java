package com.neklaway.havermiddle_east.roomDataBase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.neklaway.havermiddle_east.entities.Customer;
import com.neklaway.havermiddle_east.entities.HMECode;
import com.neklaway.havermiddle_east.entities.IBAUSO;
import com.neklaway.havermiddle_east.entities.TimeSheet;
import com.neklaway.havermiddle_east.entities.Visa;
import com.neklaway.havermiddle_east.roomDataBase.dao.BackupDao;
import com.neklaway.havermiddle_east.roomDataBase.dao.CustomerDao;
import com.neklaway.havermiddle_east.roomDataBase.dao.HMECodeDao;
import com.neklaway.havermiddle_east.roomDataBase.dao.IBAUSOdao;
import com.neklaway.havermiddle_east.roomDataBase.dao.TimeSheetDao;
import com.neklaway.havermiddle_east.roomDataBase.dao.VisaDao;


@Database(entities = {Customer.class, HMECode.class, IBAUSO.class, TimeSheet.class, Visa.class}, version = 5,autoMigrations = {@AutoMigration(from = 4,to = 5)})
public abstract class MainDataBase extends RoomDatabase {

    private static MainDataBase instance;

    public abstract CustomerDao customerDao();

    public abstract HMECodeDao hmeCodeDao();

    public abstract IBAUSOdao ibauSOdao();

    public abstract TimeSheetDao timeSheetDao();

    public abstract VisaDao visaDao();

    public abstract BackupDao backupDao();

    private static final String TAG = "MainDataBase";

    public static synchronized MainDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MainDataBase.class, "mainData.db").addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build();
        }

        return (instance);
    }

    public static void closeInstance() {
        if (instance.isOpen()) {
            instance.close();
        }
        instance = null;
    }


    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE CUSTOMER_TABLE RENAME TO CUSTOMER_old");
            database.execSQL("CREATE TABLE CUSTOMER_TABLE (ID INTEGER PRIMARY KEY not Null,CUSTOMER_NAME TEXT, CUSTOMER_CITY TEXT,CUSTOMER_COUNTRY TEXT)");
            database.execSQL("INSERT INTO CUSTOMER_TABLE (ID,CUSTOMER_NAME,CUSTOMER_CITY,CUSTOMER_COUNTRY) SELECT ID,CUSTOMER_NAME,CUSTOMER_CITY,CUSTOMER_COUNTRY FROM CUSTOMER_old");
            database.execSQL("DROP TABLE IF EXISTS CUSTOMER_old");

            database.execSQL("ALTER TABLE HMECODE_TABLE RENAME TO HMECODE_old");
            database.execSQL("CREATE TABLE HMECODE_TABLE (ID INTEGER PRIMARY KEY not Null,HME_CODE TEXT, CUSTOMER_NAME TEXT,MACHINE_TYPE TEXT,MACHINE_NUMBER TEXT,WORK_DESCRIPTION TEXT,FILE_NUMBER INTEGER not Null)");
            database.execSQL("INSERT INTO HMECODE_TABLE (ID,HME_CODE , CUSTOMER_NAME,MACHINE_TYPE,MACHINE_NUMBER,WORK_DESCRIPTION,FILE_NUMBER) SELECT ID,HME_CODE , CUSTOMER_NAME,MACHINE_TYPE,MACHINE_NUMBER,WORK_DESCRIPTION,FILE_NUMBER FROM HMECODE_old");
            database.execSQL("DROP TABLE IF EXISTS HMECODE_old");

            database.execSQL("ALTER TABLE IBAUCODE_TABLE RENAME TO IBAUCODE_old");
            database.execSQL("CREATE TABLE IBAUCODE_TABLE (ID INTEGER PRIMARY KEY not Null,HME_CODE TEXT, SERVICE_CODE TEXT)");
            database.execSQL("INSERT INTO IBAUCODE_TABLE (ID,HME_CODE,SERVICE_CODE) SELECT ID,HME_CODE,SERVICE_CODE FROM IBAUCODE_old");
            database.execSQL("DROP TABLE IF EXISTS IBAUCODE_old");

            database.execSQL("ALTER TABLE TIME_SHEET_TABLE RENAME TO TIME_SHEET_old");
            database.execSQL("CREATE TABLE TIME_SHEET_TABLE (ID INTEGER PRIMARY KEY not Null,DATE TEXT, TRAVEL_START TEXT,WORK_START TEXT,WORK_END TEXT,TRAVEL_END TEXT,BREAK_TIME TEXT,TRAVEL_DISTANCE INTEGER not Null,CUSTOMER TEXT,HME_CODE TEXT,TIME_SHEET_CREATED INTEGER not Null,WEEKEND INTEGER not Null,SERVICE_CODE TEXT)");
            database.execSQL("INSERT INTO TIME_SHEET_TABLE (ID,DATE,TRAVEL_START,WORK_START,WORK_END,TRAVEL_END,BREAK_TIME,TRAVEL_DISTANCE,CUSTOMER,HME_CODE,TIME_SHEET_CREATED,WEEKEND,SERVICE_CODE) SELECT ID,DATE,TRAVEL_START,WORK_START,WORK_END,TRAVEL_END,BREAK_TIME,TRAVEL_DISTANCE,CUSTOMER,HME_CODE,TIME_SHEET_CREATED,WEEKEND,SERVICE_CODE FROM TIME_SHEET_old");
            database.execSQL("DROP TABLE IF EXISTS TIME_SHEET_old");

            database.execSQL("ALTER TABLE VISA_LOG_TABLE RENAME TO VISA_LOG_old");
            database.execSQL("CREATE TABLE VISA_LOG_TABLE (ID INTEGER PRIMARY KEY not Null,DATE TEXT, ENABLED INTEGER NOT NULL DEFAULT 1, COUNTRY TEXT)");
            database.execSQL("INSERT INTO VISA_LOG_TABLE (ID,DATE,COUNTRY) SELECT ID,DATE,COUNTRY FROM VISA_LOG_old");
            database.execSQL("DROP TABLE IF EXISTS VISA_LOG_old");

            // Migrate Date for Time_Sheet
            Cursor c = database.query("SELECT * FROM TIME_SHEET_TABLE");

            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(0);
                    String oldDate = c.getString(1);

                    String[] dateSplit = oldDate.split("/");
                    String day = dateSplit[0];
                    String month = dateSplit[1];
                    String year = dateSplit[2];
                    String newDate = year + '/' + month + '/' + day;

                    database.execSQL("UPDATE TIME_SHEET_TABLE SET DATE = '" + newDate + "' WHERE ID= " + id);

                } while (c.moveToNext());
                // moving our cursor to next.
            }

            // Migrate Date for Visa
            Cursor cursor = database.query("SELECT * FROM VISA_LOG_TABLE");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String oldDate = cursor.getString(1);

                    String[] dateSplit = oldDate.split("/");
                    String day = dateSplit[0];
                    String month = dateSplit[1];
                    String year = dateSplit[2];
                    String newDate = year + '/' + month + '/' + day;

                    database.execSQL("UPDATE VISA_LOG_TABLE SET DATE = '" + newDate + "' WHERE ID= " + id);

                } while (c.moveToNext());
                // moving our cursor to next.
            }


            Log.d(TAG, "migrate: database from 2 to 3");
        }
    };

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            String createIBAUServiceOrder = "CREATE TABLE IBAUCODE_TABLE ( ID INTEGER PRIMARY KEY AUTOINCREMENT,HME_CODE TEXT, SERVICE_CODE TEXT)";
            String addWeekEndToTimeSheet = "ALTER TABLE  TIME_SHEET_TABLE ADD COLUMN WEEKEND INTEGER";
            String addIBAUToTimeSheet = "ALTER TABLE  TIME_SHEET_TABLE ADD COLUMN SERVICE_CODE TEXT";

            database.execSQL(createIBAUServiceOrder);
            database.execSQL(addWeekEndToTimeSheet);
            database.execSQL(addIBAUToTimeSheet);
            Log.d(TAG, "migrate: database from 1 to 2");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE UNIQUE INDEX index_HMECODE_TABLE_HME_CODE on HMECODE_TABLE (HME_CODE);");
            database.execSQL("CREATE UNIQUE INDEX index_CUSTOMER_TABLE_CUSTOMER_NAME on CUSTOMER_TABLE (CUSTOMER_NAME);");

            Log.d(TAG, "migrate: database from 3 to 4");
        }
    };

}



