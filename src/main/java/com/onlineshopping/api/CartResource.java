package com.onlineshopping.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onlineshopping.dao.CartItemDao;
import com.onlineshopping.dao.ProductDao;
import com.onlineshopping.model.CartItem;
import com.onlineshopping.model.Product;
import com.onlineshopping.model.User;
import com.onlineshopping.utils.GmailSender;
import com.onlineshopping.utils.MailSender;
import com.onlineshopping.utils.PDFGenerator;
import com.onlineshopping.utils.ZohoMailSender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cart")
public class CartResource {
    private static final Logger logger = LoggerFactory.getLogger(CartResource.class);
    
    // Method for getting all the cart item details
    @GET
    @Path("/cartItems")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartItems(@Context HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        // JsonArray cartArray = new JsonArray();

        // List<CartItem> cartItems = CartItemDao.getCartItems(user.getUserId());

        // for (CartItem cartItem: cartItems) {
        //     Product product = ProductDao.getProductByProductId(cartItem.getProductId());
        //     JsonObject jsonObject = new JsonObject();
        //     jsonObject.addProperty("cartItemId", cartItem.getCartItemId());
        //     jsonObject.addProperty("productId", product.getProductId());
        //     jsonObject.addProperty("name", product.getProductName());
        //     jsonObject.addProperty("description", product.getProductDescription());
        //     jsonObject.addProperty("price", product.getProductPrice());
        //     jsonObject.addProperty("quantity", cartItem.getQuantity());
        //     jsonObject.addProperty("productImage", product.getProductImage());
        //     cartArray.add(jsonObject);
        // }

        JsonArray cartArray = CartItemDao.getCartItemsJson(user.getUserId());
        return Response.ok().entity(cartArray.toString()).build();
    }

    // Method for updating quantity of the cart item
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateQuantity(
        @FormParam("cartItemId") int cartItemId,
        @FormParam("quantityChange") int quantityChange
    ) {
        JsonObject jsonObject = new JsonObject();
        CartItemDao.updateItemQuantity(cartItemId, quantityChange);
        jsonObject.addProperty("status", "success");
        jsonObject.addProperty("message", "Item Quantity Updated");
        logger.info("Cart Item with Cart Item Id: {} details updated", cartItemId);
        return Response.ok().entity(jsonObject.toString()).build();
    }

    // Method for removing cart item from the cart
    @DELETE
    @Path("/remove")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeItem(
        @FormParam("cartItemId") int cartItemId
    ) {
        CartItemDao.removeItemFromCart(cartItemId);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "success");
        jsonObject.addProperty("message", "Item removed from cart");
        logger.info("Cart Item with Cart Item Id: {} removed from the cart", cartItemId);
        return Response.ok().entity(jsonObject.toString()).build();
    }

    // Method for checkout
    @POST
    @Path("/checkout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkout(@Context HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<CartItem> cartItems = CartItemDao.getCartItems(user.getUserId());
        JsonObject jsonObject = new JsonObject();

        if (cartItems.isEmpty()) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "No products in the cart!");
            logger.warn("Empty cart cannot be checked out");
            return Response.ok(jsonObject.toString()).build();
        } 
        double total = calculateTotalAmount(cartItems);

        byte[] pdf = PDFGenerator.generatePDF(cartItems, total);
        MailSender gmailSender = new GmailSender();
        MailSender zohoMailSender = new ZohoMailSender();
        new Thread(() -> {
            try{
                gmailSender.sendMail(user.getEmail(), pdf);
                zohoMailSender.sendMail(user.getEmail(), pdf);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Error sending Bill as mail", e);
            }
        } ).start();

        CartItemDao.clearCart(user.getUserId());
        jsonObject.addProperty("status", "success");
        jsonObject.addProperty("message", "Checkout Successful. Your Bill is sent to your mail!");
        logger.info("Cart Items checked out");
        return Response.ok().entity(jsonObject.toString()).build();
    }

     // Method for calculating the subtotal amount for the cart
    private static double calculateTotalAmount(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            Product product = ProductDao.getProductByProductId(item.getProductId());
            total += (product.getProductPrice() * item.getQuantity());
        }
        return total;
    }
}
