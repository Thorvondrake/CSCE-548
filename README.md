# Nocturne Archive: Rare Book Inventory System
**CSCE 548 - Software Engineering Project**

## Project Overview
The Nocturne Archive is a comprehensive multi-tiered Java application designed to manage rare book inventories, author information, title catalogs, and sales transactions. It implements a professional service-oriented architecture ensuring complete separation of concerns between the user interface, business logic, and data persistence across all four database entities.

## Architecture Overview
This project implements a complete 3-tier architecture:
- **Presentation Layer**: Web interface (index.html) and HTTP server (App.java)
- **Service Layer**: REST-like API services for all entities 
- **Business Layer**: Business logic and validation for all entities
- **Data Layer**: Database operations (DatabaseManager.java)

### Technical Architecture Details
* **Presentation Layer**: HTTP web server with JSON API endpoints and comprehensive web interface
* **Service Layer**: REST-like services acting as the primary interface for external consumers
* **Business Layer**: Domain-specific validation rules and business logic implementation
* **Data Access Layer**: JDBC-based database operations with connection management

## Entities Covered (All 4 Tables)

### Authors (Authors table)
- **Business Layer**: AuthorBusinessLogic.java
- **Service Layer**: AuthorService.java  
- **Model**: Author.java
- **CRUD Operations**: Create, Read All, Read By ID, Update, Delete

### Titles (Titles table)
- **Business Layer**: TitleBusinessLogic.java
- **Service Layer**: TitleService.java
- **Model**: Title.java
- **CRUD Operations**: Create, Read All, Read By ID, Read By Author, Update, Delete

### Inventory (Inventory_Items table)
- **Business Layer**: BookBusinessLogic.java  
- **Service Layer**: BookService.java
- **Model**: BookItem.java
- **CRUD Operations**: Create, Read All, Read By ID, Read Signed Books, Update Price, Delete

### Sales (Sales_Log table)
- **Business Layer**: SaleBusinessLogic.java
- **Service Layer**: SaleService.java
- **Model**: Sale.java
- **CRUD Operations**: Create, Read All, Read By ID, Read By Item, Revenue Calculation, Update, Delete

## File Structure
```
NocturneArchiveProject/src/
├── Models:
│   ├── Author.java
│   ├── Title.java  
│   ├── BookItem.java
│   └── Sale.java
├── Data Layer:
│   └── DatabaseManager.java
├── Business Layer:
│   ├── AuthorBusinessLogic.java
│   ├── TitleBusinessLogic.java
│   ├── BookBusinessLogic.java
│   └── SaleBusinessLogic.java
├── Service Layer:
│   ├── AuthorService.java
│   ├── TitleService.java
│   ├── BookService.java
│   └── SaleService.java
└── Applications:
    ├── App.java (HTTP Web Server)
    └── ServiceTestRunner.java (Test Runner)
```

## Service Layer API Patterns

All services follow REST-like patterns accessible via HTTP endpoints:

### AuthorService
- `getAuthors()` → GET /authors
- `getAuthor(id)` → GET /authors/{id}
- `postAuthor(name, nationality, year)` → POST /authors
- `putAuthor(id, name, nationality, year)` → PUT /authors/{id}
- `deleteAuthor(id)` → DELETE /authors/{id}

### TitleService  
- `getTitles()` → GET /titles
- `getTitle(id)` → GET /titles/{id}
- `getTitlesByAuthor(authorId)` → GET /titles/author/{authorId}
- `postTitle(authorId, name, genre, year)` → POST /titles
- `putTitle(id, authorId, name, genre, year)` → PUT /titles/{id}
- `deleteTitle(id)` → DELETE /titles/{id}

### BookService (Inventory)
- `getArchive()` → GET /inventory
- `getBook(id)` → GET /inventory/{id}
- `getSignedArchive()` → GET /inventory/signed
- `postBook(titleId, condition, price, signed)` → POST /inventory
- `putPriceChange(id, price)` → PUT /inventory/{id}/price
- `deleteBook(id)` → DELETE /inventory/{id}

### SaleService
- `getSales()` → GET /sales
- `getSale(id)` → GET /sales/{id}
- `getSalesForItem(itemId)` → GET /sales/item/{itemId}
- `getTotalRevenue()` → GET /sales/revenue
- `postSale(itemId, price, buyer)` → POST /sales  
- `putSale(id, itemId, date, price, buyer)` → PUT /sales/{id}
- `deleteSale(id)` → DELETE /sales/{id}

## Business Layer Features

Each business layer includes:
- **Validation**: Input validation and business rules
- **Error Handling**: Proper error messages for invalid operations
- **Business Logic**: Domain-specific operations beyond basic CRUD
- **Data Integrity**: Ensures referential integrity between entities

**Examples**:
- AuthorBusinessLogic validates birth years (1000-2024)
- TitleBusinessLogic ensures authors exist before creating titles
- SaleBusinessLogic calculates revenue and validates positive prices
- BookBusinessLogic filters signed books and validates conditions

## Testing the Complete System

### Method 1: Automated Test Runner
```bash
cd NocturneArchiveProject
javac -cp "src:lib/*" src/*.java
java -cp "src:lib/*" ServiceTestRunner
```

### Method 2: Web Server (Production Mode)
```bash
cd NocturneArchiveProject
javac -cp "src:lib/*" src/*.java
java -cp "src:lib/*" App
```
- Starts HTTP server on port 8080 (or PORT environment variable)
- Access via browser: http://localhost:8080
- Complete web interface with tabs for all 4 entities

### Method 3: Web Interface Testing
The comprehensive web interface provides:
- **Authors Tab**: Get all authors, lookup by ID, count operations
- **Titles Tab**: Get all titles, lookup by ID, filter by author, count
- **Inventory Tab**: Get all items, signed items only, lookup by ID, count  
- **Sales Tab**: Get all sales, lookup by ID, filter by item, revenue calculation

## Cloud Deployment & Hosting

### Hosting Information
**Platform**: Render.com (Web Service)
**Database**: Aiven.io (Managed MySQL)

### Deployment Steps
1. **Containerization**: Use provided Dockerfile (Amazon Corretto OpenJDK 17)
2. **CI/CD**: Connect Render to GitHub repository
3. **Environment Variables**: Set DB_URL, DB_USER, DB_PASSWORD in Render dashboard
4. **Build Command**: `javac -cp ".:lib/*" $(find . -name "*.java") -d bin`
5. **Start Command**: `java -cp "bin:lib/*" App`

## Environment Configuration
The application is configured to be environment-agnostic using system variables:
* `DB_URL`: JDBC connection string for the Aiven MySQL instance
* `DB_USER`: Database administrative username
* `DB_PASSWORD`: Secure service password
* `PORT`: HTTP server port (defaults to 8080 for local development)

## Testing Evidence

Run `ServiceTestRunner` to see:
1. **CREATE operations** for all 4 entities
2. **READ operations** (All, By ID, Filtered) for all entities
3. **UPDATE operations** for all entities
4. **Business logic** demonstrations (revenue calculation, filtering, etc.)
5. **Complete architecture validation** (Service → Business → Data → Database)

This demonstrates full compliance with assignment requirements and complete functionality across all database entities with proper architectural separation.