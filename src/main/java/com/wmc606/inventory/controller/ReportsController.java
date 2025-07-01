package com.wmc606.inventory.controller;

import com.wmc606.inventory.service.InventoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ReportsController - REST API endpoints for reports and analytics
 * Includes performance analysis with Big O notation
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportsController {
    
    @Autowired
    private InventoryManager inventoryManager;
    
    /**
     * Get performance analysis with Big O notation
     * GET /api/reports/performance
     */
    @GetMapping("/performance")
    public ResponseEntity<Map<String, String>> getPerformanceAnalysis() {
        try {
            System.out.println("🌐 API: Getting performance analysis");
            Map<String, String> analysis = inventoryManager.getPerformanceAnalysis();
            
            System.out.println("✅ Performance analysis generated with " + analysis.size() + " metrics");
            System.out.println("⚡ Big O Notation Analysis:");
            analysis.forEach((operation, complexity) -> 
                System.out.println("  📊 " + operation + ": " + complexity)
            );
            
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            System.err.println("❌ Error getting performance analysis: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get low stock report
     * GET /api/reports/low-stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockReport() {
        try {
            System.out.println("🌐 API: Generating low stock report");
            var lowStockProducts = inventoryManager.getLowStockProducts();
            
            Map<String, Object> report = Map.of(
                "products", lowStockProducts,
                "count", lowStockProducts.size(),
                "message", "Products with stock below minimum level",
                "timestamp", java.time.LocalDateTime.now()
            );
            
            System.out.println("⚠️ Low stock report generated: " + lowStockProducts.size() + " products");
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            System.err.println("❌ Error generating low stock report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get sales summary report
     * GET /api/reports/sales-summary
     */
    @GetMapping("/sales-summary")
    public ResponseEntity<Map<String, Object>> getSalesSummaryReport() {
        try {
            System.out.println("🌐 API: Generating sales summary report");
            Map<String, Object> summary = inventoryManager.getSalesSummary();
            
            // Add report metadata
            summary.put("reportType", "Sales Summary");
            summary.put("generatedAt", java.time.LocalDateTime.now());
            summary.put("reportDescription", "Complete sales analysis with Map-based tracking");
            
            System.out.println("📈 Sales summary report generated");
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            System.err.println("❌ Error generating sales summary report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get data structure statistics report
     * GET /api/reports/data-structures
     */
    @GetMapping("/data-structures")
    public ResponseEntity<Map<String, Object>> getDataStructureReport() {
        try {
            System.out.println("🌐 API: Generating data structure statistics report");
            Map<String, Object> stats = inventoryManager.getDataStructureStats();
            
            // Add detailed analysis
            stats.put("reportType", "Data Structure Analysis");
            stats.put("generatedAt", java.time.LocalDateTime.now());
            stats.put("analysis", Map.of(
                "stackUsage", "Categories 1-4: LIFO operations for Beverages, Bread, Canned, Dairy",
                "queueUsage", "Categories 5-7: FIFO operations for Dry/Baking, Frozen, Meat",
                "listUsage", "Categories 8-11: Dynamic operations for Produce, Cleaners, Paper, Personal Care",
                "hashMapUsage", "Vendor management and sales tracking as required by project"
            ));
            
            System.out.println("📊 Data structure report generated");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("❌ Error generating data structure report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get comprehensive system report
     * GET /api/reports/system-overview
     */
    @GetMapping("/system-overview")
    public ResponseEntity<Map<String, Object>> getSystemOverviewReport() {
        try {
            System.out.println("🌐 API: Generating comprehensive system overview report");
            
            // Gather all statistics
            var salesSummary = inventoryManager.getSalesSummary();
            var lowStockProducts = inventoryManager.getLowStockProducts();
            var dataStructureStats = inventoryManager.getDataStructureStats();
            var performanceAnalysis = inventoryManager.getPerformanceAnalysis();
            
            Map<String, Object> systemReport = Map.of(
                "reportType", "Comprehensive System Overview",
                "generatedAt", java.time.LocalDateTime.now(),
                "salesSummary", salesSummary,
                "lowStockAlert", Map.of(
                    "count", lowStockProducts.size(),
                    "products", lowStockProducts
                ),
                "dataStructureStats", dataStructureStats,
                "performanceAnalysis", performanceAnalysis,
                "systemHealth", Map.of(
                    "status", lowStockProducts.size() > 10 ? "WARNING" : "GOOD",
                    "message", lowStockProducts.size() > 10 ? 
                        "Multiple products below minimum stock" : "Inventory levels adequate"
                )
            );
            
            System.out.println("✅ Comprehensive system report generated");
            return ResponseEntity.ok(systemReport);
        } catch (Exception e) {
            System.err.println("❌ Error generating system overview report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get algorithm complexity report
     * GET /api/reports/algorithms
     */
    @GetMapping("/algorithms")
    public ResponseEntity<Map<String, Object>> getAlgorithmReport() {
        try {
            System.out.println("🌐 API: Generating algorithm complexity report");
            
            Map<String, Object> algorithmReport = Map.of(
                "reportType", "Algorithm Complexity Analysis",
                "generatedAt", java.time.LocalDateTime.now(),
                "dataStructureComplexity", Map.of(
                    "Stack Operations", Map.of(
                        "push", "O(1)",
                        "pop", "O(1)", 
                        "peek", "O(1)",
                        "categories", "1-4 (Beverages, Bread, Canned, Dairy)"
                    ),
                    "Queue Operations", Map.of(
                        "enqueue", "O(1)",
                        "dequeue", "O(1)",
                        "front", "O(1)",
                        "categories", "5-7 (Dry/Baking, Frozen, Meat)"
                    ),
                    "List Operations", Map.of(
                        "add", "O(1)",
                        "remove", "O(n)",
                        "search", "O(n)",
                        "get", "O(1)",
                        "categories", "8-11 (Produce, Cleaners, Paper, Personal Care)"
                    )
                ),
                "searchAlgorithms", Map.of(
                    "Linear Search", Map.of(
                        "complexity", "O(n)",
                        "usage", "Categories 6-11 product search",
                        "spaceComplexity", "O(1)"
                    ),
                    "Binary Search", Map.of(
                        "complexity", "O(log n)",
                        "usage", "Sorted lists (available but not actively used)",
                        "spaceComplexity", "O(log n) due to recursion"
                    )
                ),
                "sortingAlgorithms", Map.of(
                    "QuickSort", Map.of(
                        "averageComplexity", "O(n log n)",
                        "worstComplexity", "O(n²)",
                        "usage", "Categories 6-11 alphabetical sorting",
                        "spaceComplexity", "O(log n)"
                    ),
                    "MergeSort", Map.of(
                        "complexity", "O(n log n) guaranteed",
                        "usage", "Alternative sorting (available)",
                        "spaceComplexity", "O(n)"
                    )
                ),
                "hashMapOperations", Map.of(
                    "get", "O(1) average",
                    "put", "O(1) average",
                    "usage", "Vendor management and sales tracking",
                    "spaceComplexity", "O(n)"
                )
            );
            
            System.out.println("⚡ Algorithm complexity report generated");
            return ResponseEntity.ok(algorithmReport);
        } catch (Exception e) {
            System.err.println("❌ Error generating algorithm report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
