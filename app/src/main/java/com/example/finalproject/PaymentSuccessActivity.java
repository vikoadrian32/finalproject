package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class PaymentSuccessActivity extends AppCompatActivity {

    private Button btnBackHome;
    private TextView amountPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        // Initialize Button
        btnBackHome = findViewById(R.id.btnBackHome);

        amountPay = findViewById(R.id.amount);
        Intent intent = getIntent();
        String price = intent.getStringExtra("totalPrice");
        amountPay.setText(price);
        // Set Click Listener to navigate back to home
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to HomeActivity (replace HomeActivity.class with your main activity)
                Intent intent = new Intent(PaymentSuccessActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
