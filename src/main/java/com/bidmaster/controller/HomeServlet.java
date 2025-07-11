package com.bidmaster.controller;

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

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName());
    
    private CategoryService categoryService;
    private ItemService itemService;
    
    public void init() {
        categoryService = new CategoryServiceImpl();
        itemService = new ItemServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get categories with item counts
            List<Category> categories = categoryService.getAllCategoriesWithItemCount();
            
            // Get featured items
            List<Item> featuredItems = itemService.getFeaturedItems(8);
            
            // Get new arrivals
            List<Item> newItems = itemService.getNewItems(8);
            
            // Set attributes for JSP
            request.setAttribute("categories", categories);
            request.setAttribute("featuredItems", featuredItems);
            request.setAttribute("newItems", newItems);
            
            // Forward to home page
            request.getRequestDispatcher("index.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in HomeServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
