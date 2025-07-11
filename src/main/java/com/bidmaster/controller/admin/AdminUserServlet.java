package com.bidmaster.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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

/**
 * Servlet implementation class AdminUserServlet
 * Handles user management in the admin panel
 */
@WebServlet("/admin/AdminUserServlet")
public class AdminUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminUserServlet.class.getName());
    
    private UserService userService;
    
    public void init() {
        userService = new UserServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is an admin
        if (session == null || session.getAttribute("userId") == null || 
                !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            // Check if search is requested
            String searchTerm = request.getParameter("searchTerm");
            List<User> userList;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Search for users
                userList = userService.searchUsers(searchTerm);
                request.setAttribute("searchTerm", searchTerm);
            } else {
                // Get all users
                userList = userService.getAllUsers();
            }
            
            request.setAttribute("userList", userList);
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminUserServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
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
        
        // Get action parameter
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("AdminUserServlet");
            return;
        }
        
        try {
            switch (action) {
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    response.sendRedirect("AdminUserServlet");
                    break;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminUserServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Handles user deletion
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            
            // Check if trying to delete self
            int adminId = (int) request.getSession().getAttribute("userId");
            if (userId == adminId) {
                request.setAttribute("errorMessage", "You cannot delete your own account");
                doGet(request, response);
                return;
            }
            
            boolean deleted = userService.deleteUser(userId);
            
            if (deleted) {
                LOGGER.log(Level.INFO, "User deleted: ID {0}", userId);
                request.setAttribute("successMessage", "User deleted successfully");
            } else {
                LOGGER.log(Level.WARNING, "Failed to delete user: ID {0}", userId);
                request.setAttribute("errorMessage", "Failed to delete user");
            }
            
            doGet(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid user ID format", e);
            request.setAttribute("errorMessage", "Invalid user ID");
            doGet(request, response);
        }
    }
}
