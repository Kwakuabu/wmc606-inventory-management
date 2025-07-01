package com.wmc606.inventory.repository;

import com.wmc606.inventory.entities.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * VendorRepository - Data access layer for Vendor entity
 * Used for HashMap vendor management as required by project
 */
@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    /**
     * Find vendor by name (case-insensitive)
     * @param name The vendor name
     * @return Optional containing the vendor if found
     */
    Optional<Vendor> findByNameIgnoreCase(String name);
    
    /**
     * Find vendors by name containing keyword (case-insensitive)
     * Used for search functionality
     * @param name The search keyword
     * @return List of vendors matching the search
     */
    List<Vendor> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find vendor by email
     * @param email The vendor email
     * @return Optional containing the vendor if found
     */
    Optional<Vendor> findByEmail(String email);
    
    /**
     * Find vendors by contact person
     * @param contactPerson The contact person name
     * @return List of vendors with specified contact person
     */
    List<Vendor> findByContactPersonContainingIgnoreCase(String contactPerson);
    
    /**
     * Find all vendors ordered by name
     * @return List of all vendors sorted by name
     */
    List<Vendor> findAllByOrderByNameAsc();
    
    /**
     * Get vendor count
     * @return Total number of vendors
     */
    @Query("SELECT COUNT(v) FROM Vendor v")
    long getTotalVendorCount();
    
    /**
     * Find vendors with products
     * @return List of vendors that have products
     */
    @Query("SELECT DISTINCT v FROM Vendor v WHERE SIZE(v.products) > 0")
    List<Vendor> findVendorsWithProducts();
    
    /**
     * Find vendors without products
     * @return List of vendors that have no products
     */
    @Query("SELECT v FROM Vendor v WHERE SIZE(v.products) = 0")
    List<Vendor> findVendorsWithoutProducts();
    
    /**
     * Search vendors by multiple criteria
     * @param searchTerm The search term to match against name, contact person, or email
     * @return List of vendors matching the search criteria
     */
    @Query("SELECT v FROM Vendor v WHERE " +
           "LOWER(v.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(v.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(v.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Vendor> searchVendors(@Param("searchTerm") String searchTerm);
    
    /**
     * Check if vendor name exists
     * @param name The vendor name to check
     * @return true if vendor exists, false otherwise
     */
    boolean existsByNameIgnoreCase(String name);
    
    /**
     * Check if vendor email exists
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
