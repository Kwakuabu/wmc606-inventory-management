package com.wmc606.inventory.repository;

import com.wmc606.inventory.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CategoryRepository - Data access layer for Category entity
 * Provides CRUD operations and custom queries for categories
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find category by name
     * @param name The category name
     * @return Optional containing the category if found
     */
    Optional<Category> findByName(String name);
    
    /**
     * Find categories by data structure type
     * @param dataStructureType The type (STACK, QUEUE, LIST)
     * @return List of categories with specified data structure type
     */
    List<Category> findByDataStructureType(Category.DataStructureType dataStructureType);
    
    /**
     * Find all Stack categories (1-4)
     * @return List of categories using Stack data structure
     */
    @Query("SELECT c FROM Category c WHERE c.dataStructureType = 'STACK'")
    List<Category> findStackCategories();
    
    /**
     * Find all Queue categories (5-7)
     * @return List of categories using Queue data structure
     */
    @Query("SELECT c FROM Category c WHERE c.dataStructureType = 'QUEUE'")
    List<Category> findQueueCategories();
    
    /**
     * Find all List categories (8-11)
     * @return List of categories using List data structure
     */
    @Query("SELECT c FROM Category c WHERE c.dataStructureType = 'LIST'")
    List<Category> findListCategories();
    
    /**
     * Check if category name exists
     * @param name The category name to check
     * @return true if category exists, false otherwise
     */
    boolean existsByName(String name);
}
