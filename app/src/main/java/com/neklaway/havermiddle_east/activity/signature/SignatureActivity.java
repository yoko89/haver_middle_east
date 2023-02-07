package com.neklaway.havermiddle_east.activity.signature;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.neklaway.havermiddle_east.R;

import java.util.Objects;

public class SignatureActivity extends AppCompatActivity {

    private SignaturePad mSignaturePad;
    private Button btnClear, btnOK;
    private SignatureViewModel viewModel;
    static final String signSuccess = "SignSuccessful";

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        i.putExtra(signSuccess, false);
        setResult(RESULT_OK,i);

        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        Objects.requireNonNull(getSupportActionBar()).hide(); //make more space


        mSignaturePad = findViewById(R.id.signature_pad);
        btnClear = findViewById(R.id.btnClear);
        btnOK = findViewById(R.id.btnOK);

        viewModel = new ViewModelProvider(this).get(SignatureViewModel.class);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                btnOK.setEnabled(true);
                btnClear.setEnabled(true);
                }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                btnOK.setEnabled(false);
                btnClear.setEnabled(false);
            }
        });

        btnClear.setOnClickListener(v -> mSignaturePad.clear());

        btnOK.setOnClickListener(v -> {
            viewModel.setSignature(mSignaturePad.getTransparentSignatureBitmap(),getIntent().getExtras().getString(SignatureViewActivity.signOf));

            Intent out = getIntent();
            out.putExtra(signSuccess, true);
            setResult(RESULT_OK,out);

            finish();

        });



    }
}