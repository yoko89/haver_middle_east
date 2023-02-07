package com.neklaway.havermiddle_east.activity.visaLog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.entities.Visa;
import com.neklaway.havermiddle_east.utils.DateFormatter;

import java.util.Calendar;

public class VisaLogActivity extends AppCompatActivity {

    private EditText etVisaDate;
    private DatePickerDialog datePickerDialog;
    private Spinner spVisaCountry;
    private RecyclerView rvVisa;

    private VisaLogAdapter visaAdapter;

    private VisaLogViewModel viewModel;

    Calendar cal;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_log);

        // initiate the date picker and a button
        etVisaDate = findViewById(R.id.et_visa_date);
        spVisaCountry = findViewById(R.id.sp_visa_country);

        rvVisa = findViewById(R.id.rv_visa);
        visaAdapter = new VisaLogAdapter();
        rvVisa.setLayoutManager(new LinearLayoutManager(this));
        rvVisa.setAdapter(visaAdapter);


        viewModel = new ViewModelProvider(this).get(VisaLogViewModel.class);
        viewModel.getAll().observe(this, visas -> visaAdapter.submitList(visas));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            viewModel.delete(visaAdapter.getVisaAt(viewHolder.getAdapterPosition()));

                            //Yes button clicked
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            visaAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            //No button clicked
                            break;

                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(VisaLogActivity.this);
                builder.setMessage("Delete Entry?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).setCancelable(false).show();

            }
        }).attachToRecyclerView(rvVisa);


        // perform click event on edit text
        etVisaDate.setOnClickListener(v -> {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(VisaLogActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        // set day of month , month and year value in the edit text
                        etVisaDate.setText(DateFormatter.dateFormatToLocaleString(cal));

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        visaAdapter.setListener(visa -> {
            Visa newVisa = new Visa(visa.getCountry(), visa.getDate(),!visa.isEnabled());
            newVisa.setId(visa.getId());
            viewModel.update(newVisa);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu_only, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if (etVisaDate.getText().toString().isEmpty()) {
                new AlertDialog.Builder(VisaLogActivity.this)
                        .setTitle("Missing Data")
                        .setMessage("Please complete missing data")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                Visa new_data = new Visa(spVisaCountry.getSelectedItem().toString(), DateFormatter.formatDateForDataBase(cal), true);
                viewModel.insert(new_data);
            }
        }
        return super.onOptionsItemSelected(item);

    }
}