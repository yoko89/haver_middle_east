package com.neklaway.havermiddle_east.activity.backupRestore;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.neklaway.havermiddle_east.notification.NotificationCreator;
import com.neklaway.havermiddle_east.roomDataBase.MainDataBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BackupService extends JobIntentService {

    private final String tag = "Backup";
    private final static int jobID = 2;
    private final List<Integer> errorCode = new ArrayList<>();

    public BackupService() {
    }

    public static void enqueueWork(Context context, Intent i) {
        enqueueWork(context, BackupService.class, jobID, i);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(tag, "Backup service started");

        Notification notification = new NotificationCreator(this).CreateForegroundServiceStart("Backup Running", true);

        startForeground(jobID, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(tag, "Backup service destroyed");


        StringBuilder notificationText;
        if (errorCode.size() == 0) {
            notificationText = new StringBuilder("Backup Completed Successfully");
        } else {
            notificationText = new StringBuilder("Backup Failed with errors");
            for (int i = 0; i < errorCode.size(); i++) {
                notificationText.append(", ").append(errorCode.get(i));
            }
        }

        new NotificationCreator(this).CreateServiceNotification("Backup done", notificationText.toString(), 0);

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        MainDataBase dataBase = MainDataBase.getInstance(getApplication());
        dataBase.backupDao().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
        dataBase.close();
        Log.d(tag, "onHandleWork: database closed "+!dataBase.isOpen());
        File directory = new File("/storage/emulated/0/Download", "HME_Backup");
        if (!directory.exists()) {
            boolean success = directory.mkdir();
            if (success) {
                Log.d(tag, "Backup folder created in " + directory.getAbsolutePath());
            } else {
                errorCode.add(1);
                Log.d(tag, "Backup folder failed in " + directory.getAbsolutePath());
            }
        }

        Calendar c = Calendar.getInstance();


        String folderName = c.get(Calendar.DAY_OF_MONTH) + "_" + (c.get(Calendar.MONTH) + 1) + "_" + c.get(Calendar.YEAR);

        File dateDir = new File(directory, folderName);
        if (directory.exists()) {
            if (!dateDir.exists()) {
                boolean success = dateDir.mkdir();

                if (success) {
                    Log.d(tag, "Backup folder created in " + dateDir.getAbsolutePath());
                } else {
                    errorCode.add(2);
                    Log.v(tag, "Backup folder failed in " + dateDir.getAbsolutePath());
                }

            } else {
                NotificationCreator nc = new NotificationCreator(getApplicationContext());
                nc.CreateServiceNotification("BackUp Already Created Today", "Please delete if you want to make a new Backup.", 99);
            }
        }

        if (dateDir.exists()) {
            //Start actual backup
            File sharedPrefs = new File(getDataDir(), "shared_prefs");
            String[] sharedPrefsChildren = sharedPrefs.list();

            if (sharedPrefsChildren != null) {
                if (sharedPrefsChildren.length > 0)
                    for (String sharedPrefsChild : sharedPrefsChildren) {
                        File source = new File(getDataDir() + "/shared_prefs", sharedPrefsChild);
                        File dest = new File(dateDir, sharedPrefsChild);
                        copyFiles(source, dest);
                    }
            }
            File dataBaseFile = getDatabasePath("mainData.db");
            File dataBaseDest = new File(dateDir, "mainData.db");
            if (dataBaseFile.exists()) {
                copyFiles(dataBaseFile, dataBaseDest);
            }

            File files = getFilesDir();
            String[] filesChildren = files.list();

            if (filesChildren != null) {
                if (filesChildren.length > 0) {
                    for (String filesChild : filesChildren) {
                        File backupFoldersSource = new File(getFilesDir(), filesChild);
                        File backupFoldersDest = new File(dateDir, filesChild);
                        if (!backupFoldersDest.exists() && backupFoldersSource.isDirectory()) {
                            boolean success = backupFoldersDest.mkdir();
                            if (success) {
                                Log.d(tag, "Backup folder created in " + backupFoldersDest.getAbsolutePath());
                            } else {
                                errorCode.add(5);
                                Log.d(tag, "Backup folder failed in " + backupFoldersDest.getAbsolutePath());
                            }
                        }

                        String[] restoreFolderChildren = backupFoldersSource.list();

                        if (restoreFolderChildren != null) {
                            if (restoreFolderChildren.length > 0)
                                for (String restoreFolderChild : restoreFolderChildren) {
                                    File source = new File(backupFoldersSource, restoreFolderChild);
                                    File dest = new File(backupFoldersDest, restoreFolderChild);
                                    if (source.isFile()) {
                                        copyFiles(source, dest);
                                    }
                                }
                        }
                    }

                }
            }
        }


    }

    void copyFiles(File source, File dest) {
        try {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(dest);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            Log.d(tag, "copyFiles: file : "+ dest.getName() + " copied" );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            errorCode.add(3);
        } catch (IOException e) {
            e.printStackTrace();
            errorCode.add(4);
        }
    }


}

