package com.neklaway.havermiddle_east.activity.newCustomer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.entities.Customer;

import java.util.Objects;


public class NewCustomerActivity extends AppCompatActivity {

    private EditText etCustomerName, etCustomerCity, etCustomerCountry;
    private RecyclerView rvAllCustomer;

    private NewCustomerViewModel viewModel;

    private int editID;
    private String oldCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        //initialize
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerCity = findViewById(R.id.etCustomerCity);
        etCustomerCountry = findViewById(R.id.etCustomerCountry);

        rvAllCustomer = findViewById(R.id.rvAllCustomer);

        editID = -1;

        viewModel = new ViewModelProvider(this).get(NewCustomerViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        CustomerListAdapter customerListAdapter = new CustomerListAdapter();
        rvAllCustomer.setLayoutManager(new LinearLayoutManager(this));
        rvAllCustomer.setAdapter(customerListAdapter);


        viewModel.getAllCustomers().observe(this, customerListAdapter::submitList);


        customerListAdapter.setListener(customer -> {

            //get old name
            editID = customer.getId();
            oldCustomer = customer.getCustomerName();
            //set editText
            etCustomerCity.setText(customer.getCustomerCity());
            etCustomerCountry.setText(customer.getCustomerCountry());
            etCustomerName.setText(customer.getCustomerName());

            invalidateOptionsMenu();
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


            if (etCustomerName.getText().toString().trim().isEmpty() || etCustomerCity.getText().toString().trim().isEmpty() || etCustomerCountry.getText().toString().trim().isEmpty()) {
                new AlertDialog.Builder(NewCustomerActivity.this)
                        .setTitle("Missing Data")
                        .setMessage("Please complete missing data")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                Customer new_data = new Customer(etCustomerName.getText().toString(),
                        etCustomerCity.getText().toString(),
                        etCustomerCountry.getText().toString());

                viewModel.insert(new_data).observe(this, insertValue -> {
                    if(insertValue == -1L)
                        Toast.makeText(this, "Can't duplicate customer", Toast.LENGTH_LONG).show();
                });
                etCustomerCity.setText("");
                etCustomerCountry.setText("");
                etCustomerName.setText("");
            }
            return true;
        } else if (item.getItemId() == R.id.edit) {
            if (editID != -1) {
                Customer editableCustomer = new Customer(etCustomerName.getText().toString(),
                        etCustomerCity.getText().toString(),
                        etCustomerCountry.getText().toString());
                editableCustomer.setId(editID);

                viewModel.update(editableCustomer,oldCustomer);
                etCustomerCity.setText("");
                etCustomerCountry.setText("");
                etCustomerName.setText("");
                editID=-1;
                invalidateOptionsMenu();
            }
            return true;
        } else return super.onOptionsItemSelected(item);

    }
}