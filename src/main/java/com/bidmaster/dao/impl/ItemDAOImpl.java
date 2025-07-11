package com.bidmaster.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.ItemDAO;
import com.bidmaster.model.Category;
import com.bidmaster.model.Item;
import com.bidmaster.util.DBConnectionUtil;

public class ItemDAOImpl implements ItemDAO {
    private static final Logger LOGGER = Logger.getLogger(ItemDAOImpl.class.getName());
    
    // SQL queries
    private static final String INSERT_ITEM = 
        "INSERT INTO Items (title, description, startingPrice, reservePrice, currentPrice, " +
        "categoryId, sellerId, startTime, endTime, status, imageUrl) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String GET_ITEM_BY_ID = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.itemId = ?";
    
    private static final String GET_ALL_ITEMS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "ORDER BY i.createdAt DESC";
    
    private static final String GET_ITEMS_BY_CATEGORY = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.categoryId = ? " +
        "ORDER BY i.createdAt DESC";
    
    private static final String GET_ITEMS_BY_SELLER = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.sellerId = ? " +
        "ORDER BY i.createdAt DESC";
    
    private static final String SEARCH_ITEMS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE (i.title LIKE ? OR i.description LIKE ?) AND i.status = 'active' " +
        "ORDER BY i.createdAt DESC";
    
    private static final String GET_ACTIVE_ITEMS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.status = 'active' AND i.endTime > NOW() " +
        "ORDER BY i.endTime ASC";
    
