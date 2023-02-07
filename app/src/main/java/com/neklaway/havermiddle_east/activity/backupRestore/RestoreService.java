package com.neklaway.havermiddle_east.activity.backupRestore;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.documentfile.provider.DocumentFile;

import com.neklaway.havermiddle_east.notification.NotificationCreator;
import com.neklaway.havermiddle_east.roomDataBase.MainDataBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestoreService extends JobIntentService {

    private final String tag = "Restore";
    private final static int jobID = 3;
    private final List<Integer> errorCode = new ArrayList<>();


    public RestoreService() {
    }

    public static void enqueueWork(Context context, Intent i) {
        enqueueWork(context, RestoreService.class, jobID, i);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(tag, "Restore service started");


        Notification notification = new NotificationCreator(this).CreateForegroundServiceStart("Restore Running",true);
        startForeground(jobID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(tag, "Restore service destroyed");


        StringBuilder notificationText;
        if (errorCode.size() == 0) {
            notificationText = new StringBuilder("RESTART APP");
        } else {
            notificationText = new StringBuilder("Restore Failed with errors");
            for (int i = 0; i < errorCode.size(); i++) {
                notificationText.append(", ").append(errorCode.get(i));
            }
        }
        new NotificationCreator(this).CreateServiceNotification("Restore Done",notificationText.toString(),0);

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        Uri uri = intent.getParcelableExtra("folder");
        DocumentFile backupFolder = DocumentFile.fromTreeUri(getApplicationContext(),uri);
        //Check available files
        DocumentFile[] files = Objects.requireNonNull(backupFolder).listFiles();
        boolean DBAvailable = false;
        boolean sharedDateDataAvailable = false;
        boolean sharedUserSettingsAvailable = false;
        boolean userSignAvailable =false;

        for(DocumentFile file : files){
               //start restore
            InputStream source = null;
            File dest = null;

            if(Objects.requireNonNull(file.getName()).compareTo("mainData.db")==0){
                DBAvailable = true;
                MainDataBase dataBase = MainDataBase.getInstance(getApplication());
                dataBase.close();
                MainDataBase.closeInstance();

                try {
                    source = getContentResolver().openInputStream(file.getUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                dest = new File(getDataDir()+"/databases","mainData.db");
            }
            else if(file.getName().compareTo("new_date_data.xml") == 0){
                sharedDateDataAvailable = true;

                try {
                    source = getContentResolver().openInputStream(file.getUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                dest = new File(getDataDir()+"/shared_prefs","new_date_data.xml");
            }
            else if(file.getName().compareTo("user_settings.xml") == 0){
                sharedUserSettingsAvailable = true;

                try {
                    source = getContentResolver().openInputStream(file.getUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                dest = new File(getDataDir()+"/shared_prefs","user_settings.xml");
            }
            else if(file.getName().compareTo("signatures") == 0){
                userSignAvailable = true;

                try {
                    source = getContentResolver().openInputStream(file.listFiles()[0].getUri());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                dest = new File(getFilesDir()+"/signatures","userSign");
            }

            if(source != null){
                copyFiles(source,dest);
                Log.v(tag,"File "+file.getName() + " restored");
            }


        }
        if(!DBAvailable) errorCode.add(1);
        if (!sharedDateDataAvailable)  errorCode.add(2);
        if (!sharedUserSettingsAvailable) errorCode.add(3);
        if (!userSignAvailable) errorCode.add(4);

        }




        void copyFiles (InputStream source , File dest){
        try{
            OutputStream out;

            if(dest.exists()){
                dest.delete();
            }
            Objects.requireNonNull(dest.getParentFile()).mkdirs();
            dest.createNewFile();
            out = new FileOutputStream(dest);

            byte[] buf = new byte[1024];
            int len;
            while ((len = source.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            source.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("TAG", "copyFiles: " + e.getMessage());
            errorCode.add(5);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "copyFiles: " + e.getMessage());
            errorCode.add(6);
        }
        }


    }

