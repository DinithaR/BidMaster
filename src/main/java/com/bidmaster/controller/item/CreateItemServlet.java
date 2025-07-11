package com.bidmaster.controller.item;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

@WebServlet("/CreateItemServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class CreateItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CreateItemServlet.class.getName());
    
    private ItemService itemService;
    
    public void init() {
        itemService = new ItemServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        
        response.sendRedirect("SellerDashboardServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        
        boolean isAdminCreated = "true".equals(request.getParameter("adminCreated"));
        
        // Get seller ID (from form if admin created, otherwise from session)
        int sellerId;
        if (isAdminCreated) {
            try {
                sellerId = Integer.parseInt(request.getParameter("sellerId"));
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid seller ID");
                if (isAdminCreated) {
                    response.sendRedirect("admin/AdminItemServlet");
                } else {
                    response.sendRedirect("SellerDashboardServlet");
                }
                return;
            }
        } else {
            sellerId = (int) session.getAttribute("userId");
        }
        
        // Get form parameters
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startingPriceStr = request.getParameter("startingPrice");
        String reservePriceStr = request.getParameter("reservePrice");
        String categoryIdStr = request.getParameter("categoryId");
        String auctionDurationStr = request.getParameter("auctionDuration");
        String startTimeStr = request.getParameter("startTime");
        String status = request.getParameter("status");
        
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
        
        // Calculate start and end times
        LocalDateTime startTime;
        LocalDateTime endTime;
        
        if (startTimeStr != null && !startTimeStr.trim().isEmpty()) {
            try {
                // Parse the start time if provided
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                startTime = LocalDateTime.parse(startTimeStr, formatter);
            } catch (Exception e) {
                startTime = LocalDateTime.now();
            }
        } else {
            // Default to current time if not provided
            startTime = LocalDateTime.now();
        }
        
        // Calculate end time based on auction duration
        int durationDays = 7; // Default to 7 days
        if (auctionDurationStr != null && !auctionDurationStr.isEmpty()) {
            try {
                durationDays = Integer.parseInt(auctionDurationStr);
            } catch (NumberFormatException e) {
                // Use default if parsing fails
            }
        }
        
        endTime = startTime.plusDays(durationDays);
        
        // Process image upload
        String imageUrl = null;
        try {
            Part filePart = request.getPart("itemImage");
            if (filePart != null && filePart.getSize() > 0) {
                imageUrl = ImageUploadUtil.uploadImage(filePart, getServletContext());
            }
        } catch (Exception e) {
            errorMessage = "Error uploading image: " + e.getMessage();
            isValid = false;
        }
        
        if (!isValid) {
            // Use session for error message to avoid infinite loop with forward
            session.setAttribute("errorMessage", errorMessage);
            
            if (isAdminCreated) {
                response.sendRedirect("admin/AdminItemServlet");
            } else {
                response.sendRedirect("SellerDashboardServlet");
            }
            return;
        }
        
        try {
            // Create item object
            Item item = new Item();
            item.setTitle(title);
            item.setDescription(description);
            item.setStartingPrice(startingPrice);
            item.setReservePrice(reservePrice);
            item.setCurrentPrice(startingPrice);
            item.setImageUrl(imageUrl);
            item.setCategoryId(categoryId);
            item.setSellerId(sellerId);
            item.setStartTime(startTime);
            item.setEndTime(endTime);
            
            // Set status (default to pending if not provided)
            if (status != null && !status.isEmpty()) {
                item.setStatus(status);
            } else {
                item.setStatus("pending");
            }
            
            // Save item
            int itemId = itemService.createItem(item);
            LOGGER.log(Level.INFO, "Item created: {0}, ID: {1}", new Object[]{title, itemId});
            
            // Set success message
            session.setAttribute("successMessage", "Item created successfully!");
            
            // Redirect based on who created the item
            if (isAdminCreated) {
                response.sendRedirect("admin/AdminItemServlet");
            } else {
                response.sendRedirect("SellerDashboardServlet");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating item", e);
            
            // Set error message
            session.setAttribute("errorMessage", "Error creating item: " + e.getMessage());
            
            // Redirect based on who created the item
            if (isAdminCreated) {
                response.sendRedirect("admin/AdminItemServlet");
            } else {
                response.sendRedirect("SellerDashboardServlet");
            }
        }
    }
}
