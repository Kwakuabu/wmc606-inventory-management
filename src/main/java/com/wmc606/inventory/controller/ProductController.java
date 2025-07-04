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
 * ProductController - Enhanced with debugging for DataStructureType issues
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
     * Add new product - ENHANCED WITH DEBUGGING
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request) {
        System.out.println("🚀 DEBUG: Starting addProduct...");
        
        try {
            // STEP 1: Validate request structure
            if (request == null) {
                System.err.println("❌ Request is null");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Request cannot be null"));
            }
            
            if (request.getProduct() == null) {
                System.err.println("❌ Product data is null");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Product data cannot be null"));
            }
            
            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                System.err.println("❌ Invalid quantity: " + request.getQuantity());
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Quantity must be greater than 0"));
            }
            
            Product productData = request.getProduct();
            System.out.println("📦 Product name: " + productData.getName());
            System.out.println("📊 Quantity: " + request.getQuantity());
            
            // STEP 2: Debug Category Information
            Category incomingCategory = productData.getCategory();
            if (incomingCategory == null) {
                System.err.println("❌ Category is null in request");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Category cannot be null"));
            }
            
            Long categoryId = incomingCategory.getCategoryId();
            System.out.println("🔍 DEBUG: Requested Category ID: " + categoryId);
            
            if (categoryId == null) {
                System.err.println("❌ Category ID is null");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Category ID cannot be null"));
            }
            
            // STEP 3: Validate Category ID Range
            if (categoryId < 1 || categoryId > 11) {
                System.err.println("❌ Invalid category ID range: " + categoryId);
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid category ID: " + categoryId + ". Valid categories are 1-11"));
            }
            
            // STEP 4: Fetch and DEBUG Category from Database
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            if (!categoryOptional.isPresent()) {
                System.err.println("❌ Category not found in database: " + categoryId);
                
                // DEBUG: Show what categories ARE available
                System.out.println("🔍 DEBUG: Available categories:");
                categoryRepository.findAll().forEach(cat -> 
                    System.out.println("   ID " + cat.getCategoryId() + ": " + cat.getName())
                );
                
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Category not found with ID: " + categoryId));
            }
            
            Category dbCategory = categoryOptional.get();
            System.out.println("✅ DEBUG: Category found in database:");
            System.out.println("   - ID: " + dbCategory.getCategoryId());
            System.out.println("   - Name: " + dbCategory.getName());
            
            // STEP 5: DEBUG DataStructureType specifically
            Category.DataStructureType dataStructureType = null;
            try {
                dataStructureType = dbCategory.getDataStructureType();
                System.out.println("   - DataStructureType: " + dataStructureType);
            } catch (Exception e) {
                System.err.println("💀 ERROR getting DataStructureType: " + e.getMessage());
                e.printStackTrace();
                
                // Try to fix it immediately
                Category.DataStructureType correctType = null;
                if (categoryId >= 1 && categoryId <= 4) {
                    correctType = Category.DataStructureType.STACK;
                } else if (categoryId >= 5 && categoryId <= 7) {
                    correctType = Category.DataStructureType.QUEUE;
                } else if (categoryId >= 8 && categoryId <= 11) {
                    correctType = Category.DataStructureType.LIST;
                }
                
                if (correctType != null) {
                    System.out.println("🔧 EMERGENCY FIX: Setting DataStructureType to " + correctType);
                    dbCategory.setDataStructureType(correctType);
                    dbCategory = categoryRepository.save(dbCategory);
                    dataStructureType = correctType;
                    System.out.println("✅ Emergency fix completed");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Cannot determine correct DataStructureType for category " + categoryId));
                }
            }
            
            if (dataStructureType == null) {
                System.err.println("💀 DataStructureType is still null after all checks!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "System error: Category data structure type is invalid"));
            }
            
            // STEP 6: Validate Vendor
            if (productData.getVendor() == null || productData.getVendor().getVendorId() == null) {
                System.err.println("❌ Vendor is null or has null ID");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Vendor must be selected"));
            }
            
            Optional<Vendor> vendorOptional = vendorRepository.findById(productData.getVendor().getVendorId());
            if (!vendorOptional.isPresent()) {
                System.err.println("❌ Vendor not found: " + productData.getVendor().getVendorId());
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Vendor not found with ID: " + productData.getVendor().getVendorId()));
            }
            
            // STEP 7: Set validated entities
            productData.setCategory(dbCategory);
            productData.setVendor(vendorOptional.get());
            
            System.out.println("✅ All validations passed. Adding product to inventory...");
            System.out.println("📂 Category: " + dbCategory.getName() + " (" + dataStructureType + ")");
            System.out.println("🏪 Vendor: " + vendorOptional.get().getName());
            
            // STEP 8: Add to inventory using InventoryManager
            Product savedProduct = inventoryManager.addGoodsToInventory(productData, request.getQuantity());
            
            System.out.println("🎉 Product added successfully!");
            System.out.println("   - Product ID: " + savedProduct.getProductId());
            System.out.println("   - Name: " + savedProduct.getName());
            System.out.println("   - Quantity: " + savedProduct.getQuantityInStock());
            System.out.println("   - Data Structure: " + dataStructureType);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
            
        } catch (IllegalStateException e) {
            System.err.println("💀 IllegalStateException in addProduct: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error", "Category configuration error", 
                    "message", e.getMessage(),
                    "solution", "Please refresh the page and ensure you select a valid category (1-11)"
                ));
            
        } catch (Exception e) {
            System.err.println("💀 Unexpected error in addProduct: " + e.getClass().getSimpleName());
            System.err.println("💀 Error message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "System error occurred", 
                    "message", e.getMessage(),
                    "type", e.getClass().getSimpleName(),
                    "solution", "Please try again or contact support if the problem persists"
                ));
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
     * Search products
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
            
            if (categoryId != null && (categoryId < 1 || categoryId > 11)) {
                System.out.println("❌ Invalid category ID for search: " + categoryId);
                return ResponseEntity.badRequest().build();
            }
            
            List<Product> products;
            if (categoryId != null) {
                products = inventoryManager.searchProducts(searchTerm, categoryId);
            } else {
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
     * Sort products alphabetically
     * GET /api/products/sort/{categoryId}
     */
    @GetMapping("/sort/{categoryId}")
    public ResponseEntity<List<Product>> sortProducts(@PathVariable Long categoryId) {
        try {
            System.out.println("🌐 API: Sorting products for category: " + categoryId);
            
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
     * Issue goods (make a sale)
     * POST /api/products/{id}/issue
     */
    @PostMapping("/{id}/issue")
    public ResponseEntity<?> issueGoods(@PathVariable Long id, @RequestBody IssueRequest request) {
        try {
            System.out.println("🌐 API: Issuing goods for product: " + id);
            System.out.println("📊 Quantity: " + request.getQuantity());
            System.out.println("👤 Customer: " + request.getCustomerName());
            
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