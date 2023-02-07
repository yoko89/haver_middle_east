package com.neklaway.havermiddle_east.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {
    private String directoryName = "images";
    private String fileName = "image.png";
    private final Context context;
    private boolean external = false;

    public ImageSaver(Context context) {
        this.context = context;
    }

    public ImageSaver setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ImageSaver setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public ImageSaver setDirectory(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    public void save(Bitmap bitmapImage) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @NonNull
    private File createFile() {
        File directory;
        if (external) {
            directory = getAlbumStorageDir(directoryName);
        } else {
            directory = new File(context.getFilesDir() + "/" + directoryName);
        }
        if (!directory.exists()) {
            directory.mkdir();
        }

        return new File(directory, fileName);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getStorageDirectory(), albumName);
        if (!file.mkdirs()) {
            Log.e("ImageSaver", "Directory not created");
        }
        return file;
    }

    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}