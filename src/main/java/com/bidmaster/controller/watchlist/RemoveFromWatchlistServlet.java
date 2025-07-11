package com.bidmaster.controller.watchlist;

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

import com.bidmaster.service.WatchlistService;
import com.bidmaster.service.impl.WatchlistServiceImpl;

/**
 * Servlet implementation class RemoveFromWatchlistServlet
 * Handles removing items from a user's watchlist
 */
@WebServlet("/RemoveFromWatchlistServlet")
public class RemoveFromWatchlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RemoveFromWatchlistServlet.class.getName());
    
    private WatchlistService watchlistService;
    
    public void init() {
        watchlistService = new WatchlistServiceImpl();
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
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
        
        try {
            // Get item ID from request parameter
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            
            // Remove item from watchlist
            boolean removed = watchlistService.removeFromWatchlist(userId, itemId);
            
            // Check if this request is from the watchlist page
            boolean fromWatchlist = "true".equals(request.getParameter("fromWatchlist"));
            
            // Determine redirect URL
            String redirectUrl;
            if (fromWatchlist) {
                redirectUrl = "WatchlistServlet";
            } else {
                // Get the referer URL for redirect
                String referer = request.getHeader("Referer");
                redirectUrl = (referer != null && !referer.isEmpty()) ? referer : "ItemListServlet";
            }
            
            // Add success/error message as parameter
            if (removed) {
                LOGGER.log(Level.INFO, "Item {0} removed from watchlist for user {1}", new Object[]{itemId, userId});
                redirectUrl = addMessageParameter(redirectUrl, "message=Item removed from watchlist");
            } else {
                LOGGER.log(Level.WARNING, "Failed to remove item {0} from watchlist for user {1}", new Object[]{itemId, userId});
                redirectUrl = addMessageParameter(redirectUrl, "error=Failed to remove item from watchlist");
            }
            
            // Redirect back to the appropriate page
            response.sendRedirect(redirectUrl);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in RemoveFromWatchlistServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Helper method to add a message parameter to a URL
     * 
     * @param url The original URL
     * @param parameter The parameter to add
     * @return The URL with the added parameter
     */
    private String addMessageParameter(String url, String parameter) {
        if (url.contains("?")) {
            return url + "&" + parameter;
        } else {
            return url + "?" + parameter;
        }
    }
}
