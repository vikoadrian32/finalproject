package com.example.finalproject;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DetailProduct extends AppCompatActivity {
    private TextView productName,productBrand,productPrice;
    private ImageView productImage;
    private Button addtoCart;
    private ImageButton backButton;
    private FloatingActionButton likeBtn;
    private String url = "http://192.168.1.59/WMP/get_recommend.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productName = findViewById(R.id.nameProduct);
        productBrand = findViewById(R.id.brandProduct);
        productPrice = findViewById(R.id.priceProduct);
        productImage = findViewById(R.id.imageProduct);

        Intent intent = getIntent();
        String product_name = intent.getStringExtra("productName");
        String product_brand = intent.getStringExtra("productBrand");
        String product_price = intent.getStringExtra("productPrice");
        String product_image = intent.getStringExtra("productImage");
        String product_discount_price = intent.getStringExtra("productDiscount");

        productName.setText(product_name);
        productBrand.setText(product_brand);
        if (Double.parseDouble(product_discount_price.replace(",", ".")) > 0
                && Double.parseDouble(product_discount_price.replace(",", ".")) < Double.parseDouble(product_price.replace(",", "."))) {
            productPrice.setText("$" + product_discount_price);
        } else {
            productPrice.setText("$" + product_price);
        }

        Picasso.get().load("http://192.168.1.59/WMP/" + product_image).into(productImage);

        likeBtn = findViewById(R.id.recommendBtn);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRecommendationToServer();
            }
        });

        backButton=findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        addtoCart = findViewById(R.id.addtoCart);
        addtoCart.setOnClickListener(v-> {
            SharedPreferences sharedPreferences = getSharedPreferences("userProfile",Context.MODE_PRIVATE);
            String url = "http://192.168.1.59/WMP/add_to_cart.php";
            String product_id = intent.getStringExtra("productId");
            String price;
            String userId  = sharedPreferences.getString("id","0");

            if (Double.parseDouble(product_discount_price.replace(",", ".")) > 0
                    && Double.parseDouble(product_discount_price.replace(",", ".")) < Double.parseDouble(product_price.replace(",", "."))) {
                price = product_discount_price;
            } else {
                price = product_price;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);

                    Toast.makeText(getApplicationContext(), "Produk berhasil ditambahkan ke cart", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.getMessage());
                    Toast.makeText(getApplicationContext(), "Gagal menambahkan produk: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected java.util.Map<String, String> getParams() {
                    java.util.Map<String, String> params = new java.util.HashMap<>();
                    params.put("user_id",userId);
                    params.put("product_id",product_id);
                    params.put("product_price",price);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        });
    }

    private void sendRecommendationToServer(){
        SharedPreferences sharedPreferences = getSharedPreferences("userProfile",MODE_PRIVATE);
        Intent intent = getIntent();
        String user_id = sharedPreferences.getString("id",null);
        String product_id = intent.getStringExtra("productId");
        int point = 1;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Menangani response sukses dari server
                        Log.d("Response", response);
                        Toast.makeText(DetailProduct.this, "Success Recommendation Product", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menangani error response dari server
                        Log.e("Error", error.getMessage());
                        Toast.makeText(DetailProduct.this, "Regist Failed", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                // Mengirimkan parameter ke server
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("user_id", user_id);
                params.put("product_id", product_id);
                params.put("point", Integer.toString(point));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}