package com.bidmaster.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.CategoryDAO;
import com.bidmaster.model.Category;
import com.bidmaster.util.DBConnectionUtil;

public class CategoryDAOImpl implements CategoryDAO {
    private static final Logger LOGGER = Logger.getLogger(CategoryDAOImpl.class.getName());
    
    // SQL queries
    private static final String INSERT_CATEGORY = 
        "INSERT INTO Categories (categoryName, description, parentCategoryId) VALUES (?, ?, ?)";
    
    private static final String GET_CATEGORY_BY_ID = 
        "SELECT * FROM Categories WHERE categoryId = ?";
    
    private static final String GET_ALL_CATEGORIES = 
        "SELECT * FROM Categories ORDER BY categoryName";
    
    private static final String GET_SUBCATEGORIES = 
        "SELECT * FROM Categories WHERE parentCategoryId = ? ORDER BY categoryName";
    
    private static final String UPDATE_CATEGORY = 
        "UPDATE Categories SET categoryName = ?, description = ?, parentCategoryId = ? WHERE categoryId = ?";
    
    private static final String DELETE_CATEGORY = 
        "DELETE FROM Categories WHERE categoryId = ?";
    
    private static final String GET_ALL_CATEGORIES_WITH_ITEM_COUNT = 
        "SELECT c.*, COUNT(i.itemId) AS itemCount " +
        "FROM Categories c " +
        "LEFT JOIN Items i ON c.categoryId = i.categoryId AND i.status = 'active' " +
        "GROUP BY c.categoryId " +
        "ORDER BY c.categoryName";

    @Override
    public int insertCategory(Category category) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setString(2, category.getDescription());
            
            if (category.getParentCategoryId() != null && category.getParentCategoryId() > 0) {
                preparedStatement.setInt(3, category.getParentCategoryId());
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating category failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int categoryId = generatedKeys.getInt(1);
                    category.setCategoryId(categoryId);
                    LOGGER.log(Level.INFO, "Category created successfully: {0}, ID: {1}", 
                            new Object[]{category.getCategoryName(), categoryId});
                    return categoryId;
                } else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting category", e);
            throw e;
        }
    }

    @Override
    public Category getCategoryById(int categoryId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_BY_ID)) {
            
            preparedStatement.setInt(1, categoryId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractCategoryFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting category by ID: " + categoryId, e);
            throw e;
        }
        
        return null;
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CATEGORIES);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Category category = extractCategoryFromResultSet(resultSet);
                categories.add(category);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all categories", e);
            throw e;
        }
        
        return categories;
    }

    @Override
    public List<Category> getSubcategories(int parentCategoryId) throws SQLException {
        List<Category> subcategories = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SUBCATEGORIES)) {
            
            preparedStatement.setInt(1, parentCategoryId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = extractCategoryFromResultSet(resultSet);
                    subcategories.add(category);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting subcategories for parent ID: " + parentCategoryId, e);
            throw e;
        }
        
        return subcategories;
    }

    @Override
    public boolean updateCategory(Category category) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY)) {
            
            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setString(2, category.getDescription());
            
            if (category.getParentCategoryId() != null && category.getParentCategoryId() > 0) {
                preparedStatement.setInt(3, category.getParentCategoryId());
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
            }
            
            preparedStatement.setInt(4, category.getCategoryId());
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Category updated: {0}, Rows affected: {1}", 
                    new Object[]{category.getCategoryId(), rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating category: " + category.getCategoryId(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteCategory(int categoryId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CATEGORY)) {
            
            preparedStatement.setInt(1, categoryId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Category deleted: {0}, Rows affected: {1}", 
                    new Object[]{categoryId, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting category: " + categoryId, e);
            throw e;
        }
    }
    
    @Override
    public List<Category> getAllCategoriesWithItemCount() throws SQLException {
        List<Category> categories = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CATEGORIES_WITH_ITEM_COUNT);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Category category = extractCategoryFromResultSet(resultSet);
                
                // Add item count
                category.setItemCount(resultSet.getInt("itemCount"));
                
                // Set a default icon since it's not in the database
                category.setIcon("fa-tag");
                
                categories.add(category);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting categories with item count", e);
            throw e;
        }
        
        return categories;
    }
    
    /**
     * Extracts a Category object from a ResultSet
     * 
     * @param resultSet The ResultSet containing category data
     * @return The extracted Category object
     * @throws SQLException if a database error occurs
     */
    private Category extractCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setCategoryId(resultSet.getInt("categoryId"));
        category.setCategoryName(resultSet.getString("categoryName"));
        category.setDescription(resultSet.getString("description"));
        
        int parentCategoryId = resultSet.getInt("parentCategoryId");
        if (!resultSet.wasNull()) {
            category.setParentCategoryId(parentCategoryId);
        }
        
        // Set default icon since it's not in the database
        category.setIcon("fa-tag");
        
        return category;
    }
}
