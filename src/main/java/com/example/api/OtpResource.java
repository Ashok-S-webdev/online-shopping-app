package com.example.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.User;
import com.google.gson.JsonObject;

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

@Path("/otp")
public class OtpResource {
    private static final Logger logger = LoggerFactory.getLogger(OtpResource.class);

    @POST
    @Path("/verify")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyOtp(@FormParam("otp") String enteredOtp, @Context HttpServletRequest request) {
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
        }else {
            jsonObject.addProperty("status", "error");
            jsonObject.addProperty("message", "Invalid otp");
            logger.warn("Invalid OTP");
        }

        return Response.ok().entity(jsonObject.toString()).build();
    }
}
