package com.wmc606.inventory.controller;

import com.wmc606.inventory.entities.Vendor;
import com.wmc606.inventory.service.InventoryManager;
import com.wmc606.inventory.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * VendorController - REST API endpoints for vendor management
 * Implements HashMap vendor storage as required by project
 */
@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "*")
public class VendorController {
    
    @Autowired
    private InventoryManager inventoryManager;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    /**
     * Get all vendors from database
     * GET /api/vendors
     */
    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        try {
            System.out.println("🌐 API: Getting all vendors from database");
            List<Vendor> vendors = vendorRepository.findAll();
            System.out.println("✅ Found " + vendors.size() + " vendors in database");
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            System.err.println("❌ Error getting vendors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get all vendors from HashMap (as required by project)
     * GET /api/vendors/map
     */
    @GetMapping("/map")
    public ResponseEntity<Map<Long, Vendor>> getAllVendorsMap() {
        try {
            System.out.println("🌐 API: Getting all vendors from HashMap");
            Map<Long, Vendor> vendorMap = inventoryManager.getAllVendorsMap();
            System.out.println("✅ HashMap contains " + vendorMap.size() + " vendors");
            return ResponseEntity.ok(vendorMap);
        } catch (Exception e) {
            System.err.println("❌ Error getting vendors from HashMap: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get vendor by ID from database
     * GET /api/vendors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        try {
            System.out.println("🌐 API: Getting vendor by ID from database: " + id);
            return vendorRepository.findById(id)
                    .map(vendor -> {
                        System.out.println("✅ Vendor found: " + vendor.getName());
                        return ResponseEntity.ok(vendor);
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Vendor not found with ID: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error getting vendor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get vendor from HashMap (demonstrates HashMap usage)
     * GET /api/vendors/map/{id}
     */
    @GetMapping("/map/{id}")
    public ResponseEntity<Vendor> getVendorFromMap(@PathVariable Long id) {
        try {
            System.out.println("🌐 API: Getting vendor from HashMap: " + id);
            Vendor vendor = inventoryManager.getVendorFromMap(id);
            if (vendor != null) {
                System.out.println("✅ Vendor found in HashMap: " + vendor.getName());
                return ResponseEntity.ok(vendor);
            } else {
                System.out.println("❌ Vendor not found in HashMap: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("❌ Error getting vendor from HashMap: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Add new vendor (saves to database and HashMap)
     * POST /api/vendors
     */
    @PostMapping
    public ResponseEntity<?> addVendor(@RequestBody Vendor vendor) {
        try {
            System.out.println("🌐 API: Adding new vendor");
            System.out.println("🏪 Vendor: " + vendor.getName());
            
            // Check if vendor already exists
            if (vendorRepository.existsByNameIgnoreCase(vendor.getName())) {
                System.out.println("⚠️ Vendor already exists: " + vendor.getName());
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Vendor with this name already exists"));
            }
            
            Vendor savedVendor = inventoryManager.addVendor(vendor);
            
            System.out.println("✅ Vendor added successfully with ID: " + savedVendor.getVendorId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVendor);
        } catch (Exception e) {
            System.err.println("❌ Error adding vendor: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add vendor", "message", e.getMessage()));
        }
    }
    
    /**
     * Update vendor
     * PUT /api/vendors/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVendor(@PathVariable Long id, @RequestBody Vendor vendorDetails) {
        try {
            System.out.println("🌐 API: Updating vendor ID: " + id);
            
            return vendorRepository.findById(id)
                    .map(vendor -> {
                        vendor.setName(vendorDetails.getName());
                        vendor.setContactPerson(vendorDetails.getContactPerson());
                        vendor.setPhone(vendorDetails.getPhone());
                        vendor.setEmail(vendorDetails.getEmail());
                        vendor.setAddress(vendorDetails.getAddress());
                        
                        Vendor updatedVendor = vendorRepository.save(vendor);
                        
                        // Update in HashMap as well
                        inventoryManager.getAllVendorsMap(); // This will refresh the HashMap
                        
                        System.out.println("✅ Vendor updated: " + updatedVendor.getName());
                        return ResponseEntity.ok(updatedVendor);
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Vendor not found for update: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error updating vendor: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update vendor", "message", e.getMessage()));
        }
    }
    
    /**
     * Delete vendor
     * DELETE /api/vendors/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVendor(@PathVariable Long id) {
        try {
            System.out.println("🌐 API: Deleting vendor ID: " + id);
            
            return vendorRepository.findById(id)
                    .map(vendor -> {
                        // Check if vendor has products
                        if (!vendor.getProducts().isEmpty()) {
                            System.out.println("⚠️ Cannot delete vendor with products: " + vendor.getName());
                            return ResponseEntity.badRequest()
                                .body(Map.of("error", "Cannot delete vendor with associated products"));
                        }
                        
                        vendorRepository.delete(vendor);
                        
                        // Refresh HashMap after deletion
                        inventoryManager.getAllVendorsMap();
                        
                        System.out.println("✅ Vendor deleted: " + vendor.getName());
                        return ResponseEntity.ok()
                            .body(Map.of("message", "Vendor deleted successfully"));
                    })
                    .orElseGet(() -> {
                        System.out.println("❌ Vendor not found for deletion: " + id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            System.err.println("❌ Error deleting vendor: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete vendor", "message", e.getMessage()));
        }
    }
    
    /**
     * Search vendors
     * GET /api/vendors/search?searchTerm=...
     */
    @GetMapping("/search")
    public ResponseEntity<List<Vendor>> searchVendors(@RequestParam String searchTerm) {
        try {
            System.out.println("🌐 API: Searching vendors");
            System.out.println("🔍 Search term: " + searchTerm);
            
            List<Vendor> vendors = vendorRepository.searchVendors(searchTerm);
            
            System.out.println("✅ Search completed. Found " + vendors.size() + " vendors");
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            System.err.println("❌ Error searching vendors: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get vendor statistics
     * GET /api/vendors/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getVendorStats() {
        try {
            System.out.println("🌐 API: Getting vendor statistics");
            
            long totalVendors = vendorRepository.getTotalVendorCount();
            List<Vendor> vendorsWithProducts = vendorRepository.findVendorsWithProducts();
            List<Vendor> vendorsWithoutProducts = vendorRepository.findVendorsWithoutProducts();
            Map<Long, Vendor> vendorMap = inventoryManager.getAllVendorsMap();
            
            Map<String, Object> stats = Map.of(
                "totalVendors", totalVendors,
                "vendorsWithProducts", vendorsWithProducts.size(),
                "vendorsWithoutProducts", vendorsWithoutProducts.size(),
                "hashMapSize", vendorMap.size()
            );
            
            System.out.println("✅ Vendor statistics retrieved");
            System.out.println("📊 Total vendors: " + totalVendors);
            System.out.println("📦 Vendors with products: " + vendorsWithProducts.size());
            System.out.println("📭 Vendors without products: " + vendorsWithoutProducts.size());
            System.out.println("🗺️ HashMap size: " + vendorMap.size());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("❌ Error getting vendor statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
