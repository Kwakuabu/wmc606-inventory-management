package com.wmc606.inventory.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wmc606.inventory.entities.Category;
import com.wmc606.inventory.entities.Product;
import com.wmc606.inventory.entities.Sale;
import com.wmc606.inventory.entities.Vendor;
import com.wmc606.inventory.repository.CategoryRepository;
import com.wmc606.inventory.repository.ProductRepository;
import com.wmc606.inventory.repository.VendorRepository;
import com.wmc606.inventory.service.InventoryManager;

/**
 * ProductController - REST API endpoints for product management
 * Handles operations using data structures based on product categories
 * FIXED VERSION with enhanced error handling and validation
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private InventoryManager inventoryManager;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired 
    private VendorRepository vendorRepository;
    
    /**
     * Get all products
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            System.out.println("🌐 API: Getting all products");
            List<Product> products = productRepository.findAll();
            System.out.println("✅ Found " + products.size() + " products");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("❌ Error getting products: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get product by ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            System.out.println("🌐 API: Getting product by ID: " + id);
            return productRepository.findById(id)
                    .map(product -> {
                        System.out.println("✅ Product found: " + product.getName());
                        return ResponseEntity.ok(product);
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Product not found with ID: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error getting product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Add new product with quantity (uses data structures) - ENHANCED ERROR HANDLING
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request) {
        try {
            System.out.println("🌐 API: Adding new product - DEBUG MODE");
            System.out.println("📦 Product name: " + request.getProduct().getName());
            System.out.println("📊 Quantity: " + request.getQuantity());
            
            // DEBUG: Check the incoming category data
            Product productData = request.getProduct();
            Category incomingCategory = productData.getCategory();
            
            System.out.println("🔍 DEBUG: Incoming category info:");
            if (incomingCategory != null) {
                System.out.println("   - Category ID from frontend: " + incomingCategory.getCategoryId());
                System.out.println("   - Category name from frontend: " + incomingCategory.getName());
                System.out.println("   - Category dataStructureType from frontend: " + incomingCategory.getDataStructureType());
            } else {
                System.out.println("   - ❌ Category is NULL from frontend!");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Category cannot be null"));
            }
            
            // DEBUG: Fetch the category from database
            Long categoryId = incomingCategory.getCategoryId();
            System.out.println("🔍 DEBUG: Looking up category ID " + categoryId + " in database...");
            
            // ENHANCED: Check for invalid category IDs
            if (categoryId == null || categoryId < 1 || categoryId > 11) {
                System.out.println("❌ DEBUG: Invalid category ID: " + categoryId + ". Valid range is 1-11");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid category ID: " + categoryId + ". Valid categories are 1-11", 
                               "validCategories", "1=Beverages, 2=Bread, 3=Canned, 4=Dairy, 5=Dry/Baking, 6=Frozen, 7=Meat, 8=Produce, 9=Cleaners, 10=Paper, 11=Personal Care"));
            }
            
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            if (!categoryOptional.isPresent()) {
                System.out.println("❌ DEBUG: Category not found in database with ID: " + categoryId);
                
                // ENHANCED: Show available categories for debugging
                System.out.println("🔍 DEBUG: Available categories in database:");
                categoryRepository.findAll().forEach(cat -> 
                    System.out.println("   - ID " + cat.getCategoryId() + ": " + cat.getName() + " (" + cat.getDataStructureType() + ")")
                );
                
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Category not found with ID: " + categoryId, 
                               "suggestion", "Please refresh the page and try again. Available categories: 1-11"));
            }
            
            Category dbCategory = categoryOptional.get();
            System.out.println("✅ DEBUG: Category found in database:");
            System.out.println("   - ID: " + dbCategory.getCategoryId());
            System.out.println("   - Name: " + dbCategory.getName());
            System.out.println("   - DataStructureType: " + dbCategory.getDataStructureType());
            System.out.println("   - Description: " + dbCategory.getDescription());
            
            // ENHANCED: Check if dataStructureType is null with better error message
            if (dbCategory.getDataStructureType() == null) {
                System.out.println("❌ DEBUG: DataStructureType is NULL in database category!");
                
                // Try to fix it automatically based on category ID
                Category.DataStructureType fixedType = null;
                if (categoryId >= 1 && categoryId <= 4) {
                    fixedType = Category.DataStructureType.STACK;
                } else if (categoryId >= 5 && categoryId <= 7) {
                    fixedType = Category.DataStructureType.QUEUE;
                } else if (categoryId >= 8 && categoryId <= 11) {
                    fixedType = Category.DataStructureType.LIST;
                }
                
                if (fixedType != null) {
                    System.out.println("🔧 DEBUG: Auto-fixing DataStructureType to: " + fixedType);
                    dbCategory.setDataStructureType(fixedType);
                    dbCategory = categoryRepository.save(dbCategory);
                    System.out.println("✅ DEBUG: DataStructureType fixed and saved");
                } else {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Category has null DataStructureType and cannot be auto-fixed. Category ID: " + categoryId));
                }
            }
            
            // Set the correct category from database
            productData.setCategory(dbCategory);
            
            // DEBUG: Verify vendor
            if (productData.getVendor() != null && productData.getVendor().getVendorId() != null) {
                Optional<Vendor> vendorOptional = vendorRepository.findById(productData.getVendor().getVendorId());
                if (!vendorOptional.isPresent()) {
                    System.out.println("❌ DEBUG: Vendor not found with ID: " + productData.getVendor().getVendorId());
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Vendor not found with ID: " + productData.getVendor().getVendorId()));
                }
                productData.setVendor(vendorOptional.get());
                System.out.println("✅ DEBUG: Vendor set: " + vendorOptional.get().getName());
            } else {
                System.out.println("❌ DEBUG: Vendor is null or has null ID");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Vendor cannot be null"));
            }
            
            System.out.println("🔍 DEBUG: About to call inventoryManager.addGoodsToInventory...");
            Product savedProduct = inventoryManager.addGoodsToInventory(productData, request.getQuantity());
            
            System.out.println("✅ DEBUG: Product added successfully with ID: " + savedProduct.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
            
        } catch (IllegalStateException e) {
            System.err.println("❌ DEBUG: IllegalStateException in addProduct: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Category data structure error", "message", e.getMessage(), 
                           "solution", "Please refresh the page and ensure you select a valid category (1-11)"));
            
        } catch (Exception e) {
            System.err.println("❌ DEBUG: Exception in addProduct: " + e.getClass().getSimpleName());
            System.err.println("❌ DEBUG: Exception message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add product", "message", e.getMessage(), "type", e.getClass().getSimpleName()));
        }
    }
    
    /**
     * Update product
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        try {
            System.out.println("🌐 API: Updating product ID: " + id);
            
            return productRepository.findById(id)
                    .map(product -> {
                        product.setName(productDetails.getName());
                        product.setPrice(productDetails.getPrice());
                        product.setDescription(productDetails.getDescription());
                        product.setMinimumStockLevel(productDetails.getMinimumStockLevel());
                        
                        Product updatedProduct = productRepository.save(product);
                        System.out.println("✅ Product updated: " + updatedProduct.getName());
                        return ResponseEntity.ok(updatedProduct);
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Product not found for update: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error updating product: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update product", "message", e.getMessage()));
        }
    }
    
    /**
     * Delete product
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            System.out.println("🌐 API: Deleting product ID: " + id);
            
            return productRepository.findById(id)
                    .map(product -> {
                        productRepository.delete(product);
                        System.out.println("✅ Product deleted: " + product.getName());
                        return ResponseEntity.ok()
                            .body(Map.of("message", "Product deleted successfully"));
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Product not found for deletion: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error deleting product: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete product", "message", e.getMessage()));
        }
    }
    
    /**
     * Get products by category
     * GET /api/products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            System.out.println("🌐 API: Getting products for category: " + categoryId);
            
            // Validate category ID range
            if (categoryId < 1 || categoryId > 11) {
                System.out.println("❌ Invalid category ID: " + categoryId);
                return ResponseEntity.badRequest().build();
            }
            
            List<Product> products = productRepository.findByCategoryCategoryId(categoryId);
            System.out.println("✅ Found " + products.size() + " products in category " + categoryId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("❌ Error getting products by category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Search products (uses custom algorithms for categories 6-11)
     * GET /api/products/search?searchTerm=...&categoryId=...
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String searchTerm,
            @RequestParam(required = false) Long categoryId) {
        try {
            System.out.println("🌐 API: Searching products");
            System.out.println("🔍 Search term: " + searchTerm);
            System.out.println("📂 Category: " + categoryId);
            
            // Validate category ID if provided
            if (categoryId != null && (categoryId < 1 || categoryId > 11)) {
                System.out.println("❌ Invalid category ID for search: " + categoryId);
                return ResponseEntity.badRequest().build();
            }
            
            List<Product> products;
            if (categoryId != null) {
                // Use custom search algorithms for specific categories
                products = inventoryManager.searchProducts(searchTerm, categoryId);
            } else {
                // Search all products
                products = productRepository.findByProductCodeContainingIgnoreCase(searchTerm);
            }
            
            System.out.println("✅ Search completed. Found " + products.size() + " products");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("❌ Error searching products: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Sort products alphabetically (uses QuickSort for categories 6-11)
     * GET /api/products/sort/{categoryId}
     */
    @GetMapping("/sort/{categoryId}")
    public ResponseEntity<List<Product>> sortProducts(@PathVariable Long categoryId) {
        try {
            System.out.println("🌐 API: Sorting products for category: " + categoryId);
            
            // Validate category ID range
            if (categoryId < 1 || categoryId > 11) {
                System.out.println("❌ Invalid category ID for sorting: " + categoryId);
                return ResponseEntity.badRequest().build();
            }
            
            List<Product> sortedProducts = inventoryManager.sortProductsAlphabetically(categoryId);
            System.out.println("✅ Sorted " + sortedProducts.size() + " products");
            return ResponseEntity.ok(sortedProducts);
        } catch (Exception e) {
            System.err.println("❌ Error sorting products: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get low stock products
     * GET /api/products/low-stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        try {
            System.out.println("🌐 API: Getting low stock products");
            List<Product> lowStockProducts = inventoryManager.getLowStockProducts();
            System.out.println("⚠️ Found " + lowStockProducts.size() + " low stock products");
            return ResponseEntity.ok(lowStockProducts);
        } catch (Exception e) {
            System.err.println("❌ Error getting low stock products: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Issue goods (make a sale) - uses data structure operations
     * POST /api/products/{id}/issue
     */
    @PostMapping("/{id}/issue")
    public ResponseEntity<?> issueGoods(@PathVariable Long id, @RequestBody IssueRequest request) {
        try {
            System.out.println("🌐 API: Issuing goods for product: " + id);
            System.out.println("📊 Quantity: " + request.getQuantity());
            System.out.println("👤 Customer: " + request.getCustomerName());
            
            // Validate input
            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Quantity must be greater than 0"));
            }
            
            if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Customer name is required"));
            }
            
            Sale sale = inventoryManager.issueGoods(id, request.getQuantity(), request.getCustomerName());
            
            System.out.println("✅ Goods issued successfully. Sale ID: " + sale.getSaleId());
            return ResponseEntity.ok(sale);
        } catch (RuntimeException e) {
            System.err.println("❌ Business error issuing goods: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ System error issuing goods: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "System error", "message", e.getMessage()));
        }
    }
    
    /**
     * Get data structure statistics
     * GET /api/products/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDataStructureStats() {
        try {
            System.out.println("🌐 API: Getting data structure statistics");
            Map<String, Object> stats = inventoryManager.getDataStructureStats();
            System.out.println("✅ Statistics retrieved");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("❌ Error getting statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get products by data structure type
     * GET /api/products/by-data-structure/{type}
     */
    @GetMapping("/by-data-structure/{type}")
    public ResponseEntity<List<Product>> getProductsByDataStructure(@PathVariable String type) {
        try {
            System.out.println("🌐 API: Getting products by data structure: " + type);
            
            Category.DataStructureType dataStructureType;
            try {
                dataStructureType = Category.DataStructureType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid data structure type: " + type);
                return ResponseEntity.badRequest().build();
            }
            
            List<Product> products = productRepository.findByDataStructureType(dataStructureType);
            System.out.println("✅ Found " + products.size() + " products with " + type + " data structure");
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("❌ Error getting products by data structure: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get product count by category
     * GET /api/products/count-by-category
     */
    @GetMapping("/count-by-category")
    public ResponseEntity<Map<String, Object>> getProductCountByCategory() {
        try {
            System.out.println("🌐 API: Getting product count by category");
            
            Map<String, Object> counts = Map.of(
                "stackProducts", productRepository.findStackProducts().size(),
                "queueProducts", productRepository.findQueueProducts().size(),
                "listProducts", productRepository.findListProducts().size()
            );
            
            System.out.println("✅ Product counts retrieved");
            return ResponseEntity.ok(counts);
        } catch (Exception e) {
            System.err.println("❌ Error getting product counts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Check product availability
     * GET /api/products/{id}/availability
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String, Object>> checkProductAvailability(@PathVariable Long id) {
        try {
            System.out.println("🌐 API: Checking availability for product: " + id);
            
            return productRepository.findById(id)
                    .map(product -> {
                        boolean isAvailable = product.getQuantityInStock() > 0;
                        boolean isLowStock = product.getQuantityInStock() <= product.getMinimumStockLevel();
                        
                        Map<String, Object> availability = Map.of(
                            "productId", product.getProductId(),
                            "productName", product.getName(),
                            "currentStock", product.getQuantityInStock(),
                            "minimumStockLevel", product.getMinimumStockLevel(),
                            "isAvailable", isAvailable,
                            "isLowStock", isLowStock,
                            "dataStructureType", product.getCategory().getDataStructureType().toString()
                        );
                        
                        System.out.println("✅ Availability checked for: " + product.getName());
                        return ResponseEntity.ok(availability);
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Product not found for availability check: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error checking product availability: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Helper classes for request bodies
    public static class ProductRequest {
        private Product product;
        private Integer quantity;
        
        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    
    public static class IssueRequest {
        private Integer quantity;
        private String customerName;
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
    }
}