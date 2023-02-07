package com.neklaway.havermiddle_east.activity.signature;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.neklaway.havermiddle_east.R;

public class SignatureViewActivity extends AppCompatActivity {

    private Button btnNewSign;
    private ImageView ivSign;
    private SignatureViewModel viewModel;
    public static final String signOf = "signatureOf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_view);

        btnNewSign = findViewById(R.id.btnNewSign);
        ivSign = findViewById(R.id.ivSignature);

        viewModel = new ViewModelProvider(this).get(SignatureViewModel.class);


        btnNewSign.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SignatureActivity.class);
            i.putExtra(signOf,"userSign");
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        Bitmap bm = viewModel.getSignature(getIntent().getExtras().getString(signOf));

        ivSign.setImageBitmap(bm);


        super.onResume();
    }
}