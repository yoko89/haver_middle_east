package com.neklaway.havermiddle_east.activity.newDate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.activity.ibauSO.IBAUSOActivity;
import com.neklaway.havermiddle_east.activity.newCustomer.NewCustomerActivity;
import com.neklaway.havermiddle_east.activity.newHMECode.NewHMECodeActivity;
import com.neklaway.havermiddle_east.entities.TimeSheet;
import com.neklaway.havermiddle_east.utils.DateFormatter;

import java.util.Calendar;

public class NewDateActivity extends AppCompatActivity {

    private EditText etTimeSheetDate, etTravelStartTime, etWorkStartTime, etWorkEndTime, etTravelEndTime, etTravelDistance;
    private Button btnTravelStartNow, btnWorkStartNow, btnWorkEndNow, btnTravelEndNow, btnDateNow;
    private ImageButton btnAddCustomer, btnAddHMECode, btnAddIBAUCode;
    private Spinner spBreakTime, spCustomer, spHMECode, spIBAUCode;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SwitchCompat swWeekend, swWeekEndOverTime, swTravelDay;
    private TextView tvWorkStart, tvWorkEnd, tvTravelStart, tvTravelEnd, tvTravelDist;
    private LinearLayout loBreak, loIBAUCode;
    private ConstraintLayout loNewDate;

    private static final String NEW_DATE_DATA = "new_date_data";
    private static final String DATE_KEY = "date_key";
    private static final String TRAVEL_START_TIME_KEY = "travel_start_time_key";
    private static final String WORK_START_TIME_KEY = "work_start_time_key";
    private static final String WORK_END_TIME_KEY = "work_end_time_key";
    private static final String TRAVEL_END_TIME_KEY = "travel_end_time_key";
    private static final String BREAK_HOUR_KEY = "break_hour_key";
    private static final String TRAVEL_DISTANCE_KEY = "travel_distance_key";
    private static final String CUSTOMER_NAME_KEY = "customer_name_key";
    private static final String HME_CODE_KEY = "HME_code_key";
    private static final String OVER_TIME_KEY = "over_time_key";
    private static final String IBAU_SO_KEY = "IBAU_SO_key";
    private static final String travelDay = "travel_day_key";

    private static final String USER_SETTINGS = "user_settings";
    private static final String BREAK_DEFAULT_KEY = "break_default_key";
    private static final String IBAU_USER_KEY = "IBAU_user_key";
    private static final String CLEAR_DATE_KEY = "clear_date_key";

    private NewDateViewModel viewModel;

    private SharedPreferences settings;

