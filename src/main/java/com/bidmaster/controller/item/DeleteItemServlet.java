package com.bidmaster.controller.item;

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

import com.bidmaster.model.Item;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.impl.ItemServiceImpl;
import com.bidmaster.util.ImageUploadUtil;

@WebServlet("/DeleteItemServlet")
public class DeleteItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DeleteItemServlet.class.getName());
    
    private ItemService itemService;
    
    public void init() {
        itemService = new ItemServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("role");
        
        try {
            // Get item ID from request parameter
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            
            // Get item details
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                LOGGER.log(Level.WARNING, "Item not found: {0}", itemId);
                session.setAttribute("errorMessage", "Item not found");
                response.sendRedirect("SellerDashboardServlet");
                return;
            }
            
            // Check if user is the seller of this item or an admin
            if (item.getSellerId() != userId && !"admin".equals(userRole)) {
                LOGGER.log(Level.WARNING, "Unauthorized access to delete item: {0} by user: {1}",
                        new Object[]{itemId, userId});
                session.setAttribute("errorMessage", "You are not authorized to delete this item");
                response.sendRedirect("SellerDashboardServlet");
                return;
            }
            
            // Delete item image if exists
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                ImageUploadUtil.deleteImage(item.getImageUrl(), getServletContext());
            }
            
            // Delete item
            boolean deleted = itemService.deleteItem(itemId);
            
            if (deleted) {
                LOGGER.log(Level.INFO, "Item deleted: {0}", itemId);
                session.setAttribute("successMessage", "Item deleted successfully");
            } else {
                LOGGER.log(Level.WARNING, "Failed to delete item: {0}", itemId);
                session.setAttribute("errorMessage", "Failed to delete item");
            }
            
            // Redirect based on user role
            if ("admin".equals(userRole)) {
                response.sendRedirect("admin/AdminItemServlet");
            } else {
                response.sendRedirect("SellerDashboardServlet");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            session.setAttribute("errorMessage", "Invalid item ID");
            response.sendRedirect("SellerDashboardServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in DeleteItemServlet", e);
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect("SellerDashboardServlet");
        }
    }
}
