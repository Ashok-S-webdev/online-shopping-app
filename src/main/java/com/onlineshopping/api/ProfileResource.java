package com.onlineshopping.api;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onlineshopping.dao.UserDao;
import com.onlineshopping.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/profile")
public class ProfileResource {
    private static final Logger logger = LoggerFactory.getLogger(ProfileResource.class);

    // Method for getting user profile details
    @GET
    @Path("/getUserProfile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(@Context HttpServletRequest request) throws SQLException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not logged in").build();
        }

        String username = (String) session.getAttribute("username");
        User user = UserDao.getUserByUsername(username);

        return Response.ok().entity(new Gson().toJson(user)).build();
    }

     // Method for updating user profile details
    @POST
    @Path("/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(
        @Context HttpServletRequest request,
        @FormParam("userId") int userId,
        @FormParam("email") String email,
        @FormParam("mobile") String mobile,
        @FormDataParam("profile-picture") InputStream filePart
    ) throws IOException {
        JsonObject jsonObject = new JsonObject();

        boolean isImageAvailable = false;
        if (filePart != null) {
            isImageAvailable = true;
        }
        try {
            if (isImageAvailable) {
                UserDao.updateUserProfileWithImage(userId, email, mobile, filePart);
            } else {
                UserDao.updateUserProfile(userId, email, mobile);
            }
        } catch (Exception e) {
            logger.error("Error updating User Profile with User Id {}", userId, e);
            e.printStackTrace();
        }

        jsonObject.addProperty("status", "success");
        jsonObject.addProperty("message", "User profile updated");
        logger.info("User profile with User Id: {} updated", userId);

        return Response.ok().entity(jsonObject.toString()).build();
    }
}