    // Date for the Day
    private Calendar date;



    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_date);

        viewModel = new ViewModelProvider(this).get(NewDateViewModel.class);

        SharedPreferences NewDateData = getSharedPreferences(NEW_DATE_DATA, MODE_PRIVATE);

        settings = getSharedPreferences(USER_SETTINGS, MODE_PRIVATE);

        // initiate items
        etTimeSheetDate = findViewById(R.id.et_time_sheet_date);
        etTravelStartTime = findViewById(R.id.et_travel_start_time);
        etWorkStartTime = findViewById(R.id.et_work_start_time);
        etWorkEndTime = findViewById(R.id.et_work_end_time);
        etTravelEndTime = findViewById(R.id.et_travel_end_time);
        etTravelDistance = findViewById(R.id.et_travel_distance);

        tvTravelStart = findViewById(R.id.tvTravelStart);
        tvTravelEnd = findViewById(R.id.tvTravelEnd);
        tvWorkStart = findViewById(R.id.tvWorkStart);
        tvWorkEnd = findViewById(R.id.tvWorkEnd);
        tvTravelDist = findViewById(R.id.tv_travel_distance);

        loBreak = findViewById(R.id.lo_break_time);
        loNewDate = findViewById(R.id.loNewDate);
        loIBAUCode = findViewById(R.id.lo_IBAU_code);

        btnTravelStartNow = findViewById(R.id.btn_travel_start_time_now);
        btnWorkStartNow = findViewById(R.id.btn_work_start_time_now);
        btnWorkEndNow = findViewById(R.id.btn_work_end_time_now);
        btnTravelEndNow = findViewById(R.id.btn_travel_end_time_now);
        btnDateNow = findViewById(R.id.btn_date_now);
        btnAddCustomer = findViewById(R.id.btn_add_customer);
        btnAddHMECode = findViewById(R.id.btn_add_hme_code);
        btnAddIBAUCode = findViewById(R.id.btn_add_IBAU_code);


        spBreakTime = findViewById(R.id.sp_break_time);
        spCustomer = findViewById(R.id.sp_customer);
        spHMECode = findViewById(R.id.sp_HME_code);
        spIBAUCode = findViewById(R.id.sp_IBAU_code);

        swWeekend = findViewById(R.id.sw_weekend);
        swWeekEndOverTime = findViewById(R.id.sw_weekendOverTime);
        swTravelDay = findViewById(R.id.sw_travel_day);


        if (settings.getBoolean(IBAU_USER_KEY, false)) {
            loIBAUCode.setVisibility(View.VISIBLE);
        } else {
            loIBAUCode.setVisibility(View.GONE);
        }


        viewModel.getAllCustomers().observe(this, customers -> {
            ArrayAdapter<String> allCustomerAdapter = new ArrayAdapter<>(NewDateActivity.this,
                    android.R.layout.simple_list_item_1,
                    customers);

            spCustomer.setAdapter(allCustomerAdapter);

            try {
                int selection = NewDateData.getInt(CUSTOMER_NAME_KEY, 0);
                   if(selection < customers.size())
                spCustomer.setSelection(selection);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


        spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setCustomer(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewModel.getHME().observe(NewDateActivity.this, hmeList -> {


            ArrayAdapter<String> customerHMECode = new ArrayAdapter<>(NewDateActivity.this,
                    android.R.layout.simple_list_item_1,
                    hmeList);

            spHMECode.setAdapter(customerHMECode);

            try {
                int selection = NewDateData.getInt(HME_CODE_KEY, 0);
                if(selection < hmeList.size() && selection>=0) {
                    spHMECode.setSelection(selection);
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        spHMECode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setHME(parent.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (settings.getBoolean(IBAU_USER_KEY, false)) {
            viewModel.getIBAU().observe(NewDateActivity.this, ibauList -> {
                ArrayAdapter<String> ibauAdapter = new ArrayAdapter<>(NewDateActivity.this, android.R.layout.simple_list_item_1, ibauList);
                spIBAUCode.setAdapter(ibauAdapter);
                try {
                    int selection = NewDateData.getInt(IBAU_SO_KEY, 0);
                    if(selection < ibauList.size() && selection>=0) {
                        spIBAUCode.setSelection(selection);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }


        // perform click event on edit text

        etTimeSheetDate.setOnClickListener(v -> {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            // current year
            int mYear = c.get(Calendar.YEAR);
            // current month
            int mMonth = c.get(Calendar.MONTH);
            // current day
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            // date picker dialog
            datePickerDialog = new DatePickerDialog(NewDateActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        date = Calendar.getInstance();
                        date.set(year, monthOfYear, dayOfMonth);

                        // set day of month , month and year value in the edit text
                        etTimeSheetDate.setText(DateFormatter.dateFormatToLocaleString(date));

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        btnDateNow.setOnClickListener(v -> {
            // calender class's instance and get current date , month and year from calender
            date = Calendar.getInstance();

            etTimeSheetDate.setText(DateFormatter.dateFormatToLocaleString(date));
        });

        etTravelStartTime.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(NewDateActivity.this, (timePicker, hourOfDay, minutes) -> etTravelStartTime.setText(String.format("%02d:%02d", hourOfDay, minutes)), 0, 0, true);

            timePickerDialog.show();


        });

        btnTravelStartNow.setOnClickListener(v -> etTravelStartTime.setText(getTime()));


        etWorkStartTime.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(NewDateActivity.this, (timePicker, hourOfDay, minutes) -> {

                etWorkStartTime.setText(String.format("%02d:%02d", hourOfDay, minutes));

                if (etTravelStartTime.getText().toString().compareTo(etWorkStartTime.getText().toString()) > 0) {
                    etWorkStartTime.getText().clear();
                    Toast.makeText(getApplicationContext(), "Work Start Time must be Equal or after Travel Start Time", Toast.LENGTH_SHORT).show();
                }
            }, 0, 0, true);

            timePickerDialog.show();


        });

        btnWorkStartNow.setOnClickListener(v -> {
            etWorkStartTime.setText(getTime());

            if (etTravelStartTime.getText().toString().compareTo(etWorkStartTime.getText().toString()) > 0) {
                etWorkStartTime.getText().clear();
                Toast.makeText(getApplicationContext(), "Work Start Time must be Equal or after Travel Start Time", Toast.LENGTH_SHORT).show();
            }

        });

        etWorkEndTime.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(NewDateActivity.this, (timePicker, hourOfDay, minutes) -> {

                etWorkEndTime.setText(String.format("%02d:%02d", hourOfDay, minutes));

                if (etWorkStartTime.getText().toString().compareTo(etWorkEndTime.getText().toString()) > 0) {
                    etWorkEndTime.getText().clear();
                    Toast.makeText(getApplicationContext(), "Work End Time must be Equal or after Work Start Time", Toast.LENGTH_SHORT).show();
                }
            }, 0, 0, true);

            timePickerDialog.show();

        });

        btnWorkEndNow.setOnClickListener(v -> {
            etWorkEndTime.setText(getTime());

            if (etWorkStartTime.getText().toString().compareTo(etWorkEndTime.getText().toString()) > 0) {
                etWorkEndTime.getText().clear();
                Toast.makeText(getApplicationContext(), "Work End Time must be Equal or after Work Start Time", Toast.LENGTH_SHORT).show();
            }
        });

        etTravelEndTime.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(NewDateActivity.this, (timePicker, hourOfDay, minutes) -> {

                etTravelEndTime.setText(String.format("%02d:%02d", hourOfDay, minutes));

            if (swTravelDay.isChecked() && (etTravelStartTime.getText().toString().compareTo(etTravelEndTime.getText().toString()) > 0)) {
                etTravelEndTime.getText().clear();
                Toast.makeText(getApplicationContext(), "Travel End Time must be Equal or after Travel Start Time", Toast.LENGTH_SHORT).show();

            } else if (!swTravelDay.isChecked() && (etWorkEndTime.getText().toString().compareTo(etTravelEndTime.getText().toString()) > 0)) {
                    etTravelEndTime.getText().clear();
                    Toast.makeText(getApplicationContext(), "Travel End Time must be Equal or after Work End Time", Toast.LENGTH_SHORT).show();
                }
            }, 0, 0, true);

            timePickerDialog.show();

        });

        btnTravelEndNow.setOnClickListener(v -> {
            etTravelEndTime.setText(getTime());

            if ((etWorkEndTime.getText().toString().compareTo(etTravelEndTime.getText().toString()) > 0) && !swTravelDay.isChecked()) {
                etTravelEndTime.getText().clear();
                Toast.makeText(getApplicationContext(), "Travel End Time must be Equal or after Work End Time", Toast.LENGTH_SHORT).show();
            } else if ((etTravelStartTime.getText().toString().compareTo(etTravelEndTime.getText().toString()) > 0) && swTravelDay.isChecked()) {
                etTravelEndTime.getText().clear();
                Toast.makeText(getApplicationContext(), "Travel End Time must be after Travel Start Time", Toast.LENGTH_SHORT).show();
            }

        });


        btnAddCustomer.setOnClickListener(v -> {

            Intent i = new Intent(getApplicationContext(), NewCustomerActivity.class);
            startActivity(i);
        });

        btnAddHMECode.setOnClickListener(v -> {

            Intent i = new Intent(getApplicationContext(), NewHMECodeActivity.class);
            startActivity(i);
        });

        btnAddIBAUCode.setOnClickListener(v -> {

            Intent i = new Intent(getApplicationContext(), IBAUSOActivity.class);
            startActivity(i);
        });


        swWeekend.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etTravelStartTime.setVisibility(View.GONE);
                etWorkStartTime.setVisibility(View.GONE);
                etWorkEndTime.setVisibility(View.GONE);
                etTravelEndTime.setVisibility(View.GONE);
                etTravelDistance.setVisibility(View.GONE);
                btnTravelStartNow.setVisibility(View.GONE);
                btnWorkStartNow.setVisibility(View.GONE);
                btnWorkEndNow.setVisibility(View.GONE);
                btnTravelEndNow.setVisibility(View.GONE);
                tvTravelStart.setVisibility(View.GONE);
                tvTravelEnd.setVisibility(View.GONE);
                tvWorkStart.setVisibility(View.GONE);
                tvWorkEnd.setVisibility(View.GONE);
                tvTravelDist.setVisibility(View.GONE);
                etTravelDistance.setVisibility(View.GONE);
                loBreak.setVisibility(View.GONE);
                swWeekEndOverTime.setVisibility(View.GONE);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(loNewDate);
                constraintSet.connect(R.id.et_time_sheet_date, ConstraintSet.START, R.id.tvDate, ConstraintSet.END, 32);
                constraintSet.applyTo(loNewDate);
            } else {
                etTravelStartTime.setVisibility(View.VISIBLE);
                etWorkStartTime.setVisibility(View.VISIBLE);
                etWorkEndTime.setVisibility(View.VISIBLE);
                etTravelEndTime.setVisibility(View.VISIBLE);
                etTravelDistance.setVisibility(View.VISIBLE);
                btnTravelStartNow.setVisibility(View.VISIBLE);
                btnWorkStartNow.setVisibility(View.VISIBLE);
                btnWorkEndNow.setVisibility(View.VISIBLE);
                btnTravelEndNow.setVisibility(View.VISIBLE);
                tvTravelStart.setVisibility(View.VISIBLE);
                tvTravelEnd.setVisibility(View.VISIBLE);
                tvWorkStart.setVisibility(View.VISIBLE);
                tvWorkEnd.setVisibility(View.VISIBLE);
                tvTravelDist.setVisibility(View.VISIBLE);
                etTravelDistance.setVisibility(View.VISIBLE);
                loBreak.setVisibility(View.VISIBLE);
                swWeekEndOverTime.setVisibility(View.VISIBLE);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(loNewDate);
                constraintSet.connect(R.id.et_time_sheet_date, ConstraintSet.START, R.id.et_travel_start_time, ConstraintSet.START, 0);
                constraintSet.connect(R.id.et_time_sheet_date, ConstraintSet.TOP, R.id.sw_travel_day, ConstraintSet.BOTTOM, 8);
                constraintSet.connect(R.id.et_time_sheet_date, ConstraintSet.END, R.id.btn_date_now, ConstraintSet.START, 8);
                constraintSet.applyTo(loNewDate);
            }

        });

        swTravelDay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etWorkStartTime.setVisibility(View.GONE);
                etWorkEndTime.setVisibility(View.GONE);
                tvWorkStart.setVisibility(View.GONE);
                tvWorkEnd.setVisibility(View.GONE);
                loBreak.setVisibility(View.GONE);
                btnWorkStartNow.setVisibility(View.GONE);
                btnWorkEndNow.setVisibility(View.GONE);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(loNewDate);
                constraintSet.connect(R.id.et_travel_end_time, ConstraintSet.TOP, R.id.et_travel_start_time, ConstraintSet.BOTTOM, 8);
                constraintSet.connect(R.id.et_travel_end_time, ConstraintSet.START, R.id.et_travel_start_time, ConstraintSet.START, 0);
                constraintSet.connect(R.id.et_travel_end_time, ConstraintSet.END, R.id.btn_travel_end_time_now, ConstraintSet.START, 8);

                constraintSet.applyTo(loNewDate);
            } else {
                etWorkStartTime.setVisibility(View.VISIBLE);
                etWorkEndTime.setVisibility(View.VISIBLE);
                tvWorkStart.setVisibility(View.VISIBLE);
                tvWorkEnd.setVisibility(View.VISIBLE);
                btnWorkStartNow.setVisibility(View.VISIBLE);
                btnWorkEndNow.setVisibility(View.VISIBLE);
                loBreak.setVisibility(View.VISIBLE);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(loNewDate);
                constraintSet.connect(R.id.et_travel_end_time, ConstraintSet.TOP, R.id.et_work_end_time, ConstraintSet.BOTTOM, 8);

                constraintSet.applyTo(loNewDate);
            }

        });

    }


    @Override
    protected void onPause() {
        SharedPreferences NewDateData = getSharedPreferences(NEW_DATE_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = NewDateData.edit();
        if (date != null) {
            editor.putString(DATE_KEY, DateFormatter.formatDateForDataBase(date));
        } else {
            editor.putString(DATE_KEY, "");
        }
        editor.putString(TRAVEL_START_TIME_KEY, etTravelStartTime.getText().toString());
        editor.putString(WORK_START_TIME_KEY, etWorkStartTime.getText().toString());
        editor.putString(WORK_END_TIME_KEY, etWorkEndTime.getText().toString());
        editor.putString(TRAVEL_END_TIME_KEY, etTravelEndTime.getText().toString());
        editor.putInt(BREAK_HOUR_KEY, spBreakTime.getSelectedItemPosition());
        editor.putString(TRAVEL_DISTANCE_KEY, etTravelDistance.getText().toString());
        editor.putInt(CUSTOMER_NAME_KEY, spCustomer.getSelectedItemPosition());
        editor.putInt(HME_CODE_KEY, spHMECode.getSelectedItemPosition());
        editor.putBoolean(OVER_TIME_KEY, swWeekEndOverTime.isChecked());
        editor.putInt(IBAU_SO_KEY, spIBAUCode.getSelectedItemPosition());
        editor.putBoolean(travelDay, swTravelDay.isChecked());

        editor.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {

        SharedPreferences NewDateData = getSharedPreferences(NEW_DATE_DATA, MODE_PRIVATE);

        date = DateFormatter.formatDateToCalendar(NewDateData.getString(DATE_KEY, ""));
        if (date != null) {
            etTimeSheetDate.setText(DateFormatter.dateFormatToLocaleString(date));
        }
        etTravelStartTime.setText(NewDateData.getString(TRAVEL_START_TIME_KEY, ""));
        etWorkStartTime.setText(NewDateData.getString(WORK_START_TIME_KEY, ""));
        etWorkEndTime.setText(NewDateData.getString(WORK_END_TIME_KEY, ""));
        etTravelEndTime.setText(NewDateData.getString(TRAVEL_END_TIME_KEY, ""));
        spBreakTime.setSelection(NewDateData.getInt(BREAK_HOUR_KEY, settings.getInt(BREAK_DEFAULT_KEY, 0)));
        etTravelDistance.setText(NewDateData.getString(TRAVEL_DISTANCE_KEY, ""));
        swTravelDay.setChecked(NewDateData.getBoolean(travelDay, false));
        swWeekEndOverTime.setChecked(NewDateData.getBoolean(OVER_TIME_KEY, false));


        super.onResume();
    }

    @SuppressLint("DefaultLocale")
    String getTime() {
        final Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY); // current year
        int minutes = c.get(Calendar.MINUTE); // current month

        return (String.format("%02d:%02d", hours, minutes));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu_only, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            onBackPressed();
            return true;

        } else if (item.getItemId() == R.id.save) {
            if (
                    ((etTravelStartTime.getText().toString().isEmpty() || etTravelEndTime.getText().toString().isEmpty() || etTravelDistance.getText().toString().isEmpty()) && !swWeekend.isChecked())
                            || ((etWorkStartTime.getText().toString().isEmpty() || etWorkEndTime.getText().toString().isEmpty()) && (!swWeekend.isChecked() && !swTravelDay.isChecked()))
                            || etTimeSheetDate.getText().toString().isEmpty()
                            || spHMECode.getSelectedItem() == null
                            || spCustomer.getSelectedItem() == null
                            || (spIBAUCode.getSelectedItem() == null && settings.getBoolean(IBAU_USER_KEY, false))
            ) {
                new AlertDialog.Builder(NewDateActivity.this)
                        .setTitle("Missing Data")
                        .setMessage("Please complete missing data")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else if (spBreakTime == null || spCustomer == null || spHMECode == null) {
                new AlertDialog.Builder(NewDateActivity.this)
                        .setTitle("Missing Data")
                        .setMessage("Please complete missing data")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {

                String[] breakTimeArray;
                ArrayAdapter<String> adapter;
                breakTimeArray = getResources().getStringArray(R.array.break_time);
                adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        breakTimeArray);

                TimeSheet new_data;

                String ibauCode = "not applicable";
                if (spIBAUCode.getSelectedItem() != null) {
                    ibauCode = spIBAUCode.getSelectedItem().toString();
                }

                if (swWeekend.isChecked()) {
                    new_data = new TimeSheet(
                            DateFormatter.formatDateForDataBase(date),
                            "---",
                            "---",
                            "---",
                            "---",
                            "---",
                            0,
                            spCustomer.getSelectedItem().toString(),
                            spHMECode.getSelectedItem().toString(),
                            false,
                            false,
                            ibauCode,
                            false);
                } else if (swTravelDay.isChecked()) {
                    new_data = new TimeSheet(
                            DateFormatter.formatDateForDataBase(date),
                            etTravelStartTime.getText().toString(),
                            "---",
                            "---",
                            etTravelEndTime.getText().toString(),
                            "---",
                            Integer.parseInt(etTravelDistance.getText().toString()),
                            spCustomer.getSelectedItem().toString(),
                            spHMECode.getSelectedItem().toString(), false,
                            swWeekEndOverTime.isChecked(),
                            ibauCode,
                            false);
                } else {
                    new_data = new TimeSheet(
                            DateFormatter.formatDateForDataBase(date),
                            etTravelStartTime.getText().toString(),
                            etWorkStartTime.getText().toString(),
                            etWorkEndTime.getText().toString(),
                            etTravelEndTime.getText().toString(),
                            adapter.getItem(spBreakTime.getSelectedItemPosition()),
                            Integer.parseInt(etTravelDistance.getText().toString()),
                            spCustomer.getSelectedItem().toString(),
                            spHMECode.getSelectedItem().toString(), false,
                            swWeekEndOverTime.isChecked(),
                            ibauCode,
                            swWeekEndOverTime.isChecked());
                }


                viewModel.insert(new_data);


                Toast.makeText(getApplicationContext(), "Date added", Toast.LENGTH_SHORT).show();
                etTimeSheetDate.setText("");
                date = null;
                swWeekend.setChecked(false);
                swWeekEndOverTime.setChecked(false);
                swTravelDay.setChecked(false);

                if (settings.getBoolean(CLEAR_DATE_KEY, false)) {
                    etTravelStartTime.setText("");
                    etWorkStartTime.setText("");
                    etWorkEndTime.setText("");
                    etTravelEndTime.setText("");
                    etTravelDistance.setText("");
                    spBreakTime.setSelection(settings.getInt(BREAK_DEFAULT_KEY, 0));

                }
            }

            return true;
        } else return super.onOptionsItemSelected(item);

    }

}