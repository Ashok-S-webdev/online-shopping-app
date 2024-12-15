package com.example.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/index.html", "/verify-otp.html", "/register.html", "/admin/*", "/user/*"})
public class RoleBasedPageFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setHeader("Expires", "0");


        HttpSession session = httpRequest.getSession(false);

        String uri = httpRequest.getRequestURI();
        if (session != null && session.getAttribute("role") != null) {
            String role = (String) session.getAttribute("role");

            if (uri.equals(httpRequest.getContextPath() + "/index.html") ||
            uri.equals(httpRequest.getContextPath() + "/") ||
            uri.equals(httpRequest.getContextPath() + "/register.html") ||
            uri.equals(httpRequest.getContextPath() + "/verify-otp.html")) {
                if ("admin".equals(role)) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/admin.html");
                } else if ("user".equals(role)) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/user/user.html");
                }
                return;
            }
            if (uri.contains("/admin") && !"admin".equals(role)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/user/user.html");
                System.out.println("Redirecting to user page because the user is not an admin.");
                return;
            }
    
            if (uri.contains("/user") && !"user".equals(role)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/admin.html");
                System.out.println("Redirecting to admin page because the user is not an user.");
                return;
            }
        } else if (session != null && session.getAttribute("role") == null) {
            if (!uri.equals(httpRequest.getContextPath() + "/verify-otp.html")) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/verify-otp.html");
            }
        }
         else {
            if (uri.contains("/admin") || uri.contains("/user") || 
            uri.equals(httpRequest.getContextPath() + "/verify-otp.html")) {
                System.out.println("redirecting to login");
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.html");
                return; 
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
