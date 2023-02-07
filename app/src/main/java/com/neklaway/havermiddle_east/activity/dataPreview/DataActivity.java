package com.neklaway.havermiddle_east.activity.dataPreview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.activity.hmeView.HMEViewActivity;
import com.neklaway.havermiddle_east.activity.ibauView.IBAUViewActivity;
import com.neklaway.havermiddle_east.activity.newCustomer.NewCustomerActivity;

public class DataActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        String settings1 = "user_settings";
        SharedPreferences settings = getSharedPreferences(settings1, MODE_PRIVATE);

        Button btnAddCustomer = findViewById(R.id.btn_add_customer);
        Button btnHMECode = findViewById(R.id.btn_hme_code);
        Button btnIBAUCode = findViewById(R.id.btn_IBAU_code);

        String IBAUUser = "IBAU_user_key";
        if(settings.getBoolean(IBAUUser,false)){
            btnIBAUCode.setVisibility(View.VISIBLE);
        }else{
            btnIBAUCode.setVisibility(View.GONE);
        }

        btnAddCustomer.setOnClickListener(v -> {

            Intent i = new Intent(getApplicationContext(), NewCustomerActivity.class);
            startActivity(i);
        });

        btnHMECode.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), HMEViewActivity.class);
            startActivity(i);
        });

        btnIBAUCode.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), IBAUViewActivity.class);
            startActivity(i);
        });



    }
}