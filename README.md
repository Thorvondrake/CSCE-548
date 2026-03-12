# Nocturne Archive: Rare Book Inventory System
**CSCE 548 - Software Engineering Project | Spring 2026**

[![Live Demo](https://img.shields.io/badge/Live%20Demo-Online-brightgreen)](https://thorvondrake.github.io/CSCE-548/)
[![Backend API](https://img.shields.io/badge/API-Deployed-blue)](https://csce-548.onrender.com)
[![Build Status](https://img.shields.io/badge/Build-Passing-success)](https://github.com/Thorvondrake/CSCE-548)
[![Java](https://img.shields.io/badge/Java-17+-orange)](https://adoptium.net/)
[![Database](https://img.shields.io/badge/Database-MySQL-blue)](https://aiven.io)
[![Deployment](https://img.shields.io/badge/Deploy-Render.com-purple)](https://render.com)

## Quick Start

### Try the Live Demo
1. **Web Interface**: [Nocturne Archive Live Demo](https://thorvondrake.github.io/CSCE-548/)
2. **API Endpoints**: [Backend Service](https://csce-548.onrender.com/authors)
3. **Search Features**:
   - Search authors by nationality: [American Authors](https://csce-548.onrender.com/authors/nationality/American)
   - Search sales by buyer: [James Crawford's Purchases](https://csce-548.onrender.com/sales/buyer/James%20Crawford)

### Local Development Setup
```bash
# Clone the repository
git clone https://github.com/Thorvondrake/CSCE-548.git
cd CSCE-548/NocturneArchiveProject

# Compile the Java application
javac -cp "src:lib/*" src/*.java

# Start the local server
java -cp "src:lib/*" App

# Open your browser
open http://localhost:8080
```

### Complete Deployment
For production deployment with cloud hosting, see **[DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)**

## Project Overview
The Nocturne Archive is a comprehensive multi-tiered Java application designed to manage rare book inventories, author information, title catalogs, and sales transactions. Built with a professional service-oriented architecture, it ensures complete separation of concerns between the user interface, business logic, and data persistence across all four database entities.

### Key Features
- **Complete CRUD Operations**: Full Create, Read, Update, Delete functionality for all entities
- **Advanced Search**: Filter authors by nationality and sales by buyer name
- **Real-time Web Interface**: Professional tabbed interface with form validation
- **Cloud-Hosted**: Production deployment on Render.com with Aiven.io database
- **RESTful API**: HTTP endpoints for all business operations
- **Professional Architecture**: 3-tier separation with proper error handling

## Architecture Overview
This project implements a professional 3-tier architecture with complete separation of concerns:

- **Presentation Layer**: Modern web interface (index.html) with HTTP server (App.java)
- **Service Layer**: RESTful API services for all entities with advanced search capabilities
- **Business Layer**: Domain-specific validation rules and business logic implementation
- **Data Layer**: JDBC-based database operations with secure connection management

### Technical Stack
- **Backend**: Java 17, JDBC, HTTP Server
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Database**: MySQL 8.0 (Aiven.io managed)
- **Hosting**: Render.com (Backend), GitHub Pages (Frontend)
- **Architecture**: 3-tier with REST-like APIs

## Full CRUD Web Interface

### Complete Entity Management
- **CREATE Operations**: Add new records via web forms for all 4 entities
- **READ Operations**: View all records, single records by ID, and filtered views
- **UPDATE Operations**: Modify existing records through web interface
- **DELETE Operations**: Available via service layer (optional for assignment)

### Enhanced Web Interface
- **Tabbed Navigation**: Separate management interfaces for each entity type
- **Form Validation**: Client-side and server-side validation for all inputs
- **Real-time Feedback**: Success/error messages for all operations
- **Professional Styling**: Modern dark theme with responsive design

## Entities Covered

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
    └── SafeServiceTestRunner.java (Production-Safe Test Runner)
```

## Service Layer API Patterns

All services follow REST-like patterns accessible via HTTP endpoints:

### AuthorService
- `getAuthors()` → GET /authors
- `getAuthor(id)` → GET /authors/{id}
- `getAuthorsByNationality(nationality)` → GET /authors/nationality/{nationality}
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
- `getSalesByBuyer(buyerName)` → GET /sales/buyer/{buyerName}
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

### Method 1: Web Interface
1. **Open Web Client**: Navigate to hosted frontend or localhost
2. **Test CREATE**: Use "Create New [Entity]" forms in each tab
3. **Test UPDATE**: Use "Update Existing [Entity]" forms
4. **Test READ**: Use "Get All" and "Get Single" operations
5. **Verify Database**: Check database records match web operations

### Method 2: Production-Safe Test Runner
```bash
cd NocturneArchiveProject
javac -cp "src:lib/*" src/*.java
java -cp "src:lib/*" SafeServiceTestRunner
```
*Note: Set `PRODUCTION=true` environment variable for read-only testing in production environments*

### Method 3: API Testing
```bash
# Test all CRUD operations via curl
curl -X GET https://csce-548.onrender.com/authors
curl -X POST https://csce-548.onrender.com/authors -H "Content-Type: application/json" -d '{"fullName":"Test Author","nationality":"Test","birthYear":2000}'
curl -X PUT https://csce-548.onrender.com/authors/1 -H "Content-Type: application/json" -d '{"id":1,"fullName":"Updated Author","nationality":"Updated","birthYear":2001}'
```

### Method 4: Web Server (Production Mode)
```bash
cd NocturneArchiveProject
javac -cp "src:lib/*" src/*.java
java -cp "src:lib/*" App
```
- Starts HTTP server on port 8080 (or PORT environment variable)
- Access via browser: http://localhost:8080
- Complete web interface with tabs for all 4 entities

### Method 5: Enhanced Web Interface Features
The comprehensive web interface provides advanced functionality:

#### Authors Management
- **Create & Update**: Professional forms with validation
- **Search by Nationality**: Filter authors by country (e.g., "American", "British", "Irish")
- **Complete CRUD**: Create, read by ID, update, count operations

#### Titles Management  
- **Author Association**: Link titles to existing authors
- **Genre Categorization**: Organize by literary genres
- **Author Filtering**: View all titles by specific author

#### Inventory Management
- **Condition Tracking**: Mint, Fine, Very Good, Good, Fair, Poor conditions
- **Signed Books Filter**: Separate view for autographed items
- **Price Management**: Update pricing for market changes

#### Sales Management
- **Transaction Recording**: Complete sale documentation
- **Buyer Search**: Find all purchases by specific buyer name
- **Revenue Analytics**: Real-time total revenue calculation
- **Item History**: View all sales for specific inventory items

### Search Features
- **Authors by Nationality**: Search for authors from specific countries with case-insensitive matching
- **Sales by Buyer**: Find all transactions for a particular customer with flexible name matching
- **URL-Safe Searches**: Handles spaces and special characters in search terms

## Deployment & Getting Started

### Quick Start Options

#### Option 1: Use the Live Demo (Recommended)
- **Web Interface**: [https://thorvondrake.github.io/CSCE-548/](https://thorvondrake.github.io/CSCE-548/)
- **API Testing**: [https://csce-548.onrender.com/authors](https://csce-548.onrender.com/authors)
- **Ready to use** - no setup required!

#### Option 2: Local Development
```bash
# Prerequisites: Java 17+, Git
git clone https://github.com/Thorvondrake/CSCE-548.git
cd CSCE-548/NocturneArchiveProject
javac -cp "src:lib/*" src/*.java
java -cp "src:lib/*" App
# Open http://localhost:8080
```

#### Option 3: Production Deployment
Complete cloud deployment with your own database:
1. **Follow**: [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for step-by-step instructions
2. **Setup**: Aiven.io database + Render.com hosting
3. **Result**: Your own professional cloud-hosted system

### Testing & Verification

#### Essential Tests (5 minutes)
1. **Web Interface**: Open live demo, test each tab
2. **CRUD Operations**: Create → Read → Update for any entity
3. **Search Features**: Try "American" authors, "James Crawford" sales
4. **API Endpoints**: Visit direct API URLs for JSON responses

#### Complete Testing (15 minutes)
See [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md#part-5-testing-all-functionality) for comprehensive testing procedures with screenshot requirements.

### Technical Details

**Environment Variables** (for production deployment):
```bash
DB_URL=mysql://user:pass@host:port/db?ssl-mode=REQUIRED
DB_USER=your_db_user  
DB_PASSWORD=your_db_password
PORT=10000
```

**Database Setup**: Use `production_database_reset.sql` for clean, professional data including 15 authors, 30 titles, 60+ inventory items, and sample sales records.

## Requirements Compliance

**Complete 3-tier Architecture**  
**All 4 Database Entities** (Authors, Titles, Inventory, Sales)  
**Full CRUD Operations** via Web Interface  
**Professional Service Layer** with REST-like APIs  
**Business Logic Layer** with validation and rules  
**Cloud Deployment** (Production-ready)  
**Enhanced Features** (Search by nationality, buyer filtering)

## Development & Contribution

This project demonstrates professional software engineering practices:
- **Multi-tier Architecture**: Clear separation of concerns
- **Cloud-Native Design**: Environment-agnostic configuration  
- **Professional APIs**: RESTful endpoints with proper error handling
- **Modern DevOps**: CI/CD with containerized deployment
- **Comprehensive Testing**: Multiple testing methodologies

## License

Academic project developed for CSCE 548 Software Engineering coursework.  
Provided for educational and demonstration purposes.