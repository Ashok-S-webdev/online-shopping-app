package com.example.model;


public class Product {
    private int productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private String productImage;
    
    public String getProductImage() {
        return productImage;
    }
    
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    
    public Product(int productId, String productName, String productDescription, double productPrice, String productImage) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }
    
    public Product(String productName, String productDescription, double productPrice, String productImage) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }
    
    public int getProductId() {
        return productId;
    }
    
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public double getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
    
    @Override
    public String toString() {
        return "Product{" +
        "productId=" + productId +
        ", productName='" + productName + '\'' +
        ", productDescription='" + productDescription + '\'' +
        ", productPrice=" + productPrice +
        '}';
    }
}
