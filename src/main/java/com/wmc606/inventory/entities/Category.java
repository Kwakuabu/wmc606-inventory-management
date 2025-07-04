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
 * Enhanced Category Entity with safety checks and auto-repair
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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "data_structure_type")
    private DataStructureType dataStructureType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
    
    /**
     * Enum for Data Structure Types
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
    
    // Getters and Setters
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
     * Enhanced getDataStructureType with auto-repair and detailed logging
     */
    public DataStructureType getDataStructureType() { 
        System.out.println("🔍 DEBUG: Getting DataStructureType for category " + categoryId + " (" + name + ")");
        System.out.println("🔍 DEBUG: Raw dataStructureType value: " + dataStructureType);
        
        // If null, try auto-repair
        if (dataStructureType == null && categoryId != null) {
            System.err.println("⚠️ WARNING: DataStructureType is null for category " + categoryId + ". Attempting auto-repair...");
            dataStructureType = getCorrectDataStructureType(categoryId);
            
            if (dataStructureType != null) {
                System.out.println("🔧 Auto-repaired DataStructureType to: " + dataStructureType);
                return dataStructureType;
            }
        }
        
        if (dataStructureType == null) {
            String expectedType = getExpectedDataStructureTypeString();
            System.err.println("💀 CRITICAL: DataStructureType is still null after auto-repair attempt!");
            System.err.println("💀 Category: " + name + " (ID: " + categoryId + ")");
            System.err.println("💀 Expected: " + expectedType);
            
            throw new IllegalStateException(
                "DataStructureType cannot be null for category: " + name + " (ID: " + categoryId + 
                "). Expected: " + expectedType + ". Please check database integrity.");
        }
        
        System.out.println("✅ DataStructureType retrieved successfully: " + dataStructureType);
        return dataStructureType; 
    }
    
    /**
     * Get correct DataStructureType based on category ID
     */
    private DataStructureType getCorrectDataStructureType(Long categoryId) {
        if (categoryId >= 1 && categoryId <= 4) {
            return DataStructureType.STACK;
        } else if (categoryId >= 5 && categoryId <= 7) {
            return DataStructureType.QUEUE;
        } else if (categoryId >= 8 && categoryId <= 11) {
            return DataStructureType.LIST;
        }
        return null;
    }
    
    /**
     * Get expected DataStructureType as string for error messages
     */
    private String getExpectedDataStructureTypeString() {
        if (categoryId == null) return "Unknown (category ID is null)";
        
        if (categoryId >= 1 && categoryId <= 4) {
            return "STACK (Categories 1-4: Beverages, Bread, Canned, Dairy)";
        } else if (categoryId >= 5 && categoryId <= 7) {
            return "QUEUE (Categories 5-7: Dry/Baking, Frozen, Meat)";
        } else if (categoryId >= 8 && categoryId <= 11) {
            return "LIST (Categories 8-11: Produce, Cleaners, Paper, Personal Care)";
        }
        
        return "Invalid category ID: " + categoryId;
    }
    
    public void setDataStructureType(DataStructureType dataStructureType) { 
        System.out.println("🔧 Setting DataStructureType for category " + categoryId + " to: " + dataStructureType);
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
     * Safe check methods with exception handling
     */
    public boolean isStackCategory() {
        try {
            return DataStructureType.STACK.equals(getDataStructureType());
        } catch (Exception e) {
            System.err.println("⚠️ Error checking if stack category: " + e.getMessage());
            return categoryId != null && categoryId >= 1 && categoryId <= 4;
        }
    }
    
    public boolean isQueueCategory() {
        try {
            return DataStructureType.QUEUE.equals(getDataStructureType());
        } catch (Exception e) {
            System.err.println("⚠️ Error checking if queue category: " + e.getMessage());
            return categoryId != null && categoryId >= 5 && categoryId <= 7;
        }
    }
    
    public boolean isListCategory() {
        try {
            return DataStructureType.LIST.equals(getDataStructureType());
        } catch (Exception e) {
            System.err.println("⚠️ Error checking if list category: " + e.getMessage());
            return categoryId != null && categoryId >= 8 && categoryId <= 11;
        }
    }
    
    /**
     * Get user-friendly description of data structure operations
     */
    public String getDataStructureDescription() {
        try {
            switch (getDataStructureType()) {
                case STACK:
                    return "LIFO (Last In, First Out) - O(1) Push/Pop operations";
                case QUEUE:
                    return "FIFO (First In, First Out) - O(1) Enqueue/Dequeue operations";
                case LIST:
                    return "Dynamic operations - O(1) Add, O(n) Search/Remove, O(n log n) Sort";
                default:
                    return "Unknown data structure operations";
            }
        } catch (Exception e) {
            return "Data structure configuration error: " + e.getMessage();
        }
    }
    
    /**
     * Debug method to check raw database value
     */
    public String debugDataStructureType() {
        return "Raw dataStructureType field value: " + dataStructureType + 
               " (Category ID: " + categoryId + ", Name: " + name + ")";
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
    
    /**
     * Enhanced toString for debugging
     */
    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", dataStructureType=" + dataStructureType + 
                " (raw value from DB)" +
                ", description='" + description + '\'' +
                '}';
    }
}