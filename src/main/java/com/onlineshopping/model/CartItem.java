package com.onlineshopping.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cart Item details")
public class CartItem {

    @Schema(description = "Unique identifier of cart item", example = "1")
    private int cartItemId;

    @Schema(description = "Identifier of User", example = "1")
    private int userId;

    @Schema(description = "Identifier of product", example = "1")
    private int productId;

    @Schema(description = "Cart Item quantity", example = "1")
    private int quantity;

    public CartItem(int cartItemId, int userId, int productId, int quantity) {
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

