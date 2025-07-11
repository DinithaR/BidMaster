package com.bidmaster.service;

import java.sql.SQLException;
import java.util.List;

import com.bidmaster.model.Category;

/**
 * Service interface for Category operations
 */
public interface CategoryService {
    /**
     * Gets a category by its ID
     *
     * @param categoryId The category ID
     * @return The category, or null if not found
     * @throws SQLException if a database error occurs
     */
    Category getCategoryById(int categoryId) throws SQLException;

    /**
     * Gets all categories
     *
     * @return List of all categories
     * @throws SQLException if a database error occurs
     */
    List<Category> getAllCategories() throws SQLException;

    /**
     * Gets subcategories of a parent category
     *
     * @param parentCategoryId The parent category ID
     * @return List of subcategories
     * @throws SQLException if a database error occurs
     */
    List<Category> getSubcategories(int parentCategoryId) throws SQLException;

    /**
     * Creates a new category
     *
     * @param category The category to create
     * @return The ID of the created category
     * @throws SQLException if a database error occurs
     */
    int createCategory(Category category) throws SQLException;

    /**
     * Updates a category
     *
     * @param category The category to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateCategory(Category category) throws SQLException;

    /**
     * Deletes a category
     *
     * @param categoryId The category ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean deleteCategory(int categoryId) throws SQLException;
    
    /**
     * Gets all categories with item count
     *
     * @return List of categories with item count
     * @throws SQLException if a database error occurs
     */
    List<Category> getAllCategoriesWithItemCount() throws SQLException;
}
