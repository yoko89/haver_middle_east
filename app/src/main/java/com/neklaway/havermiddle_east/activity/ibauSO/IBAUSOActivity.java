package com.neklaway.havermiddle_east.activity.ibauSO;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.activity.oldDate.OldDateAdapter;
import com.neklaway.havermiddle_east.entities.IBAUSO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IBAUSOActivity extends AppCompatActivity {

    private Spinner spCustomer, spHMECode;
    private EditText etIBAUCode;
    private RecyclerView rvIBAU;
    private IBAUSOViewModel viewModel;

    private final String newDateData = "new_date_data";
    private final String customerName = "customer_name_key";
    private final String HMECode = "HME_code_key";
    private boolean firstRunCustomer = true;
    private boolean firstRunHME = true;

    private int editID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibau_so);

        spCustomer = findViewById(R.id.sp_customer);
        spHMECode = findViewById(R.id.sp_HME_code);
        etIBAUCode = findViewById(R.id.et_IBAUCode);

        rvIBAU = findViewById(R.id.rvIBAUSO);

        SharedPreferences NewDateData = getSharedPreferences(newDateData, MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this).get(IBAUSOViewModel.class);


        viewModel.getCustomerList().observe(this, customersName -> {
            ArrayAdapter<String> allCustomerAdapter = new ArrayAdapter<>(IBAUSOActivity.this,
                    android.R.layout.simple_list_item_1, customersName);
            spCustomer.setAdapter(allCustomerAdapter);
            if (firstRunCustomer) {
                spCustomer.setSelection(NewDateData.getInt(customerName, 0));
                firstRunCustomer = false;
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

        viewModel.getCustomerHMEs().observe(IBAUSOActivity.this, customerHMEs -> {
            ArrayAdapter<String> customerHMECode = new ArrayAdapter<>(IBAUSOActivity.this,
                    android.R.layout.simple_list_item_1, customerHMEs);

            spHMECode.setAdapter(customerHMECode);
            if (firstRunHME) {
                spHMECode.setSelection(NewDateData.getInt(HMECode, 0));
                firstRunHME = false;
            }
        });

        spHMECode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id2) {
                viewModel.setHME(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewModel.getIBAU().observe(IBAUSOActivity.this, ibausoData -> {
            List<String> IBAUSO = ibausoData.stream().map(ibausoData1 -> com.neklaway.havermiddle_east.entities.IBAUSO.toString(ibausoData1)).collect(Collectors.toList());
            OldDateAdapter oldDateAdapter = new OldDateAdapter(IBAUSO);
            rvIBAU.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvIBAU.setAdapter(oldDateAdapter);

            oldDateAdapter.setListener(new OldDateAdapter.Listener() {
                @Override
                public void onClick(int position) {
                    editID = ibausoData.get(position).getId();
                    invalidateOptionsMenu();
                    //set editText
                    etIBAUCode.setText(ibausoData.get(position).getIBAUSO());
                }

                @Override
                public void onLongClick(int id) {

                }


            });
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_edit_menu, menu);
        menu.getItem(1).setVisible(editID != -1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            onBackPressed();
            return true;

        } else if (item.getItemId() == R.id.save) {
            if (spCustomer.getSelectedItem().toString().isEmpty() || spHMECode.getSelectedItem().toString().trim().isEmpty() || etIBAUCode.getText().toString().trim().isEmpty()) {
                new AlertDialog.Builder(IBAUSOActivity.this)
                        .setTitle("Missing Data")
                        .setMessage("Please complete missing data")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {

                IBAUSO new_data = new IBAUSO(spHMECode.getSelectedItem().toString().trim(),
                        etIBAUCode.getText().toString().trim());
                viewModel.insert(new_data);
                etIBAUCode.setText("");
            }
            return true;
        } else if (item.getItemId() == R.id.edit) {

            IBAUSO ibausoUpdated = new IBAUSO(spHMECode.getSelectedItem().toString().trim(), etIBAUCode.getText().toString().trim());
            ibausoUpdated.setId(editID);
            viewModel.update(ibausoUpdated);
            etIBAUCode.setText("");
            editID=-1;
            invalidateOptionsMenu();
            return true;
        } else return super.onOptionsItemSelected(item);

    }
}