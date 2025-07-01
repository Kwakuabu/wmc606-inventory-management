package com.wmc606.inventory.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Vendor Entity - Represents suppliers/vendors
 * Used in HashMap for vendor management as required by project
 */
@Entity
@Table(name = "vendors")
public class Vendor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Long vendorId;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(name = "contact_person", length = 100)
    private String contactPerson;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 100)
    private String email;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // One-to-Many relationship with Product
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
    
    // Constructors
    public Vendor() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Vendor(String name, String contactPerson, String phone, String email, String address) {
        this.name = name;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getVendorId() { 
        return vendorId; 
    }
    
    public void setVendorId(Long vendorId) { 
        this.vendorId = vendorId; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getContactPerson() { 
        return contactPerson; 
    }
    
    public void setContactPerson(String contactPerson) { 
        this.contactPerson = contactPerson; 
    }
    
    public String getPhone() { 
        return phone; 
    }
    
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getAddress() { 
        return address; 
    }
    
    public void setAddress(String address) { 
        this.address = address; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public List<Product> getProducts() { 
        return products; 
    }
    
    public void setProducts(List<Product> products) { 
        this.products = products; 
    }
    
    @Override
    public String toString() {
        return "Vendor{" +
                "vendorId=" + vendorId +
                ", name='" + name + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
