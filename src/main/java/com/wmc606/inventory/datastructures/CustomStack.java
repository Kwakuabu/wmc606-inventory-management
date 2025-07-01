package com.wmc606.inventory.datastructures;

import java.util.ArrayList;
import java.util.EmptyStackException;

/**
 * Custom Stack Implementation for Categories 1-4
 * (Beverages, Bread/Bakery, Canned/Jarred Goods, Dairy)
 * 
 * LIFO (Last In, First Out) principle
 * Time Complexity: Push O(1), Pop O(1), Peek O(1)
 * Space Complexity: O(n) where n is number of elements
 */
public class CustomStack<T> {
    private ArrayList<T> stack;
    private int top;
    
    /**
     * Constructor - Initialize empty stack
     */
    public CustomStack() {
        stack = new ArrayList<>();
        top = -1;
        System.out.println("📚 Stack initialized for categories 1-4 (LIFO principle)");
    }
    
    /**
     * Push operation - Add element to top of stack
     * Time Complexity: O(1)
     * @param item The item to push onto the stack
     */
    public void push(T item) {
        stack.add(item);
        top++;
        System.out.println("➕ PUSH: Added item to stack. New size: " + size());
    }
    
    /**
     * Pop operation - Remove and return top element
     * Time Complexity: O(1)
     * @return The top element of the stack
     * @throws EmptyStackException if stack is empty
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T item = stack.get(top);
        stack.remove(top);
        top--;
        System.out.println("➖ POP: Removed item from stack. New size: " + size());
        return item;
    }
    
    /**
     * Peek operation - Return top element without removing
     * Time Complexity: O(1)
     * @return The top element of the stack
     * @throws EmptyStackException if stack is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.get(top);
    }
    
    /**
     * Check if stack is empty
     * Time Complexity: O(1)
     * @return true if stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return top == -1;
    }
    
    /**
     * Get current size of stack
     * Time Complexity: O(1)
     * @return Number of elements in stack
     */
    public int size() {
        return top + 1;
    }
    
    /**
     * Get all items in stack (for display purposes)
     * Time Complexity: O(n)
     * @return ArrayList containing all stack elements
     */
    public ArrayList<T> getAllItems() {
        return new ArrayList<>(stack);
    }
    
    /**
     * Clear all elements from stack
     * Time Complexity: O(1)
     */
    public void clear() {
        stack.clear();
        top = -1;
        System.out.println("🗑️ Stack cleared");
    }
    
    @Override
    public String toString() {
        return "CustomStack{size=" + size() + ", isEmpty=" + isEmpty() + "}";
    }
}
