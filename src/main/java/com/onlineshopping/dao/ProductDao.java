package com.onlineshopping.dao;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.onlineshopping.model.Product;
import com.onlineshopping.utils.DBUtils;
import com.onlineshopping.utils.ImageUtils;

// Database Operations on Products
public class ProductDao {

        // Method to get all product details from database
    public static List<Product> getProducts() {
        final String query = "SELECT * FROM products";
        List<Product> productList = new ArrayList<>();

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String productDescription = rs.getString("product_description");
                double productPrice = rs.getDouble("product_price");
                Blob imageBlob = rs.getBlob("product_image");

                String image = ImageUtils.Base64Converter(imageBlob);

                Product product = new Product(productId, productName, productDescription, productPrice, image);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
    

        // Method to get product details for pagination from database
    public static List<Product> getProductsForPage(int page, int pageSize) {
        List<Product> productList = new ArrayList<>();
        final String query = "SELECT * FROM PRODUCTS LIMIT ?, ?";
        try(Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, (page - 1) * pageSize);
            statement.setInt(2, pageSize);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                String productDescription = resultSet.getString("product_description");
                double productPrice = resultSet.getDouble("product_price");
                Blob imageBlob = resultSet.getBlob("product_image");

                String image = ImageUtils.Base64Converter(imageBlob);

                Product product = new Product(productId, productName, productDescription, productPrice, image);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    // Method to get product by product id from database
    public static Product getProductByProductId(int id) {
        final String query = "SELECT * FROM products WHERE product_id = ?";
        Product product = null;

        try (Connection connection = DBUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                String productDescription = resultSet.getString("product_description");
                double productPrice = resultSet.getDouble("product_price");
                Blob image = resultSet.getBlob("product_image");

                String productImage = ImageUtils.Base64Converter(image);

                product = new Product(productId, productName, productDescription, productPrice, productImage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    // Method to get product details by product name from database
    public static Product getProductByProductName(String name) {
        final String query = "SELECT * FROM products WHERE product_name = ?";
        Product product = null;

        try (Connection connection = DBUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                String productDescription = resultSet.getString("product_description");
                double productPrice = resultSet.getDouble("product_price");
                Blob image = resultSet.getBlob("product_image");

                String productImage = ImageUtils.Base64Converter(image);

                product = new Product(productId, productName, productDescription, productPrice, productImage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    // Method to add product to database
    public static void addProductToDB(String name, String description, Double price, InputStream image) {
        final String query = "INSERT INTO products (product_name, product_image, product_description, product_price) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setBlob(2, image);
            statement.setString(3, description);
            statement.setDouble(4, price);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Product added successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update product details to database
    public static void updateProductDetails(int id, String description, Double price) {
        final String query = "UPDATE products SET product_description = ?, product_price = ? WHERE product_id = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, description);
            statement.setDouble(2, price);
            statement.setInt(3, id);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Product details updated");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update product details and product image to database
    public static void updateProductWithImage(int id, String description, Double price, InputStream image) {
        final String query = "UPDATE products SET product_description = ?, product_price = ?, product_image = ? WHERE product_id = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, description);
            statement.setDouble(2, price);
            statement.setBlob(3, image);
            statement.setInt(4, id);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Product details updated");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to remove product from database
    public static void removeProduct(int productId) {
        final String query = "DELETE FROM products WHERE product_id = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item removed from the cart successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get total products count for pagination purpose
    public static int getTotalProductsCount() {
        int count = 0;

        final String query = "SELECT COUNT(*) FROM products";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
