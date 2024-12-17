package com.onlineshopping.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onlineshopping.dao.ProductDao;
import com.onlineshopping.dao.UserDao;
import com.onlineshopping.model.Product;
import com.onlineshopping.model.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    // Method for getting all the user details
    @GET
    @Path("/getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all users", description = "Fetch a list of all registered users")
    @ApiResponses(
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    )
    public Response getUsers(@Context HttpServletRequest request) {
        User loggedInUser = (User) request.getSession().getAttribute("user");
        
        if (loggedInUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not logged in").build();
        }
        
        List<User> users = UserDao.getUsersList(loggedInUser.getUserId());
        return Response.ok(new Gson().toJson(users)).build();
    }

    // Method for getting all the product details
    @GET
    @Path("/getProducts")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all products", description = "Fetch a list of all the product details")
    @ApiResponses(
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    )
    public Response getProducts(@Context HttpServletRequest request) {
        List<Product> products = ProductDao.getProducts();
        return Response.ok(new Gson().toJson(products)).build();
    }

    // Method for getting a single product detail with productId
    @GET
    @Path("/getProductDetails")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get specific product details", description = "Fetch a specific product details using a productId")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product with the productId not found")
    })
    public Response getProductDetails(
        @QueryParam("productId")
        @Parameter(description = "product ID to retrieve product details", required = true) int productId
    ) {
        Product product = ProductDao.getProductByProductId(productId);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Product not found").build();
        }
        return Response.ok(new Gson().toJson(product)).build();
    }

    // Method for removing product from database
    @DELETE
    @Path("/removeProduct")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Remove product", description = "Remove product from the database")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product removed from the system")
    })
    public Response removeProduct(
        @QueryParam("productId")
        @Parameter(description = "product ID to retrieve product details", required = true) int productId
    ) {
        ProductDao.removeProduct(productId);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "success");
        jsonObject.addProperty("message", "Product removed");
        logger.info("Product with Product Id: {} removed", productId);

        return Response.ok(jsonObject.toString()).build();
    }

    // Method for updating product details in database
    @PUT
    @Path("/updateProduct")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update product", description = "Update details of the product in the database")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product details updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product with the product id not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response updateProduct(
        @Parameter(description = "product ID", required = true)
        @FormParam("productId") int productId,

        @Parameter(description = "Product Description")
        @FormParam("productDescription") String desc,

        @Parameter(description = "Product Price")
        @FormParam("productPrice") double price,

        @Parameter(description = "Product Image")
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

        // For checking if image is uploaded
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

    // Method to add new product
    @POST
    @Path("/addProduct")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Add a new product", description = "Add a new product to the database")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "New product created and added to the database"),
        @ApiResponse(responseCode = "409", description = "Product with the same name exists"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response addProduct(
            @Parameter(description = "Product Name", required = true)
            @FormParam("productName") String name,
            
            @Parameter(description = "Product Description", required = true)
            @FormParam("productDescription") String desc,
            
            @Parameter(description = "Product Price", required = true)
            @FormParam("productPrice") double price,

            @Parameter(description = "Product Image")
            @FormDataParam("productImage") Part filePart) {

        JsonObject jsonObject = new JsonObject();
        Product existingProduct = ProductDao.getProductByProductName(name);

        if (existingProduct != null) {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Product already exists");
            return Response.status(Response.Status.CONFLICT).entity(jsonObject.toString()).build();
        }

         // Chekcing if image is uploaded
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
    
    // Method for getting default image from default path
    private InputStream getDefaultImage() throws FileNotFoundException {
        File defaultImage = new File(DEFAULT_IMAGE_PATH);
        return new FileInputStream(defaultImage);
    }
}
