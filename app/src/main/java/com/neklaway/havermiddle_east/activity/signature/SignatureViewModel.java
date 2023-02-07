package com.neklaway.havermiddle_east.activity.signature;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.neklaway.havermiddle_east.utils.ImageSaver;

public class SignatureViewModel extends AndroidViewModel {

    private static final String signFolder = "signatures";

    public SignatureViewModel(@NonNull Application application) {
        super(application);
    }

    public Bitmap getSignature(String fileName){
        return new ImageSaver(getApplication())
                .setFileName(fileName)
                .setExternal(false)//image save in external directory or app folder default value is false
                .setDirectory(signFolder)
                .load(); //Bitmap from your code
    }

    public void setSignature(Bitmap bm,String fileName){
        new ImageSaver(getApplication())
                .setFileName(fileName)
                .setExternal(false)//image save in external directory or app folder default value is false
                .setDirectory(signFolder)
                .save(bm); //Bitmap from your code
    }
}
