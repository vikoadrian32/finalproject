package com.example.finalproject;

public class Cart {
    private String product_id;
    private String product_name;
    private String product_brand;
    private String product_price;
    private int quantity;
    private String product_image;

    public Cart(String product_id,String product_name, String product_brand, String product_price, int quantity,String product_image) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_brand = product_brand;
        this.product_price = product_price;
        this.quantity = quantity;
        this.product_image = product_image;
    }

    public String getProduct_id(){
        return product_id;
    }


    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public String getProduct_price() {
        return product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct_image(){
        return  product_image;
    }
}
