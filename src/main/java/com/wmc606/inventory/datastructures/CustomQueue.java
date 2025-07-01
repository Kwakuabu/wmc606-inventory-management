package com.wmc606.inventory.datastructures;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Custom Queue Implementation for Categories 5-7
 * (Dry/Baking Goods, Frozen Foods, Meat)
 * 
 * FIFO (First In, First Out) principle
 * Time Complexity: Enqueue O(1), Dequeue O(1), Front O(1)
 * Space Complexity: O(n) where n is number of elements
 */
public class CustomQueue<T> {
    private ArrayList<T> queue;
    private int front;
    private int rear;
    private int size;
    
    /**
     * Constructor - Initialize empty queue
     */
    public CustomQueue() {
        queue = new ArrayList<>();
        front = 0;
        rear = -1;
        size = 0;
        System.out.println("🚶 Queue initialized for categories 5-7 (FIFO principle)");
    }
    
    /**
     * Enqueue operation - Add element to rear of queue
     * Time Complexity: O(1)
     * @param item The item to add to the queue
     */
    public void enqueue(T item) {
        queue.add(item);
        rear++;
        size++;
        System.out.println("➕ ENQUEUE: Added item to queue. New size: " + size());
    }
    
    /**
     * Dequeue operation - Remove and return front element
     * Time Complexity: O(1) amortized
     * @return The front element of the queue
     * @throws NoSuchElementException if queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T item = queue.get(front);
        front++;
        size--;
        
        // Reset queue when it becomes empty to save memory
        if (isEmpty()) {
            front = 0;
            rear = -1;
            queue.clear();
            System.out.println("🔄 Queue reset after becoming empty");
        }
        
        System.out.println("➖ DEQUEUE: Removed item from queue. New size: " + size());
        return item;
    }
    
    /**
     * Front operation - Return front element without removing
     * Time Complexity: O(1)
     * @return The front element of the queue
     * @throws NoSuchElementException if queue is empty
     */
    public T front() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return queue.get(front);
    }
    
    /**
     * Check if queue is empty
     * Time Complexity: O(1)
     * @return true if queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Get current size of queue
     * Time Complexity: O(1)
     * @return Number of elements in queue
     */
    public int size() {
        return size;
    }
    
    /**
     * Get all items in queue (for display purposes)
     * Time Complexity: O(n)
     * @return ArrayList containing all queue elements in order
     */
    public ArrayList<T> getAllItems() {
        ArrayList<T> items = new ArrayList<>();
        for (int i = front; i <= rear && i < queue.size(); i++) {
            items.add(queue.get(i));
        }
        return items;
    }
    
    /**
     * Clear all elements from queue
     * Time Complexity: O(1)
     */
    public void clear() {
        queue.clear();
        front = 0;
        rear = -1;
        size = 0;
        System.out.println("🗑️ Queue cleared");
    }
    
    @Override
    public String toString() {
        return "CustomQueue{size=" + size() + ", isEmpty=" + isEmpty() + 
               ", front=" + front + ", rear=" + rear + "}";
    }
}
