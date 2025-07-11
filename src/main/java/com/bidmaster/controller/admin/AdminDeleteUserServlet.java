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

import com.bidmaster.service.UserService;
import com.bidmaster.service.impl.UserServiceImpl;

/**
 * Servlet implementation class AdminDeleteUserServlet
 * Handles deleting users from the admin panel
 */
@WebServlet("/admin/AdminDeleteUserServlet")
public class AdminDeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminDeleteUserServlet.class.getName());
    
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
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            
            // Check if trying to delete self
            int adminId = (int) session.getAttribute("userId");
            if (userId == adminId) {
                request.setAttribute("errorMessage", "You cannot delete your own account");
                request.getRequestDispatcher("AdminUserServlet").forward(request, response);
                return;
            }
            
            boolean deleted = userService.deleteUser(userId);
            
            if (deleted) {
                LOGGER.log(Level.INFO, "User deleted by admin: ID {0}", userId);
                request.setAttribute("successMessage", "User deleted successfully");
            } else {
                LOGGER.log(Level.WARNING, "Failed to delete user: ID {0}", userId);
                request.setAttribute("errorMessage", "Failed to delete user");
            }
            
            request.getRequestDispatcher("AdminUserServlet").forward(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid user ID format", e);
            request.setAttribute("errorMessage", "Invalid user ID");
            request.getRequestDispatcher("AdminUserServlet").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error deleting user", e);
            request.setAttribute("errorMessage", "Error deleting user: " + e.getMessage());
            request.getRequestDispatcher("AdminUserServlet").forward(request, response);
        }
    }
}
