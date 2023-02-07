package com.neklaway.havermiddle_east.activity.oldDate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class OldDateActivity extends AppCompatActivity {

    private Spinner spHMECode, spCustomer;
    private RecyclerView rvDates;

    private OldDateViewModel viewModel;
    private OldDateAdapter fileAdapter;

    private final String newDateData = "new_date_data";
    private final String customerName = "customer_name_key";
    private final String HMECode = "HME_code_key";
    private boolean firstRunCustomer = true;
    private boolean firstRunHME = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_date);

        spCustomer = findViewById(R.id.sp_customer);
        spHMECode = findViewById(R.id.sp_HME_code);
        rvDates = findViewById(R.id.rv_dates);

        viewModel = new ViewModelProvider(this).get(OldDateViewModel.class);

        SharedPreferences NewDateData = getSharedPreferences(newDateData, MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        viewModel.getCustomerName().observe(this, customersName -> {
            //Update Spinner
            ArrayAdapter<String> allCustomerAdapter = new ArrayAdapter<>(OldDateActivity.this,
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
                viewModel.setSelectedCustomer(parent.getSelectedItem().toString().trim());
                rvDates.setAdapter(null);
            }

                @Override
                public void onNothingSelected (AdapterView < ? > parent){

                }
            });

        viewModel.getCustomerHMEs().observe(OldDateActivity.this, customerHMEs -> {
            ArrayAdapter<String> customerHMECode = new ArrayAdapter<>(OldDateActivity.this,
                    android.R.layout.simple_list_item_1, customerHMEs);

            spHMECode.setAdapter(customerHMECode);
            if(firstRunHME){
                spHMECode.setSelection(NewDateData.getInt(HMECode, 0));
                firstRunHME = false;
            }
        });



        spHMECode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

            {
                @Override
                public void onItemSelected (AdapterView < ? > parent, View view,int position,
                long id){
                File directory = new File(getFilesDir() + "/" + spHMECode.getSelectedItem().toString());
                String[] filesList = directory.list();

                if (filesList != null) {
                    fileAdapter = new OldDateAdapter(Arrays.asList(filesList));

                    rvDates.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvDates.setAdapter(fileAdapter);

                    fileAdapter.setListener(new OldDateAdapter.Listener() {
                        @Override
                        public void onClick(int id) {
                            Intent viewFile = new Intent(Intent.ACTION_VIEW);
                            Uri pdfUri = FileProvider.getUriForFile(OldDateActivity.this, "com.neklaway.havermiddle_east.provider", new File(getFilesDir() + "/" + spHMECode.getSelectedItem().toString() + "/" + filesList[id]));
                            viewFile.setDataAndType(pdfUri, "application/pdf");
                            viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(viewFile);
                        }

                        @Override
                        public void onLongClick(int id) {
                            Intent sendFile = new Intent(Intent.ACTION_SEND);
                            sendFile.setType("message/rfc822");
                            Uri pdfUri = FileProvider.getUriForFile(OldDateActivity.this, "com.neklaway.havermiddle_east.provider", new File(getFilesDir() + "/" + spHMECode.getSelectedItem().toString() + "/" + filesList[id]));

                            sendFile.putExtra(Intent.EXTRA_SUBJECT, "Time Sheet " + spHMECode.getSelectedItem().toString());
                            sendFile.putExtra(Intent.EXTRA_STREAM, pdfUri);
                            sendFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(sendFile);
                        }

                    });
                } else {
                    rvDates.setAdapter(null);
                }


            }

                @Override
                public void onNothingSelected (AdapterView < ? > parent){

            }
            });


        }
    }