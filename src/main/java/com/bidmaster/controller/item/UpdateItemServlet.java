package com.bidmaster.controller.item;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import com.bidmaster.model.Item;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.impl.ItemServiceImpl;
import com.bidmaster.util.ImageUploadUtil;
import com.bidmaster.util.ValidationUtil;

@WebServlet("/UpdateItemServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class UpdateItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UpdateItemServlet.class.getName());
    
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
            response.sendRedirect("LoginServlet");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("role");
        
        try {
            // Get item ID from request parameter
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            
            // Get existing item
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                LOGGER.log(Level.WARNING, "Item not found: {0}", itemId);
                session.setAttribute("errorMessage", "Item not found");
                response.sendRedirect("SellerDashboardServlet");
                return;
            }
            
            // Check if user is the seller of this item or an admin
            if (item.getSellerId() != userId && !"admin".equals(userRole)) {
                LOGGER.log(Level.WARNING, "Unauthorized access to update item: {0} by user: {1}",
                        new Object[]{itemId, userId});
                session.setAttribute("errorMessage", "You are not authorized to update this item");
                response.sendRedirect("SellerDashboardServlet");
                return;
            }
            
            // Get form parameters
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String startingPriceStr = request.getParameter("startingPrice");
            String reservePriceStr = request.getParameter("reservePrice");
            String categoryIdStr = request.getParameter("categoryId");
            
            // Validate input
            boolean isValid = true;
            String errorMessage = "";
            
            if (!ValidationUtil.isNotEmpty(title)) {
                errorMessage = "Title is required";
                isValid = false;
            }
            
            if (!ValidationUtil.isNotEmpty(description)) {
                errorMessage = "Description is required";
                isValid = false;
            }
            
            BigDecimal startingPrice = null;
            try {
                startingPrice = new BigDecimal(startingPriceStr);
                if (startingPrice.compareTo(BigDecimal.ZERO) <= 0) {
                    errorMessage = "Starting price must be greater than 0";
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                errorMessage = "Please enter a valid starting price";
                isValid = false;
            }
            
            BigDecimal reservePrice = null;
            if (reservePriceStr != null && !reservePriceStr.isEmpty()) {
                try {
                    reservePrice = new BigDecimal(reservePriceStr);
                    if (reservePrice.compareTo(BigDecimal.ZERO) <= 0) {
                        errorMessage = "Reserve price must be greater than 0";
                        isValid = false;
                    }
                } catch (NumberFormatException e) {
                    errorMessage = "Please enter a valid reserve price";
                    isValid = false;
                }
            }
            
            int categoryId = 0;
            try {
                categoryId = Integer.parseInt(categoryIdStr);
            } catch (NumberFormatException e) {
                errorMessage = "Please select a category";
                isValid = false;
            }
            
            // Process image upload if provided
            String imageUrl = item.getImageUrl(); // Keep existing image by default
            
            try {
                Part filePart = request.getPart("itemImage");
                if (filePart != null && filePart.getSize() > 0) {
                    // Delete old image if exists
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        ImageUploadUtil.deleteImage(imageUrl, getServletContext());
                    }
                    
                    // Upload new image
                    imageUrl = ImageUploadUtil.uploadImage(filePart, getServletContext());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing image upload", e);
                errorMessage = "Error uploading image: " + e.getMessage();
                isValid = false;
            }
            
            if (!isValid) {
                session.setAttribute("errorMessage", errorMessage);
                response.sendRedirect("SellerDashboardServlet");
                return;
            }
            
            // Update item object
            item.setTitle(title);
            item.setDescription(description);
            item.setStartingPrice(startingPrice);
            item.setReservePrice(reservePrice);
            item.setImageUrl(imageUrl);
            item.setCategoryId(categoryId);
            
            // Save updated item
            boolean updated = itemService.updateItem(item);
            
            if (updated) {
                LOGGER.log(Level.INFO, "Item updated: {0}", itemId);
                session.setAttribute("successMessage", "Item updated successfully");
            } else {
                LOGGER.log(Level.WARNING, "Failed to update item: {0}", itemId);
                session.setAttribute("errorMessage", "Failed to update item");
            }
            
            // Redirect back to seller dashboard
            response.sendRedirect("SellerDashboardServlet");
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            session.setAttribute("errorMessage", "Invalid item ID");
            response.sendRedirect("SellerDashboardServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in UpdateItemServlet", e);
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect("SellerDashboardServlet");
        }
    }
}
