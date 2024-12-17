package com.onlineshopping.api;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/logout")
public class LogoutResource {
    private static final Logger logger = LoggerFactory.getLogger(LogoutResource.class);

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Logout user", description = "The user session is invalidated and the user is logged out")
    @ApiResponses({
        @ApiResponse(responseCode = "301", description = "Redirected to login page")
    })
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            logger.info("User successfully logged out");
        }

        return Response.seeOther(URI.create(request.getContextPath() + "/index.html")).build();
    }
}
