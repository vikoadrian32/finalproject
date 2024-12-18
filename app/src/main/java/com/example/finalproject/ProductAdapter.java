package com.example.finalproject;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productBrand.setText(product.getProductBrand());
        Picasso.get().load("http://192.168.1.59/WMP/" + product.getProductImage()).into(holder.productImage);
        if (product.getDiscountPersen() > 0 &&
                Double.parseDouble(product.getDiscountPrice().replace(",", ".")) < Double.parseDouble(product.getProductPrice())) {

            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.productPrice.setText("Price: $" + product.getProductPrice());
            holder.productDiscountPrice.setVisibility(View.VISIBLE);
            holder.productDiscountPrice.setText("Price: $" + product.getDiscountPrice());

            int discount = (int) (product.getDiscountPersen() * 100);
            holder.discountTag.setVisibility(View.VISIBLE);
            holder.discountTag.setText("-" + discount + "%");

        } else {
            holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)); // Menghapus line-through
            holder.productPrice.setVisibility(View.VISIBLE);
            holder.productDiscountPrice.setVisibility(View.GONE);
            holder.productPrice.setText("Price: $" + product.getProductPrice());
            holder.discountTag.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(v ->{
            openProductDetail(v,product);
        });
        holder.addToCart.setOnClickListener(v ->{
            openProductDetail(v,product);
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productBrand, productPrice,productDiscountPrice,discountTag;
        ImageView productImage;
        Button addToCart;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productBrand = itemView.findViewById(R.id.brandName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productDiscountPrice = itemView.findViewById(R.id.productDiscountPrice);
            discountTag = itemView.findViewById(R.id.discountTag);
            addToCart = itemView.findViewById(R.id.addCart);
        }
    }

    private  void openProductDetail(View view, Product product){
        Intent intent = new Intent(view.getContext(),DetailProduct.class);
        intent.putExtra("productId",product.getProductId());
        intent.putExtra("productName",product.getProductName());
        intent.putExtra("productBrand",product.getProductBrand());
        intent.putExtra("productPrice",product.getProductPrice());
        intent.putExtra("productDiscount",product.getDiscountPrice());
        intent.putExtra("productImage",product.getProductImage());
        intent.putExtra("discountPersen",product.getDiscountPersen());
        view.getContext().startActivity(intent);
    }
}
