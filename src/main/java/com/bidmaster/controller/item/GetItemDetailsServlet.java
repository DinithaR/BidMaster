package com.bidmaster.controller.item;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.google.gson.Gson;

@WebServlet("/GetItemDetailsServlet")
public class GetItemDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(GetItemDetailsServlet.class.getName());
    
    private ItemService itemService;
    private Gson gson;
    
    public void init() {
        itemService = new ItemServiceImpl();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("role");
        
        try {
            // Get item ID from request parameter
            int itemId = Integer.parseInt(request.getParameter("id"));
            
            // Get item details
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            // Check if user is the seller of this item or an admin
            if (item.getSellerId() != userId && !"admin".equals(userRole)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
            // Set default values for missing properties
            if (item.getCondition() == null) {
                item.setCondition("Not specified");
            }
            if (item.getLocation() == null) {
                item.setLocation("Not specified");
            }
            if (item.getShippingInfo() == null) {
                item.setShippingInfo("Contact seller for shipping details");
            }
            if (item.getPaymentMethods() == null) {
                item.setPaymentMethods("Contact seller for payment options");
            }
            
            // Set response content type
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            // Convert item to JSON and send response
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(item));
            out.flush();
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in GetItemDetailsServlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
