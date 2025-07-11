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

import com.bidmaster.model.Item;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.impl.ItemServiceImpl;

@WebServlet("/ItemListServlet")
public class ItemListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ItemListServlet.class.getName());
    
    private ItemService itemService;
    
    public void init() {
        itemService = new ItemServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "listAll";
        }
        
        try {
            switch (action) {
                case "listActive":
                    listActiveItems(request, response);
                    break;
                case "listByCategory":
                    listItemsByCategory(request, response);
                    break;
                case "listBySeller":
                    listItemsBySeller(request, response);
                    break;
                case "search":
                    searchItems(request, response);
                    break;
                case "listEndingSoon":
                    listEndingSoonItems(request, response);
                    break;
                default:
                    listAllItems(request, response);
                    break;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in ItemListServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    private void listAllItems(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        List<Item> items = itemService.getAllItems();
        request.setAttribute("items", items);
        request.setAttribute("pageTitle", "All Items");
        request.getRequestDispatcher("items/list.jsp").forward(request, response);
    }
    
    private void listActiveItems(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        List<Item> items = itemService.getActiveItems();
        request.setAttribute("items", items);
        request.setAttribute("pageTitle", "Active Auctions");
        request.getRequestDispatcher("items/list.jsp").forward(request, response);
    }
    
    private void listItemsByCategory(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        List<Item> items = itemService.getItemsByCategory(categoryId);
        request.setAttribute("items", items);
        request.setAttribute("pageTitle", "Items by Category");
        request.setAttribute("categoryId", categoryId);
        request.getRequestDispatcher("items/list.jsp").forward(request, response);
    }
    
    private void listItemsBySeller(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int sellerId = Integer.parseInt(request.getParameter("sellerId"));
        List<Item> items = itemService.getItemsBySeller(sellerId);
        request.setAttribute("items", items);
        request.setAttribute("pageTitle", "Items by Seller");
        request.setAttribute("sellerId", sellerId);
        request.getRequestDispatcher("items/list.jsp").forward(request, response);
    }
    
    private void searchItems(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<Item> items = itemService.searchItems(searchTerm);
        request.setAttribute("items", items);
        request.setAttribute("pageTitle", "Search Results");
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("items/list.jsp").forward(request, response);
    }
    
    private void listEndingSoonItems(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int hours = 24; // Default to 24 hours
        if (request.getParameter("hours") != null) {
            hours = Integer.parseInt(request.getParameter("hours"));
        }
        List<Item> items = itemService.getEndingSoonItems(hours);
        request.setAttribute("items", items);
        request.setAttribute("pageTitle", "Ending Soon");
        request.setAttribute("hours", hours);
        request.getRequestDispatcher("items/list.jsp").forward(request, response);
    }
}
