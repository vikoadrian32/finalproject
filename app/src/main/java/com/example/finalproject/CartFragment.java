package com.example.finalproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String url = "http://192.168.1.59/WMP/get_cart.php";
    private ArrayList<Cart> cartList;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private String mParam1;
    private String mParam2;
    private double totalPrice = 0.0;
    private TextView textTotal;
    private Button checkout;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartList,getContext());
        recyclerView.setAdapter(cartAdapter);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userProfile",MODE_PRIVATE);
        String userId = sharedPreferences.getString("id","0");
        textTotal = view.findViewById(R.id.cartTotalPrice);
        fetchCart(userId);

        checkout = view.findViewById(R.id.checkoutButton);
        checkout.setOnClickListener(v->{
            List<String> productIds = new ArrayList<>();
            for (Cart cartItem : cartList) {
                productIds.add(cartItem.getProduct_id());
            }
            // Kirim data product_id ke activity checkout
            Intent intent = new Intent(getContext(), OvoPaymentActivity.class);
            intent.putStringArrayListExtra("product_ids", new ArrayList<>(productIds));
            startActivity(intent);
        });

        return view;
    }

    public void fetchCart(String userId) {
        String requestUrl = url + "?user_id=" + userId;
        StringRequest request = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CartResponse", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                cartList.clear();
                                totalPrice = 0.0;
                                JSONArray data = jsonResponse.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject product = data.getJSONObject(i);
                                    String product_id = product.getString("product_id");
                                    String name = product.getString("product_name");
                                    String brand = product.getString("product_brand");
                                    String price = product.getString("product_price");
                                    String product_image = product.getString("product_image");
                                    int quantity = product.getInt("quantity");
                                    Log.d("Cart", "Name: " + name + ", Brand: " + brand + ", Price: " + price + ", Quantity: " + quantity);
                                    double productPrice = Double.parseDouble(price);
                                    double totalProductPrice = productPrice * quantity;
                                    totalPrice += totalProductPrice;
                                    cartList.add(new Cart(product_id,name, brand, price, quantity, product_image));
                                }
                                cartAdapter.notifyDataSetChanged();
                                textTotal.setText("$" + String.format("%.2f", totalPrice));
                                Log.d("CartList", "Cart contains: " + cartList.size() + " products");
                                saveTotalPriceToPreferences(totalPrice);
                            } else {
                                Log.e("CartError", "Failed to load cart: " + jsonResponse.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("CartError", "JSON Parsing Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CartError", "Volley Error: " + error.getMessage());
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Create a request queue and add the request
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    private void saveTotalPriceToPreferences(double totalPrice) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("cartPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("total_price", (float) totalPrice);
        editor.apply();
        Log.d("Cart", "Total price saved to SharedPreferences: " + totalPrice);
    }

}