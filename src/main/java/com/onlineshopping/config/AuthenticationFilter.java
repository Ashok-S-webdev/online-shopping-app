package com.onlineshopping.config;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.google.gson.JsonObject;

@WebFilter(urlPatterns = {"/api/admin/*", "/api/user/*", "/api/profile/*", "/api/cart/*"}) 
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false); 
        String requestURI = httpRequest.getRequestURI();

        httpResponse.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "error");
        jsonObject.addProperty("message", "Unauthorized, Cannot access this page");

        if (session != null && session.getAttribute("role") != null) {
            String role = (String) session.getAttribute("role");

            if (requestURI.contains("/api/admin") && !"admin".equals(role)) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write(jsonObject.toString());                
                return;
            }

            if ((requestURI.contains("/api/user") ||
            requestURI.contains("/api/profile") ||
            requestURI.contains("/api/cart")) && !"user".equals(role)) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write(jsonObject.toString());
                return;
            }
        } else {
            if (requestURI.contains("/api/admin") ||
            requestURI.contains("/api/user") ||
            requestURI.contains("/api/profile") ||
            requestURI.contains("/api/cart")) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write(jsonObject.toString());
                return;
            }
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
