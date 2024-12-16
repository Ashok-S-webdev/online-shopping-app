package com.example.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Repo.ProductDao;
import com.example.Repo.UserDao;
import com.example.model.Product;
import com.example.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin")
public class AdminResource {
    private static final Logger logger = LoggerFactory.getLogger(AdminResource.class);
    private static final String DEFAULT_IMAGE_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\online-shopping-app\\data\\clock.jpg";

    @GET
    @Path("/getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@Context HttpServletRequest request) {
        User loggedInUser = (User) request.getSession().getAttribute("user");
        
        if (loggedInUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not logged in").build();
        }
        
        List<User> users = UserDao.getUsersList(loggedInUser.getUserId());
        return Response.ok(new Gson().toJson(users)).build();
    }

    @GET
    @Path("/getProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@Context HttpServletRequest request) {
        List<Product> products = ProductDao.getProducts();
        return Response.ok(new Gson().toJson(products)).build();
    }

    @GET
    @Path("/getProductDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductDetails(@QueryParam("productId") int productId) {
        Product product = ProductDao.getProductByProductId(productId);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Product not found").build();
        }
        return Response.ok(new Gson().toJson(product)).build();
    }

    @DELETE
    @Path("/removeProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeProduct(@QueryParam("productId") int productId) {
        ProductDao.removeProduct(productId);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "success");
        jsonObject.addProperty("message", "Product removed");
        logger.info("Product with Product Id: {} removed", productId);

        return Response.ok(jsonObject.toString()).build();
    }

    @PUT
    @Path("/updateProduct")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(
        @FormParam("productId") int productId,
        @FormParam("productDescription") String desc,
        @FormParam("productPrice") double price,
        @FormDataParam("productImage") InputStream filePart) throws IOException 
    {
        JsonObject jsonObject = new JsonObject();
        Product product = ProductDao.getProductByProductId(productId);

        if (product == null) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Product not found");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonObject.toString()).build();
        }

        boolean isImageAvailable = false;

        if (filePart != null) {
            isImageAvailable = true;
        }

        try {
            if (isImageAvailable) {
                ProductDao.updateProductWithImage(productId, desc, price, filePart);
            } else { 
                ProductDao.updateProductDetails(productId, desc, price);
            }
            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("message", "Product details updated successfully");
            logger.info("Product with Product Id: {} details updated.", productId);
            return Response.ok(jsonObject.toString()).build();
        } catch (Exception e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Error updating product: " + e.getMessage());
            logger.info("Error in updating Product with Product Id: {} {}", productId, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject.toString()).build();
        }
    }

    @POST
    @Path("/addProduct")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(
            @FormParam("productName") String name,
            @FormParam("productDescription") String desc,
            @FormParam("productPrice") double price,
            @FormDataParam("productImage") Part filePart) {

        JsonObject jsonObject = new JsonObject();
        Product existingProduct = ProductDao.getProductByProductName(name);

        if (existingProduct != null) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Product already exists");
            return Response.status(Response.Status.CONFLICT).entity(jsonObject.toString()).build();
        }

        InputStream fileContent = null;
        try {
            if (filePart != null && filePart.getSize() > 0) {
                fileContent = filePart.getInputStream();
            } else {
                fileContent = getDefaultImage();
            }

            ProductDao.addProductToDB(name, desc, price, fileContent);

            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("message", "Product added successfully");
            logger.info("New Product added with Product Name: {}", name);
            return Response.status(Response.Status.CREATED).entity(jsonObject.toString()).build();

        } catch (IOException e) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Error adding product: " + e.getMessage());
            logger.error("Error adding new Product", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject.toString()).build();
        }
    }
    
    private InputStream getDefaultImage() throws FileNotFoundException {
        File defaultImage = new File(DEFAULT_IMAGE_PATH);
        return new FileInputStream(defaultImage);
    }
}
