package com.wmc606.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * MainController - Serves the frontend HTML interface
 * Handles routing for the web application
 */
@Controller
public class MainController {
    
    /**
     * Serve the main frontend page
     * GET /
     */
    @GetMapping("/")
    public String index() {
        System.out.println("🌐 Serving main frontend page");
        return "index"; // This will serve src/main/resources/templates/index.html
    }
    
    /**
     * Handle SPA routing - forward all non-API routes to index
     * This ensures that the frontend can handle its own routing
     */
    @GetMapping("/{path:[^\\.]*}")
    public String redirect() {
        System.out.println("🔄 Redirecting to frontend for SPA routing");
        return "forward:/";
    }
}
