package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<Cart> cartList;
    private Context context;
    private double totalPrice = 0.0;
    public CartAdapter(List<Cart> cartList,Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_design, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }



    @Override
    public void onBindViewHolder(CartAdapter.CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.productName.setText(cart.getProduct_name());
        holder.productBrand.setText(cart.getProduct_brand());
        holder.productPrice.setText(cart.getProduct_price());


        Picasso.get().load("http://192.168.27.175/WMP/" + cart.getProduct_image()).into(holder.productImage);

        holder.productQuantity.setText(Integer.toString(cart.getQuantity()));

        holder.btnIncrease.setOnClickListener(v->{
            SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("userProfile", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("id", "0");
            String url = "http://192.168.8.153/WMP/add_to_cart.php";
            String productId = cart.getProduct_id(); // Ambil product_id dari objek Cart
            String price = cart.getProduct_price(); // Ambil harga produk
            int newQuantity = cart.getQuantity() + 1; // Menambah jumlah produk di keranjang


            // Kirimkan permintaan POST untuk memperbarui jumlah produk di keranjang
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(v.getContext(), "Jumlah produk berhasil ditambah", Toast.LENGTH_SHORT).show();
                    // Update jumlah produk di tampilan
//                    cart.setQuantity(newQuantity);
                    holder.productQuantity.setText(Integer.toString(newQuantity)); // Update tampilan jumlah
                    updateTotalPrice();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(v.getContext(), "Gagal menambah jumlah produk: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Mengirimkan parameter untuk update quantity
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", userId);
                    params.put("product_id", productId);
                    params.put("product_price", price);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
            requestQueue.add(stringRequest);
        });

        holder.btnDecrease.setOnClickListener(v->{
            String url = "http://192.168.8.153/WMP/remove_cart.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                if (cart.getQuantity() > 1) {
                                    cart.setQuantity(cart.getQuantity() - 1);  // Kurangi kuantitas produk
                                    holder.productQuantity.setText(String.valueOf(cart.getQuantity()));  // Update tampilan jumlah
                                    updateTotalPrice();
                                } else {
                                    cartList.remove(position);
                                    notifyItemRemoved(position);
                                    updateTotalPrice();
                                }
//                                updateTotalPrice();
                            } else {
                                Toast.makeText(v.getContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(v.getContext(), "Gagal menghapus produk: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_id", cart.getProduct_id());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
            requestQueue.add(stringRequest);

        });


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    private void updateTotalPrice() {
        totalPrice = 0.0;
        for (Cart cartItem : cartList) {
            double productPrice = Double.parseDouble(cartItem.getProduct_price());
            totalPrice += productPrice * cartItem.getQuantity();
        }
        // Update total price in Fragment or Activity
        TextView textTotal = ((Activity) context).findViewById(R.id.cartTotalPrice);  // Assuming it's in the activity
        textTotal.setText("$" + String.format("%.2f", totalPrice));
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productBrand, productPrice,productQuantity;
        ImageView productImage;
        ImageButton btnIncrease, btnDecrease;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.cartProductName);
            productBrand = itemView.findViewById(R.id.cartBrandName);
            productPrice = itemView.findViewById(R.id.cartPrice);
            productImage = itemView.findViewById(R.id.cartImage);
            productQuantity = itemView.findViewById(R.id.cartQuantity);
            btnIncrease = itemView.findViewById(R.id.increaseQuantityBtn);
            btnDecrease = itemView.findViewById(R.id.decreaseQuantityBtn);

        }
    }





}
