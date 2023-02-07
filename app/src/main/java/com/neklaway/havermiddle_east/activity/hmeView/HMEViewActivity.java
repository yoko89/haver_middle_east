package com.neklaway.havermiddle_east.activity.hmeView;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;

import java.util.Objects;

public class HMEViewActivity extends AppCompatActivity {

    private RecyclerView rvHME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hme_view);


        rvHME = findViewById(R.id.rvHME);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        HMEViewModel viewModel = new ViewModelProvider(this).get(HMEViewModel.class);

        viewModel.getAllHME().observe(this, hmeCodes -> {
            HMEListAdapter hmeListAdapter = new HMEListAdapter(hmeCodes);
            rvHME.setAdapter(hmeListAdapter);
            rvHME.setLayoutManager(new LinearLayoutManager(this));

            hmeListAdapter.setListener(customer -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(HMEViewActivity.this);
                builder.setMessage(customer).setNeutralButton("OK", null).show();

            });
        });

    }
}