package com.wmc606.inventory.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Category Entity - Represents product categories
 * Each category uses a different data structure:
 * - STACK: Categories 1-4 (Beverages, Bread, Canned, Dairy)
 * - QUEUE: Categories 5-7 (Dry/Baking, Frozen, Meat)
 * - LIST: Categories 8-11 (Produce, Cleaners, Paper, Personal Care)
 */
@Entity
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    // FIXED: Ensure proper enum mapping with validation
    @Enumerated(EnumType.STRING)
    @Column(name = "data_structure_type", nullable = false)
    private DataStructureType dataStructureType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // FIXED: Add @JsonIgnore to prevent infinite recursion
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
    
    /**
     * Enum for Data Structure Types
     * Matches the database ENUM values exactly
     */
    public enum DataStructureType {
        STACK,  // Categories 1-4: LIFO operations
        QUEUE,  // Categories 5-7: FIFO operations
        LIST    // Categories 8-11: Dynamic operations
    }
    
    // Constructors
    public Category() {}
    
    public Category(String name, DataStructureType dataStructureType, String description) {
        this.name = name;
        this.dataStructureType = dataStructureType;
        this.description = description;
    }
    
    // Getters and Setters with validation
    public Long getCategoryId() { 
        return categoryId; 
    }
    
    public void setCategoryId(Long categoryId) { 
        this.categoryId = categoryId; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    /**
     * Get data structure type with null safety
     * @return DataStructureType or throws exception if null
     */
    public DataStructureType getDataStructureType() { 
        if (dataStructureType == null) {
            throw new IllegalStateException("DataStructureType cannot be null for category: " + name + " (ID: " + categoryId + ")");
        }
        return dataStructureType; 
    }
    
    public void setDataStructureType(DataStructureType dataStructureType) { 
        if (dataStructureType == null) {
            throw new IllegalArgumentException("DataStructureType cannot be null");
        }
        this.dataStructureType = dataStructureType; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public List<Product> getProducts() { 
        return products; 
    }
    
    public void setProducts(List<Product> products) { 
        this.products = products; 
    }
    
    /**
     * Check if this category uses STACK data structure
     * @return true if STACK, false otherwise
     */
    public boolean isStackCategory() {
        return DataStructureType.STACK.equals(this.dataStructureType);
    }
    
    /**
     * Check if this category uses QUEUE data structure
     * @return true if QUEUE, false otherwise
     */
    public boolean isQueueCategory() {
        return DataStructureType.QUEUE.equals(this.dataStructureType);
    }
    
    /**
     * Check if this category uses LIST data structure
     * @return true if LIST, false otherwise
     */
    public boolean isListCategory() {
        return DataStructureType.LIST.equals(this.dataStructureType);
    }
    
    /**
     * Get user-friendly description of data structure operations
     * @return Description of operations for this data structure type
     */
    public String getDataStructureDescription() {
        if (dataStructureType == null) {
            return "Unknown data structure";
        }
        
        switch (dataStructureType) {
            case STACK:
                return "LIFO (Last In, First Out) - O(1) Push/Pop operations";
            case QUEUE:
                return "FIFO (First In, First Out) - O(1) Enqueue/Dequeue operations";
            case LIST:
                return "Dynamic operations - O(1) Add, O(n) Search/Remove, O(n log n) Sort";
            default:
                return "Unknown data structure operations";
        }
    }
    
    /**
     * Get the typical category IDs for this data structure type
     * @return Range description for category IDs
     */
    public String getCategoryRange() {
        if (dataStructureType == null) {
            return "Unknown range";
        }
        
        switch (dataStructureType) {
            case STACK:
                return "Categories 1-4";
            case QUEUE:
                return "Categories 5-7";
            case LIST:
                return "Categories 8-11";
            default:
                return "Unknown range";
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return categoryId != null && categoryId.equals(category.categoryId);
    }
    
    @Override
    public int hashCode() {
        return categoryId != null ? categoryId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", dataStructureType=" + dataStructureType +
                ", description='" + description + '\'' +
                '}';
    }
}