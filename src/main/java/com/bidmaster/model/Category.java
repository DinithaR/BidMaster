package com.bidmaster.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a category for items
 */
public class Category {
    private int categoryId;
    private String categoryName;
    private String description;
    private Integer parentCategoryId;
    private String icon;
    private int itemCount;
    
    // For display purposes
    private List<Category> subcategories;
    private String parentCategoryName;

    /**
     * Default constructor
     */
    public Category() {
        this.subcategories = new ArrayList<>();
    }

    /**
     * Constructor with name and description
     *
     * @param categoryName The category name
     * @param description The category description
     */
    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
        this.subcategories = new ArrayList<>();
    }

    /**
     * Constructor with all fields
     *
     * @param categoryId The category ID
     * @param categoryName The category name
     * @param description The category description
     * @param parentCategoryId The parent category ID (can be null)
     */
    public Category(int categoryId, String categoryName, String description, Integer parentCategoryId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.subcategories = new ArrayList<>();
    }

    /**
     * Gets the category ID
     *
     * @return The category ID
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category ID
     *
     * @param categoryId The category ID
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets the category name
     *
     * @return The category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the category name
     *
     * @param categoryName The category name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Gets the category description
     *
     * @return The category description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the category description
     *
     * @param description The category description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the parent category ID
     *
     * @return The parent category ID, or null if this is a top-level category
     */
    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    /**
     * Sets the parent category ID
     *
     * @param parentCategoryId The parent category ID
     */
    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    /**
     * Gets the icon
     *
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon
     *
     * @param icon The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Gets the item count
     *
     * @return The item count
     */
    public int getItemCount() {
        return itemCount;
    }

    /**
     * Sets the item count
     *
     * @param itemCount The item count
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * Gets the subcategories of this category
     *
     * @return List of subcategories
     */
    public List<Category> getSubcategories() {
        return subcategories;
    }

    /**
     * Sets the subcategories of this category
     *
     * @param subcategories List of subcategories
     */
    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }

    /**
     * Adds a subcategory to this category
     *
     * @param subcategory The subcategory to add
     */
    public void addSubcategory(Category subcategory) {
        if (this.subcategories == null) {
            this.subcategories = new ArrayList<>();
        }
        this.subcategories.add(subcategory);
    }

    /**
     * Gets the parent category name
     *
     * @return The parent category name
     */
    public String getParentCategoryName() {
        return parentCategoryName;
    }

    /**
     * Sets the parent category name
     *
     * @param parentCategoryName The parent category name
     */
    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    /**
     * Checks if this category has subcategories
     *
     * @return true if this category has subcategories, false otherwise
     */
    public boolean hasSubcategories() {
        return subcategories != null && !subcategories.isEmpty();
    }

    /**
     * Checks if this category is a subcategory
     *
     * @return true if this category has a parent, false otherwise
     */
    public boolean isSubcategory() {
        return parentCategoryId != null;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                '}';
    }
}
