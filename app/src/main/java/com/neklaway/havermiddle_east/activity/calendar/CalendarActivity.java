package com.neklaway.havermiddle_east.activity.calendar;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;

import java.util.Calendar;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity {

    private TextView tvDate,tvMonth;

    private CalendarAdapter calendarAdapter;

    private CalendarViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageButton btnForward = findViewById(R.id.btnForward);
        ImageButton btnBackward = findViewById(R.id.btnBackward);

        RecyclerView rvHistory = findViewById(R.id.rvHistory);

        calendarAdapter = new CalendarAdapter();
        rvHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvHistory.setAdapter(calendarAdapter);

        tvDate = findViewById(R.id.tvDate);
        tvMonth = findViewById(R.id.tvMonth);

        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        Calendar calendar = Calendar.getInstance();

        updateList(calendar);
        btnForward.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH,1);
            updateList(calendar);
        });

        btnBackward.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH,-1);
            updateList(calendar);
        });

        viewModel.getTimeSheet().observe(this, timeSheets -> calendarAdapter.submitList(timeSheets));
    }

    private void updateList(Calendar calendar){
        String date = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        tvDate.setText(date);

        tvMonth.setText(calendar.getDisplayName(Calendar.MONTH,Calendar.SHORT_STANDALONE, Locale.ENGLISH));
        viewModel.setCalender(calendar);
    }


}
