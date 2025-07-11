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
import jakarta.servlet.http.HttpSession;

import com.bidmaster.model.Bid;
import com.bidmaster.model.Item;
import com.bidmaster.model.User;
import com.bidmaster.service.BidService;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.UserService;
import com.bidmaster.service.WatchlistService;
import com.bidmaster.service.impl.BidServiceImpl;
import com.bidmaster.service.impl.ItemServiceImpl;
import com.bidmaster.service.impl.UserServiceImpl;
import com.bidmaster.service.impl.WatchlistServiceImpl;

@WebServlet("/ItemDetailsServlet")
public class ItemDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ItemDetailsServlet.class.getName());
    
    private ItemService itemService;
    private BidService bidService;
    private UserService userService;
    private WatchlistService watchlistService;
    
    public void init() {
        itemService = new ItemServiceImpl();
        bidService = new BidServiceImpl();
        userService = new UserServiceImpl();
        watchlistService = new WatchlistServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get success message from session if it exists
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("successMessage") != null) {
            request.setAttribute("successMessage", session.getAttribute("successMessage"));
            session.removeAttribute("successMessage");
        }

        try {
            // Get item ID from request parameter
            int itemId = Integer.parseInt(request.getParameter("id"));
            
            // Get item details
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                LOGGER.log(Level.WARNING, "Item not found: {0}", itemId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
            
            // Get bid history
            List<Bid> bidHistory = bidService.getBidsByItem(itemId);
            
            // Get seller info
            User sellerInfo = userService.getUserById(item.getSellerId());
            
            // Set default values for seller rating if needed
            if (sellerInfo != null) {
                if (sellerInfo.getRating() == 0) {
                    sellerInfo.setRating(0);
                }
                if (sellerInfo.getRatingCount() == 0) {
                    sellerInfo.setRatingCount(0);
                }
                if (sellerInfo.getItemsSold() == 0) {
                    sellerInfo.setItemsSold(0);
                }
            }
            
            // Get similar items
            List<Item> similarItems = itemService.getSimilarItems(itemId, 4);
            
            // Check if item is in user's watchlist
            boolean isInWatchlist = false;
            
            if (session != null && session.getAttribute("userId") != null) {
                int userId = (int) session.getAttribute("userId");
                isInWatchlist = watchlistService.isItemInWatchlist(userId, itemId);
            }
            
            // Set attributes for JSP
            request.setAttribute("item", item);
            request.setAttribute("bidHistory", bidHistory);
            request.setAttribute("sellerInfo", sellerInfo);
            request.setAttribute("similarItems", similarItems);
            request.setAttribute("isInWatchlist", isInWatchlist);
            
            // Forward to item details page
            request.getRequestDispatcher("item-details.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in ItemDetailsServlet: {0}", e.getMessage());
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in ItemDetailsServlet: {0}", e.getMessage());
            request.setAttribute("errorMessage", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle any POST requests by forwarding to doGet
        doGet(request, response);
    }
}
