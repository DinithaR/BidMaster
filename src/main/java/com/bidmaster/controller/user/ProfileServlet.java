package com.bidmaster.controller.user;

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
import com.bidmaster.model.Feedback;
import com.bidmaster.model.Item;
import com.bidmaster.model.Transaction;
import com.bidmaster.model.User;
import com.bidmaster.model.WatchlistItem;
import com.bidmaster.service.BidService;
import com.bidmaster.service.FeedbackService;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.TransactionService;
import com.bidmaster.service.UserService;
import com.bidmaster.service.WatchlistService;
import com.bidmaster.service.impl.BidServiceImpl;
import com.bidmaster.service.impl.FeedbackServiceImpl;
import com.bidmaster.service.impl.ItemServiceImpl;
import com.bidmaster.service.impl.TransactionServiceImpl;
import com.bidmaster.service.impl.UserServiceImpl;
import com.bidmaster.service.impl.WatchlistServiceImpl;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());
    
    private UserService userService;
    private BidService bidService;
    private ItemService itemService;
    private WatchlistService watchlistService;
    private TransactionService transactionService;
    private FeedbackService feedbackService;
    
    public void init() {
        userService = new UserServiceImpl();
        bidService = new BidServiceImpl();
        itemService = new ItemServiceImpl();
        watchlistService = new WatchlistServiceImpl();
        transactionService = new TransactionServiceImpl();
        feedbackService = new FeedbackServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        try {
            // Get user details
            User user = userService.getUserById(userId);
            
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // Get user's bids
            List<Bid> userBids = bidService.getBidsByBidder(userId);
            
            // Get user's watchlist - FIXED LINE 83
            List<WatchlistItem> watchlist = watchlistService.getWatchlistByUser(userId);
            
            // Get user's listings (if seller)
            List<Item> listings = null;
            if ("seller".equals(user.getRole())) {
                listings = itemService.getItemsBySeller(userId);
            }
            
            // Get user's transactions
            List<Transaction> transactions = transactionService.getTransactionsByUser(userId);
            
            // Get feedback received
            List<Feedback> feedbackReceived = feedbackService.getFeedbackReceived(userId);
            
            // Get feedback given
            List<Feedback> feedbackGiven = feedbackService.getFeedbackGiven(userId);
            
            // Calculate user rating
            double userRating = 0;
            if (!feedbackReceived.isEmpty()) {
                double totalRating = 0;
                for (Feedback feedback : feedbackReceived) {
                    totalRating += feedback.getRating();
                }
                userRating = totalRating / feedbackReceived.size();
            }
            
            // Set attributes for JSP
            request.setAttribute("user", user);
            request.setAttribute("userBids", userBids);
            request.setAttribute("watchlist", watchlist);
            request.setAttribute("listings", listings);
            request.setAttribute("transactions", transactions);
            request.setAttribute("feedbackReceived", feedbackReceived);
            request.setAttribute("feedbackGiven", feedbackGiven);
            request.setAttribute("userRating", userRating);
            
            // Forward to profile page
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in ProfileServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
