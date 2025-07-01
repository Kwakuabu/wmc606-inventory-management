package com.wmc606.inventory.controller;

import com.wmc606.inventory.entities.Product;
import com.wmc606.inventory.entities.Sale;
import com.wmc606.inventory.service.InventoryManager;
import com.wmc606.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ProductController - REST API endpoints for product management
 * Handles operations using data structures based on product categories
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private InventoryManager inventoryManager;
    
    @Autowired
    private ProductRepository productRepository;
    
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
     * Add new product with quantity (uses data structures)
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductRequest request) {
        try {
            System.out.println("🌐 API: Adding new product");
            System.out.println("📦 Product: " + request.getProduct().getName());
            System.out.println("📊 Quantity: " + request.getQuantity());
            
            Product product = inventoryManager.addGoodsToInventory(request.getProduct(), request.getQuantity());
            
            System.out.println("✅ Product added successfully with ID: " + product.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception e) {
            System.err.println("❌ Error adding product: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add product", "message", e.getMessage()));
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
            System.out.println("�� API: Deleting product ID: " + id);
            
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
