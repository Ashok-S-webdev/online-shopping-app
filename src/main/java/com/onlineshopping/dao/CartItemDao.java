package com.onlineshopping.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onlineshopping.model.CartItem;
import com.onlineshopping.utils.DBUtils;
import com.onlineshopping.utils.ImageUtils;

// Database Operations on Cart Items
public class CartItemDao {

    // Method to retrieve all cart item details from database
    public static List<CartItem> getCartItems(int loggedInUserId) {
        final String query = "SELECT * FROM cartitems WHERE user_id = ?";
        List<CartItem> cartItemsList = new ArrayList<>();

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int cartItemId = rs.getInt("cart_item_id");
                int userId = rs.getInt("user_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");

                CartItem cartItem = new CartItem(cartItemId, userId, productId, quantity);
                cartItemsList.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItemsList;
    }

    // Method for getting cart items along with product details
    public static JsonArray getCartItemsJson(int userId) {
        final String query = "SELECT * FROM cartitems c INNER JOIN products p ON c.product_id = p.product_id WHERE user_id = ?";
        JsonArray jsonArray = new JsonArray();

        try (Connection connection = DBUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                
                jsonObject.addProperty("cartItemId", resultSet.getInt("cart_item_id"));
                jsonObject.addProperty("productId", resultSet.getInt("product_id"));
                jsonObject.addProperty("name", resultSet.getString("product_name"));
                jsonObject.addProperty("description", resultSet.getString("product_description"));
                jsonObject.addProperty("price", resultSet.getDouble("product_price"));
                jsonObject.addProperty("quantity", resultSet.getInt("quantity"));
                Blob productBlob = resultSet.getBlob("product_image");
                jsonObject.addProperty("productImage", ImageUtils.Base64Converter(productBlob));
                jsonArray.add(jsonObject);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    // Method to retrieve cart item details by cart item id from database
    public static CartItem getCartItemByCartItemId(int id) {
        final String query = "SELECT * FROM cartitems WHERE cart_item_id = ?";
        CartItem cartItem = null;

        try (Connection connection = DBUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int cartItemId = resultSet.getInt("cart_item_id");
                int userId = resultSet.getInt("user_id");
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                cartItem = new CartItem(cartItemId, userId, productId, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItem;
    }

    // Method to retrieve cart item details by product id from database
    public static CartItem getCartItemByProductId(int id) {
        final String query = "SELECT * FROM cartitems WHERE product_id = ?";
        CartItem cartItem = null;

        try (Connection connection = DBUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int cartItemId = resultSet.getInt("cart_item_id");
                int userId = resultSet.getInt("user_id");
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                cartItem = new CartItem(cartItemId, userId, productId, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItem;
    }

    // Method to retrieve cart items of specific user
    public static CartItem getCartItemByProductIdAndUserId(int uId, int pId) {
        final String query = "SELECT * FROM cartitems WHERE user_id = ? AND product_id = ?";
        CartItem cartItem = null;

        try (Connection connection = DBUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, uId);
            statement.setInt(2, pId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int cartItemId = resultSet.getInt("cart_item_id");
                int userId = resultSet.getInt("user_id");
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");

                cartItem = new CartItem(cartItemId, userId, productId, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItem;
    }

    // Method to add product details to database
    public static boolean addProductToCart(int userId, int productId) {
        final String query = "INSERT INTO cartitems (user_id, product_id, quantity) VALUES (?, ?, 1)";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item added to cart successfully");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to remove cart item from database
    public static void removeItemFromCart(int cartItemId) {
        final String query = "DELETE FROM cartitems WHERE cart_item_id = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cartItemId);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item removed from the cart successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update quantity of cart item in database
    public static void updateItemQuantity(int cartItemId, int i) {
        CartItem cartItem = getCartItemByCartItemId(cartItemId);
        int newQuantity = cartItem.getQuantity() + i;
        if (newQuantity == 0) {
            removeItemFromCart(cartItemId);
        }
        final String query = "UPDATE cartitems SET quantity = ? WHERE cart_item_id = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newQuantity);
            statement.setInt(2, cartItemId);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item quantity updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to clear all cart items in database for a specific user 
    public static void clearCart(int userId) {
        final String query = "DELETE FROM cartitems WHERE user_id = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Cart Checked out");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