    private static final String GET_ENDING_SOON_ITEMS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.status = 'active' AND i.endTime BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL ? HOUR) " +
        "ORDER BY i.endTime ASC";
    
    private static final String UPDATE_ITEM = 
        "UPDATE Items SET title = ?, description = ?, startingPrice = ?, reservePrice = ?, " +
        "currentPrice = ?, categoryId = ?, imageUrl = ?, status = ?, startTime = ?, endTime = ? " +
        "WHERE itemId = ?";
    
    private static final String UPDATE_ITEM_STATUS = 
        "UPDATE Items SET status = ? WHERE itemId = ?";
    
    private static final String UPDATE_CURRENT_PRICE = 
        "UPDATE Items SET currentPrice = ? WHERE itemId = ?";
    
    private static final String DELETE_ITEM = 
        "DELETE FROM Items WHERE itemId = ?";
    
    private static final String GET_ACTIVE_ITEM_COUNT = 
        "SELECT COUNT(*) FROM Items WHERE status = 'active' AND endTime > NOW()";
    
    private static final String GET_NEW_ITEM_COUNT = 
        "SELECT COUNT(*) FROM Items WHERE createdAt >= DATE_SUB(NOW(), INTERVAL ? DAY)";
    
    private static final String GET_RECENT_ITEMS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "ORDER BY i.createdAt DESC LIMIT ?";
    
    private static final String GET_ITEMS_IN_DATE_RANGE = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE DATE(i.createdAt) BETWEEN ? AND ? " +
        "ORDER BY i.createdAt DESC";
    
    private static final String GET_ITEMS_WITH_MOST_BIDS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "COUNT(b.bidId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "LEFT JOIN Bids b ON i.itemId = b.itemId " +
        "WHERE DATE(i.createdAt) BETWEEN ? AND ? " +
        "GROUP BY i.itemId " +
        "ORDER BY bidCount DESC " +
        "LIMIT ?";
    
    private static final String GET_ACTIVE_ITEMS_BY_SELLER = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.sellerId = ? AND i.status = 'active' AND i.endTime > NOW() " +
        "ORDER BY i.endTime ASC";
    
    private static final String GET_ACTIVE_ITEM_COUNT_BY_SELLER = 
        "SELECT COUNT(*) FROM Items WHERE sellerId = ? AND status = 'active' AND endTime > NOW()";
    
    private static final String GET_FEATURED_ITEMS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.status = 'active' AND i.endTime > NOW() " +
        "ORDER BY i.bidCount DESC, i.endTime ASC " +
        "LIMIT ?";
    
    private static final String GET_NEW_ITEMS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.status = 'active' AND i.endTime > NOW() " +
        "ORDER BY i.createdAt DESC " +
        "LIMIT ?";
    
    private static final String GET_SIMILAR_ITEMS = 
        "SELECT i.*, c.categoryName, u.username AS sellerUsername, " +
        "(SELECT COUNT(*) FROM Bids b WHERE b.itemId = i.itemId) AS bidCount " +
        "FROM Items i " +
        "JOIN Categories c ON i.categoryId = c.categoryId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE i.categoryId = (SELECT categoryId FROM Items WHERE itemId = ?) " +
        "AND i.itemId != ? AND i.status = 'active' AND i.endTime > NOW() " +
        "ORDER BY RAND() " +
        "LIMIT ?";
    
    private static final String GET_ALL_CATEGORIES_WITH_ITEM_COUNT = 
        "SELECT c.*, COUNT(i.itemId) AS itemCount " +
        "FROM Categories c " +
        "LEFT JOIN Items i ON c.categoryId = i.categoryId AND i.status = 'active' " +
        "GROUP BY c.categoryId " +
        "ORDER BY c.categoryName";

    @Override
    public int insertItem(Item item) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ITEM, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, item.getTitle());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setBigDecimal(3, item.getStartingPrice());
            preparedStatement.setBigDecimal(4, item.getReservePrice());
            preparedStatement.setBigDecimal(5, item.getCurrentPrice());
            preparedStatement.setInt(6, item.getCategoryId());
            preparedStatement.setInt(7, item.getSellerId());
            preparedStatement.setTimestamp(8, Timestamp.valueOf(item.getStartTime()));
            preparedStatement.setTimestamp(9, Timestamp.valueOf(item.getEndTime()));
            preparedStatement.setString(10, item.getStatus());
            preparedStatement.setString(11, item.getImageUrl());
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int itemId = generatedKeys.getInt(1);
                    item.setItemId(itemId);
                    LOGGER.log(Level.INFO, "Item created successfully: {0}, ID: {1}", 
                            new Object[]{item.getTitle(), itemId});
                    return itemId;
                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting item", e);
            throw e;
        }
    }

    @Override
    public Item getItemById(int itemId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEM_BY_ID)) {
            
            preparedStatement.setInt(1, itemId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractItemFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting item by ID: " + itemId, e);
            throw e;
        }
        
        return null;
    }

    @Override
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ITEMS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Item item = extractItemFromResultSet(resultSet);
                items.add(item);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all items", e);
            throw e;
        }
        
        return items;
    }

    @Override
    public List<Item> getItemsByCategory(int categoryId) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEMS_BY_CATEGORY)) {
            
            preparedStatement.setInt(1, categoryId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting items by category: " + categoryId, e);
            throw e;
        }
        
        return items;
    }

    @Override
    public List<Item> getItemsBySeller(int sellerId) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEMS_BY_SELLER)) {
            
            preparedStatement.setInt(1, sellerId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting items by seller: " + sellerId, e);
            throw e;
        }
        
        return items;
    }

    @Override
    public List<Item> searchItems(String searchTerm) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_ITEMS)) {
            
            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching items: " + searchTerm, e);
            throw e;
        }
        
        return items;
    }

    @Override
    public List<Item> getActiveItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACTIVE_ITEMS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Item item = extractItemFromResultSet(resultSet);
                items.add(item);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting active items", e);
            throw e;
        }
        
        return items;
    }

    @Override
    public List<Item> getEndingSoonItems(int hours) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ENDING_SOON_ITEMS)) {
            
            preparedStatement.setInt(1, hours);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting ending soon items", e);
            throw e;
        }
        
        return items;
    }

    @Override
    public boolean updateItem(Item item) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ITEM)) {
            
            preparedStatement.setString(1, item.getTitle());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setBigDecimal(3, item.getStartingPrice());
            preparedStatement.setBigDecimal(4, item.getReservePrice());
            preparedStatement.setBigDecimal(5, item.getCurrentPrice());
            preparedStatement.setInt(6, item.getCategoryId());
            preparedStatement.setString(7, item.getImageUrl());
            preparedStatement.setString(8, item.getStatus());
            preparedStatement.setTimestamp(9, Timestamp.valueOf(item.getStartTime()));
            preparedStatement.setTimestamp(10, Timestamp.valueOf(item.getEndTime()));
            preparedStatement.setInt(11, item.getItemId());
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Item updated: {0}, Rows affected: {1}", 
                    new Object[]{item.getItemId(), rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating item: " + item.getItemId(), e);
            throw e;
        }
    }

    @Override
    public boolean updateItemStatus(int itemId, String status) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ITEM_STATUS)) {
            
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, itemId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Item status updated: {0}, Status: {1}, Rows affected: {2}", 
                    new Object[]{itemId, status, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating item status: " + itemId, e);
            throw e;
        }
    }

    @Override
    public boolean updateCurrentPrice(int itemId, double newPrice) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CURRENT_PRICE)) {
            
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, itemId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Item price updated: {0}, New price: {1}, Rows affected: {2}", 
                    new Object[]{itemId, newPrice, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating item price: " + itemId, e);
            throw e;
        }
    }

    @Override
    public boolean deleteItem(int itemId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ITEM)) {
            
            preparedStatement.setInt(1, itemId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Item deleted: {0}, Rows affected: {1}", 
                    new Object[]{itemId, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting item: " + itemId, e);
            throw e;
        }
    }

    @Override
    public int getActiveItemCount() throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACTIVE_ITEM_COUNT);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            
            return 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting active item count", e);
            throw e;
        }
    }

    @Override
    public int getNewItemCount(int days) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_NEW_ITEM_COUNT)) {
            
            preparedStatement.setInt(1, days);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting new item count", e);
            throw e;
        }
    }

    @Override
    public List<Item> getRecentItems(int limit) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_RECENT_ITEMS)) {
            
            preparedStatement.setInt(1, limit);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent items", e);
            throw e;
        }
        
        return items;
    }

    @Override
    public List<Item> getItemsCreatedInDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEMS_IN_DATE_RANGE)) {
            
            preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting items in date range", e);
            throw e;
        }
        
        return items;
    }

    @Override
    public List<Item> getItemsWithMostBids(LocalDate startDate, LocalDate endDate, int limit) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEMS_WITH_MOST_BIDS)) {
            
            preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            preparedStatement.setInt(3, limit);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting items with most bids", e);
            throw e;
        }
        
        return items;
    }

    @Override
    public List<Item> getActiveItemsBySeller(int sellerId) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACTIVE_ITEMS_BY_SELLER)) {
            
            preparedStatement.setInt(1, sellerId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting active items by seller: " + sellerId, e);
            throw e;
        }
        
        return items;
    }

    @Override
    public int getActiveItemCountBySeller(int sellerId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACTIVE_ITEM_COUNT_BY_SELLER)) {
            
            preparedStatement.setInt(1, sellerId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting active item count by seller: " + sellerId, e);
            throw e;
        }
    }
    
    @Override
    public List<Item> getFeaturedItems(int limit) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_FEATURED_ITEMS)) {
            
            preparedStatement.setInt(1, limit);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting featured items", e);
            throw e;
        }
        
        return items;
    }
    
    @Override
    public List<Item> getNewItems(int limit) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_NEW_ITEMS)) {
            
            preparedStatement.setInt(1, limit);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting new items", e);
            throw e;
        }
        
        return items;
    }
    
    @Override
    public List<Item> getSimilarItems(int itemId, int limit) throws SQLException {
        List<Item> items = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SIMILAR_ITEMS)) {
            
            preparedStatement.setInt(1, itemId);
            preparedStatement.setInt(2, itemId);
            preparedStatement.setInt(3, limit);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = extractItemFromResultSet(resultSet);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting similar items", e);
            throw e;
        }
        
        return items;
    }
    
    @Override
    public List<Category> getAllCategoriesWithItemCount() throws SQLException {
        List<Category> categories = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CATEGORIES_WITH_ITEM_COUNT);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Category category = new Category();
                category.setCategoryId(resultSet.getInt("categoryId"));
                category.setCategoryName(resultSet.getString("categoryName"));
                category.setDescription(resultSet.getString("description"));
                category.setParentCategoryId(resultSet.getInt("parentCategoryId"));
                category.setIcon(resultSet.getString("icon"));
                
                // Add item count
                category.setItemCount(resultSet.getInt("itemCount"));
                
                categories.add(category);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting categories with item count", e);
            throw e;
        }
        
        return categories;
    }
    
    /**
     * Extracts an Item object from a ResultSet
     * 
     * @param resultSet The ResultSet containing item data
     * @return The extracted Item object
     * @throws SQLException if a database error occurs
     */
    private Item extractItemFromResultSet(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setItemId(resultSet.getInt("itemId"));
        item.setTitle(resultSet.getString("title"));
        item.setDescription(resultSet.getString("description"));
        item.setStartingPrice(resultSet.getBigDecimal("startingPrice"));
        item.setReservePrice(resultSet.getBigDecimal("reservePrice"));
        item.setCurrentPrice(resultSet.getBigDecimal("currentPrice"));
        item.setCategoryId(resultSet.getInt("categoryId"));
        item.setCategoryName(resultSet.getString("categoryName"));
        item.setSellerId(resultSet.getInt("sellerId"));
        item.setSellerUsername(resultSet.getString("sellerUsername"));
        
        Timestamp startTime = resultSet.getTimestamp("startTime");
        if (startTime != null) {
            item.setStartTime(startTime.toLocalDateTime());
        }
        
        Timestamp endTime = resultSet.getTimestamp("endTime");
        if (endTime != null) {
            item.setEndTime(endTime.toLocalDateTime());
        }
        
        Timestamp createdAt = resultSet.getTimestamp("createdAt");
        if (createdAt != null) {
            item.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        item.setStatus(resultSet.getString("status"));
        item.setImageUrl(resultSet.getString("imageUrl"));
        
        try {
            item.setBidCount(resultSet.getInt("bidCount"));
        } catch (SQLException e) {
            // bidCount might not be present in all queries
            item.setBidCount(0);
        }
        
        return item;
    }
}
