package com.neklaway.havermiddle_east.activity.newTimeSheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.activity.signature.SignatureActivity;
import com.neklaway.havermiddle_east.entities.Customer;
import com.neklaway.havermiddle_east.entities.HMECode;
import com.neklaway.havermiddle_east.entities.TimeSheet;
import com.neklaway.havermiddle_east.utils.PDFCreator;

import java.io.File;
import java.util.List;


public class NewTimeSheetActivity extends AppCompatActivity {

    private Spinner spHMECode, spCustomer, spIBAUCode;
    private ImageButton btnNewSign, btnWithoutSign, btnViewPdf, btnAttachMail;
    private EditText etSignerName;
    private NewTimeSheetAdapter dateAdapter;

    private NewTimeSheetViewModel viewModel;

    private List<TimeSheet> unCreatedTimeSheetList;
    private Customer customerObject;


    private final String newDateData = "new_date_data";
    private final String customerName = "customer_name_key";
    private final String hmeCodeKey = "HME_code_key";
    private final String IBAU_SO = "IBAU_SO_key";


    private boolean isIbau = false;


    private List<HMECode> hmeCodeObject;

    int selectedHme = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_time_sheet);

        spCustomer = findViewById(R.id.sp_customer);
        spHMECode = findViewById(R.id.sp_HME_code);
        spIBAUCode = findViewById(R.id.sp_IBAU_code);
        RecyclerView rvDates = findViewById(R.id.rv_dates);
        btnNewSign = findViewById(R.id.btnNewSign);
        btnWithoutSign = findViewById(R.id.btnWithoutSign);
        btnViewPdf = findViewById(R.id.btnViewPdf);
        btnAttachMail = findViewById(R.id.btnAttachMail);
        etSignerName = findViewById(R.id.etSignerName);
        LinearLayout loIBAUCode = findViewById(R.id.lo_IBAU_code);

        btnViewPdf.setEnabled(false);
        btnAttachMail.setEnabled(false);

        viewModel = new ViewModelProvider(this).get(NewTimeSheetViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //get settings
        String settings1 = "user_settings";
        SharedPreferences settings = getSharedPreferences(settings1, MODE_PRIVATE);
        SharedPreferences NewDateData = getSharedPreferences(newDateData, MODE_PRIVATE);

        //Update Spinner
        viewModel.allCustomers().observe(this, customers -> {
            ArrayAdapter<String> allCustomerAdapter = new ArrayAdapter<>(NewTimeSheetActivity.this,
                    android.R.layout.simple_list_item_1,
                    customers);
            spCustomer.setAdapter(allCustomerAdapter);

            try {
                int selection = NewDateData.getInt(customerName, 0);
                if(selection < customers.size() && selection>=0) {
                    spCustomer.setSelection(selection);
                }
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

        viewModel.getCustomerObjectForCustomer().observe(this, customers -> customerObject = customers.get(0));

        viewModel.getHME().observe(NewTimeSheetActivity.this, hmeCodes -> {
            ArrayAdapter<String> customerHMECode = new ArrayAdapter<>(NewTimeSheetActivity.this,
                    android.R.layout.simple_list_item_1, hmeCodes);
            spHMECode.setAdapter(customerHMECode);

            try {
                int selection = NewDateData.getInt(hmeCodeKey, 0);
                if(selection < hmeCodes.size() && selection>=0) {
                    spHMECode.setSelection(selection);
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

        dateAdapter = new NewTimeSheetAdapter();
        rvDates.setAdapter(dateAdapter);
        rvDates.setLayoutManager(new LinearLayoutManager(this));


        spHMECode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setHME(parent.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        viewModel.getHMEObjectForHMECode().observe(this, hmeCodes -> hmeCodeObject = hmeCodes);

        String IBAUUser = "IBAU_user_key";
        if (!settings.getBoolean(IBAUUser, false)) {
            if (loIBAUCode.getVisibility() != View.GONE) {
                loIBAUCode.setVisibility(View.GONE);
                isIbau = true;
            }

            viewModel.getUnCreatedDatesByHMECode().observe(NewTimeSheetActivity.this, unCreatedList -> {
                if (unCreatedList.isEmpty()) {
                    btnNewSign.setEnabled(false);
                    btnWithoutSign.setEnabled(false);
                    etSignerName.setEnabled(false);
                } else {
                    btnNewSign.setEnabled(true);
                    btnWithoutSign.setEnabled(true);
                    etSignerName.setEnabled(true);
                    unCreatedTimeSheetList = unCreatedList;
                }
            });


            viewModel.getTimeSheetFromHME().observe(NewTimeSheetActivity.this,
                    timeSheets -> dateAdapter.submitList(timeSheets));


        } else {
            if (loIBAUCode.getVisibility() != View.VISIBLE) {
                loIBAUCode.setVisibility(View.VISIBLE);
            }


            viewModel.getIBAU().observe(NewTimeSheetActivity.this,
                    ibauList -> {
                        ArrayAdapter<String> customerIBAUCode = new ArrayAdapter<>(NewTimeSheetActivity.this, android.R.layout.simple_list_item_1, ibauList);
                        spIBAUCode.setAdapter(customerIBAUCode);

                        try {
                            int selection = NewDateData.getInt(IBAU_SO, 0);
                            if(selection < ibauList.size() && selection>=0) {
                                spIBAUCode.setSelection(selection);
                            }
                        }catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
                    });

            spIBAUCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    viewModel.setIBAU(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            viewModel.getTimeSheetFromIBAU().observe(NewTimeSheetActivity.this, timeSheets -> dateAdapter.submitList(timeSheets));


            viewModel.getUnCreatedDatesByIBAUCode().observe(NewTimeSheetActivity.this, unCreatedList -> {
                if (unCreatedList.isEmpty()) {
                    btnNewSign.setEnabled(false);
                    btnWithoutSign.setEnabled(false);
                    etSignerName.setEnabled(false);
                } else {
                    btnNewSign.setEnabled(true);
                    btnWithoutSign.setEnabled(true);
                    etSignerName.setEnabled(true);
                    unCreatedTimeSheetList = unCreatedList;
                }

            });
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                    viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (dateAdapter.getTimeSheetAt(viewHolder.getAdapterPosition()).isTimeSheetCreated()) {
                    Toast.makeText(NewTimeSheetActivity.this, "Can't delete this item\nDate created in Timesheet", Toast.LENGTH_SHORT).show();
                    dateAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                } else {
                    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                viewModel.delete(dateAdapter.getTimeSheetAt(viewHolder.getAdapterPosition()));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dateAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                break;
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(NewTimeSheetActivity.this);
                    builder.setMessage("Delete Entry?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).setCancelable(false).show();
                }
            }
        }).attachToRecyclerView(rvDates);

        btnNewSign.setOnClickListener(v ->

        {


            if (etSignerName.getText().toString().trim().isEmpty()) {
                new AlertDialog.Builder(NewTimeSheetActivity.this)
                        .setTitle("Missing Data")
                        .setMessage("Please complete Customer Signer Name")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                Intent i = new Intent(this, SignatureActivity.class);
                i.putExtra("signatureOf", spHMECode.getSelectedItem().toString().trim());

                activityResultLauncher.launch(i);


            }

        });


        btnWithoutSign.setOnClickListener(v ->
                createPDF());


        btnAttachMail.setOnClickListener(v -> {
            List<HMECode> hmeCodes = hmeCodeObject;

            Intent sendFile = new Intent(Intent.ACTION_SEND);
            sendFile.setType("message/rfc822");
            Uri pdfUri;
            if (!hmeCodes.isEmpty()) {
                if (hmeCodes.get(0).getFileNumber() == 2) {
                    pdfUri = FileProvider.getUriForFile(NewTimeSheetActivity.this, "com.neklaway.havermiddle_east.provider", new File(getFilesDir() + "/" + spHMECode.getSelectedItem().toString() + "/" + spHMECode.getSelectedItem().toString() + ".pdf"));
                } else {
                    int fileNumber = hmeCodes.get(0).getFileNumber() - 2;
                    pdfUri = FileProvider.getUriForFile(NewTimeSheetActivity.this, "com.neklaway.havermiddle_east.provider", new File(getFilesDir() + "/" + spHMECode.getSelectedItem().toString() + "/" + spHMECode.getSelectedItem().toString() + "_" + fileNumber + ".pdf"));
                }

                sendFile.putExtra(Intent.EXTRA_SUBJECT, "Time Sheet " + spHMECode.getSelectedItem().toString());
                sendFile.putExtra(Intent.EXTRA_STREAM, pdfUri);
                sendFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(sendFile);
            }
        });


        btnViewPdf.setOnClickListener(v -> {
            List<HMECode> hmeCodes = hmeCodeObject;

            Intent sendFile = new Intent(Intent.ACTION_SEND);
            sendFile.setType("message/rfc822");
            Uri pdfUri;
            if (hmeCodes.get(0).getFileNumber() == 2) {
                pdfUri = FileProvider.getUriForFile(NewTimeSheetActivity.this, "com.neklaway.havermiddle_east.provider", new File(getFilesDir() + "/" + spHMECode.getSelectedItem().toString() + "/" + spHMECode.getSelectedItem().toString() + ".pdf"));
            } else {
                int fileNumber = hmeCodes.get(0).getFileNumber() - 2;
                pdfUri = FileProvider.getUriForFile(NewTimeSheetActivity.this, "com.neklaway.havermiddle_east.provider", new File(getFilesDir() + "/" + spHMECode.getSelectedItem().toString() + "/" + spHMECode.getSelectedItem().toString() + "_" + fileNumber + ".pdf"));
            }
            Intent viewFile = new Intent(Intent.ACTION_VIEW);
            viewFile.setDataAndType(pdfUri, "application/pdf");
            viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(viewFile);

        });


    }


    @SuppressLint("DefaultLocale")
    private void createPDF() {
        PDFCreator pdfCreator = new PDFCreator(unCreatedTimeSheetList,
                hmeCodeObject.get(0),
                customerObject,
                isIbau,
                etSignerName.getText().toString().trim(),
                getApplication());

        pdfCreator.createPDF();


        pdfCreator.pdfCreated.observe(this, pdfCreated -> {
            if (pdfCreated) {
                btnAttachMail.setEnabled(true);
                btnViewPdf.setEnabled(true);
                selectedHme = spHMECode.getSelectedItemPosition();
            } else {
                btnAttachMail.setEnabled(false);
                btnViewPdf.setEnabled(false);
            }
        });

    }


    @Override
    protected void onPause() {
        SharedPreferences NewDateData = getSharedPreferences(newDateData, MODE_PRIVATE);
        SharedPreferences.Editor editor = NewDateData.edit();

        editor.putInt(customerName, spCustomer.getSelectedItemPosition());
        editor.putInt(hmeCodeKey, spHMECode.getSelectedItemPosition());
        editor.putInt(IBAU_SO, spIBAUCode.getSelectedItemPosition());

        editor.apply();
        super.onPause();
    }


    final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                boolean signatureOk = intent.getBooleanExtra("SignSuccessful", false);

                if (!signatureOk) {
                    Toast.makeText(getApplicationContext(), "Signature Failed", Toast.LENGTH_LONG).show();
                }

                if (signatureOk) {
                    createPDF();
                }

            }
        }

    });

}