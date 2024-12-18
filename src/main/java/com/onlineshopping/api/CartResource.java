package com.onlineshopping.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onlineshopping.dao.CartItemDao;
import com.onlineshopping.dao.ProductDao;
import com.onlineshopping.model.CartItem;
import com.onlineshopping.model.CartItemProductResponse;
import com.onlineshopping.model.ErrorResponse;
import com.onlineshopping.model.Product;
import com.onlineshopping.model.SuccessResponse;
import com.onlineshopping.model.User;
import com.onlineshopping.utils.GmailSender;
import com.onlineshopping.utils.MailSender;
import com.onlineshopping.utils.PDFGenerator;
import com.onlineshopping.utils.ZohoMailSender;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all cart items", description = "Fetch all the cart items of the user", tags = {"Cart", "User"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Retrieved all the cart items from database",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CartItemProductResponse.class)
        )),
        @ApiResponse(responseCode = "401", description = "Cannot access this api",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        ))
    })
    public Response getCartItems(@Context HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        JsonArray cartArray = CartItemDao.getCartItemsJson(user.getUserId());
        return Response.ok().entity(cartArray.toString()).build();
    }

    // Method for updating quantity of the cart item
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update cart item", description = "Update the quantity of the cart item in user cart", tags = {"Cart", "User"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart item quantity updated",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SuccessResponse.class)
        )),
        @ApiResponse(responseCode = "401", description = "Cannot access this api",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        ))
    })
    public Response updateQuantity(
        @Parameter(description = "Cart Item ID", required = true)
        @FormParam("cartItemId") int cartItemId,

        @Parameter(description = "Quantity", required = true)
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
    @Operation(summary = "Remove cart item", description = "Remove cart item from the user cart", tags = {"Cart", "User"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart item removed from the user cart",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SuccessResponse.class)
        )),
        @ApiResponse(responseCode = "401", description = "Cannot access this api",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        ))
    })
    public Response removeItem(
        @Parameter(description = "Cart Item ID", required = true)
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
    @Operation(summary = "Checkout cart", description = "Checkout the user cart", tags = {"Cart", "User"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart checked out and bill sent to user",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SuccessResponse.class)
        )),
        @ApiResponse(responseCode = "404", description = "No products in the cart",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )),
        @ApiResponse(responseCode = "401", description = "Cannot access this api",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        ))
    })
    public Response checkout(@Context HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<CartItem> cartItems = CartItemDao.getCartItems(user.getUserId());
        JsonObject jsonObject = new JsonObject();

        if (cartItems.isEmpty()) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "No products in the cart!");
            logger.warn("Empty cart cannot be checked out");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonObject).build();
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
