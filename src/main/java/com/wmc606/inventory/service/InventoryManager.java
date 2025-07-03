package com.wmc606.inventory.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wmc606.inventory.datastructures.CustomList;
import com.wmc606.inventory.datastructures.CustomQueue;
import com.wmc606.inventory.datastructures.CustomStack;
import com.wmc606.inventory.entities.Category;
import com.wmc606.inventory.entities.Product;
import com.wmc606.inventory.entities.Sale;
import com.wmc606.inventory.entities.Vendor;
import com.wmc606.inventory.repository.CategoryRepository;
import com.wmc606.inventory.repository.ProductRepository;
import com.wmc606.inventory.repository.SaleRepository;
import com.wmc606.inventory.repository.VendorRepository;

/**
 * Core Inventory Management Service
 * Implements different data structures based on product categories:
 * - Categories 1-4: Stack (LIFO) - Beverages, Bread, Canned, Dairy
 * - Categories 5-7: Queue (FIFO) - Dry/Baking, Frozen, Meat
 * - Categories 8-11: List (Dynamic) - Produce, Cleaners, Paper, Personal Care
 */
@Service
@Transactional
public class InventoryManager {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private SaleRepository saleRepository;
    
    // Data structures for different categories
    private Map<Long, CustomStack<Product>> categoryStacks = new ConcurrentHashMap<>();
    private Map<Long, CustomQueue<Product>> categoryQueues = new ConcurrentHashMap<>();
    private Map<Long, CustomList<Product>> categoryLists = new ConcurrentHashMap<>();
    
    // HashMap for vendor information (as required by project)
    private Map<Long, Vendor> vendorMap = new ConcurrentHashMap<>();
    
    // Map for tracking product sales (as required by project)
    private Map<String, Integer> productSalesMap = new ConcurrentHashMap<>();
    
    /**
     * Add goods to inventory using appropriate data structure
     * Time Complexity: O(1) for Stack/Queue, O(1) for List
     */
    public Product addGoodsToInventory(Product product, Integer quantity) {
        System.out.println("🚀 Adding goods to inventory...");
        
        // Save product to database first
        Product savedProduct = productRepository.save(product);
        
        // Update quantity
        savedProduct.setQuantityInStock(savedProduct.getQuantityInStock() + quantity);
        savedProduct = productRepository.save(savedProduct);
        
        Category category = savedProduct.getCategory();
        Long categoryId = category.getCategoryId();
        
        System.out.println("📦 Product: " + savedProduct.getName());
        System.out.println("📂 Category: " + category.getName() + " (ID: " + categoryId + ")");
        System.out.println("🔧 Data Structure: " + category.getDataStructureType());
        
        // Add to appropriate data structure based on category
        switch (category.getDataStructureType()) {
            case STACK:
                addToStack(categoryId, savedProduct);
                System.out.println("📚 Added to STACK (Categories 1-4: LIFO)");
                break;
            case QUEUE:
                addToQueue(categoryId, savedProduct);
                System.out.println("🚶 Added to QUEUE (Categories 5-7: FIFO)");
                break;
            case LIST:
                addToList(categoryId, savedProduct);
                System.out.println("📋 Added to LIST (Categories 8-11: Dynamic)");
                break;
        }
        
        System.out.println("✅ Product added successfully with " + quantity + " items!");
        return savedProduct;
    }
    
