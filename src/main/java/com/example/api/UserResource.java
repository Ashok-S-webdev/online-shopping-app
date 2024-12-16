package com.example.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Repo.CartItemDao;
import com.example.Repo.ProductDao;
import com.example.model.CartItem;
import com.example.model.Product;
import com.example.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo(@Context HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("status", "success");
        jsonObject.addProperty("username", user.getUsername());

        return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@QueryParam("page") @DefaultValue("1") int page) {
        List<Product> products = ProductDao.getProductsForPage(page, PAGE_SIZE);

        int totalProductsCount = ProductDao.getTotalProductsCount();
        int totalPages = (int) Math.ceil(totalProductsCount / (double) PAGE_SIZE);

        Map<String, Object> result = new HashMap<>();
        result.put("products", products);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);

        return Response.status(Response.Status.OK).entity(new Gson().toJson(result)).build();
    }

    @POST
    @Path("/addToCart")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToCart(
        @FormParam("productId") int productId,
        @Context HttpServletRequest request
    ) {
        User user = (User) request.getSession().getAttribute("user");
        CartItem cartItem = CartItemDao.getCartItemByProductId(productId);
        if (cartItem == null) {
            boolean addedToCart = CartItemDao.addProductToCart(user.getUserId(), productId);
            JsonObject jsonObject = new JsonObject();
            if (addedToCart) {
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "Product added to cart successfully");
                logger.info("Product with Product Id: {} added to cart", productId);
                return Response.status(Response.Status.OK).entity(jsonObject.toString()).build();
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
