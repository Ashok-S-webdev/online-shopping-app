package com.onlineshopping.api;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.onlineshopping.dao.UserDao;
import com.onlineshopping.model.User;
import com.onlineshopping.utils.Security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/register")
public class RegisterResource {
    private static final Logger logger = LoggerFactory.getLogger(RegisterResource.class);
    private static final String DEFAULT_IMAGE_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\online-shopping-app\\data\\profile-icon.png";

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "New user registration", description = "Registering and adding new user to database")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created and added to database"),
        @ApiResponse(responseCode = "409", description = "User with the same username exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response registerUser(
        @Parameter(description = "Username", required = true)
        @FormParam("username") String username,

        @Parameter(description = "Password", required = true)
        @FormParam("password") String password,

        @Parameter(description = "Email ID", required = true)
        @FormParam("email") String email,

        @Parameter(description = "Mobile Number", required = true)
        @FormParam("mobile") String mobile) throws SQLException {
            JsonObject jsonObject = new JsonObject();

            User user = UserDao.getUserByUsername(username);
            if (user != null) {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Username already exists!");
                return Response.status(Response.Status.CONFLICT).entity(jsonObject.toString()).build();
            }

            String passwordHash = Security.encryptPassword(password);

            InputStream fileContent;
            try {
                fileContent = getDefaultImage();
            } catch (FileNotFoundException e) {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Error accessing default profile image");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject.toString()).build();
            }

            boolean userCreated = UserDao.addUser(username, passwordHash, email, mobile, fileContent);
            if (userCreated) {
                jsonObject.addProperty("status", "success");
                jsonObject.addProperty("message", "User created successfully");
                logger.info("New User Registration successful");
                return Response.status(Response.Status.CREATED).entity(jsonObject.toString()).build();
            } else {
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Error creating account");
                logger.info("Error in creating New User account");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject.toString()).build();
            }
        }

        // For default image
    private InputStream getDefaultImage() throws FileNotFoundException {
        File defaultImage = new File(DEFAULT_IMAGE_PATH);
        return new FileInputStream(defaultImage);
    }
}
