package com.wmc606.inventory.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Sale Entity - Represents issued goods/sales transactions
 * Used to track product sales for Map-based sales tracking
 */
@Entity
@Table(name = "sales")
public class Sale {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;
    
    // Many-to-One relationship with Product
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Column(name = "quantity_sold", nullable = false)
    private Integer quantitySold;
    
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "sale_date")
    private LocalDateTime saleDate;
    
    @Column(name = "customer_name", length = 100)
    private String customerName;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    // Constructors
    public Sale() {
        this.saleDate = LocalDateTime.now();
    }
    
    public Sale(Product product, Integer quantitySold, BigDecimal unitPrice, String customerName) {
        this.product = product;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantitySold));
        this.customerName = customerName;
        this.saleDate = LocalDateTime.now();
    }
    
    // Calculate total amount automatically
    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        if (unitPrice != null && quantitySold != null) {
            this.totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantitySold));
        }
    }
    
    // Getters and Setters
    public Long getSaleId() { 
        return saleId; 
    }
    
    public void setSaleId(Long saleId) { 
        this.saleId = saleId; 
    }
    
    public Product getProduct() { 
        return product; 
    }
    
    public void setProduct(Product product) { 
        this.product = product; 
    }
    
    public Integer getQuantitySold() { 
        return quantitySold; 
    }
    
    public void setQuantitySold(Integer quantitySold) { 
        this.quantitySold = quantitySold; 
    }
    
    public BigDecimal getUnitPrice() { 
        return unitPrice; 
    }
    
    public void setUnitPrice(BigDecimal unitPrice) { 
        this.unitPrice = unitPrice; 
    }
    
    public BigDecimal getTotalAmount() { 
        return totalAmount; 
    }
    
    public void setTotalAmount(BigDecimal totalAmount) { 
        this.totalAmount = totalAmount; 
    }
    
    public LocalDateTime getSaleDate() { 
        return saleDate; 
    }
    
    public void setSaleDate(LocalDateTime saleDate) { 
        this.saleDate = saleDate; 
    }
    
    public String getCustomerName() { 
        return customerName; 
    }
    
    public void setCustomerName(String customerName) { 
        this.customerName = customerName; 
    }
    
    public String getNotes() { 
        return notes; 
    }
    
    public void setNotes(String notes) { 
        this.notes = notes; 
    }
    
    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", product=" + (product != null ? product.getName() : "null") +
                ", quantitySold=" + quantitySold +
                ", totalAmount=" + totalAmount +
                ", customerName='" + customerName + '\'' +
                ", saleDate=" + saleDate +
                '}';
    }
}
