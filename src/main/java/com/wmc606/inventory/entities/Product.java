package com.wmc606.inventory.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Product Entity - Main product information
 * Products are managed using different data structures based on category
 */
@Entity
@Table(name = "products")
public class Product implements Comparable<Product> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    // FIXED: Add @JsonIgnoreProperties to prevent infinite recursion
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("products")
    private Category category;
    
    // FIXED: Add @JsonIgnoreProperties to prevent infinite recursion
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id")
    @JsonIgnoreProperties("products")
    private Vendor vendor;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "quantity_in_stock")
    private Integer quantityInStock = 0;
    
    @Column(name = "minimum_stock_level")
    private Integer minimumStockLevel = 10;
    
    @Column(name = "product_code", unique = true, length = 50)
    private String productCode;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Product() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Product(String name, Category category, Vendor vendor, BigDecimal price, 
                  Integer quantityInStock, String productCode) {
        this.name = name;
        this.category = category;
        this.vendor = vendor;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.productCode = productCode;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Update timestamp before saving
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getProductId() { 
        return productId; 
    }
    
    public void setProductId(Long productId) { 
        this.productId = productId; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public Category getCategory() { 
        return category; 
    }
    
    public void setCategory(Category category) { 
        this.category = category; 
    }
    
    public Vendor getVendor() { 
        return vendor; 
    }
    
    public void setVendor(Vendor vendor) { 
        this.vendor = vendor; 
    }
    
    public BigDecimal getPrice() { 
        return price; 
    }
    
    public void setPrice(BigDecimal price) { 
        this.price = price; 
    }
    
    public Integer getQuantityInStock() { 
        return quantityInStock; 
    }
    
    public void setQuantityInStock(Integer quantityInStock) { 
        this.quantityInStock = quantityInStock; 
    }
    
    public Integer getMinimumStockLevel() { 
        return minimumStockLevel; 
    }
    
    public void setMinimumStockLevel(Integer minimumStockLevel) { 
        this.minimumStockLevel = minimumStockLevel; 
    }
    
    public String getProductCode() { 
        return productCode; 
    }
    
    public void setProductCode(String productCode) { 
        this.productCode = productCode; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) { 
        this.updatedAt = updatedAt; 
    }
    
    // Comparable interface for sorting (required for QuickSort in CustomList)
    @Override
    public int compareTo(Product other) {
        return this.name.compareToIgnoreCase(other.name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return productId != null && productId.equals(product.productId);
    }
    
    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                ", productCode='" + productCode + '\'' +
                '}';
    }
}