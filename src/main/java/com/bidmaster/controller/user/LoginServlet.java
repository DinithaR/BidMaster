package com.bidmaster.controller.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.bidmaster.model.User;
import com.bidmaster.service.UserService;
import com.bidmaster.service.impl.UserServiceImpl;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    
    private UserService userService;
    
    public void init() {
        userService = new UserServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            // Redirect based on role
            String role = (String) session.getAttribute("role");
            if ("admin".equals(role)) {
                response.sendRedirect("admin/AdminDashboardServlet");
            } else if ("seller".equals(role)) {
                response.sendRedirect("SellerDashboardServlet");
            } else {
                response.sendRedirect("index.jsp");
            }
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            // Validate credentials
            boolean isValid = userService.validateUser(username, password);
            
            if (isValid) {
                // Get user details
                User user = userService.getUserByUsername(username);
                
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullName", user.getFullName());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("role", user.getRole());
                
                LOGGER.log(Level.INFO, "User logged in: {0}", username);
                
                // Redirect based on role
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect("admin/AdminDashboardServlet");
                } else if ("seller".equals(user.getRole())) {
                    response.sendRedirect("SellerDashboardServlet");
                } else {
                    response.sendRedirect("index.jsp");
                }
            } else {
                request.setAttribute("errorMessage", "Invalid username or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during login", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
