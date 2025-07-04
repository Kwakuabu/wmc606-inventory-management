package com.wmc606.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wmc606.inventory.entities.Category;
import com.wmc606.inventory.entities.Vendor;
import com.wmc606.inventory.repository.CategoryRepository;
import com.wmc606.inventory.repository.VendorRepository;

/**
 * SIMPLE and RELIABLE Database Initializer
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private VendorRepository vendorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("🔧 Simple Database Initializer - Starting...");
        
        // Fix existing categories
        fixAllCategories();
        
        // Create vendors if needed
        if (vendorRepository.count() == 0) {
            createVendors();
        }
        
        System.out.println("✅ Database initialization completed!");
    }

    private void fixAllCategories() {
        System.out.println("🔧 Fixing all categories...");
        
        for (long id = 1; id <= 11; id++) {
            var categoryOpt = categoryRepository.findById(id);
            
            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                
                // Fix missing DataStructureType
                if (category.getDataStructureType() == null) {
                    Category.DataStructureType correctType = getCorrectType(id);
                    category.setDataStructureType(correctType);
                    categoryRepository.save(category);
                    System.out.println("🔧 Fixed category " + id + " → " + correctType);
                }
                
                // Fix missing name
                if (category.getName() == null || category.getName().isEmpty()) {
                    category.setName(getCorrectName(id));
                    categoryRepository.save(category);
                    System.out.println("🔧 Fixed name for category " + id);
                }
            } else {
                // Create missing category
                Category newCategory = new Category(
                    getCorrectName(id),
                    getCorrectType(id),
                    "Category " + id + " - " + getCorrectName(id)
                );
                categoryRepository.save(newCategory);
                System.out.println("🆕 Created category " + id);
            }
        }
        
        System.out.println("✅ All categories fixed!");
    }
    
    private Category.DataStructureType getCorrectType(long id) {
        if (id >= 1 && id <= 4) return Category.DataStructureType.STACK;
        if (id >= 5 && id <= 7) return Category.DataStructureType.QUEUE;
        if (id >= 8 && id <= 11) return Category.DataStructureType.LIST;
        return Category.DataStructureType.LIST;
    }
    
    private String getCorrectName(long id) {
        switch ((int) id) {
            case 1: return "Beverages";
            case 2: return "Bread/Bakery";
            case 3: return "Canned/Jarred Goods";
            case 4: return "Dairy";
            case 5: return "Dry/Baking Goods";
            case 6: return "Frozen Foods";
            case 7: return "Meat";
            case 8: return "Produce";
            case 9: return "Cleaners";
            case 10: return "Paper Goods";
            case 11: return "Personal Care";
            default: return "Category " + id;
        }
    }
    
    private void createVendors() {
        System.out.println("🏪 Creating vendors...");
        
        vendorRepository.save(new Vendor("Fresh Foods Ltd", "John Mensah", "+233 24 123 4567", 
            "john@freshfoods.com", "123 Market Street, Accra, Ghana"));
        vendorRepository.save(new Vendor("Daily Supplies Co", "Sarah Asante", "+233 20 987 6543", 
            "sarah@dailysupplies.com", "456 Trade Avenue, Kumasi, Ghana"));
        vendorRepository.save(new Vendor("Quality Grocers", "Michael Osei", "+233 26 555 7890", 
            "michael@qualitygrocers.com", "789 Commerce Road, Tema, Ghana"));
        
        System.out.println("✅ Vendors created!");
    }
}