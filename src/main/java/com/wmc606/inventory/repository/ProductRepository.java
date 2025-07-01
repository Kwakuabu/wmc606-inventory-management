package com.wmc606.inventory.repository;

import com.wmc606.inventory.entities.Product;
import com.wmc606.inventory.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ProductRepository - Data access layer for Product entity
 * Supports different data structure operations based on category
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Find product by product code
     * @param productCode The unique product code
     * @return Optional containing the product if found
     */
    Optional<Product> findByProductCode(String productCode);
    
    /**
     * Find products by category ID
     * @param categoryId The category ID
     * @return List of products in the specified category
     */
    List<Product> findByCategoryCategoryId(Long categoryId);
    
    /**
     * Find products by category ID ordered by name (for sorting in categories 6-11)
     * @param categoryId The category ID
     * @return List of products sorted alphabetically by name
     */
    List<Product> findByCategoryCategoryIdOrderByNameAsc(Long categoryId);
    
    /**
     * Find products by vendor ID
     * @param vendorId The vendor ID
     * @return List of products from the specified vendor
     */
    List<Product> findByVendorVendorId(Long vendorId);
    
    /**
     * Find products by name containing keyword (case-insensitive)
     * @param name The search keyword
     * @return List of products matching the search
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products by product code containing keyword (case-insensitive)
     * @param productCode The search keyword
     * @return List of products matching the product code search
     */
    List<Product> findByProductCodeContainingIgnoreCase(String productCode);
    
    /**
     * Search products in specific category
     * @param searchTerm The search term
     * @param categoryId The category ID
     * @return List of products matching search in specified category
     */
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.productCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Product> findByNameContainingIgnoreCaseAndCategoryCategoryId(
        @Param("searchTerm") String searchTerm, 
        @Param("categoryId") Long categoryId);
    
    /**
     * Find low stock products (quantity <= minimum stock level)
     * Critical for inventory management
     * @return List of products that need restocking
     */
    @Query("SELECT p FROM Product p WHERE p.quantityInStock <= p.minimumStockLevel")
    List<Product> findLowStockProducts();
    
    /**
     * Find products by category data structure type
     * @param dataStructureType The data structure type (STACK, QUEUE, LIST)
     * @return List of products in categories using specified data structure
     */
    @Query("SELECT p FROM Product p WHERE p.category.dataStructureType = :dataStructureType")
    List<Product> findByDataStructureType(@Param("dataStructureType") Category.DataStructureType dataStructureType);
    
    /**
     * Find Stack category products (Categories 1-4)
     * @return List of products managed with Stack data structure
     */
    @Query("SELECT p FROM Product p WHERE p.category.dataStructureType = 'STACK'")
    List<Product> findStackProducts();
    
    /**
     * Find Queue category products (Categories 5-7)
     * @return List of products managed with Queue data structure
     */
    @Query("SELECT p FROM Product p WHERE p.category.dataStructureType = 'QUEUE'")
    List<Product> findQueueProducts();
    
    /**
     * Find List category products (Categories 8-11)
     * @return List of products managed with List data structure
     */
    @Query("SELECT p FROM Product p WHERE p.category.dataStructureType = 'LIST'")
    List<Product> findListProducts();
    
    /**
     * Find products by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of products within price range
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find products with stock above minimum level
     * @return List of well-stocked products
     */
    @Query("SELECT p FROM Product p WHERE p.quantityInStock > p.minimumStockLevel")
    List<Product> findWellStockedProducts();
    
    /**
     * Get total product count
     * @return Total number of products
     */
    @Query("SELECT COUNT(p) FROM Product p")
    long getTotalProductCount();
    
    /**
     * Get total inventory value
     * @return Sum of (price * quantity) for all products
     */
    @Query("SELECT SUM(p.price * p.quantityInStock) FROM Product p")
    BigDecimal getTotalInventoryValue();
    
    /**
     * Find products by category and stock status
     * @param categoryId The category ID
     * @param belowMinimum true to find products below minimum stock, false for above
     * @return List of products matching criteria
     */
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId AND " +
           "(:belowMinimum = true AND p.quantityInStock <= p.minimumStockLevel) OR " +
           "(:belowMinimum = false AND p.quantityInStock > p.minimumStockLevel)")
    List<Product> findByCategoryAndStockStatus(@Param("categoryId") Long categoryId, 
                                              @Param("belowMinimum") boolean belowMinimum);
    
    /**
     * Check if product code exists
     * @param productCode The product code to check
     * @return true if product code exists, false otherwise
     */
    boolean existsByProductCode(String productCode);
}
