package com.neklaway.havermiddle_east.activity.newHMECode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.entities.HMECode;

import java.util.Objects;

public class NewHMECodeActivity extends AppCompatActivity {

    private Spinner spCustomer;
    private EditText etHMECode, etMachineType, etMachineNumber, etWorkDescription;
    private RecyclerView rvHMECode;

    private final String newDateData = "new_date_data";
    private final String customerName = "customer_name_key";
    private boolean firstRunCustomer = true;

    private NewHMECodeViewModel viewModel;

    private String oldHMECode;
    private int editID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hme_code);

        spCustomer = findViewById(R.id.sp_customer);
        etHMECode = findViewById(R.id.et_HME_code);
        etMachineType = findViewById(R.id.et_machine_type);
        etMachineNumber = findViewById(R.id.et_machine_number);
        etWorkDescription = findViewById(R.id.et_work_description);

        rvHMECode = findViewById(R.id.rvHMECode);

        viewModel = new ViewModelProvider(this).get(NewHMECodeViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        SharedPreferences NewDateData = getSharedPreferences(newDateData, MODE_PRIVATE);


        viewModel.getAllCustomers().observe(this, customersName -> {
            ArrayAdapter<String> allCustomerAdapter = new ArrayAdapter<>(NewHMECodeActivity.this,
                    android.R.layout.simple_list_item_1,
                    customersName);

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

        NewHMECodeAdapter newHMECodeAdapter = new NewHMECodeAdapter();
        rvHMECode.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvHMECode.setAdapter(newHMECodeAdapter);


        viewModel.getHME().observe(NewHMECodeActivity.this, hmeCodes -> {

            newHMECodeAdapter.submitList(hmeCodes);


            newHMECodeAdapter.setListener(hmeCode -> {

                //get old name
                oldHMECode = hmeCode.getHMECode();
                editID = hmeCode.getId();
                invalidateOptionsMenu();

                //set editText
                etHMECode.setText(hmeCode.getHMECode());
                etMachineNumber.setText(hmeCode.getMachineNumber());
                etMachineType.setText(hmeCode.getMachineType());
                etWorkDescription.setText(hmeCode.getDescriptionWork());
            });
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                    viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            viewModel.delete(newHMECodeAdapter.getCurrentList().get(viewHolder.getAdapterPosition()));
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            newHMECodeAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(NewHMECodeActivity.this);
                builder.setMessage("Delete Entry?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).setCancelable(false).show();
            }
        }).attachToRecyclerView(rvHMECode);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.edit) {
            HMECode updatedHME = new HMECode(etHMECode.getText().toString().trim(), spCustomer.getSelectedItem().toString().trim(), etMachineType.getText().toString().trim(), etMachineNumber.getText().toString().trim(), etWorkDescription.getText().toString().trim(), 0);
            updatedHME.setId(editID);
            viewModel.update(updatedHME, oldHMECode);
            etHMECode.setText("");
            etMachineNumber.setText("");
            etMachineType.setText("");
            etWorkDescription.setText("");
            editID = -1;
            invalidateOptionsMenu();
            return true;
        } else if (item.getItemId() == R.id.save) {
            if (spCustomer.getSelectedItem().toString().isEmpty() || etHMECode.getText().toString().trim().isEmpty() || etMachineType.getText().toString().trim().isEmpty() || etMachineNumber.getText().toString().trim().isEmpty() || etWorkDescription.getText().toString().trim().isEmpty()) {
                new AlertDialog.Builder(NewHMECodeActivity.this)
                        .setTitle("Missing Data")
                        .setMessage("Please complete missing data")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                HMECode new_data = new HMECode(
                        etHMECode.getText().toString(),
                        spCustomer.getSelectedItem().toString(),
                        etMachineType.getText().toString(),
                        etMachineNumber.getText().toString(),
                        etWorkDescription.getText().toString(), 1);

                viewModel.insert(new_data).observe(NewHMECodeActivity.this, insertValue -> {
                    if(insertValue == -1L) Toast.makeText(this, "Can't Duplicate HME Code", Toast.LENGTH_LONG).show();
                });
                etHMECode.setText("");
                etMachineNumber.setText("");
                etMachineType.setText("");
                etWorkDescription.setText("");
            }
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_edit_menu, menu);
        menu.getItem(1).setVisible(editID != -1);
        return true;
    }

}