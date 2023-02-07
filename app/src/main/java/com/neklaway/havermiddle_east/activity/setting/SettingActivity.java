package com.neklaway.havermiddle_east.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.activity.backupRestore.BackupRestoreActivity;
import com.neklaway.havermiddle_east.activity.signature.SignatureViewActivity;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private EditText etUserName, etVisaLogReminder, etVisaReminder;
    private Spinner spBreakDefaultTime;
    private Button btnUserSignature, btnBackup;
    private SwitchCompat swIBAU, swClear, swHideCompleted;
    private SettingViewModel viewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        etUserName = findViewById(R.id.et_user_name);
        etVisaLogReminder = findViewById(R.id.et_visa_log_reminder);
        spBreakDefaultTime = findViewById(R.id.sp_break_time_default);
        btnUserSignature = findViewById(R.id.btnUserSignature);
        btnBackup = findViewById(R.id.btnBackup);
        etVisaReminder = findViewById(R.id.et_visa_time);
        swIBAU = findViewById(R.id.swIBAU);
        swClear = findViewById(R.id.swClear);
        swHideCompleted = findViewById(R.id.sw_hide_completed);

        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        btnUserSignature.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SignatureViewActivity.class);
            i.putExtra(SignatureViewActivity.signOf, "userSign");
            startActivity(i);

        });

        btnBackup.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), BackupRestoreActivity.class);
            startActivity(i);

        });


    }

    @Override
    protected void onPause() {
        viewModel.setSettings(etUserName.getText().toString().trim(),
                Integer.parseInt(etVisaLogReminder.getText().toString().trim()),
                spBreakDefaultTime.getSelectedItemPosition(),
                Integer.parseInt(etVisaReminder.getText().toString().trim()),
                swIBAU.isChecked(),
                swClear.isChecked(),
                swHideCompleted.isChecked());
        super.onPause();
    }

    @Override
    protected void onResume() {
        ArrayList<String> settingsList = viewModel.getSettings();
        etUserName.setText(settingsList.get(0));
        etVisaLogReminder.setText(settingsList.get(1));
        spBreakDefaultTime.setSelection(Integer.parseInt(settingsList.get(2)));
        etVisaReminder.setText(settingsList.get(3));
        swIBAU.setChecked(settingsList.get(4).equals("true"));
        swClear.setChecked(settingsList.get(5).equals("true"));
        swHideCompleted.setChecked(settingsList.get(6).equals("true"));

        super.onResume();

    }

}