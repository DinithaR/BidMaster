package com.bidmaster.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.CategoryDAO;
import com.bidmaster.dao.impl.CategoryDAOImpl;
import com.bidmaster.model.Category;
import com.bidmaster.service.CategoryService;

/**
 * Implementation of CategoryService interface
 */
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = Logger.getLogger(CategoryServiceImpl.class.getName());
    
    private CategoryDAO categoryDAO;
    
    public CategoryServiceImpl() {
        this.categoryDAO = new CategoryDAOImpl();
    }

    @Override
    public Category getCategoryById(int categoryId) throws SQLException {
        try {
            return categoryDAO.getCategoryById(categoryId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting category by ID", e);
            throw e;
        }
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        try {
            return categoryDAO.getAllCategories();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all categories", e);
            throw e;
        }
    }

    @Override
    public List<Category> getSubcategories(int parentCategoryId) throws SQLException {
        try {
            return categoryDAO.getSubcategories(parentCategoryId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting subcategories", e);
            throw e;
        }
    }

    @Override
    public int createCategory(Category category) throws SQLException {
        try {
            return categoryDAO.insertCategory(category);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating category", e);
            throw e;
        }
    }

    @Override
    public boolean updateCategory(Category category) throws SQLException {
        try {
            return categoryDAO.updateCategory(category);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating category", e);
            throw e;
        }
    }

    @Override
    public boolean deleteCategory(int categoryId) throws SQLException {
        try {
            return categoryDAO.deleteCategory(categoryId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting category", e);
            throw e;
        }
    }
    
    @Override
    public List<Category> getAllCategoriesWithItemCount() throws SQLException {
        try {
            return categoryDAO.getAllCategoriesWithItemCount();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting categories with item count", e);
            throw e;
        }
    }
}
