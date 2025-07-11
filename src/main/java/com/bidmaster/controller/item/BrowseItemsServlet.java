package com.bidmaster.controller.item;

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

import com.bidmaster.model.Category;
import com.bidmaster.model.Item;
import com.bidmaster.service.CategoryService;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.impl.CategoryServiceImpl;
import com.bidmaster.service.impl.ItemServiceImpl;

@WebServlet("/BrowseItemsServlet")
public class BrowseItemsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BrowseItemsServlet.class.getName());
    
    private ItemService itemService;
    private CategoryService categoryService;
    
    public void init() {
        itemService = new ItemServiceImpl();
        categoryService = new CategoryServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get filter parameters
            String categoryIdStr = request.getParameter("categoryId");
            String sortBy = request.getParameter("sortBy");
            String searchTerm = request.getParameter("search");
            String priceMinStr = request.getParameter("priceMin");
            String priceMaxStr = request.getParameter("priceMax");
            String page = request.getParameter("page");
            
            // Default page to 1 if not specified
            int currentPage = 1;
            if (page != null && !page.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(page);
                    if (currentPage < 1) {
                        currentPage = 1;
                    }
                } catch (NumberFormatException e) {
                    // Use default if parsing fails
                }
            }
            
            // Items per page
            int itemsPerPage = 20;
            
            // Get items based on filters
            List<Item> items;
            int totalItems;
            
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                // Filter by category
                try {
                    int categoryId = Integer.parseInt(categoryIdStr);
                    items = itemService.getItemsByCategory(categoryId);
                    totalItems = items.size();
                    
                    // Get category for display
                    Category category = categoryService.getCategoryById(categoryId);
                    request.setAttribute("selectedCategory", category);
                } catch (NumberFormatException e) {
                    items = itemService.getActiveItems();
                    totalItems = items.size();
                }
            } else if (searchTerm != null && !searchTerm.isEmpty()) {
                // Search items
                items = itemService.searchItems(searchTerm);
                totalItems = items.size();
                request.setAttribute("searchTerm", searchTerm);
            } else {
                // Get all active items
                items = itemService.getActiveItems();
                totalItems = items.size();
            }
            
            // Add debug logging to check if items are retrieved
            LOGGER.log(Level.INFO, "Retrieved {0} items before filtering", items.size());
            
            // Apply price filters if specified
            if (priceMinStr != null && !priceMinStr.isEmpty() || priceMaxStr != null && !priceMaxStr.isEmpty()) {
                // Create final variables for use in lambda
                final double finalPriceMin;
                final double finalPriceMax;
                
                // Initialize with default values
                double tempPriceMin = 0;
                double tempPriceMax = Double.MAX_VALUE;
                
                if (priceMinStr != null && !priceMinStr.isEmpty()) {
                    try {
                        tempPriceMin = Double.parseDouble(priceMinStr);
                    } catch (NumberFormatException e) {
                        // Use default if parsing fails
                    }
                }
                
                if (priceMaxStr != null && !priceMaxStr.isEmpty()) {
                    try {
                        tempPriceMax = Double.parseDouble(priceMaxStr);
                    } catch (NumberFormatException e) {
                        // Use default if parsing fails
                    }
                }
                
                // Assign to final variables
                finalPriceMin = tempPriceMin;
                finalPriceMax = tempPriceMax;
                
                // Filter items by price using the final variables
                items = items.stream()
                        .filter(item -> item.getCurrentPrice() != null &&
                                item.getCurrentPrice().doubleValue() >= finalPriceMin &&
                                item.getCurrentPrice().doubleValue() <= finalPriceMax)
                        .toList();
                totalItems = items.size();
                
                request.setAttribute("priceMin", tempPriceMin);
                request.setAttribute("priceMax", tempPriceMax);
            }
            
            // Apply sorting if specified
            if (sortBy != null && !sortBy.isEmpty()) {
                switch (sortBy) {
                    case "priceAsc":
                        items = items.stream()
                                .sorted((i1, i2) -> {
                                    if (i1.getCurrentPrice() == null && i2.getCurrentPrice() == null) return 0;
                                    if (i1.getCurrentPrice() == null) return -1;
                                    if (i2.getCurrentPrice() == null) return 1;
                                    return i1.getCurrentPrice().compareTo(i2.getCurrentPrice());
                                })
                                .toList();
                        break;
                    case "priceDesc":
                        items = items.stream()
                                .sorted((i1, i2) -> {
                                    if (i1.getCurrentPrice() == null && i2.getCurrentPrice() == null) return 0;
                                    if (i1.getCurrentPrice() == null) return 1;
                                    if (i2.getCurrentPrice() == null) return -1;
                                    return i2.getCurrentPrice().compareTo(i1.getCurrentPrice());
                                })
                                .toList();
                        break;
                    case "endingSoon":
                        items = items.stream()
                                .sorted((i1, i2) -> {
                                    if (i1.getEndTime() == null && i2.getEndTime() == null) return 0;
                                    if (i1.getEndTime() == null) return 1;
                                    if (i2.getEndTime() == null) return -1;
                                    return i1.getEndTime().compareTo(i2.getEndTime());
                                })
                                .toList();
                        break;
                    case "newest":
                        items = items.stream()
                                .sorted((i1, i2) -> {
                                    if (i1.getCreatedAt() == null && i2.getCreatedAt() == null) return 0;
                                    if (i1.getCreatedAt() == null) return 1;
                                    if (i2.getCreatedAt() == null) return -1;
                                    return i2.getCreatedAt().compareTo(i1.getCreatedAt());
                                })
                                .toList();
                        break;
                    default:
                        // Default sorting
                        break;
                }
                
                request.setAttribute("sortBy", sortBy);
            }
            
            // Add debug logging after filtering and sorting
            LOGGER.log(Level.INFO, "Retrieved {0} items after filtering and sorting", items.size());
            
            // Calculate pagination
            int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
            int startIndex = (currentPage - 1) * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, totalItems);
            
            // Calculate endItem for display
            int endItem = Math.min(currentPage * itemsPerPage, totalItems);
            request.setAttribute("endItem", endItem);
            
            // Get subset of items for current page
            List<Item> pagedItems;
            if (startIndex < totalItems) {
                pagedItems = items.subList(startIndex, endIndex);
            } else {
                pagedItems = List.of();
            }
            
            // Get all categories for filter dropdown
            List<Category> categories = categoryService.getAllCategories();
            
            // Set attributes for JSP
            request.setAttribute("items", pagedItems);
            request.setAttribute("categories", categories);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalItems", totalItems);
            
            // Forward to browse items page
            request.getRequestDispatcher("browse-items.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in BrowseItemsServlet: {0}", e.getMessage());
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in BrowseItemsServlet: {0}", e.getMessage());
            request.setAttribute("errorMessage", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
