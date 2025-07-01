package com.wmc606.inventory.entities;

import jakarta.persistence.*;
import java.util.List;

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
    
    @Enumerated(EnumType.STRING)
    @Column(name = "data_structure_type", nullable = false)
    private DataStructureType dataStructureType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // One-to-Many relationship with Product
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
    
    public DataStructureType getDataStructureType() { 
        return dataStructureType; 
    }
    
    public void setDataStructureType(DataStructureType dataStructureType) { 
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
