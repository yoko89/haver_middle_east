package com.neklaway.havermiddle_east.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.activity.backupRestore.BackupRestoreActivity;
import com.neklaway.havermiddle_east.activity.calendar.CalendarActivity;
import com.neklaway.havermiddle_east.activity.dataPreview.DataActivity;
import com.neklaway.havermiddle_east.activity.newDate.NewDateActivity;
import com.neklaway.havermiddle_east.activity.newTimeSheet.NewTimeSheetActivity;
import com.neklaway.havermiddle_east.activity.oldDate.OldDateActivity;
import com.neklaway.havermiddle_east.activity.setting.SettingActivity;
import com.neklaway.havermiddle_east.activity.signature.SignatureActivity;
import com.neklaway.havermiddle_east.activity.visaLog.VisaLogActivity;
import com.neklaway.havermiddle_east.notification.AlarmReceiver;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button btnNewDate, btnVisaLog, btnNewTimeSheet, btnOldDate, btnData, btnCalendar;
    SharedPreferences settings;

    final String Settings = "user_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiate items
        btnNewDate = findViewById(R.id.btnNewDate);
        btnVisaLog = findViewById(R.id.btnVisaLog);
        btnNewTimeSheet = findViewById(R.id.btnTimeSheet);
        btnOldDate = findViewById(R.id.btnOldTimeSheet);
        btnData = findViewById(R.id.btnData);
        btnCalendar = findViewById(R.id.btnCalender);

        startAlarm();
        checkDataFilled();

        btnNewDate.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), NewDateActivity.class);
            startActivity(i);

        });

        btnOldDate.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), OldDateActivity.class);
            startActivity(i);
        });

        btnVisaLog.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), VisaLogActivity.class);
            startActivity(i);
        });

        btnNewTimeSheet.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), NewTimeSheetActivity.class);
            startActivity(i);
        });

        btnData.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), DataActivity.class);
            startActivity(i);
        });

        btnCalendar.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(i);
        });


    }

    private void checkDataFilled() {
        settings = getSharedPreferences(Settings, Context.MODE_PRIVATE);
        final String UserName = "user_name_key";

        if (settings.getString(UserName, "").isEmpty()) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("User name missing")
                    .setMessage("Please set username in settings or Restore backup")
                    .setPositiveButton(R.string.settings, (dialog, which) -> {
                        Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(i);
                    })
                    .setNeutralButton(R.string.restore, ((dialog, which) -> {
                        Intent i = new Intent(getApplicationContext(), BackupRestoreActivity.class);
                        startActivity(i);
                    }))
                    .show();
        } else if (!new File(getFilesDir() + "/signatures/userSign").exists()) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("User signature missing")
                    .setMessage("Please put your signature")
                    .setPositiveButton(R.string.signature, (dialog, which) -> {
                        Intent i = new Intent(getApplicationContext(), SignatureActivity.class);
                        i.putExtra("signatureOf", "userSign");
                        startActivity(i);
                    })
                    .show();
        }
    }

    private void startAlarm() {
        final String reminderTime = "reminder_time_key";

        settings = getSharedPreferences(Settings, Context.MODE_PRIVATE);
        int reminder = settings.getInt(reminderTime, 8);

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) > reminder) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, reminder);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 123, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    @Override
    protected void onStop() {
        getCacheDir().delete();
        getExternalCacheDir().delete();

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Settings) {
            Intent i = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);

    }
}