package com.example.finalproject;

public class Product {
    private String productId;
    private String productName;
    private String productBrand;
    private String productType;
    private String productPrice;
    private String productImage;
    private String discountPrice;
    private double discountPersen;

    public Product(String productId,String productName, String productBrand,String productType ,String productPrice, String productImage,String discountPrice,double discountPersen) {
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.productType = productType;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.discountPrice = discountPrice;
        this.discountPersen = discountPersen;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductImage() {
        return productImage;
    }
    public String getProductType(){
        return productType;
    }
    public String getProductId(){
        return productId;
    }

    public String getDiscountPrice(){
        return  discountPrice;
    }
    public double getDiscountPersen(){
        return discountPersen;
    }

    
}
