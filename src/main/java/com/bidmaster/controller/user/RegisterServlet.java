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

import com.bidmaster.model.User;
import com.bidmaster.service.UserService;
import com.bidmaster.service.impl.UserServiceImpl;
import com.bidmaster.util.ValidationUtil;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    
    private UserService userService;
    
    public void init() {
        userService = new UserServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get form parameters
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String contactNo = request.getParameter("contactNo");
        String role = request.getParameter("role");
        
        // Set default role if not specified or invalid
        if (role == null || (!role.equals("user") && !role.equals("seller"))) {
            role = "user";
        }
        
        // Validate input
        boolean isValid = true;
        
        if (!ValidationUtil.isValidUsername(username)) {
            request.setAttribute("usernameError", "Username must be 3-20 characters long and contain only letters, numbers, and underscores");
            isValid = false;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("emailError", "Please enter a valid email address");
            isValid = false;
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            request.setAttribute("passwordError", "Password must be at least 8 characters long");
            isValid = false;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("confirmPasswordError", "Passwords do not match");
            isValid = false;
        }
        
        if (!ValidationUtil.isNotEmpty(fullName)) {
            request.setAttribute("fullNameError", "Full name is required");
            isValid = false;
        }
        
        if (contactNo != null && !contactNo.isEmpty() && !ValidationUtil.isValidContactNo(contactNo)) {
            request.setAttribute("contactNoError", "Please enter a valid contact number");
            isValid = false;
        }
        
        if (!isValid) {
            // Return to form with error messages
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.setAttribute("contactNo", contactNo);
            request.setAttribute("role", role);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        try {
            // Check if username already exists
            if (userService.getUserByUsername(username) != null) {
                request.setAttribute("usernameError", "Username already exists");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.setAttribute("contactNo", contactNo);
                request.setAttribute("role", role);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            
            // Check if email already exists
            if (userService.getUserByEmail(email) != null) {
                request.setAttribute("emailError", "Email already exists");
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.setAttribute("fullName", fullName);
                request.setAttribute("contactNo", contactNo);
                request.setAttribute("role", role);
                request.getRequestDispatcher("register.jsp").forward(request, response);
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
            
            LOGGER.log(Level.INFO, "User registered: {0}", username);
            
            // Redirect to login page with success message
            request.setAttribute("successMessage", "Registration successful! Please login.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during registration", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
