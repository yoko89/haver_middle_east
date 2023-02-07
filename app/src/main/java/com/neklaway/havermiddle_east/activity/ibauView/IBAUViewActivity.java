package com.neklaway.havermiddle_east.activity.ibauView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;

import java.util.Objects;

public class IBAUViewActivity extends AppCompatActivity {

    private RecyclerView rvIBAU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibau_view);

        IBAUViewViewModel viewModel = new ViewModelProvider(this).get(IBAUViewViewModel.class);

        rvIBAU = findViewById(R.id.rvIBAU);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        viewModel.getAllIBAUSO().observe(this, ibauSOs -> {

            IBAUListAdapter ibauListAdapter = new IBAUListAdapter(ibauSOs);
            rvIBAU.setAdapter(ibauListAdapter);

            ibauListAdapter.setListener(hmeCode -> {

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(IBAUViewActivity.this);
                builder.setMessage(hmeCode).setNeutralButton("OK", dialogClickListener).show();
            });
        });

        rvIBAU.setLayoutManager(new LinearLayoutManager(this));


    }
}