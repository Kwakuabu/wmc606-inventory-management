package com.wmc606.inventory.controller;

import com.wmc606.inventory.entities.Sale;
import com.wmc606.inventory.repository.SaleRepository;
import com.wmc606.inventory.service.InventoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * SalesController - REST API endpoints for sales management
 * Handles issued goods tracking and sales reporting
 */
@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SalesController {
    
    @Autowired
    private SaleRepository saleRepository;
    
    @Autowired
    private InventoryManager inventoryManager;
    
    /**
     * Get all sales
     * GET /api/sales
     */
    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        try {
            System.out.println("🌐 API: Getting all sales");
            List<Sale> sales = saleRepository.findAll();
            System.out.println("✅ Found " + sales.size() + " sales records");
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            System.err.println("❌ Error getting sales: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get sale by ID
     * GET /api/sales/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        try {
            System.out.println("🌐 API: Getting sale by ID: " + id);
            return saleRepository.findById(id)
                    .map(sale -> {
                        System.out.println("✅ Sale found: $" + sale.getTotalAmount() + 
                                         " to " + sale.getCustomerName());
                        return ResponseEntity.ok(sale);
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Sale not found with ID: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error getting sale: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get sales by date range
     * GET /api/sales/date-range?startDate=...&endDate=...
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Sale>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            System.out.println("🌐 API: Getting sales by date range");
            System.out.println("📅 From: " + startDate + " To: " + endDate);
            
            List<Sale> sales = saleRepository.findBySaleDateBetween(startDate, endDate);
            
            System.out.println("✅ Found " + sales.size() + " sales in date range");
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            System.err.println("❌ Error getting sales by date range: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get sales by customer
     * GET /api/sales/customer?name=...
     */
    @GetMapping("/customer")
    public ResponseEntity<List<Sale>> getSalesByCustomer(@RequestParam String name) {
        try {
            System.out.println("🌐 API: Getting sales by customer: " + name);
            List<Sale> sales = saleRepository.findByCustomerNameContainingIgnoreCase(name);
            System.out.println("✅ Found " + sales.size() + " sales for customer: " + name);
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            System.err.println("❌ Error getting sales by customer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get sales by product
     * GET /api/sales/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Sale>> getSalesByProduct(@PathVariable Long productId) {
        try {
            System.out.println("🌐 API: Getting sales by product: " + productId);
            List<Sale> sales = saleRepository.findByProductProductId(productId);
            System.out.println("✅ Found " + sales.size() + " sales for product: " + productId);
            return ResponseEntity.ok(sales);
        } catch (Exception e) {
            System.err.println("❌ Error getting sales by product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get sales summary (using Map-based tracking)
     * GET /api/sales/summary
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSalesSummary() {
        try {
            System.out.println("🌐 API: Getting sales summary with Map-based tracking");
            Map<String, Object> summary = inventoryManager.getSalesSummary();
            
            System.out.println("✅ Sales summary generated:");
            System.out.println("💰 Total Revenue: $" + summary.get("totalRevenue"));
            System.out.println("📦 Total Items Sold: " + summary.get("totalItemsSold"));
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            System.err.println("❌ Error getting sales summary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get product sales summary
     * GET /api/sales/product-summary
     */
    @GetMapping("/product-summary")
    public ResponseEntity<List<Object[]>> getProductSalesSummary() {
        try {
            System.out.println("🌐 API: Getting product sales summary");
            List<Object[]> summary = saleRepository.getProductSalesSummary();
            
            System.out.println("✅ Product sales summary generated for " + summary.size() + " products");
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            System.err.println("❌ Error getting product sales summary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get category sales summary
     * GET /api/sales/category-summary
     */
    @GetMapping("/category-summary")
    public ResponseEntity<List<Object[]>> getCategorySalesSummary() {
        try {
            System.out.println("🌐 API: Getting category sales summary");
            List<Object[]> summary = saleRepository.getCategorySalesSummary();
            
            System.out.println("✅ Category sales summary generated");
            summary.forEach(row -> 
                System.out.println("📂 " + row[0] + ": " + row[1] + " items, $" + row[2])
            );
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            System.err.println("❌ Error getting category sales summary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get top selling products
     * GET /api/sales/top-products
     */
    @GetMapping("/top-products")
    public ResponseEntity<List<Object[]>> getTopSellingProducts() {
        try {
            System.out.println("🌐 API: Getting top selling products");
            List<Object[]> topProducts = saleRepository.getTopSellingProducts();
            
            System.out.println("✅ Top selling products retrieved");
            return ResponseEntity.ok(topProducts);
        } catch (Exception e) {
            System.err.println("❌ Error getting top selling products: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get recent sales
     * GET /api/sales/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Sale>> getRecentSales() {
        try {
            System.out.println("🌐 API: Getting recent sales");
            List<Sale> recentSales = saleRepository.findRecentSales();
            
            // Limit to last 20 sales
            List<Sale> limitedSales = recentSales.size() > 20 ? 
                recentSales.subList(0, 20) : recentSales;
            
            System.out.println("✅ Retrieved " + limitedSales.size() + " recent sales");
            return ResponseEntity.ok(limitedSales);
        } catch (Exception e) {
            System.err.println("❌ Error getting recent sales: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get daily sales summary
     * GET /api/sales/daily-summary
     */
    @GetMapping("/daily-summary")
    public ResponseEntity<List<Object[]>> getDailySalesSummary() {
        try {
            System.out.println("🌐 API: Getting daily sales summary");
            List<Object[]> dailySummary = saleRepository.getDailySalesSummary();
            
            System.out.println("✅ Daily sales summary generated for " + dailySummary.size() + " days");
            return ResponseEntity.ok(dailySummary);
        } catch (Exception e) {
            System.err.println("❌ Error getting daily sales summary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get monthly sales summary
     * GET /api/sales/monthly-summary
     */
    @GetMapping("/monthly-summary")
    public ResponseEntity<List<Object[]>> getMonthlySalesSummary() {
        try {
            System.out.println("🌐 API: Getting monthly sales summary");
            List<Object[]> monthlySummary = saleRepository.getMonthlySalesSummary();
            
            System.out.println("✅ Monthly sales summary generated for " + monthlySummary.size() + " months");
            return ResponseEntity.ok(monthlySummary);
        } catch (Exception e) {
            System.err.println("❌ Error getting monthly sales summary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get sales statistics
     * GET /api/sales/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSalesStats() {
        try {
            System.out.println("🌐 API: Getting sales statistics");
            
            long totalSales = saleRepository.getTotalSalesCount();
            BigDecimal totalRevenue = saleRepository.getTotalRevenue();
            long totalItemsSold = saleRepository.getTotalItemsSold();
            
            Map<String, Object> stats = Map.of(
                "totalSales", totalSales,
                "totalRevenue", totalRevenue,
                "totalItemsSold", totalItemsSold,
                "averageSaleAmount", totalSales > 0 ? 
                    totalRevenue.divide(BigDecimal.valueOf(totalSales), 2, BigDecimal.ROUND_HALF_UP) : 
                    BigDecimal.ZERO
            );
            
            System.out.println("✅ Sales statistics retrieved");
            System.out.println("📊 Total sales: " + totalSales);
            System.out.println("💰 Total revenue: $" + totalRevenue);
            System.out.println("📦 Total items sold: " + totalItemsSold);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("❌ Error getting sales statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
