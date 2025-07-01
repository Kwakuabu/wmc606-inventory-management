package com.wmc606.inventory.datastructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Custom List Implementation for Categories 8-11
 * (Produce, Cleaners, Paper Goods, Personal Care)
 * 
 * Dynamic array with search and sort capabilities
 * Time Complexity: Add O(1), Remove O(n), Get O(1), Search O(n)
 * Space Complexity: O(n) where n is number of elements
 */
public class CustomList<T> implements Iterable<T> {
    private ArrayList<T> list;
    
    /**
     * Constructor - Initialize empty list
     */
    public CustomList() {
        list = new ArrayList<>();
        System.out.println("📋 List initialized for categories 8-11 (Dynamic operations)");
    }
    
    /**
     * Add operation - Add element to end of list
     * Time Complexity: O(1) amortized
     * @param item The item to add to the list
     */
    public void add(T item) {
        list.add(item);
        System.out.println("➕ ADD: Added item to list. New size: " + size());
    }
    
    /**
     * Remove operation by index
     * Time Complexity: O(n)
     * @param index The index to remove from
     * @return The removed item
     */
    public T remove(int index) {
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        T removedItem = list.remove(index);
        System.out.println("➖ REMOVE at index " + index + ": New size: " + size());
        return removedItem;
    }
    
    /**
     * Remove operation by item
     * Time Complexity: O(n)
     * @param item The item to remove
     * @return true if item was removed, false otherwise
     */
    public boolean remove(T item) {
        boolean removed = list.remove(item);
        if (removed) {
            System.out.println("➖ REMOVE item: New size: " + size());
        }
        return removed;
    }
    
    /**
     * Get operation - Retrieve element at index
     * Time Complexity: O(1)
     * @param index The index to get from
     * @return The item at the index
     */
    public T get(int index) {
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return list.get(index);
    }
    
    /**
     * Get current size of list
     * Time Complexity: O(1)
     * @return Number of elements in list
     */
    public int size() {
        return list.size();
    }
    
    /**
     * Check if list is empty
     * Time Complexity: O(1)
     * @return true if list is empty, false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    /**
     * Get all items in list (for display purposes)
     * Time Complexity: O(n)
     * @return ArrayList containing all list elements
     */
    public ArrayList<T> getAllItems() {
        return new ArrayList<>(list);
    }
    
    /**
     * Linear Search Implementation
     * Time Complexity: O(n)
     * @param item The item to search for
     * @return Index of item, or -1 if not found
     */
    public int linearSearch(T item) {
        System.out.println("🔍 LINEAR SEARCH: Searching for item...");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(item)) {
                System.out.println("✅ Found at index: " + i);
                return i;
            }
        }
        System.out.println("❌ Item not found");
        return -1;
    }
    
    /**
     * QuickSort Implementation
     * Average Time Complexity: O(n log n)
     * @param <Comparable> items must implement Comparable interface
     */
    @SuppressWarnings("unchecked")
    public void quickSort() {
        if (list.size() <= 1) return;
        System.out.println("⚡ QUICKSORT: Sorting " + list.size() + " items...");
        long startTime = System.nanoTime();
        quickSortRecursive(0, list.size() - 1);
        long endTime = System.nanoTime();
        System.out.println("✅ QuickSort completed in " + (endTime - startTime)/1000000.0 + " ms");
    }
    
    @SuppressWarnings("unchecked")
    private void quickSortRecursive(int low, int high) {
        if (low < high) {
            int pivotIndex = partition(low, high);
            quickSortRecursive(low, pivotIndex - 1);
            quickSortRecursive(pivotIndex + 1, high);
        }
    }
    
    @SuppressWarnings("unchecked")
    private int partition(int low, int high) {
        Comparable<T> pivot = (Comparable<T>) list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            Comparable<T> current = (Comparable<T>) list.get(j);
            if (current.compareTo((T) pivot) <= 0) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }
    
    private void swap(int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    /**
     * Clear all elements from list
     * Time Complexity: O(1)
     */
    public void clear() {
        list.clear();
        System.out.println("🗑️ List cleared");
    }
    
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
    
    @Override
    public String toString() {
        return "CustomList{size=" + size() + ", isEmpty=" + isEmpty() + "}";
    }
}
