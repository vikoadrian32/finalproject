package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class OvoPaymentActivity extends AppCompatActivity {

    private EditText editPhoneNumber;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovo_payment);

        // Initialize views
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        btnNext = findViewById(R.id.btnNext);

        // Next button click logic
        btnNext.setOnClickListener(v -> {
            String phoneNumber = editPhoneNumber.getText().toString().trim();
            Intent getid = getIntent();
            ArrayList<String> productIds = getid.getStringArrayListExtra("product_ids");

            if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 10) {
                Toast.makeText(OvoPaymentActivity.this, "Please enter a valid OVO number", Toast.LENGTH_SHORT).show();
            } else {
                // Navigate to passcode page and pass the phone number
                Intent intent = new Intent(OvoPaymentActivity.this, OvoPasscodeActivity.class);
                intent.putExtra("PHONE_NUMBER", phoneNumber);
                intent.putExtra("productId",productIds);
                startActivity(intent);
            }
        });
    }
}
