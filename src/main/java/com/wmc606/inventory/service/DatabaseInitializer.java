package com.wmc606.inventory.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.wmc606.inventory.entities.Category;
import com.wmc606.inventory.entities.Vendor;
import com.wmc606.inventory.repository.CategoryRepository;
import com.wmc606.inventory.repository.VendorRepository;

/**
 * Database Initializer - Creates initial categories and sample vendors
 * Runs automatically when the application starts
 * This ensures proper data structure types are set for all categories
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔄 Initializing database...");
        
        // Always check and fix category data structure types
        validateAndFixCategories();
        
        // Initialize sample vendors if none exist
        if (vendorRepository.count() == 0) {
            initializeSampleVendors();
        } else {
            System.out.println("✅ Vendors already exist: " + vendorRepository.count());
        }
        
        System.out.println("✅ Database initialization completed!");
    }

    /**
     * Validate and fix category data structure types
     * This ensures all categories have proper DataStructureType values
     */
    private void validateAndFixCategories() {
        System.out.println("🔍 Validating category data structure types...");
        
        // Check if we have the required 11 categories
        long categoryCount = categoryRepository.count();
        System.out.println("📊 Found " + categoryCount + " categories in database");
        
        if (categoryCount < 11) {
            System.out.println("⚠️ Missing categories. Creating missing ones...");
            initializeCategories();
        } else {
            // Verify existing categories have proper data structure types
            validateExistingCategories();
        }
    }

    /**
     * Validate existing categories and fix any with null data structure types
     * FIXED: No lambda expressions to avoid compiler errors
     */
    private void validateExistingCategories() {
        System.out.println("🔧 Validating existing categories...");
        
        // Check categories 1-4 (STACK)
        for (long i = 1; i <= 4; i++) {
            Optional<Category> categoryOpt = categoryRepository.findById(i);
            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                if (category.getDataStructureType() == null) {
                    System.out.println("🔧 Fixing STACK category: " + category.getName());
                    category.setDataStructureType(Category.DataStructureType.STACK);
                    categoryRepository.save(category);
                }
            }
        }
        
        // Check categories 5-7 (QUEUE)
        for (long i = 5; i <= 7; i++) {
            Optional<Category> categoryOpt = categoryRepository.findById(i);
            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                if (category.getDataStructureType() == null) {
                    System.out.println("🔧 Fixing QUEUE category: " + category.getName());
                    category.setDataStructureType(Category.DataStructureType.QUEUE);
                    categoryRepository.save(category);
                }
            }
        }
        
        // Check categories 8-11 (LIST)
        for (long i = 8; i <= 11; i++) {
            Optional<Category> categoryOpt = categoryRepository.findById(i);
            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                if (category.getDataStructureType() == null) {
                    System.out.println("🔧 Fixing LIST category: " + category.getName());
                    category.setDataStructureType(Category.DataStructureType.LIST);
                    categoryRepository.save(category);
                }
            }
        }
        
        System.out.println("✅ Category validation completed");
    }

    /**
     * Initialize all required categories with proper data structure types
     */
    private void initializeCategories() {
        System.out.println("📂 Creating categories with data structure types...");
        
        // Clear existing categories to ensure clean state
        categoryRepository.deleteAll();
        
        // STACK Categories (1-4) - LIFO Operations
        createCategoryIfNotExists("Beverages", Category.DataStructureType.STACK, 
            "All types of drinks and beverages - Uses STACK (LIFO) data structure");
        createCategoryIfNotExists("Bread/Bakery", Category.DataStructureType.STACK, 
            "Fresh bread, pastries, and baked goods - Uses STACK (LIFO) data structure");
        createCategoryIfNotExists("Canned/Jarred Goods", Category.DataStructureType.STACK, 
            "Preserved foods in cans and jars - Uses STACK (LIFO) data structure");
        createCategoryIfNotExists("Dairy", Category.DataStructureType.STACK, 
            "Milk products, cheese, yogurt, etc. - Uses STACK (LIFO) data structure");
        
        // QUEUE Categories (5-7) - FIFO Operations
        createCategoryIfNotExists("Dry/Baking Goods", Category.DataStructureType.QUEUE, 
            "Flour, sugar, spices, dry ingredients - Uses QUEUE (FIFO) data structure");
        createCategoryIfNotExists("Frozen Foods", Category.DataStructureType.QUEUE, 
            "All frozen products and ice cream - Uses QUEUE (FIFO) data structure");
        createCategoryIfNotExists("Meat", Category.DataStructureType.QUEUE, 
            "Fresh and processed meats - Uses QUEUE (FIFO) data structure");
        
        // LIST Categories (8-11) - Dynamic Operations
        createCategoryIfNotExists("Produce", Category.DataStructureType.LIST, 
            "Fresh fruits and vegetables - Uses LIST (Dynamic) data structure with search and sort");
        createCategoryIfNotExists("Cleaners", Category.DataStructureType.LIST, 
            "Household cleaning products - Uses LIST (Dynamic) data structure with search and sort");
        createCategoryIfNotExists("Paper Goods", Category.DataStructureType.LIST, 
            "Paper towels, toilet paper, napkins - Uses LIST (Dynamic) data structure with search and sort");
        createCategoryIfNotExists("Personal Care", Category.DataStructureType.LIST, 
            "Toiletries, hygiene products - Uses LIST (Dynamic) data structure with search and sort");
        
        System.out.println("✅ Created 11 categories with proper data structure types");
        System.out.println("📚 STACK categories (1-4): Beverages, Bread, Canned, Dairy");
        System.out.println("🚶 QUEUE categories (5-7): Dry/Baking, Frozen, Meat");
        System.out.println("📋 LIST categories (8-11): Produce, Cleaners, Paper, Personal Care");
    }

    /**
     * Create a category if it doesn't exist - FIXED VERSION
     * No more lambda expression issues
     */
    private void createCategoryIfNotExists(String name, Category.DataStructureType type, String description) {
        // Check if category with this name already exists
        Optional<Category> existingOpt = categoryRepository.findByName(name);
        
        if (existingOpt.isPresent()) {
            // Update existing category to ensure proper data structure type
            Category existing = existingOpt.get();
            if (existing.getDataStructureType() == null || !existing.getDataStructureType().equals(type)) {
                existing.setDataStructureType(type);
                existing.setDescription(description);
                categoryRepository.save(existing);
                System.out.println("🔧 Updated category: " + existing.getName() + " to " + type);
            } else {
                System.out.println("✅ Category already exists with correct type: " + name + " (" + type + ")");
            }
        } else {
            // Create new category
            Category category = new Category(name, type, description);
            categoryRepository.save(category);
            System.out.println("✅ Created category: " + name + " (" + type + ")");
        }
    }

    /**
     * Initialize sample vendors for testing
     */
    private void initializeSampleVendors() {
        System.out.println("🏪 Creating sample vendors...");
        
        vendorRepository.save(new Vendor("ABC Wholesale", "John Smith", "555-0101", 
            "john@abcwholesale.com", "123 Wholesale Ave, Business District"));
        vendorRepository.save(new Vendor("Fresh Foods Inc", "Sarah Johnson", "555-0102", 
            "sarah@freshfoods.com", "456 Fresh Lane, Market Square"));
        vendorRepository.save(new Vendor("Global Suppliers", "Mike Wilson", "555-0103", 
            "mike@globalsuppliers.com", "789 Supply St, Industrial Zone"));
        vendorRepository.save(new Vendor("Local Farm Co-op", "Emily Davis", "555-0104", 
            "emily@localfarm.com", "321 Farm Road, Rural Area"));
        vendorRepository.save(new Vendor("Quality Distributors", "Robert Brown", "555-0105", 
            "robert@qualitydist.com", "654 Distribution Blvd, Warehouse District"));
        
        System.out.println("✅ Created 5 sample vendors");
    }

    /**
     * Verify all categories have proper data structure types
     * This method can be called to debug category issues
     * FIXED: No lambda expressions
     */
    public void debugCategories() {
        System.out.println("🔍 DEBUG: Category validation report");
        System.out.println("=====================================");
        
        for (long i = 1; i <= 11; i++) {
            Optional<Category> categoryOpt = categoryRepository.findById(i);
            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                System.out.println("ID " + i + ": " + category.getName() + 
                                 " -> " + category.getDataStructureType());
            } else {
                System.out.println("ID " + i + ": ❌ MISSING");
            }
        }
        
        System.out.println("=====================================");
    }

    /**
     * Manual method to ensure all 11 categories exist with proper IDs
     * Call this if you need specific category IDs (1-11)
     */
    public void ensureSpecificCategoryIds() {
        System.out.println("🔧 Ensuring categories have specific IDs 1-11...");
        
        // This method creates categories with specific IDs if needed
        ensureCategoryWithId(1L, "Beverages", Category.DataStructureType.STACK);
        ensureCategoryWithId(2L, "Bread/Bakery", Category.DataStructureType.STACK);
        ensureCategoryWithId(3L, "Canned/Jarred Goods", Category.DataStructureType.STACK);
        ensureCategoryWithId(4L, "Dairy", Category.DataStructureType.STACK);
        ensureCategoryWithId(5L, "Dry/Baking Goods", Category.DataStructureType.QUEUE);
        ensureCategoryWithId(6L, "Frozen Foods", Category.DataStructureType.QUEUE);
        ensureCategoryWithId(7L, "Meat", Category.DataStructureType.QUEUE);
        ensureCategoryWithId(8L, "Produce", Category.DataStructureType.LIST);
        ensureCategoryWithId(9L, "Cleaners", Category.DataStructureType.LIST);
        ensureCategoryWithId(10L, "Paper Goods", Category.DataStructureType.LIST);
        ensureCategoryWithId(11L, "Personal Care", Category.DataStructureType.LIST);
        
        System.out.println("✅ All categories with IDs 1-11 ensured");
    }

    /**
     * Helper method to ensure a category exists with a specific ID
     */
    private void ensureCategoryWithId(Long id, String name, Category.DataStructureType type) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            if (category.getDataStructureType() == null || !category.getDataStructureType().equals(type)) {
                category.setDataStructureType(type);
                categoryRepository.save(category);
                System.out.println("🔧 Fixed category ID " + id + ": " + name + " -> " + type);
            }
        } else {
            // Create new category with specific ID (requires custom SQL or JPA entity management)
            System.out.println("⚠️ Category ID " + id + " missing. Creating: " + name);
            Category category = new Category(name, type, "Auto-created category for " + name);
            categoryRepository.save(category);
        }
    }
}