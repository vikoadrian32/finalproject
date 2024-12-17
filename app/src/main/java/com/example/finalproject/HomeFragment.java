package com.example.finalproject;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    private String url = "http://192.168.27.175/WMP/get_products.php";
    private String get_most_url = "http://192.168.27.175/WMP/get_most_recommend.php";
    String url_find_product = "http://192.168.27.175/WMP/get_product.php";
    String url_get_name= "http://192.168.27.175/WMP/get_product_name.php";
    private HashMap<String, Double> discountMap = new HashMap<>();
    private RecyclerView offerRecycler;
    private OfferAdapter offerAdapter;
    private Runnable runnable;
    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView noProductsTextView;
    private SearchView searchView;
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private ImageButton btnVoiceSearch;
    private List<String> productNames = new ArrayList<>();
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("ARG_PARAM1");
            mParam2 = getArguments().getString("ARG_PARAM2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        offerRecycler = view.findViewById(R.id.offer);
        offerRecycler.setHasFixedSize(true);
        offerRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        List<OfferItem> offerList = new ArrayList<>();
        offerList.add(new OfferItem(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.video1)));
        offerList.add(new OfferItem(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.video2)));
        offerList.add(new OfferItem(R.drawable.image2));
        offerList.add(new OfferItem(R.drawable.image3));
        offerList.add(new OfferItem(R.drawable.image4));
        offerList.add(new OfferItem(R.drawable.image5));
        offerAdapter = new OfferAdapter(offerList);
        offerRecycler.setAdapter(offerAdapter);

        startAutoScroll();

        recyclerView = view.findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        fetchDiscountProducts();

        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    fetchProductData();
                }
                return false;
            }
        });

        btnVoiceSearch = view.findViewById(R.id.voiceSearchButton);
        btnVoiceSearch.setOnClickListener(v->{
            startVoiceinput();
        });

        return view;
    }

    public int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private String normalize(String text) {
        return text.toLowerCase().trim().replaceAll("[^a-z0-9 ]", "");
    }

    private String findClosestMatch(String keyword, List<String> productNames) {
        if (productNames.isEmpty()) return keyword;
        String normalizedKeyword = normalize(keyword);
        for (String productName : productNames) {
            if (normalize(productName).contains(normalizedKeyword)) {
                return productName;
            }
        }
        int minDistance = Integer.MAX_VALUE;
        String closestMatch = productNames.get(0);
        int threshold = 10;

        for (String productName : productNames) {
            int distance = levenshteinDistance(normalizedKeyword, normalize(productName));
            Log.d("Levenshtein", "Keyword: " + normalizedKeyword + ", Product: " + normalize(productName) + ", Distance: " + distance);
            if (distance <= threshold && distance < minDistance) {
                minDistance = distance;
                closestMatch = productName;
            }
        }
        return closestMatch;
    }

    private void searchProducts(String query) {
        String url =  url_find_product +"?query=" + query;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            productList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject product = response.getJSONObject(i);
                                String productId = product.getString("id");
                                String productName = product.getString("product_name");
                                String productBrand = product.getString("product_brand");
                                String productType = product.getString("product_type");
                                String productPrice = product.getString("product_price");
                                String productImage = product.getString("product_image");

                                double originalPrice = Double.parseDouble(productPrice);
                                double discountedPrice = originalPrice;
                                double discount = 0;

                                if (discountMap.containsKey(productId)) {
                                    discount = discountMap.get(productId);
                                    discountedPrice = originalPrice * (1 - discount);
                                }
                                String discountPrice = String.format("%.2f", discountedPrice);

                                productList.add(new Product(productId, productName, productBrand, productType, productPrice, productImage, discountPrice, discount));
                            }
                            productAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed to parse data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error",error.toString());
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void getName(final String keyword){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject product = jsonArray.getJSONObject(i);
                                String productName = product.getString("product_name");
                                productNames.add(productName);
                            }
                            String correctedWord = findClosestMatch(keyword, productNames);
                            searchProducts(correctedWord);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Gagal mengambil data produk", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void startVoiceinput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Product Name...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String keyword = result.get(0);
            getName(keyword);;
        }
    }

    private void startAutoScroll() {
        runnable = new Runnable() {
            int currentPosition = 0;
            @Override
            public void run() {
                if (offerAdapter.getItemCount() > 0) {
                    currentPosition = (currentPosition + 1) % offerAdapter.getItemCount();
                    offerRecycler.smoothScrollToPosition(currentPosition);
                    handler.postDelayed(this, 5000);
                }
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private void fetchDiscountProducts() {
        StringRequest discountRequest = new StringRequest(Request.Method.GET, get_most_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String productId = obj.getString("product_id");
                                    String rank = obj.getString("rank");
                                    double discount = 0.0;
                                    switch (rank) {
                                        case "1":
                                            discount = 0.9;
                                            break;
                                        case "2":
                                            discount = 0.5;
                                            break;
                                        case "3":
                                            discount = 0.25;
                                            break;
                                        default:
                                            discount = 0.0;
                                    }

                                    discountMap.put(productId, discount);
                                }
                                fetchProductData();
                            } else {
                                Toast.makeText(getContext(), "Failed to fetch discount products", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing discount data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                        Toast.makeText(getContext(), "Failed to fetch discount products", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(discountRequest);
    }

    private void fetchProductData() {
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                productList.clear();
                                JSONArray jsonArray = jsonResponse.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject product = jsonArray.getJSONObject(i);
                                    String productId = product.getString("id");
                                    String productName = product.getString("product_name");
                                    String productBrand = product.getString("product_brand");
                                    String productType = product.getString("product_type");
                                    String productPrice = product.getString("product_price");
                                    String productImage = product.getString("product_image");

                                    double originalPrice = Double.parseDouble(productPrice);
                                    double discountedPrice = originalPrice;
                                    double discount = 0;

                                    if (discountMap.containsKey(productId)) {
                                        discount = discountMap.get(productId);
                                        discountedPrice = originalPrice * (1 - discount);
                                    }
                                    String discountPrice = String.format("%.2f", discountedPrice);
                                    productList.add(new Product(productId, productName, productBrand, productType, productPrice, productImage,discountPrice,discount));

                                }
                                productAdapter.notifyDataSetChanged();
                            } else {
                                String message = jsonResponse.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed to parse data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                        Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}
