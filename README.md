# 📦 WMC 606 - Inventory Management System

**Data Structures and Complexities of Algorithms Project**

A comprehensive inventory management system demonstrating the practical implementation of different data structures based on product categories, built with Java Spring Boot and modern web technologies.

## 🎯 Project Overview

This project implements an inventory management system that uses different data structures for different product categories:

- **📚 Stack (Categories 1-4)**: LIFO operations for Beverages, Bread/Bakery, Canned/Jarred Goods, Dairy
- **🚶 Queue (Categories 5-7)**: FIFO operations for Dry/Baking Goods, Frozen Foods, Meat  
- **📋 List (Categories 8-11)**: Dynamic operations for Produce, Cleaners, Paper Goods, Personal Care
- **🗺️ HashMap**: Vendor management and sales tracking

## ✨ Features

### 🏗️ Data Structure Implementation
- ✅ **Custom Stack** with Push/Pop operations (O(1))
- ✅ **Custom Queue** with Enqueue/Dequeue operations (O(1))
- ✅ **Custom List** with dynamic operations and algorithms
- ✅ **HashMap** for vendor management
- ✅ **Map** for sales tracking

### 🔍 Algorithm Implementation
- ✅ **Linear Search** - O(n) for categories 6-11
- ✅ **QuickSort** - O(n log n) average case for alphabetical sorting
- ✅ **Binary Search** - O(log n) for sorted data (available)
- ✅ **Performance Analysis** with Big O notation reporting

### 🌐 Web Application
- ✅ **REST API** with comprehensive endpoints
- ✅ **Modern Web Interface** (HTML/CSS/JavaScript)
- ✅ **Real-time Operations** with data structure demonstrations
- ✅ **Responsive Design** for all devices

### 📊 Management Features
- ✅ **Add Goods** using appropriate data structures
- ✅ **Issue Goods** with LIFO/FIFO/Dynamic operations
- ✅ **Vendor Management** with HashMap implementation
- ✅ **Sales Tracking** with Map-based analytics
- ✅ **Low Stock Alerts** and inventory monitoring
- ✅ **Reports and Analytics** with performance metrics

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.0, Spring Data JPA
- **Database**: MySQL (via XAMPP) / H2 (in-memory for testing)
- **Frontend**: HTML5, CSS3, JavaScript (ES6+)
- **Build Tool**: Maven 3.8+
- **Architecture**: REST API, MVC Pattern
- **Design Patterns**: Repository, Service Layer, Custom Data Structures

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- XAMPP (for MySQL) or any MySQL installation
- Modern web browser

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/wmc606-inventory-management.git
   cd wmc606-inventory-management
   ```

2. **Set up the database**
   - Start XAMPP and ensure MySQL is running
   - Open phpMyAdmin: `http://localhost/phpmyadmin`
   - Create database: `inventory_management`
   - Import the SQL schema (provided in `/database/schema.sql`)

3. **Configure the application**
   ```properties
   # src/main/resources/application.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/inventory_management
   spring.datasource.username=root
   spring.datasource.password=
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Frontend: `http://localhost:8080`
   - API Documentation: `http://localhost:8080/api`

## 📊 Data Structure Usage by Category

| Categories | Data Structure | Operations | Items |
|------------|----------------|------------|--------|
| **1-4** | **STACK** (LIFO) | Push O(1), Pop O(1) | Beverages, Bread, Canned, Dairy |
| **5-7** | **QUEUE** (FIFO) | Enqueue O(1), Dequeue O(1) | Dry/Baking, Frozen, Meat |
| **8-11** | **LIST** (Dynamic) | Add O(1), Search O(n), Sort O(n log n) | Produce, Cleaners, Paper, Personal Care |

## 🔗 API Endpoints

### Products
- `GET /api/products` - Get all products
- `POST /api/products` - Add product (uses data structures)
- `POST /api/products/{id}/issue` - Issue goods (data structure operations)
- `GET /api/products/search` - Search with custom algorithms
- `GET /api/products/sort/{categoryId}` - Sort using QuickSort

### Vendors (HashMap Implementation)
- `GET /api/vendors/map` - Get vendors from HashMap
- `POST /api/vendors` - Add vendor to HashMap
- `GET /api/vendors/map/{id}` - Get vendor from HashMap

### Categories
- `GET /api/categories/stack` - Get Stack categories (1-4)
- `GET /api/categories/queue` - Get Queue categories (5-7)  
- `GET /api/categories/list` - Get List categories (8-11)

### Reports & Analytics
- `GET /api/reports/performance` - Algorithm performance analysis
- `GET /api/reports/low-stock` - Low stock report
- `GET /api/sales/summary` - Sales summary with Map tracking

## ⚡ Performance Analysis

| Operation | Time Complexity | Space Complexity | Usage |
|-----------|----------------|------------------|--------|
| Stack Push/Pop | O(1) | O(1) | Categories 1-4 |
| Queue Enqueue/Dequeue | O(1) | O(1) | Categories 5-7 |
| List Add | O(1) | O(1) | Categories 8-11 |
| List Remove | O(n) | O(1) | Categories 8-11 |
| Linear Search | O(n) | O(1) | Categories 6-11 |
| QuickSort | O(n log n) avg | O(log n) | Categories 6-11 |
| HashMap Get/Put | O(1) avg | O(1) | Vendor Management |

## 🏆 Academic Requirements Fulfilled

✅ **Data Structures**: Custom Stack, Queue, List, HashMap, Map implementations  
✅ **Algorithms**: Search (Linear, Binary) and Sort (QuickSort, MergeSort)  
✅ **Complexity Analysis**: Big O notation documentation for all operations  
✅ **Database Integration**: MySQL with proper relationships and transactions  
✅ **User Interface**: Modern, responsive web interface  
✅ **Documentation**: Comprehensive code documentation and README  
✅ **Testing**: Functional testing and performance analysis  

## 👥 Team Contribution

| Team Member | Contribution | Percentage |
|-------------|--------------|------------|
| Member 1 | Backend Development, Data Structures | 25% |
| Member 2 | Algorithm Implementation, Performance Analysis | 25% |
| Member 3 | Frontend Development, UI/UX Design | 25% |
| Member 4 | Testing, Documentation, Integration | 25% |

## 📝 Project Structure

```
src/
├── main/
│   ├── java/com/wmc606/inventory/
│   │   ├── InventoryManagementApplication.java
│   │   ├── controller/          # REST API Controllers
│   │   ├── entities/            # JPA Entities
│   │   ├── repository/          # Data Access Layer
│   │   ├── service/             # Business Logic
│   │   └── datastructures/      # Custom Data Structures
│   └── resources/
│       ├── application.properties
│       └── templates/
│           └── index.html       # Frontend Interface
```

## 🧪 Testing

```bash
# Run tests
mvn test

# Run with specific profile
mvn spring-boot:run -Dspring.profiles.active=test

# API Testing
curl http://localhost:8080/api/products
curl http://localhost:8080/api/categories
```

## 📜 License

This project is created for educational purposes as part of the WMC 606 course curriculum.

## 🤝 Contributing

This is an academic project. For suggestions or improvements, please create an issue or contact the development team.

## 📞 Contact

- **Course**: WMC 606 - Data Structures and Complexities of Algorithms
- **Institution**: Wisconsin International University College
- **Project**: Inventory Management System with Data Structures Implementation

---

**⭐ If you found this project helpful for understanding data structures and algorithms, please give it a star!**
