package com.wmc606.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WMC 606 Inventory Management System
 * Main Spring Boot Application Class
 * 
 * This application demonstrates the use of different data structures
 * for inventory management based on product categories:
 * - Categories 1-4: Stack (LIFO) - Beverages, Bread, Canned, Dairy
 * - Categories 5-7: Queue (FIFO) - Dry/Baking, Frozen, Meat
 * - Categories 8-11: List (Dynamic) - Produce, Cleaners, Paper, Personal Care
 */
@SpringBootApplication
public class InventoryManagementApplication {

    public static void main(String[] args) {
        System.out.println("🚀 Starting WMC 606 Inventory Management System...");
        System.out.println("📊 Data Structures Implementation:");
        System.out.println("   📚 Stack (LIFO): Categories 1-4");
        System.out.println("   🚶 Queue (FIFO): Categories 5-7");
        System.out.println("   📋 List (Dynamic): Categories 8-11");
        System.out.println("🌐 Access the application at: http://localhost:8080");
        
        SpringApplication.run(InventoryManagementApplication.class, args);
        
        System.out.println("✅ Application started successfully!");
        System.out.println("💻 Frontend: http://localhost:8080");
        System.out.println("🔗 API Base: http://localhost:8080/api");
    }
}
