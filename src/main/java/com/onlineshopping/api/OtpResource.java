package com.onlineshopping.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.onlineshopping.model.User;

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
 * Resource class for OTP verification and Role assignment
 * 
 * Provides API class for verifying if the OTP entered is correct and assigns a role
 */
@Path("/otp")
public class OtpResource {
    private static final Logger logger = LoggerFactory.getLogger(OtpResource.class);

    /**
     * Verfies if the OTP entered by the user matches the OTP generated and assigns role
     * 
     * @param enteredOtp OTP entered by the user
     * @param request Http request containing the session information
     * @return A json {@link Response} object containing status of the action and a message
     */
    @POST
    @Path("/verify")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Verify OTP", description = "Checks if the entered otp is correct and assign role")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OTP is validated and user is logged in"),
        @ApiResponse(responseCode = "401", description = "Invalid OTP")
    })
    public Response verifyOtp(
        @Parameter(description = "OTP", required = true)    
        @FormParam("otp") String enteredOtp, 
        
        @Context HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Session Expired").build();
        }

        User user = (User) session.getAttribute("user");
        String otp = (String) session.getAttribute("otp");

        JsonObject jsonObject = new JsonObject();

        if (enteredOtp != null && enteredOtp.equals(otp)) {
            session.setAttribute("role", user.getRole());
            jsonObject.addProperty("status", "success");
            jsonObject.addProperty("role", user.getRole());
            logger.info("User successfully logged in");
            return Response.ok().entity(jsonObject.toString()).build();
        }

        jsonObject.addProperty("status", "error");
        jsonObject.addProperty("message", "Invalid otp");
        logger.warn("Invalid OTP");

        return Response.status(Response.Status.UNAUTHORIZED).entity(jsonObject.toString()).build();
    }
}
