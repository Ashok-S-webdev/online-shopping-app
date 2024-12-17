package com.onlineshopping.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onlineshopping.dao.CartItemDao;
import com.onlineshopping.dao.ProductDao;
import com.onlineshopping.model.CartItem;
import com.onlineshopping.model.Product;
import com.onlineshopping.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UserResource {
    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    private static final int PAGE_SIZE = 4;

    // Method for getting User info
    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get username", description = "Get username of the user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Username retrieved")
    })
    public Response getUserInfo(@Context HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("status", "success");
        jsonObject.addProperty("username", user.getUsername());

        return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
    }

    // Method for getting available products
    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get products", description = "Fetch all the products in database with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products retrieved for the current page")
    })
    public Response getProducts(
        @Parameter(description = "Page Number", required = true)
        @QueryParam("page") @DefaultValue("1") int page) {
        List<Product> products = ProductDao.getProductsForPage(page, PAGE_SIZE);

        int totalProductsCount = ProductDao.getTotalProductsCount();
        int totalPages = (int) Math.ceil(totalProductsCount / (double) PAGE_SIZE);

        Map<String, Object> result = new HashMap<>();
        result.put("products", products);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);

        return Response.status(Response.Status.OK).entity(new Gson().toJson(result)).build();
    }

    // Method for adding product to cart
    @POST
    @Path("/addToCart")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add product to cart", description = "Add a product to the user cart")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "New cart item added to the user cart"),
        @ApiResponse(responseCode = "409", description = "Product already available in the cart"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response addToCart(
        @Parameter(description = "Product ID", required = true)
        @FormParam("productId") int productId,
        
        @Context HttpServletRequest request
    ) {
        User user = (User) request.getSession().getAttribute("user");
        CartItem cartItem = CartItemDao.getCartItemByProductIdAndUserId(user.getUserId(), productId);
        if (cartItem == null) {
            boolean addedToCart = CartItemDao.addProductToCart(user.getUserId(), productId);
            JsonObject jsonObject = new JsonObject();
            if (addedToCart) {
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "Product added to cart successfully");
                logger.info("Product with Product Id: {} added to cart", productId);
                return Response.status(Response.Status.CREATED).entity(jsonObject.toString()).build();
            } else {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Error adding product to cart");
                logger.info("Error adding Product with Product Id: {} to cart", productId);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject.toString()).build();
            }
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Product Already in cart");
            logger.warn("Cannot add a Product which is already in the cart");
            return Response.status(Response.Status.CONFLICT).entity(jsonObject.toString()).build();
        }
    }

}
