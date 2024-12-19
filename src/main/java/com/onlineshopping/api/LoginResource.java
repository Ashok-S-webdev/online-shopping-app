package com.onlineshopping.api;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.onlineshopping.dao.UserDao;
import com.onlineshopping.model.User;
import com.onlineshopping.utils.OTPSender;
import com.onlineshopping.utils.Security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Resource class for User login
 * 
 * Provides API class for user login with session creation and otp generation
 */
@Path("/login")
public class LoginResource {
    private static final Logger logger = LoggerFactory.getLogger(LoginResource.class);
    
    /**
     * Creates a session without role and generates otp for further processing
     * @param username Username of the user account
     * @param password Password of the user account
     * @param request Http request to create session if user is authenticated
     * @return A json {@link Response} object containing status of the action and a message
     * @throws IOException
     * @throws SQLException
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Login user", description = "Checks for user credentials and creates a session without role")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User authenticated and OTP is sent"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response login(
        @Parameter(description = "Username", required = true)
        @FormParam("username") String username, 
        
        @Parameter(description = "Password", required = true)
        @FormParam("password") String password,
        
        @Context HttpServletRequest request
    ) throws IOException, SQLException{
        User user = authenticateUser(username, password);
        JsonObject jsonObject = new JsonObject();
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
    
            String otp = OTPSender.generateOTP();
            logger.info("OTP generated successfully");
            session.setAttribute("otp", otp);
            System.out.println("OTP: " + otp);
            try {
                OTPSender.sendOTPToUser(user.getMobile(), otp);
            } catch (Exception e) {
                e.printStackTrace();
                jsonObject.addProperty("status", "error");
                jsonObject.addProperty("message", "Failed to send OTP");
                logger.error("Error sending otp to user", e.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject.toString()).build();
            }
            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("message", "OTP sent. Please verify");
    
            return Response.ok().entity(jsonObject.toString()).build();
        } else {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Invalid username or password");
            logger.warn("Invalid Username or password");
            return Response.status(Response.Status.UNAUTHORIZED).entity(jsonObject.toString()).build();
        }
    }

    private User authenticateUser(String username, String password) throws IOException, SQLException{
        User user = UserDao.getUserByUsername(username);

        if (user != null && Security.checkPassword(password, user.getPasswordHash())) {
            return user;
        }

        return null;
    }
}
