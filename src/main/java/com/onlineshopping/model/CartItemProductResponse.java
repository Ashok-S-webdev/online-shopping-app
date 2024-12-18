package com.onlineshopping.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cart Item details")
public class CartItemProductResponse {

    @Schema(description = "CartItem ID", example = "1")
    private int cartItemId;

    @Schema(description = "Product ID", example = "1")
    private int productId;

    @Schema(description = "Product Name", example = "Product 1")
    private String productName;

    @Schema(description = "Description for the product", example = "Description of product 1")
    private String productDescription;

    @Schema(description = "Product Price", example = "250.00")
    private double productPrice;

    @Schema(description = "Base64 string of product image")
    private String productImage;

    @Schema(description = "Quantity in Cart", example = "1")
    private int quantity;

    // Getters and Setters
    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
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

    public String getProductImage() {
        return productImage;
    }
    
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

