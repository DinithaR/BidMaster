package com.bidmaster.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;
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

import com.bidmaster.model.Category;
import com.bidmaster.model.Item;
import com.bidmaster.model.User;
import com.bidmaster.service.CategoryService;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.UserService;
import com.bidmaster.service.impl.CategoryServiceImpl;
import com.bidmaster.service.impl.ItemServiceImpl;
import com.bidmaster.service.impl.UserServiceImpl;
import com.bidmaster.util.ImageUploadUtil;

@WebServlet("/admin/AdminItemServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class AdminItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminItemServlet.class.getName());
    
    private ItemService itemService;
    private CategoryService categoryService;
    private UserService userService;
    
    public void init() {
        itemService = new ItemServiceImpl();
        categoryService = new CategoryServiceImpl();
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
        
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                // Default action: list all items
                listItems(request, response);
            } else {
                switch (action) {
                    case "edit":
                        showEditForm(request, response);
                        break;
                    case "view":
                        viewItem(request, response);
                        break;
                    default:
                        listItems(request, response);
                        break;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminItemServlet", e);
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
        
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                response.sendRedirect("AdminItemServlet");
                return;
            }
            
            switch (action) {
                case "update":
                    updateItem(request, response);
                    break;
                case "delete":
                    deleteItem(request, response);
                    break;
                default:
                    response.sendRedirect("AdminItemServlet");
                    break;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminItemServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void listItems(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        LOGGER.info("Listing all items for admin dashboard");
        
        // Get filter parameters
        String status = request.getParameter("status");
        String categoryIdStr = request.getParameter("categoryId");
        String sellerIdStr = request.getParameter("sellerId");
        String priceMinStr = request.getParameter("priceMin");
        String priceMaxStr = request.getParameter("priceMax");
        
        // Get all items (implement filtering in a real application)
        List<Item> items = itemService.getAllItems();
        LOGGER.info("Retrieved " + items.size() + " items from database");
        
        // Get all categories for filter dropdown
        List<Category> categories = categoryService.getAllCategories();
        LOGGER.info("Retrieved " + categories.size() + " categories from database");
        
        // Get all sellers for filter dropdown
        List<User> sellers = userService.getUsersByRole("seller");
        LOGGER.info("Retrieved " + sellers.size() + " sellers from database");
        
        // Set attributes for JSP
        request.setAttribute("items", items);
        request.setAttribute("categories", categories);
        request.setAttribute("sellers", sellers);
        
        // Forward to items JSP
        request.getRequestDispatcher("items.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("id"));
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                request.setAttribute("errorMessage", "Item not found");
                listItems(request, response);
                return;
            }
            
            List<Category> categories = categoryService.getAllCategories();
            List<User> sellers = userService.getUsersByRole("seller");
            
            request.setAttribute("item", item);
            request.setAttribute("categories", categories);
            request.setAttribute("sellers", sellers);
            request.getRequestDispatcher("item-edit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            request.setAttribute("errorMessage", "Invalid item ID");
            listItems(request, response);
        }
    }
    
    private void viewItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("id"));
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                request.setAttribute("errorMessage", "Item not found");
                listItems(request, response);
                return;
            }
            
            request.setAttribute("item", item);
            request.getRequestDispatcher("item-view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            request.setAttribute("errorMessage", "Invalid item ID");
            listItems(request, response);
        }
    }
    
    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                request.setAttribute("errorMessage", "Item not found");
                listItems(request, response);
                return;
            }
            
            // Update item properties from form
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String categoryIdStr = request.getParameter("categoryId");
            String startingPriceStr = request.getParameter("startingPrice");
            String reservePriceStr = request.getParameter("reservePrice");
            String sellerIdStr = request.getParameter("sellerId");
            String status = request.getParameter("status");
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            
            // Validate and set values
            item.setTitle(title);
            item.setDescription(description);
            
            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                item.setCategoryId(categoryId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid category ID: " + categoryIdStr);
            }
            
            try {
                BigDecimal startingPrice = new BigDecimal(startingPriceStr);
                item.setStartingPrice(startingPrice);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid starting price: " + startingPriceStr);
            }
            
            if (reservePriceStr != null && !reservePriceStr.isEmpty()) {
                try {
                    BigDecimal reservePrice = new BigDecimal(reservePriceStr);
                    item.setReservePrice(reservePrice);
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Invalid reserve price: " + reservePriceStr);
                }
            } else {
                item.setReservePrice(null);
            }
            
            try {
                int sellerId = Integer.parseInt(sellerIdStr);
                item.setSellerId(sellerId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid seller ID: " + sellerIdStr);
            }
            
            item.setStatus(status);
            
            // Parse dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            try {
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
                item.setStartTime(startTime);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Invalid start time format: " + startTimeStr);
            }
            
            try {
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
                item.setEndTime(endTime);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Invalid end time format: " + endTimeStr);
            }
            
            // Handle image upload
            try {
                Part imagePart = request.getPart("image");
                if (imagePart != null && imagePart.getSize() > 0) {
                    // Delete old image if exists
                    if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                        ImageUploadUtil.deleteImage(item.getImageUrl(), getServletContext());
                    }
                    
                    // Upload new image
                    String imageUrl = ImageUploadUtil.uploadImage(imagePart, getServletContext());
                    item.setImageUrl(imageUrl);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing image upload", e);
                request.setAttribute("imageError", "Error uploading image: " + e.getMessage());
            }
            
            // Save changes
            boolean success = itemService.updateItem(item);
            
            if (success) {
                request.setAttribute("successMessage", "Item updated successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to update item");
            }
            
            listItems(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            request.setAttribute("errorMessage", "Invalid item ID");
            listItems(request, response);
        }
    }
    
    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            
            // Get item to delete its image
            Item item = itemService.getItemById(itemId);
            if (item != null && item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                ImageUploadUtil.deleteImage(item.getImageUrl(), getServletContext());
            }
            
            boolean success = itemService.deleteItem(itemId);
            
            if (success) {
                request.setAttribute("successMessage", "Item deleted successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to delete item");
            }
            
            listItems(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            request.setAttribute("errorMessage", "Invalid item ID");
            listItems(request, response);
        }
    }
}
