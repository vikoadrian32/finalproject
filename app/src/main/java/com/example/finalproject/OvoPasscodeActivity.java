package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OvoPasscodeActivity extends AppCompatActivity {

    private EditText passcodeDigit1, passcodeDigit2, passcodeDigit3, passcodeDigit4, passcodeDigit5, passcodeDigit6;
    private Button btnSubmitPasscode;

    private String url = "http://192.168.1.59/WMP/add_to_orders.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovo_passcode);

        // Initialize EditTexts and Button
        passcodeDigit1 = findViewById(R.id.passcodeDigit1);
        passcodeDigit2 = findViewById(R.id.passcodeDigit2);
        passcodeDigit3 = findViewById(R.id.passcodeDigit3);
        passcodeDigit4 = findViewById(R.id.passcodeDigit4);
        passcodeDigit5 = findViewById(R.id.passcodeDigit5);
        passcodeDigit6 = findViewById(R.id.passcodeDigit6);
        btnSubmitPasscode = findViewById(R.id.btnSubmitPasscode);

        setupPasscodeInput();

        // Submit Button Logic
        btnSubmitPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePasscode();
            }
        });
    }

    // Setup passcode input fields to auto-move focus
    private void setupPasscodeInput() {
        EditText[] passcodeFields = {passcodeDigit1, passcodeDigit2, passcodeDigit3, passcodeDigit4, passcodeDigit5, passcodeDigit6};

        for (int i = 0; i < passcodeFields.length; i++) {
            final int currentIndex = i;
            passcodeFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && currentIndex < passcodeFields.length - 1) {
                        passcodeFields[currentIndex + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            passcodeFields[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && passcodeFields[currentIndex].getText().length() == 0 && currentIndex > 0) {
                    passcodeFields[currentIndex - 1].requestFocus();
                }
                return false;
            });
        }
    }

    // Validate Passcode Input
    private void validatePasscode() {
        String passcode = getEnteredPasscode();

        if (passcode.length() < 6) {
            Toast.makeText(this, "Please enter all 6 digits", Toast.LENGTH_SHORT).show();
        } else if (isPasscodeCorrect(passcode)) {
            Intent getId = getIntent();
            ArrayList<String> productIds = getId.getStringArrayListExtra("productId");
            SharedPreferences sharedPreferences = getSharedPreferences("cartPreferences", MODE_PRIVATE);
            SharedPreferences sharedPreferences1 = getSharedPreferences("userProfile",MODE_PRIVATE);
            String user_id = sharedPreferences1.getString("id","0");

            float totalPrice = sharedPreferences.getFloat("total_price", 0.0f);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            Log.d("OrderResponse", "Response: " + response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                                deleteProductsFromCart(user_id);
                                Intent intent = new Intent(getApplicationContext(),PaymentSuccessActivity.class);
                                intent.putExtra("totalPrice",String.valueOf(totalPrice));
                                startActivity(intent);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                            } else {
                                Toast.makeText(getApplicationContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e("OrderError", "Error: " + error.getMessage());  // Log the error message
                        Toast.makeText(getApplicationContext(), "Failed to place order: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_ids", String.valueOf(productIds));
                    params.put("total_price", String.valueOf(totalPrice));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Incorrect Passcode. Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get the full passcode entered
    private String getEnteredPasscode() {
        return passcodeDigit1.getText().toString() +
                passcodeDigit2.getText().toString() +
                passcodeDigit3.getText().toString() +
                passcodeDigit4.getText().toString() +
                passcodeDigit5.getText().toString() +
                passcodeDigit6.getText().toString();
    }

    // Dummy method to check if the passcode is correct
    private boolean isPasscodeCorrect(String passcode) {
        String correctPasscode = "123456"; // Replace this with actual passcode validation logic
        return passcode.equals(correctPasscode);
    }
    private void deleteProductsFromCart(String userId) {
        String url = "http://192.168.1.59/WMP/delete_from_cart.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        Log.d("ClearCartResponse", "Response: " + response);
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Toast.makeText(getApplicationContext(), "Cart cleared from database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("ClearCartError", "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Error clearing cart: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId); // Kirim user_id untuk menghapus data cart
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
