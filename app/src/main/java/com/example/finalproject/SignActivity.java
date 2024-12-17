package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SignActivity extends AppCompatActivity {
    private String url = "http://192.168.27.175/WMP/insert_regist_table.php";

    EditText etUsername, etEmail, etPass;
    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);


        etUsername = findViewById(R.id.username_regist);
        etEmail = findViewById(R.id.email_regist);
        etPass = findViewById(R.id.pass_regist);
        signInBtn = findViewById(R.id.signbtn);


        signInBtn.setOnClickListener(view -> insertData());
    }


    private void insertData() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Menangani response sukses dari server
                        Log.d("Response", response);
                        Toast.makeText(SignActivity.this, "Regist Success", Toast.LENGTH_SHORT).show();
                        etUsername.setText("");
                        etEmail.setText("");
                        etPass.setText("");
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                        Toast.makeText(SignActivity.this, "Regist Failed", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
