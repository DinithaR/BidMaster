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

import com.bidmaster.model.Category;
import com.bidmaster.service.CategoryService;
import com.bidmaster.service.impl.CategoryServiceImpl;

/**
 * Servlet implementation class AdminCategoryServlet
 * Handles category management in the admin panel
 */
@WebServlet("/admin/AdminCategoryServlet")
public class AdminCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminCategoryServlet.class.getName());
    
    private CategoryService categoryService;
    
    public void init() {
        categoryService = new CategoryServiceImpl();
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
            // Get all categories
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
            
            request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminCategoryServlet", e);
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
            response.sendRedirect("AdminCategoryServlet");
            return;
        }
        
        try {
            switch (action) {
                case "add":
                    addCategory(request, response);
                    break;
                case "update":
                    updateCategory(request, response);
                    break;
                case "delete":
                    deleteCategory(request, response);
                    break;
                default:
                    response.sendRedirect("AdminCategoryServlet");
                    break;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminCategoryServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Handles adding a new category
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void addCategory(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");
        String parentCategoryIdStr = request.getParameter("parentCategoryId");
        
        if (categoryName == null || categoryName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Category name is required");
            doGet(request, response);
            return;
        }
        
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setDescription(description);
        
        if (parentCategoryIdStr != null && !parentCategoryIdStr.trim().isEmpty()) {
            try {
                int parentCategoryId = Integer.parseInt(parentCategoryIdStr);
                category.setParentCategoryId(parentCategoryId);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid parent category ID: {0}", parentCategoryIdStr);
                // Continue without parent category
            }
        }
        
        int categoryId = categoryService.createCategory(category);
        
        if (categoryId > 0) {
            LOGGER.log(Level.INFO, "Category added: {0}, ID: {1}", new Object[]{categoryName, categoryId});
            request.setAttribute("successMessage", "Category added successfully");
        } else {
            LOGGER.log(Level.WARNING, "Failed to add category: {0}", categoryName);
            request.setAttribute("errorMessage", "Failed to add category");
        }
        
        doGet(request, response);
    }
    
    /**
     * Handles updating a category
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void updateCategory(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String categoryName = request.getParameter("categoryName");
            String description = request.getParameter("description");
            String parentCategoryIdStr = request.getParameter("parentCategoryId");
            
            if (categoryName == null || categoryName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Category name is required");
                doGet(request, response);
                return;
            }
            
            Category category = categoryService.getCategoryById(categoryId);
            
            if (category == null) {
                request.setAttribute("errorMessage", "Category not found");
                doGet(request, response);
                return;
            }
            
            category.setCategoryName(categoryName);
            category.setDescription(description);
            
            if (parentCategoryIdStr != null && !parentCategoryIdStr.trim().isEmpty()) {
                try {
                    int parentCategoryId = Integer.parseInt(parentCategoryIdStr);
                    
                    // Check if parent is not the category itself
                    if (parentCategoryId == categoryId) {
                        request.setAttribute("errorMessage", "A category cannot be its own parent");
                        doGet(request, response);
                        return;
                    }
                    
                    category.setParentCategoryId(parentCategoryId);
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Invalid parent category ID: {0}", parentCategoryIdStr);
                    category.setParentCategoryId(null);
                }
            } else {
                category.setParentCategoryId(null);
            }
            
            boolean updated = categoryService.updateCategory(category);
            
            if (updated) {
                LOGGER.log(Level.INFO, "Category updated: {0}, ID: {1}", new Object[]{categoryName, categoryId});
                request.setAttribute("successMessage", "Category updated successfully");
            } else {
                LOGGER.log(Level.WARNING, "Failed to update category: {0}", categoryName);
                request.setAttribute("errorMessage", "Failed to update category");
            }
            
            doGet(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid category ID format", e);
            request.setAttribute("errorMessage", "Invalid category ID");
            doGet(request, response);
        }
    }
    
    /**
     * Handles deleting a category
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            
            boolean deleted = categoryService.deleteCategory(categoryId);
            
            if (deleted) {
                LOGGER.log(Level.INFO, "Category deleted: ID {0}", categoryId);
                request.setAttribute("successMessage", "Category deleted successfully");
            } else {
                LOGGER.log(Level.WARNING, "Failed to delete category: ID {0}", categoryId);
                request.setAttribute("errorMessage", "Failed to delete category. It may have associated items or subcategories.");
            }
            
            doGet(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid category ID format", e);
            request.setAttribute("errorMessage", "Invalid category ID");
            doGet(request, response);
        }
    }
}
