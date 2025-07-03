package com.wmc606.inventory.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.wmc606.inventory.entities.Category;
import com.wmc606.inventory.entities.Vendor;
import com.wmc606.inventory.repository.CategoryRepository;
import com.wmc606.inventory.repository.VendorRepository;

/**
 * Data Initializer for WMC 606 Project
 * Populates database with sample categories and vendors
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔧 Initializing WMC 606 sample data...");
        
        // Initialize Categories (if empty)
        if (categoryRepository.count() == 0) {
            initializeCategories();
        }
        
        // Initialize Vendors (if empty)
        if (vendorRepository.count() == 0) {
            initializeVendors();
        }
        
        System.out.println("✅ Sample data initialization completed!");
    }

    private void initializeCategories() {
        System.out.println("📂 Creating categories with data structure assignments...");
        
        // Categories 1-4: Stack (LIFO)
        Category beverages = new Category("Beverages", Category.DataStructureType.STACK, 
            "Drinks and beverages - managed with Stack (LIFO) data structure");
        
        Category bread = new Category("Bread/Bakery", Category.DataStructureType.STACK, 
            "Bread and bakery items - managed with Stack (LIFO) data structure");
        
        Category canned = new Category("Canned/Jarred Goods", Category.DataStructureType.STACK, 
            "Canned and jarred products - managed with Stack (LIFO) data structure");
        
        Category dairy = new Category("Dairy", Category.DataStructureType.STACK, 
            "Dairy products - managed with Stack (LIFO) data structure");

        // Categories 5-7: Queue (FIFO)
        Category dryGoods = new Category("Dry/Baking Goods", Category.DataStructureType.QUEUE, 
            "Dry and baking goods - managed with Queue (FIFO) data structure");
        
        Category frozen = new Category("Frozen Foods", Category.DataStructureType.QUEUE, 
            "Frozen food items - managed with Queue (FIFO) data structure");
        
        Category meat = new Category("Meat", Category.DataStructureType.QUEUE, 
            "Meat products - managed with Queue (FIFO) data structure");

        // Categories 8-11: List (Dynamic)
        Category produce = new Category("Produce", Category.DataStructureType.LIST, 
            "Fruits and vegetables - managed with List (Dynamic) data structure");
        
        Category cleaners = new Category("Cleaners", Category.DataStructureType.LIST, 
            "Cleaning products - managed with List (Dynamic) data structure");
        
        Category paperGoods = new Category("Paper Goods", Category.DataStructureType.LIST, 
            "Paper products - managed with List (Dynamic) data structure");
        
        Category personalCare = new Category("Personal Care", Category.DataStructureType.LIST, 
            "Personal care items - managed with List (Dynamic) data structure");

        // Save all categories
        categoryRepository.save(beverages);
        categoryRepository.save(bread);
        categoryRepository.save(canned);
        categoryRepository.save(dairy);
        categoryRepository.save(dryGoods);
        categoryRepository.save(frozen);
        categoryRepository.save(meat);
        categoryRepository.save(produce);
        categoryRepository.save(cleaners);
        categoryRepository.save(paperGoods);
        categoryRepository.save(personalCare);

        System.out.println("✅ Created 11 categories with data structure assignments");
    }

    private void initializeVendors() {
        System.out.println("🏪 Creating sample vendors...");
        
        Vendor vendor1 = new Vendor("Fresh Foods Ltd", "John Mensah", "+233 24 123 4567", 
            "john@freshfoods.com", "123 Market Street, Accra, Ghana");
        
        Vendor vendor2 = new Vendor("Daily Supplies Co", "Sarah Asante", "+233 20 987 6543", 
            "sarah@dailysupplies.com", "456 Trade Avenue, Kumasi, Ghana");
        
        Vendor vendor3 = new Vendor("Quality Grocers", "Michael Osei", "+233 26 555 7890", 
            "michael@qualitygrocers.com", "789 Commerce Road, Tema, Ghana");
        
        Vendor vendor4 = new Vendor("Prime Distributors", "Grace Adjei", "+233 24 111 2222", 
            "grace@primedist.com", "321 Industrial Area, Takoradi, Ghana");
        
        Vendor vendor5 = new Vendor("Metro Wholesale", "David Kumi", "+233 20 333 4444", 
            "david@metrowholesale.com", "654 Business District, Cape Coast, Ghana");

        // Save all vendors
        vendorRepository.save(vendor1);
        vendorRepository.save(vendor2);
        vendorRepository.save(vendor3);
        vendorRepository.save(vendor4);
        vendorRepository.save(vendor5);

        System.out.println("✅ Created 5 sample vendors");
    }
}