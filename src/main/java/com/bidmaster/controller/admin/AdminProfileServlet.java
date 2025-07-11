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
 * Servlet implementation class AdminProfileServlet
 * Handles admin profile updates
 */
@WebServlet("/admin/AdminProfileServlet")
public class AdminProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminProfileServlet.class.getName());
    
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
        
        int userId = (int) session.getAttribute("userId");
        
        try {
            // Get form parameters
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // Get current user
            User user = userService.getUserById(userId);
            
            if (user == null) {
                request.setAttribute("errorMessage", "User not found");
                request.getRequestDispatcher("AdminDashboardServlet").forward(request, response);
                return;
            }
            
            // Validate input
            StringBuilder errorMessage = new StringBuilder();
            
            if (!ValidationUtil.isValidEmail(email)) {
                errorMessage.append("Invalid email format. ");
            }
            
            if (!ValidationUtil.isNotEmpty(fullName)) {
                errorMessage.append("Full name is required. ");
            }
            
            // Password validation
            if (newPassword != null && !newPassword.isEmpty()) {
                if (currentPassword == null || currentPassword.isEmpty()) {
                    errorMessage.append("Current password is required to set a new password. ");
                } else if (!user.getPassword().equals(currentPassword)) {
                    errorMessage.append("Current password is incorrect. ");
                } else if (!ValidationUtil.isValidPassword(newPassword)) {
                    errorMessage.append("New password must be at least 8 characters long. ");
                } else if (!newPassword.equals(confirmPassword)) {
                    errorMessage.append("New password and confirm password do not match. ");
                }
            }
            
            if (errorMessage.length() > 0) {
                request.setAttribute("errorMessage", errorMessage.toString());
                request.getRequestDispatcher("AdminDashboardServlet").forward(request, response);
                return;
            }
            
            // Check if email is being changed and if it already exists
            if (!user.getEmail().equals(email)) {
                User existingUser = userService.getUserByEmail(email);
                if (existingUser != null) {
                    request.setAttribute("errorMessage", "Email already exists. Please use another email address.");
                    request.getRequestDispatcher("AdminDashboardServlet").forward(request, response);
                    return;
                }
            }
            
            // Update user data
            user.setEmail(email);
            user.setFullName(fullName);
            
            // Update password if provided
            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(newPassword);
            }
            
            // Save updated user
            boolean updated = userService.updateUser(user);
            
            if (updated) {
                LOGGER.log(Level.INFO, "Admin profile updated: {0}", user.getUsername());
                session.setAttribute("fullName", user.getFullName());
                request.setAttribute("successMessage", "Profile updated successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to update profile");
            }
            
            request.getRequestDispatcher("AdminDashboardServlet").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error updating admin profile", e);
            request.setAttribute("errorMessage", "Error updating profile: " + e.getMessage());
            request.getRequestDispatcher("AdminDashboardServlet").forward(request, response);
        }
    }
}
