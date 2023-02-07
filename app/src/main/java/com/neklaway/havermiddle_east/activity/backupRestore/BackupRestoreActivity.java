package com.neklaway.havermiddle_east.activity.backupRestore;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.neklaway.havermiddle_east.R;


public class  BackupRestoreActivity extends AppCompatActivity {


    final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if(intent != null) {
                Uri uri = intent.getData();

                Intent i = new Intent(getApplicationContext(), RestoreService.class);
                i.putExtra("folder",uri);
                RestoreService.enqueueWork(getApplicationContext(),i);

            }
        }

    });

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);

        Button btnBackup = findViewById(R.id.btnBackup);
        Button btnRestore = findViewById(R.id.btnRestore);

        btnBackup.setOnClickListener(v -> {
                Intent i = new Intent(this, BackupService.class);
                BackupService.enqueueWork(this,i);

        });

        btnRestore.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            activityResultLauncher.launch(i);

        });

    }


}