    /**
     * Issue goods from inventory using appropriate data structure
     * Time Complexity: O(1) for Stack/Queue, O(n) for List removal
     */
    public Sale issueGoods(Long productId, Integer quantity, String customerName) {
        System.out.println("🛒 Issuing goods...");
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        
        if (product.getQuantityInStock() < quantity) {
            throw new RuntimeException("Insufficient stock! Available: " + product.getQuantityInStock() + 
                                     ", Requested: " + quantity);
        }
        
        Category category = product.getCategory();
        Long categoryId = category.getCategoryId();
        
        System.out.println("📦 Product: " + product.getName());
        System.out.println("📂 Category: " + category.getName());
        System.out.println("🔧 Data Structure: " + category.getDataStructureType());
        System.out.println("📊 Quantity to issue: " + quantity);
        
        // Remove from appropriate data structure
        switch (category.getDataStructureType()) {
            case STACK:
                processStackRemoval(categoryId, product);
                System.out.println("📚 Removed from STACK (LIFO - Last In, First Out)");
                break;
            case QUEUE:
                processQueueRemoval(categoryId, product);
                System.out.println("🚶 Removed from QUEUE (FIFO - First In, First Out)");
                break;
            case LIST:
                processListRemoval(categoryId, product);
                System.out.println("📋 Removed from LIST (Dynamic operations)");
                break;
        }
        
        // Update stock quantity
        product.setQuantityInStock(product.getQuantityInStock() - quantity);
        productRepository.save(product);
        
        // Create sale record
        Sale sale = new Sale(product, quantity, product.getPrice(), customerName);
        sale = saleRepository.save(sale);
        
        // Update sales tracking map (as required by project)
        productSalesMap.merge(product.getProductCode(), quantity, Integer::sum);
        
        System.out.println("✅ Goods issued successfully!");
        System.out.println("💰 Sale amount: $" + sale.getTotalAmount());
        System.out.println("👤 Customer: " + customerName);
        
        return sale;
    }
    
    // Stack operations for categories 1-4 (Beverages, Bread, Canned, Dairy)
    private void addToStack(Long categoryId, Product product) {
        CustomStack<Product> stack = categoryStacks.computeIfAbsent(categoryId, k -> new CustomStack<>());
        stack.push(product);
        System.out.println("📚 PUSH operation completed. Stack size: " + stack.size());
    }
    
    private void processStackRemoval(Long categoryId, Product product) {
        CustomStack<Product> stack = categoryStacks.get(categoryId);
        if (stack != null && !stack.isEmpty()) {
            Product poppedProduct = stack.pop();
            System.out.println("📚 POP operation completed. Removed: " + poppedProduct.getName());
            System.out.println("📚 Stack size after removal: " + stack.size());
        } else {
            System.out.println("📚 Stack is empty or not initialized for category: " + categoryId);
        }
    }
    
    // Queue operations for categories 5-7 (Dry/Baking, Frozen, Meat)
    private void addToQueue(Long categoryId, Product product) {
        CustomQueue<Product> queue = categoryQueues.computeIfAbsent(categoryId, k -> new CustomQueue<>());
        queue.enqueue(product);
        System.out.println("🚶 ENQUEUE operation completed. Queue size: " + queue.size());
    }
    
    private void processQueueRemoval(Long categoryId, Product product) {
        CustomQueue<Product> queue = categoryQueues.get(categoryId);
        if (queue != null && !queue.isEmpty()) {
            Product dequeuedProduct = queue.dequeue();
            System.out.println("🚶 DEQUEUE operation completed. Removed: " + dequeuedProduct.getName());
            System.out.println("🚶 Queue size after removal: " + queue.size());
        } else {
            System.out.println("🚶 Queue is empty or not initialized for category: " + categoryId);
        }
    }
    
    // List operations for categories 8-11 (Produce, Cleaners, Paper Goods, Personal Care)
    private void addToList(Long categoryId, Product product) {
        CustomList<Product> list = categoryLists.computeIfAbsent(categoryId, k -> new CustomList<>());
        list.add(product);
        System.out.println("📋 ADD operation completed. List size: " + list.size());
    }
    
    private void processListRemoval(Long categoryId, Product product) {
        CustomList<Product> list = categoryLists.get(categoryId);
        if (list != null && !list.isEmpty()) {
            boolean removed = list.remove(product);
            if (removed) {
                System.out.println("📋 REMOVE operation completed. Removed: " + product.getName());
                System.out.println("📋 List size after removal: " + list.size());
            } else {
                System.out.println("📋 Product not found in list for removal");
            }
        } else {
            System.out.println("📋 List is empty or not initialized for category: " + categoryId);
        }
    }
    
    /**
     * Search products with different algorithms based on category
     * Categories 6-11 use custom search algorithms
     */
    public List<Product> searchProducts(String searchTerm, Long categoryId) {
        System.out.println("🔍 Searching products...");
        System.out.println("🔍 Search term: " + searchTerm);
        System.out.println("🔍 Category ID: " + categoryId);
        
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            System.out.println("❌ Category not found");
            return new ArrayList<>();
        }
        
        List<Product> results = new ArrayList<>();
        
