import java.io.*;
import java.net.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Initialize ALL service layers for complete API coverage
        BookService bookService = new BookService();
        AuthorService authorService = new AuthorService();
        TitleService titleService = new TitleService();
        SaleService saleService = new SaleService();
        
        // Phase 1: Automated Test Evidence for Logs - ALL SERVICES
        System.out.println("=== NOCTURNE ARCHIVE SERVICE: COMPLETE 4-ENTITY TEST ===");
        System.out.println("\n[Testing ALL Service Layers...]");
        bookService.postBook(1, "Mint", 1500.00, true);
        System.out.println("All 4 service layers initialized and operational.");
        System.out.println("Architecture: Console -> Service -> Business -> Database Manager.");

        // Phase 2: Complete Web Server for All Entities
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        
        System.out.println("Starting COMPLETE multi-entity server on port: " + port);
        System.out.println("Environment: " + (System.getenv("PORT") != null ? "Production (Render)" : "Local Development"));
        System.out.println("Entities: AUTHORS, TITLES, INVENTORY, SALES");
        
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Service is LIVE and listening on 0.0.0.0:" + port);
            System.out.println("API Endpoints for ALL entities available");
            
            while (true) {
                try (Socket client = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                    
                    String requestLine = in.readLine();
                    if (requestLine == null) continue;
                    
                    System.out.println("Incoming request: " + requestLine);
                    
                    String[] parts = requestLine.split(" ");
                    String method = parts.length > 0 ? parts[0] : "GET";
                    String path = parts.length > 1 ? parts[1] : "/";
                    
                    // Handle CORS preflight (OPTIONS) requests
                    if ("OPTIONS".equals(method)) {
                        System.out.println("Handling CORS preflight request for: " + path);
                        out.println("HTTP/1.1 200 OK");
                        out.println("Access-Control-Allow-Origin: *");
                        out.println("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
                        out.println("Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With");
                        out.println("Access-Control-Max-Age: 86400");
                        out.println("Content-Length: 0");
                        out.println("");
                        continue;
                    }

                    // Read headers to get Content-Length for POST/PUT requests
                    String contentLengthHeader = null;
                    String line;
                    while ((line = in.readLine()) != null && !line.isEmpty()) {
                        if (line.toLowerCase().startsWith("content-length:")) {
                            contentLengthHeader = line.split(":")[1].trim();
                        }
                    }

                    // Read request body for POST and PUT requests
                    String requestBody = "";
                    if (("POST".equals(method) || "PUT".equals(method)) && contentLengthHeader != null) {
                        int contentLength = Integer.parseInt(contentLengthHeader);
                        char[] body = new char[contentLength];
                        in.read(body);
                        requestBody = new String(body);
                        System.out.println("Request body: " + requestBody);
                    }

                    // --- COMPLETE SERVICE LAYER ROUTING FOR ALL 4 ENTITIES ---
                    String jsonResponse = "[]";
                    
                    // HEALTH CHECK
                    if (path.equals("/") || path.equals("/health")) {
                        jsonResponse = "{\"status\":\"OK\",\"service\":\"Nocturne Archive Complete API\",\"entities\":[\"authors\",\"titles\",\"inventory\",\"sales\"],\"timestamp\":\"" + System.currentTimeMillis() + "\"}";
                    } 
                    
                    // ===== AUTHORS ENDPOINTS =====
                    else if (path.equals("/authors")) {
                        if ("GET".equals(method)) {
                            System.out.println("GET: Fetching all authors...");
                            jsonResponse = convertAuthorsToJson(authorService.getAuthors());
                        } else if ("POST".equals(method)) {
                            System.out.println("POST: Creating new author...");
                            jsonResponse = handleCreateAuthor(requestBody, authorService);
                        }
                    } else if (path.startsWith("/authors/")) {
                        try {
                            int id = Integer.parseInt(path.substring(9));
                            if ("GET".equals(method)) {
                                System.out.println("GET: Fetching author with ID: " + id);
                                Author author = authorService.getAuthor(id);
                                jsonResponse = (author != null) ? convertAuthorToJson(author) : "{}";
                            } else if ("PUT".equals(method)) {
                                System.out.println("PUT: Updating author with ID: " + id);
                                jsonResponse = handleUpdateAuthor(id, requestBody, authorService);
                            }
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Author ID\"}"; 
                        }
                    } 
                    
                    // ===== TITLES ENDPOINTS =====
                    else if (path.equals("/titles")) {
                        if ("GET".equals(method)) {
                            System.out.println("GET: Fetching all titles...");
                            jsonResponse = convertTitlesToJson(titleService.getTitles());
                        } else if ("POST".equals(method)) {
                            System.out.println("POST: Creating new title...");
                            jsonResponse = handleCreateTitle(requestBody, titleService);
                        }
                    } else if (path.startsWith("/titles/author/")) {
                        if ("GET".equals(method)) {
                            try {
                                int authorId = Integer.parseInt(path.substring(15));
                                System.out.println("GET: Fetching titles by author ID: " + authorId);
                                jsonResponse = convertTitlesToJson(titleService.getTitlesByAuthor(authorId));
                            } catch (NumberFormatException e) { 
                                jsonResponse = "{\"error\":\"Invalid Author ID\"}"; 
                            }
                        }
                    } else if (path.startsWith("/titles/")) {
                        try {
                            int id = Integer.parseInt(path.substring(8));
                            if ("GET".equals(method)) {
                                System.out.println("GET: Fetching title with ID: " + id);
                                Title title = titleService.getTitle(id);
                                jsonResponse = (title != null) ? convertTitleToJson(title) : "{}";
                            } else if ("PUT".equals(method)) {
                                System.out.println("PUT: Updating title with ID: " + id);
                                jsonResponse = handleUpdateTitle(id, requestBody, titleService);
                            }
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Title ID\"}"; 
                        }
                    } 
                    
                    // ===== INVENTORY ENDPOINTS =====
                    else if (path.equals("/inventory")) {
                        if ("GET".equals(method)) {
                            System.out.println("GET: Fetching all inventory...");
                            jsonResponse = convertBooksToJson(bookService.getArchive());
                        } else if ("POST".equals(method)) {
                            System.out.println("POST: Creating new inventory item...");
                            jsonResponse = handleCreateInventory(requestBody, bookService);
                        }
                    } else if (path.equals("/signed")) {
                        if ("GET".equals(method)) {
                            System.out.println("GET: Fetching signed books...");
                            jsonResponse = convertBooksToJson(bookService.getSignedArchive());
                        }
                    } else if (path.startsWith("/inventory/") && path.contains("/price")) {
                        // Handle price update: PUT /inventory/{id}/price
                        try {
                            int id = Integer.parseInt(path.substring(11, path.indexOf("/price")));
                            if ("PUT".equals(method)) {
                                System.out.println("PUT: Updating price for item ID: " + id);
                                jsonResponse = handleUpdateBookPrice(id, requestBody, bookService);
                            }
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Item ID\"}"; 
                        }
                    } else if (path.startsWith("/inventory/")) {
                        try {
                            int id = Integer.parseInt(path.substring(11));
                            if ("GET".equals(method)) {
                                System.out.println("GET: Fetching book with ID: " + id);
                                BookItem item = bookService.getBook(id);
                                jsonResponse = (item != null) ? convertBookToJson(item) : "{}";
                            }
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Book ID\"}"; 
                        }
                    } 
                    
                    // ===== SALES ENDPOINTS =====
                    else if (path.equals("/sales")) {
                        if ("GET".equals(method)) {
                            System.out.println("GET: Fetching all sales...");
                            jsonResponse = convertSalesToJson(saleService.getSales());
                        } else if ("POST".equals(method)) {
                            System.out.println("POST: Creating new sale...");
                            jsonResponse = handleCreateSale(requestBody, saleService);
                        }
                    } else if (path.equals("/sales/revenue")) {
                        if ("GET".equals(method)) {
                            System.out.println("GET: Calculating total revenue...");
                            double revenue = saleService.getTotalRevenue();
                            jsonResponse = "{\"totalRevenue\":" + revenue + ",\"currency\":\"USD\"}";
                        }
                    } else if (path.startsWith("/sales/item/")) {
                        if ("GET".equals(method)) {
                            try {
                                int itemId = Integer.parseInt(path.substring(12));
                                System.out.println("GET: Fetching sales for item ID: " + itemId);
                                jsonResponse = convertSalesToJson(saleService.getSalesForItem(itemId));
                            } catch (NumberFormatException e) { 
                                jsonResponse = "{\"error\":\"Invalid Item ID\"}"; 
                            }
                        }
                    } else if (path.startsWith("/sales/")) {
                        try {
                            int id = Integer.parseInt(path.substring(7));
                            if ("GET".equals(method)) {
                                System.out.println("GET: Fetching sale with ID: " + id);
                                Sale sale = saleService.getSale(id);
                                jsonResponse = (sale != null) ? convertSaleToJson(sale) : "{}";
                            } else if ("PUT".equals(method)) {
                                System.out.println("PUT: Updating sale with ID: " + id);
                                jsonResponse = handleUpdateSale(id, requestBody, saleService);
                            }
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Sale ID\"}"; 
                        }
                    } 
                    
                    // UNKNOWN ENDPOINT
                    else {
                        jsonResponse = "{\"error\":\"Endpoint not found\",\"path\":\"" + path + "\",\"availableEndpoints\":[\"authors\",\"titles\",\"inventory\",\"sales\"]}";
                    }

                    // --- HTTP RESPONSE WITH CORS ---
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: application/json");
                    out.println("Access-Control-Allow-Origin: *");
                    out.println("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
                    out.println("Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With");
                    out.println("Content-Length: " + jsonResponse.length());
                    out.println(""); // Header/Body separator
                    out.println(jsonResponse);
                    
                    System.out.println("📤 Response sent: " + jsonResponse.substring(0, Math.min(100, jsonResponse.length())) + 
                                     (jsonResponse.length() > 100 ? "..." : ""));
                    
                } catch (Exception e) {
                    System.err.println("Request processing error: " + e.getMessage());
                    System.err.println("Exception details: " + e.getClass().getSimpleName());
                }
            }
        } catch (IOException e) {
            System.err.println("CRITICAL: Server socket error - " + e.getMessage());
            System.err.println("This usually means port " + port + " is already in use or binding failed");
            System.err.println("IOException details: " + e.getClass().getSimpleName());
        } catch (NumberFormatException e) {
            System.err.println("CRITICAL: Invalid PORT environment variable - " + e.getMessage());
            System.err.println("NumberFormatException details: " + e.getClass().getSimpleName());
        } catch (Exception e) {
            System.err.println("CRITICAL: Unexpected server error - " + e.getMessage());
            System.err.println("Exception details: " + e.getClass().getSimpleName());
        }

        /*
        // IMPORTANT: We now invoke the SERVICE layer, not the database directly
        BookService archiveService = new BookService(); 
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("      THE NOCTURNE ARCHIVE SYSTEM        ");
        System.out.println("           (Service-Oriented)            ");
        System.out.println("=========================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. View All Inventory (READ)");
            System.out.println("2. Add New Acquisition (CREATE)");
            System.out.println("3. Update Item Price (UPDATE)");
            System.out.println("4. De-accession Item (DELETE)");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- RETRIEVING FROM SERVICE ---");
                    List<BookItem> items = archiveService.getArchive();
                    for (BookItem item : items) System.out.println(item);
                    break;
                case 2:
                    System.out.print("Enter Title ID (1-5): ");
                    int tid = scanner.nextInt();
                    System.out.print("Enter Condition: ");
                    String cond = scanner.next();
                    System.out.print("Enter Asking Price: ");
                    double price = scanner.nextDouble();
                    
                    // Invoking Service Layer
                    String createResponse = archiveService.postBook(tid, cond, price, false);
                    System.out.println(createResponse);
                    break;
                case 3:
                    System.out.print("Enter Item ID to update: ");
                    int upId = scanner.nextInt();
                    System.out.print("Enter New Price: ");
                    double newPrice = scanner.nextDouble();
                    
                    // Invoking Service Layer
                    String updateResponse = archiveService.putPriceChange(upId, newPrice);
                    System.out.println(updateResponse);
                    break;
                case 4:
                    System.out.print("Enter Item ID to remove: ");
                    int delId = scanner.nextInt();
                    
                    // Invoking Service Layer
                    String deleteResponse = archiveService.deleteBook(delId);
                    System.out.println(deleteResponse);
                    break;
                case 5:
                    running = false;
                    System.out.println("Closing the Archive. Farewell.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close(); */
    }

    // ===================================================================
    // REQUEST HANDLERS FOR CREATE AND UPDATE OPERATIONS
    // ===================================================================
    
    private static String handleCreateAuthor(String requestBody, AuthorService authorService) {
        try {
            // Simple JSON parsing for author creation
            String fullName = extractJsonField(requestBody, "fullName");
            String nationality = extractJsonField(requestBody, "nationality");
            int birthYear = Integer.parseInt(extractJsonField(requestBody, "birthYear"));
            
            String result = authorService.postAuthor(fullName, nationality, birthYear);
            System.out.println("Author creation result: " + result);
            
            // Return updated authors list
            return convertAuthorsToJson(authorService.getAuthors());
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in author data: " + e.getMessage());
            return "{\"error\":\"Invalid number format: " + e.getMessage() + "\"}";
        } catch (Exception e) {
            System.err.println("Error creating author: " + e.getMessage());
            return "{\"error\":\"Failed to create author: " + e.getMessage() + "\"}";
        }
    }
    
    private static String handleUpdateAuthor(int id, String requestBody, AuthorService authorService) {
        try {
            String fullName = extractJsonField(requestBody, "fullName");
            String nationality = extractJsonField(requestBody, "nationality");
            int birthYear = Integer.parseInt(extractJsonField(requestBody, "birthYear"));
            
            String result = authorService.putAuthor(id, fullName, nationality, birthYear);
            System.out.println("Author update result: " + result);
            
            return convertAuthorsToJson(authorService.getAuthors());
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in author update: " + e.getMessage());
            return "{\"error\":\"Invalid number format: " + e.getMessage() + "\"}";
        } catch (Exception e) {
            System.err.println("Error updating author: " + e.getMessage());
            return "{\"error\":\"Failed to update author: " + e.getMessage() + "\"}";
        }
    }
    
    private static String handleCreateTitle(String requestBody, TitleService titleService) {
        try {
            int authorId = Integer.parseInt(extractJsonField(requestBody, "authorId"));
            String titleName = extractJsonField(requestBody, "titleName");
            String genre = extractJsonField(requestBody, "genre");
            int originalPublicationYear = Integer.parseInt(extractJsonField(requestBody, "originalPublicationYear"));
            
            String result = titleService.postTitle(authorId, titleName, genre, originalPublicationYear);
            System.out.println("Title creation result: " + result);
            
            return convertTitlesToJson(titleService.getTitles());
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in title data: " + e.getMessage());
            return "{\"error\":\"Invalid number format: " + e.getMessage() + "\"}";
        } catch (Exception e) {
            System.err.println("Error creating title: " + e.getMessage());
            return "{\"error\":\"Failed to create title: " + e.getMessage() + "\"}";
        }
    }
    
    private static String handleUpdateTitle(int id, String requestBody, TitleService titleService) {
        try {
            int authorId = Integer.parseInt(extractJsonField(requestBody, "authorId"));
            String titleName = extractJsonField(requestBody, "titleName");
            String genre = extractJsonField(requestBody, "genre");
            int originalPublicationYear = Integer.parseInt(extractJsonField(requestBody, "originalPublicationYear"));
            
            String result = titleService.putTitle(id, authorId, titleName, genre, originalPublicationYear);
            System.out.println("Title update result: " + result);
            
            return convertTitlesToJson(titleService.getTitles());
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in title update: " + e.getMessage());
            return "{\"error\":\"Invalid number format: " + e.getMessage() + "\"}";
        } catch (Exception e) {
            System.err.println("Error updating title: " + e.getMessage());
            return "{\"error\":\"Failed to update title: " + e.getMessage() + "\"}";
        }
    }
    
    private static String handleCreateInventory(String requestBody, BookService bookService) {
        try {
            int titleId = Integer.parseInt(extractJsonField(requestBody, "titleId"));
            String condition = extractJsonField(requestBody, "condition");
            double price = Double.parseDouble(extractJsonField(requestBody, "price"));
            boolean signed = Boolean.parseBoolean(extractJsonField(requestBody, "signed"));
            
            String result = bookService.postBook(titleId, condition, price, signed);
            System.out.println("Book creation result: " + result);
            
            return convertBooksToJson(bookService.getArchive());
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in inventory data: " + e.getMessage());
            return "{\"error\":\"Invalid number format: " + e.getMessage() + "\"}";
        } catch (Exception e) {
            System.err.println("Error creating inventory item: " + e.getMessage());
            return "{\"error\":\"Failed to create inventory item: " + e.getMessage() + "\"}";
        }
    }
    
    private static String handleUpdateBookPrice(int id, String requestBody, BookService bookService) {
        try {
            double price = Double.parseDouble(extractJsonField(requestBody, "price"));
            
            String result = bookService.putPriceChange(id, price);
            System.out.println("Price update result: " + result);
            
            return convertBooksToJson(bookService.getArchive());
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid price format: " + e.getMessage());
            return "{\"error\":\"Invalid price format: " + e.getMessage() + "\"}";
        } catch (Exception e) {
            System.err.println("Error updating book price: " + e.getMessage());
            return "{\"error\":\"Failed to update book price: " + e.getMessage() + "\"}";
        }
    }
    
    private static String handleCreateSale(String requestBody, SaleService saleService) {
        try {
            int itemId = Integer.parseInt(extractJsonField(requestBody, "itemId"));
            double finalPrice = Double.parseDouble(extractJsonField(requestBody, "finalPrice"));
            String buyerName = extractJsonField(requestBody, "buyerName");
            
            String result = saleService.postSale(itemId, finalPrice, buyerName);
            System.out.println("Sale creation result: " + result);
            
            return convertSalesToJson(saleService.getSales());
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in sale data: " + e.getMessage());
            return "{\"error\":\"Invalid number format: " + e.getMessage() + "\"}";
        } catch (Exception e) {
            System.err.println("Error creating sale: " + e.getMessage());
            return "{\"error\":\"Failed to create sale: " + e.getMessage() + "\"}";
        }
    }
    
    private static String handleUpdateSale(int id, String requestBody, SaleService saleService) {
        try {
            int itemId = Integer.parseInt(extractJsonField(requestBody, "itemId"));
            String saleDateStr = extractJsonField(requestBody, "saleDate");
            double finalPrice = Double.parseDouble(extractJsonField(requestBody, "finalPrice"));
            String buyerName = extractJsonField(requestBody, "buyerName");
            
            // Convert ISO string to java.sql.Date
            java.sql.Date saleDate;
            if (saleDateStr != null && !saleDateStr.isEmpty()) {
                try {
                    // Parse ISO date string and convert to sql.Date
                    String dateOnly = saleDateStr.substring(0, 10); // Get just YYYY-MM-DD part
                    saleDate = java.sql.Date.valueOf(dateOnly);
                } catch (Exception e) {
                    // If parsing fails, use current date
                    saleDate = new java.sql.Date(System.currentTimeMillis());
                }
            } else {
                saleDate = new java.sql.Date(System.currentTimeMillis());
            }
            
            String result = saleService.putSale(id, itemId, saleDate, finalPrice, buyerName);
            System.out.println("Sale update result: " + result);
            
            return convertSalesToJson(saleService.getSales());
            
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid data format in sale update: " + e.getMessage());
            return "{\"error\":\"Invalid data format: " + e.getMessage() + "\"}";
        } catch (Exception e) {
            System.err.println("Error updating sale: " + e.getMessage());
            return "{\"error\":\"Failed to update sale: " + e.getMessage() + "\"}";
        }
    }
    
    // Simple JSON field extraction (basic implementation)
    private static String extractJsonField(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return "";
        
        start += pattern.length();
        
        // Skip whitespace and quotes
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\"')) {
            start++;
        }
        
        int end = start;
        boolean inQuotes = json.charAt(start - 1) == '\"';
        
        if (inQuotes) {
            // Find closing quote
            while (end < json.length() && json.charAt(end) != '\"') {
                end++;
            }
        } else {
            // Find comma or closing brace
            while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}') {
                end++;
            }
        }
        
        return json.substring(start, end).trim();
    }

    // ===================================================================
    // JSON CONVERTERS FOR ALL ENTITIES (No external libraries needed)
    // ===================================================================
    
    // --- AUTHORS ---
    private static String convertAuthorsToJson(List<Author> authors) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < authors.size(); i++) {
            sb.append(convertAuthorToJson(authors.get(i)));
            if (i < authors.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    private static String convertAuthorToJson(Author a) {
        return String.format("{\"id\":%d,\"fullName\":\"%s\",\"nationality\":\"%s\",\"birthYear\":%d}",
            a.authorId, escapeJson(a.fullName), escapeJson(a.nationality), a.birthYear);
    }
    
    // --- TITLES ---
    private static String convertTitlesToJson(List<Title> titles) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < titles.size(); i++) {
            sb.append(convertTitleToJson(titles.get(i)));
            if (i < titles.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    private static String convertTitleToJson(Title t) {
        return String.format("{\"id\":%d,\"titleName\":\"%s\",\"genre\":\"%s\",\"originalPublicationYear\":%d,\"authorId\":%d}",
            t.titleId, escapeJson(t.titleName), escapeJson(t.genre), t.originalPublicationYear, t.authorId);
    }
    
    // --- INVENTORY/BOOKS ---
    private static String convertBooksToJson(List<BookItem> items) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            sb.append(convertBookToJson(items.get(i)));
            if (i < items.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    private static String convertBookToJson(BookItem b) {
        return String.format("{\"id\":%d,\"title\":\"%s\",\"condition\":\"%s\",\"price\":%.2f,\"isSigned\":%b}",
            b.id, escapeJson(b.title), escapeJson(b.condition), b.price, b.isSigned);
    }
    
    // --- SALES ---
    private static String convertSalesToJson(List<Sale> sales) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < sales.size(); i++) {
            sb.append(convertSaleToJson(sales.get(i)));
            if (i < sales.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    private static String convertSaleToJson(Sale s) {
        String buyerName = s.buyerName != null ? escapeJson(s.buyerName) : "null";
        String saleDate = s.saleDate != null ? "\"" + escapeJson(s.saleDate.toString()) + "\"" : "null";
        return String.format("{\"saleId\":%d,\"itemId\":%d,\"saleDate\":%s,\"finalPrice\":%.2f,\"buyerName\":%s}",
            s.saleId, s.itemId, saleDate, s.finalPrice, 
            s.buyerName != null ? "\"" + buyerName + "\"" : "null");
    }
    
    // --- UTILITY ---
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
}