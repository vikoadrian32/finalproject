package com.example.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {
    private EditText etProductName, etBrandName, etProductType, etProductPrice;
    private ImageView ivProductImage;
    private Button btnUploadImage, btnSaveProduct;
    private Bitmap selectedImage;
    private String url = "http://192.168.1.59/WMP/insert_product.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etProductName = findViewById(R.id.et_product_name);
        etBrandName = findViewById(R.id.et_brand_name);
        etProductType = findViewById(R.id.et_product_type);
        etProductPrice = findViewById(R.id.et_product_price);
        ivProductImage = findViewById(R.id.iv_product_image);
        btnUploadImage = findViewById(R.id.btn_upload_image);
        btnSaveProduct = findViewById(R.id.btn_save_product);

        btnUploadImage.setOnClickListener(v -> selectImage());
        btnSaveProduct.setOnClickListener(v -> saveProduct());
    }
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivProductImage.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void saveProduct() {
        String productName = etProductName.getText().toString();
        String brandName = etBrandName.getText().toString();
        String productType = etProductType.getText().toString();
        String productPrice = etProductPrice.getText().toString();
        String productImage = encodeImage(selectedImage);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Menangani response sukses dari server
                        Log.d("Response", response);
                        etProductName.setText("");
                        etBrandName.setText("");
                        etProductType.setText("");
                        etProductPrice.setText("");
                        ivProductImage.setImageResource(R.drawable.default_image);
                        Toast.makeText(AddProduct.this, "Add Product Success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menangani error response dari server
                        Log.e("Error", error.getMessage());
                        Toast.makeText(AddProduct.this, "Add Product Failed", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected java.util.Map<String, String> getParams() {
                // Mengirimkan parameter ke server
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("product_name", productName);
                params.put("product_brand", brandName);
                params.put("product_price", productPrice);
                params.put("product_type", productType);
                params.put("product_image", productImage);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private String encodeImage(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}