        // For categories 6-11 (List data structure), use custom search algorithms
        if (categoryId >= 6 && categoryId <= 11) {
            System.out.println("🎯 Using custom search algorithms for List categories (6-11)");
            CustomList<Product> list = categoryLists.get(categoryId);
            if (list != null) {
                // Linear search implementation - O(n)
                System.out.println("🔍 Performing LINEAR SEARCH (O(n) complexity)");
                for (Product product : list) {
                    if (product.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        product.getProductCode().toLowerCase().contains(searchTerm.toLowerCase())) {
                        results.add(product);
                        System.out.println("✅ Found: " + product.getName());
                    }
                }
            }
        } else {
            // For other categories, search in database
            System.out.println("🔍 Using database search for Stack/Queue categories (1-5, 7)");
            results = productRepository.findByNameContainingIgnoreCaseAndCategoryCategoryId(searchTerm, categoryId);
        }
        
        System.out.println("🔍 Search completed. Found " + results.size() + " products.");
        return results;
    }
    
    /**
     * Sort products alphabetically for categories 6-11 (List categories)
     * Uses QuickSort algorithm implementation
     */
    public List<Product> sortProductsAlphabetically(Long categoryId) {
        System.out.println("📊 Sorting products alphabetically...");
        System.out.println("📊 Category ID: " + categoryId);
        
        // For categories 6-11, use custom sorting algorithms
        if (categoryId >= 6 && categoryId <= 11) {
            System.out.println("⚡ Using QUICKSORT for List categories (6-11)");
            CustomList<Product> list = categoryLists.get(categoryId);
            if (list != null) {
                System.out.println("📊 Products before sorting: " + list.size());
                list.quickSort(); // O(n log n) average case
                System.out.println("✅ QuickSort completed successfully!");
                return list.getAllItems();
            }
        }
        
        // For other categories, use database sorting
        System.out.println("📊 Using database sorting for Stack/Queue categories");
        List<Product> sortedProducts = productRepository.findByCategoryCategoryIdOrderByNameAsc(categoryId);
        System.out.println("✅ Database sort completed. " + sortedProducts.size() + " products sorted.");
        return sortedProducts;
    }
    
    /**
     * Get low stock products - critical for inventory management
     */
    public List<Product> getLowStockProducts() {
        System.out.println("⚠️ Checking for low stock products...");
        List<Product> lowStockProducts = productRepository.findLowStockProducts();
        System.out.println("⚠️ Found " + lowStockProducts.size() + " products with low stock");
        
        for (Product product : lowStockProducts) {
            System.out.println("📉 Low stock: " + product.getName() + 
                             " (Current: " + product.getQuantityInStock() + 
                             ", Minimum: " + product.getMinimumStockLevel() + ")");
        }
        
        return lowStockProducts;
    }
    
    /**
     * Get sales summary with Map-based tracking - FIXED VERSION
     */
    public Map<String, Object> getSalesSummary() {
        System.out.println("📈 Generating sales summary...");
        
        try {
            // Use individual queries to avoid casting issues
            Long totalItemsSold = saleRepository.getTotalItemsSold();
            BigDecimal totalRevenue = saleRepository.getTotalRevenue();
            
            // Handle null values
            if (totalItemsSold == null) {
                totalItemsSold = 0L;
            }
            if (totalRevenue == null) {
                totalRevenue = BigDecimal.ZERO;
            }
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalRevenue", totalRevenue);
            summary.put("totalItemsSold", totalItemsSold);
            summary.put("productSales", new HashMap<>(productSalesMap)); // Map-based sales tracking
            
            System.out.println("📈 Sales Summary Generated:");
            System.out.println("💰 Total Revenue: $" + totalRevenue);
            System.out.println("📦 Total Items Sold: " + totalItemsSold);
            System.out.println("🗺️ Product Sales Map size: " + productSalesMap.size());
            
            return summary;
            
        } catch (Exception e) {
            System.err.println("❌ Error generating sales summary: " + e.getMessage());
            e.printStackTrace();
            
            // Return empty summary on error
            Map<String, Object> emptySummary = new HashMap<>();
            emptySummary.put("totalRevenue", BigDecimal.ZERO);
            emptySummary.put("totalItemsSold", 0L);
            emptySummary.put("productSales", new HashMap<>());
            emptySummary.put("error", "Failed to load sales data");
            
            return emptySummary;
        }
    }
    
    /**
     * Vendor management using HashMap (as required by project)
     */
    public Vendor addVendor(Vendor vendor) {
        System.out.println("🏪 Adding vendor to HashMap...");
        Vendor savedVendor = vendorRepository.save(vendor);
        vendorMap.put(savedVendor.getVendorId(), savedVendor);
        System.out.println("✅ Vendor added: " + savedVendor.getName());
        System.out.println("🗺️ HashMap size: " + vendorMap.size());
        return savedVendor;
    }
    
    public Vendor getVendorFromMap(Long vendorId) {
        System.out.println("🔍 Getting vendor from HashMap: " + vendorId);
        Vendor vendor = vendorMap.get(vendorId);
        if (vendor != null) {
            System.out.println("✅ Vendor found: " + vendor.getName());
        } else {
            System.out.println("❌ Vendor not found in HashMap");
        }
        return vendor;
    }
    
    public Map<Long, Vendor> getAllVendorsMap() {
        System.out.println("🗺️ Getting all vendors from HashMap...");
        
        // Load vendors into map if empty
        if (vendorMap.isEmpty()) {
            System.out.println("🔄 HashMap empty, loading from database...");
            List<Vendor> vendors = vendorRepository.findAll();
            vendors.forEach(vendor -> vendorMap.put(vendor.getVendorId(), vendor));
            System.out.println("✅ Loaded " + vendors.size() + " vendors into HashMap");
        }
        
        System.out.println("🗺️ HashMap contains " + vendorMap.size() + " vendors");
        return new HashMap<>(vendorMap);
    }
    
    /**
     * Performance analysis with Big O notation
     */
    public Map<String, String> getPerformanceAnalysis() {
        System.out.println("⚡ Generating performance analysis...");
        
        Map<String, String> analysis = new HashMap<>();
        
        analysis.put("Stack Operations (Categories 1-4)", 
            "Push: O(1), Pop: O(1), Peek: O(1) - Used for LIFO operations (Beverages, Bread, Canned, Dairy)");
        analysis.put("Queue Operations (Categories 5-7)", 
            "Enqueue: O(1), Dequeue: O(1), Front: O(1) - Used for FIFO operations (Dry/Baking, Frozen, Meat)");
        analysis.put("List Operations (Categories 8-11)", 
            "Add: O(1), Remove: O(n), Search: O(n), Get: O(1) - Used for dynamic operations (Produce, Cleaners, Paper, Personal Care)");
        analysis.put("QuickSort Algorithm (Categories 6-11)", 
            "Average: O(n log n), Worst: O(n²) - Used for sorting products alphabetically");
        analysis.put("Linear Search (Categories 6-11)", 
            "O(n) - Used for searching products in List categories");
        analysis.put("HashMap Operations (Vendor Management)", 
            "Get/Put: O(1) average - Used for vendor management and sales tracking");
        analysis.put("Database Operations", 
            "Select: O(log n) with indexes, Insert/Update: O(log n), Delete: O(log n)");
        
        System.out.println("✅ Performance analysis completed with " + analysis.size() + " metrics");
        return analysis;
    }
    
    /**
     * Get data structure statistics
     */
    public Map<String, Object> getDataStructureStats() {
        Map<String, Object> stats = new HashMap<>();
        
        int stackCount = categoryStacks.values().stream().mapToInt(CustomStack::size).sum();
        int queueCount = categoryQueues.values().stream().mapToInt(CustomQueue::size).sum();
        int listCount = categoryLists.values().stream().mapToInt(CustomList::size).sum();
        
        stats.put("stackOperations", stackCount);
        stats.put("queueOperations", queueCount);
        stats.put("listOperations", listCount);
        stats.put("vendorMapSize", vendorMap.size());
        stats.put("salesMapSize", productSalesMap.size());
        
        System.out.println("📊 Data Structure Statistics:");
        System.out.println("📚 Stack operations: " + stackCount);
        System.out.println("🚶 Queue operations: " + queueCount);
        System.out.println("📋 List operations: " + listCount);
        System.out.println("🗺️ Vendor HashMap size: " + vendorMap.size());
        System.out.println("🗺️ Sales Map size: " + productSalesMap.size());
        
        return stats;
    }
}