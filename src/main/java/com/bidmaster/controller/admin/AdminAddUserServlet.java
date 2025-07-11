package com.bidmaster.controller.admin;

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
import com.bidmaster.util.ValidationUtil;

/**
 * Servlet implementation class AdminAddUserServlet
 * Handles adding new users from the admin panel
 */
@WebServlet("/admin/AdminAddUserServlet")
public class AdminAddUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminAddUserServlet.class.getName());
    
    private UserService userService;
    
    public void init() {
        userService = new UserServiceImpl();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is an admin
        if (session == null || session.getAttribute("userId") == null || 
                !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Get form parameters
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String contactNo = request.getParameter("contactNo");
        String role = request.getParameter("role");
        
        // Validate input
        StringBuilder errorMessage = new StringBuilder();
        
        if (!ValidationUtil.isValidUsername(username)) {
            errorMessage.append("Invalid username format. ");
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            errorMessage.append("Invalid email format. ");
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            errorMessage.append("Password must be at least 8 characters long. ");
        }
        
        if (!ValidationUtil.isNotEmpty(fullName)) {
            errorMessage.append("Full name is required. ");
        }
        
        if (contactNo != null && !contactNo.isEmpty() && !ValidationUtil.isValidContactNo(contactNo)) {
            errorMessage.append("Invalid contact number format. ");
        }
        
        if (errorMessage.length() > 0) {
            request.setAttribute("errorMessage", errorMessage.toString());
            request.getRequestDispatcher("AdminUserServlet").forward(request, response);
            return;
        }
        
        try {
            // Check if username already exists
            User existingUser = userService.getUserByUsername(username);
            if (existingUser != null) {
                request.setAttribute("errorMessage", "Username already exists. Please choose another username.");
                request.getRequestDispatcher("AdminUserServlet").forward(request, response);
                return;
            }
            
            // Check if email already exists
            existingUser = userService.getUserByEmail(email);
            if (existingUser != null) {
                request.setAttribute("errorMessage", "Email already exists. Please use another email address.");
                request.getRequestDispatcher("AdminUserServlet").forward(request, response);
                return;
            }
            
            // Create user object
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setFullName(fullName);
            user.setContactNo(contactNo);
            user.setRole(role);
            
            // Register user
            userService.registerUser(user);
            
            LOGGER.log(Level.INFO, "User added by admin: {0}", username);
            
            // Redirect with success message
            request.setAttribute("successMessage", "User added successfully");
            request.getRequestDispatcher("AdminUserServlet").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error adding user", e);
            request.setAttribute("errorMessage", "Error adding user: " + e.getMessage());
            request.getRequestDispatcher("AdminUserServlet").forward(request, response);
        }
    }
}
