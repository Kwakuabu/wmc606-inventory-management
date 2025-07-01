package com.wmc606.inventory.controller;

import com.wmc606.inventory.entities.Category;
import com.wmc606.inventory.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CategoryController - REST API endpoints for category management
 * Categories determine which data structure is used for products
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * Get all categories
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            System.out.println("🌐 API: Getting all categories");
            List<Category> categories = categoryRepository.findAll();
            System.out.println("✅ Found " + categories.size() + " categories");
            
            // Log data structure types for each category
            categories.forEach(category -> 
                System.out.println("📂 " + category.getName() + " → " + category.getDataStructureType())
            );
            
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            System.err.println("❌ Error getting categories: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get category by ID
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        try {
            System.out.println("🌐 API: Getting category by ID: " + id);
            return categoryRepository.findById(id)
                    .map(category -> {
                        System.out.println("✅ Category found: " + category.getName() + 
                                         " (" + category.getDataStructureType() + ")");
                        return ResponseEntity.ok(category);
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Category not found with ID: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error getting category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get categories by data structure type
     * GET /api/categories/data-structure/{type}
     */
    @GetMapping("/data-structure/{type}")
    public ResponseEntity<List<Category>> getCategoriesByDataStructure(@PathVariable String type) {
        try {
            System.out.println("🌐 API: Getting categories by data structure: " + type);
            
            Category.DataStructureType dataStructureType;
            try {
                dataStructureType = Category.DataStructureType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid data structure type: " + type);
                return ResponseEntity.badRequest().build();
            }
            
            List<Category> categories = categoryRepository.findByDataStructureType(dataStructureType);
            
            System.out.println("✅ Found " + categories.size() + " categories with " + type + " data structure");
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            System.err.println("❌ Error getting categories by data structure: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get Stack categories (1-4)
     * GET /api/categories/stack
     */
    @GetMapping("/stack")
    public ResponseEntity<List<Category>> getStackCategories() {
        try {
            System.out.println("🌐 API: Getting Stack categories (1-4)");
            List<Category> stackCategories = categoryRepository.findStackCategories();
            
            System.out.println("✅ Found " + stackCategories.size() + " Stack categories:");
            stackCategories.forEach(cat -> 
                System.out.println("📚 " + cat.getName() + " (LIFO operations)")
            );
            
            return ResponseEntity.ok(stackCategories);
        } catch (Exception e) {
            System.err.println("❌ Error getting Stack categories: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get Queue categories (5-7)
     * GET /api/categories/queue
     */
    @GetMapping("/queue")
    public ResponseEntity<List<Category>> getQueueCategories() {
        try {
            System.out.println("🌐 API: Getting Queue categories (5-7)");
            List<Category> queueCategories = categoryRepository.findQueueCategories();
            
            System.out.println("✅ Found " + queueCategories.size() + " Queue categories:");
            queueCategories.forEach(cat -> 
                System.out.println("🚶 " + cat.getName() + " (FIFO operations)")
            );
            
            return ResponseEntity.ok(queueCategories);
        } catch (Exception e) {
            System.err.println("❌ Error getting Queue categories: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get List categories (8-11)
     * GET /api/categories/list
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> getListCategories() {
        try {
            System.out.println("🌐 API: Getting List categories (8-11)");
            List<Category> listCategories = categoryRepository.findListCategories();
            
            System.out.println("✅ Found " + listCategories.size() + " List categories:");
            listCategories.forEach(cat -> 
                System.out.println("📋 " + cat.getName() + " (Dynamic operations)")
            );
            
            return ResponseEntity.ok(listCategories);
        } catch (Exception e) {
            System.err.println("❌ Error getting List categories: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Add new category
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        try {
            System.out.println("🌐 API: Adding new category");
            System.out.println("📂 Category: " + category.getName());
            System.out.println("🔧 Data Structure: " + category.getDataStructureType());
            
            // Check if category already exists
            if (categoryRepository.existsByName(category.getName())) {
                System.out.println("⚠️ Category already exists: " + category.getName());
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Category with this name already exists"));
            }
            
            Category savedCategory = categoryRepository.save(category);
            
            System.out.println("✅ Category added successfully with ID: " + savedCategory.getCategoryId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            System.err.println("❌ Error adding category: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add category", "message", e.getMessage()));
        }
    }
    
    /**
     * Update category
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        try {
            System.out.println("🌐 API: Updating category ID: " + id);
            
            return categoryRepository.findById(id)
                    .map(category -> {
                        category.setName(categoryDetails.getName());
                        category.setDescription(categoryDetails.getDescription());
                        category.setDataStructureType(categoryDetails.getDataStructureType());
                        
                        Category updatedCategory = categoryRepository.save(category);
                        
                        System.out.println("✅ Category updated: " + updatedCategory.getName() + 
                                         " → " + updatedCategory.getDataStructureType());
                        return ResponseEntity.ok(updatedCategory);
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Category not found for update: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error updating category: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update category", "message", e.getMessage()));
        }
    }
    
    /**
     * Get category statistics
     * GET /api/categories/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCategoryStats() {
        try {
            System.out.println("🌐 API: Getting category statistics");
            
            List<Category> allCategories = categoryRepository.findAll();
            List<Category> stackCategories = categoryRepository.findStackCategories();
            List<Category> queueCategories = categoryRepository.findQueueCategories();
            List<Category> listCategories = categoryRepository.findListCategories();
            
            Map<String, Object> stats = Map.of(
                "totalCategories", allCategories.size(),
                "stackCategories", stackCategories.size(),
                "queueCategories", queueCategories.size(),
                "listCategories", listCategories.size(),
                "dataStructureDistribution", Map.of(
                    "STACK", stackCategories.size(),
                    "QUEUE", queueCategories.size(),
                    "LIST", listCategories.size()
                )
            );
            
            System.out.println("✅ Category statistics retrieved");
            System.out.println("📊 Total categories: " + allCategories.size());
            System.out.println("📚 Stack categories: " + stackCategories.size());
            System.out.println("🚶 Queue categories: " + queueCategories.size());
            System.out.println("📋 List categories: " + listCategories.size());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("❌ Error getting category statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get data structure mapping information
     * GET /api/categories/data-structure-info
     */
    @GetMapping("/data-structure-info")
    public ResponseEntity<Map<String, Object>> getDataStructureInfo() {
        try {
            System.out.println("🌐 API: Getting data structure information");
            
            Map<String, Object> info = Map.of(
                "STACK", Map.of(
                    "description", "Last In, First Out (LIFO) operations",
                    "categories", "1-4: Beverages, Bread/Bakery, Canned/Jarred Goods, Dairy",
                    "operations", List.of("Push O(1)", "Pop O(1)", "Peek O(1)"),
                    "useCase", "Products added/removed from top (newest first)"
                ),
                "QUEUE", Map.of(
                    "description", "First In, First Out (FIFO) operations", 
                    "categories", "5-7: Dry/Baking Goods, Frozen Foods, Meat",
                    "operations", List.of("Enqueue O(1)", "Dequeue O(1)", "Front O(1)"),
                    "useCase", "Products added at rear, removed from front (oldest first)"
                ),
                "LIST", Map.of(
                    "description", "Dynamic array with search and sort capabilities",
                    "categories", "8-11: Produce, Cleaners, Paper Goods, Personal Care",
                    "operations", List.of("Add O(1)", "Remove O(n)", "Search O(n)", "Sort O(n log n)"),
                    "useCase", "Flexible operations including search and sort algorithms"
                )
            );
            
            System.out.println("✅ Data structure information retrieved");
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            System.err.println("❌ Error getting data structure information: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
