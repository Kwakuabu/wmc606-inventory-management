package com.wmc606.inventory.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wmc606.inventory.entities.Sale;

/**
 * SaleRepository - Data access layer for Sale entity
 * Supports sales tracking and reporting for Map-based sales analysis
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    /**
     * Find sales by product ID
     * @param productId The product ID
     * @return List of sales for the specified product
     */
    List<Sale> findByProductProductId(Long productId);
    
    /**
     * Find sales by customer name (case-insensitive)
     * @param customerName The customer name
     * @return List of sales for the specified customer
     */
    List<Sale> findByCustomerNameContainingIgnoreCase(String customerName);
    
    /**
     * Find sales within date range
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of sales within the specified date range
     */
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find sales for today
     * @param startOfDay Start of today (00:00:00)
     * @param endOfDay End of today (23:59:59)
     * @return List of today's sales
     */
    @Query("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startOfDay AND :endOfDay")
    List<Sale> findTodaysSales(@Param("startOfDay") LocalDateTime startOfDay, 
                              @Param("endOfDay") LocalDateTime endOfDay);
    
    /**
     * Find recent sales (last N records)
     * @return List of recent sales ordered by date descending
     */
    @Query("SELECT s FROM Sale s ORDER BY s.saleDate DESC")
    List<Sale> findRecentSales();
    
    /**
     * Get sales summary - total quantity and amount - FIXED VERSION
     * @return Object array containing [totalQuantity, totalAmount]
     */
    @Query("SELECT COALESCE(SUM(s.quantitySold), 0), COALESCE(SUM(s.totalAmount), 0.0) FROM Sale s")
    Object[] getSalesSummary();
    
    /**
     * Get product sales summary - grouped by product
     * @return List of Object arrays containing [productName, totalQuantity, totalAmount]
     */
    @Query("SELECT s.product.name, SUM(s.quantitySold), SUM(s.totalAmount) " +
           "FROM Sale s GROUP BY s.product.productId, s.product.name " +
           "ORDER BY SUM(s.totalAmount) DESC")
    List<Object[]> getProductSalesSummary();
    
    /**
     * Get category sales summary - grouped by category
     * @return List of Object arrays containing [categoryName, totalQuantity, totalAmount]
     */
    @Query("SELECT s.product.category.name, SUM(s.quantitySold), SUM(s.totalAmount) " +
           "FROM Sale s GROUP BY s.product.category.categoryId, s.product.category.name " +
           "ORDER BY SUM(s.totalAmount) DESC")
    List<Object[]> getCategorySalesSummary();
    
    /**
     * Get sales by data structure type
     * @param dataStructureType The data structure type
     * @return List of Object arrays containing sales data for specific data structure
     */
    @Query("SELECT s.product.category.dataStructureType, SUM(s.quantitySold), SUM(s.totalAmount) " +
           "FROM Sale s WHERE s.product.category.dataStructureType = :dataStructureType " +
           "GROUP BY s.product.category.dataStructureType")
    Object[] getSalesByDataStructureType(@Param("dataStructureType") String dataStructureType);
    
    /**
     * Get top selling products
     * @return List of Object arrays containing [productName, totalQuantity, totalAmount]
     */
    @Query("SELECT s.product.name, SUM(s.quantitySold), SUM(s.totalAmount) " +
           "FROM Sale s GROUP BY s.product.productId, s.product.name " +
           "ORDER BY SUM(s.quantitySold) DESC")
    List<Object[]> getTopSellingProducts();
    
    /**
     * Get revenue by date range
     * @param startDate Start date
     * @param endDate End date
     * @return Total revenue for the date range
     */
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0.0) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal getRevenueByDateRange(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
    
    /**
     * Get total sales count
     * @return Total number of sales transactions
     */
    @Query("SELECT COUNT(s) FROM Sale s")
    long getTotalSalesCount();
    
    /**
     * Get total revenue - FIXED VERSION
     * @return Total revenue from all sales
     */
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0.0) FROM Sale s")
    BigDecimal getTotalRevenue();
    
    /**
     * Get total items sold - FIXED VERSION
     * @return Total quantity of items sold
     */
    @Query("SELECT COALESCE(SUM(s.quantitySold), 0) FROM Sale s")
    Long getTotalItemsSold();
    
    /**
     * Find sales above amount threshold
     * @param amount Minimum sale amount
     * @return List of high-value sales
     */
    List<Sale> findByTotalAmountGreaterThan(BigDecimal amount);
    
    /**
     * Get daily sales summary for reporting
     * @return List of Object arrays containing [date, totalSales, totalAmount]
     */
    @Query("SELECT DATE(s.saleDate), COUNT(s), COALESCE(SUM(s.totalAmount), 0.0) " +
           "FROM Sale s GROUP BY DATE(s.saleDate) ORDER BY DATE(s.saleDate) DESC")
    List<Object[]> getDailySalesSummary();
    
    /**
     * Get monthly sales summary
     * @return List of Object arrays containing [month, year, totalSales, totalAmount]
     */
    @Query("SELECT MONTH(s.saleDate), YEAR(s.saleDate), COUNT(s), COALESCE(SUM(s.totalAmount), 0.0) " +
           "FROM Sale s GROUP BY YEAR(s.saleDate), MONTH(s.saleDate) " +
           "ORDER BY YEAR(s.saleDate) DESC, MONTH(s.saleDate) DESC")
    List<Object[]> getMonthlySalesSummary();
    
    /**
     * Get average sale amount
     * @return Average sale amount
     */
    @Query("SELECT COALESCE(AVG(s.totalAmount), 0.0) FROM Sale s")
    BigDecimal getAverageSaleAmount();
    
    /**
     * Get sales count for today
     * @return Number of sales made today
     */
    @Query("SELECT COUNT(s) FROM Sale s WHERE DATE(s.saleDate) = CURDATE()")
    long getTodaySalesCount();
    
    /**
     * Get today's revenue
     * @return Total revenue for today
     */
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0.0) FROM Sale s WHERE DATE(s.saleDate) = CURDATE()")
    BigDecimal getTodaysRevenue();
    
    /**
     * Get sales count by customer
     * @param customerName Customer name
     * @return Number of sales for the customer
     */
    @Query("SELECT COUNT(s) FROM Sale s WHERE LOWER(s.customerName) = LOWER(:customerName)")
    long getSalesCountByCustomer(@Param("customerName") String customerName);
    
    /**
     * Get total amount spent by customer
     * @param customerName Customer name
     * @return Total amount spent by the customer
     */
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0.0) FROM Sale s WHERE LOWER(s.customerName) = LOWER(:customerName)")
    BigDecimal getTotalAmountByCustomer(@Param("customerName") String customerName);
    
    /**
     * Get most recent sale
     * @return The most recent sale
     */
    @Query("SELECT s FROM Sale s ORDER BY s.saleDate DESC")
    List<Sale> getMostRecentSale();
    
    /**
     * Get sales statistics for a specific product
     * @param productId Product ID
     * @return Object array containing [totalQuantity, totalAmount, salesCount]
     */
    @Query("SELECT COALESCE(SUM(s.quantitySold), 0), COALESCE(SUM(s.totalAmount), 0.0), COUNT(s) " +
           "FROM Sale s WHERE s.product.productId = :productId")
    Object[] getProductSalesStats(@Param("productId") Long productId);
    
    /**
     * Get best selling product
     * @return Object array containing [productName, totalQuantity, totalAmount]
     */
    @Query("SELECT s.product.name, SUM(s.quantitySold), SUM(s.totalAmount) " +
           "FROM Sale s GROUP BY s.product.productId, s.product.name " +
           "ORDER BY SUM(s.quantitySold) DESC")
    List<Object[]> getBestSellingProduct();
    
    /**
     * Get sales for the current month
     * @return List of sales for current month
     */
    @Query("SELECT s FROM Sale s WHERE MONTH(s.saleDate) = MONTH(CURDATE()) AND YEAR(s.saleDate) = YEAR(CURDATE())")
    List<Sale> getCurrentMonthSales();
    
    /**
     * Get sales for the current week
     * @return List of sales for current week
     */
    @Query("SELECT s FROM Sale s WHERE YEARWEEK(s.saleDate) = YEARWEEK(CURDATE())")
    List<Sale> getCurrentWeekSales();
}