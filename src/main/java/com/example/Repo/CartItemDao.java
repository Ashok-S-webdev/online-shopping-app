package com.example.Repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.CartItem;
import com.example.utils.DBUtils;

public class CartItemDao {
    public static List<CartItem> getCartItems(int loggedInUserId) {
        String query = "SELECT * FROM cartitems WHERE user_id = ?";
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

    public static CartItem getCartItemByCartItemId(int id) {
        String query = "SELECT * FROM cartitems WHERE cart_item_id = ?";
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

    public static CartItem getCartItemByProductId(int id) {
        String query = "SELECT * FROM cartitems WHERE product_id = ?";
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



    public static boolean addProductToCart(int userId, int productId) {
        String query = "INSERT INTO cartitems (user_id, product_id, quantity) VALUES (?, ?, 1)";

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

    public static void removeItemFromCart(int cartItemId) {
        String query = "DELETE FROM cartitems WHERE cart_item_id = ?";

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

    public static void updateItemQuantity(int cartItemId, int i) {
        CartItem cartItem = getCartItemByCartItemId(cartItemId);
        int newQuantity = cartItem.getQuantity() + i;
        if (newQuantity == 0) {
            removeItemFromCart(cartItemId);
        }
        String query = "UPDATE cartitems SET quantity = ? WHERE cart_item_id = ?";

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

    public static void clearCart(int userId) {
        String query = "DELETE FROM cartitems WHERE user_id = ?";

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
