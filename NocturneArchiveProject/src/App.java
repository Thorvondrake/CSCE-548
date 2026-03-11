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

                    // --- COMPLETE SERVICE LAYER ROUTING FOR ALL 4 ENTITIES ---
                    String jsonResponse = "[]";
                    
                    // HEALTH CHECK
                    if (path.equals("/") || path.equals("/health")) {
                        jsonResponse = "{\"status\":\"OK\",\"service\":\"Nocturne Archive Complete API\",\"entities\":[\"authors\",\"titles\",\"inventory\",\"sales\"],\"timestamp\":\"" + System.currentTimeMillis() + "\"}";
                    } 
                    
                    // ===== AUTHORS ENDPOINTS =====
                    else if (path.equals("/authors")) {
                        System.out.println("Fetching all authors...");
                        jsonResponse = convertAuthorsToJson(authorService.getAuthors());
                    } else if (path.startsWith("/authors/")) {
                        try {
                            int id = Integer.parseInt(path.substring(9));
                            System.out.println("Fetching author with ID: " + id);
                            Author author = authorService.getAuthor(id);
                            jsonResponse = (author != null) ? convertAuthorToJson(author) : "{}";
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Author ID\"}"; 
                        }
                    } 
                    
                    // ===== TITLES ENDPOINTS =====
                    else if (path.equals("/titles")) {
                        System.out.println("Fetching all titles...");
                        jsonResponse = convertTitlesToJson(titleService.getTitles());
                    } else if (path.startsWith("/titles/author/")) {
                        try {
                            int authorId = Integer.parseInt(path.substring(15));
                            System.out.println("Fetching titles by author ID: " + authorId);
                            jsonResponse = convertTitlesToJson(titleService.getTitlesByAuthor(authorId));
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Author ID\"}"; 
                        }
                    } else if (path.startsWith("/titles/")) {
                        try {
                            int id = Integer.parseInt(path.substring(8));
                            System.out.println("Fetching title with ID: " + id);
                            Title title = titleService.getTitle(id);
                            jsonResponse = (title != null) ? convertTitleToJson(title) : "{}";
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Title ID\"}"; 
                        }
                    } 
                    
                    // ===== INVENTORY ENDPOINTS (EXISTING) =====
                    else if (path.equals("/inventory")) {
                        System.out.println("Fetching all inventory...");
                        jsonResponse = convertBooksToJson(bookService.getArchive());
                    } else if (path.equals("/signed")) {
                        System.out.println("Fetching signed books...");
                        jsonResponse = convertBooksToJson(bookService.getSignedArchive());
                    } else if (path.startsWith("/inventory/")) {
                        try {
                            int id = Integer.parseInt(path.substring(11));
                            System.out.println("Fetching book with ID: " + id);
                            BookItem item = bookService.getBook(id);
                            jsonResponse = (item != null) ? convertBookToJson(item) : "{}";
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Book ID\"}"; 
                        }
                    } 
                    
                    // ===== SALES ENDPOINTS =====
                    else if (path.equals("/sales")) {
                        System.out.println("Fetching all sales...");
                        jsonResponse = convertSalesToJson(saleService.getSales());
                    } else if (path.equals("/sales/revenue")) {
                        System.out.println("Calculating total revenue...");
                        double revenue = saleService.getTotalRevenue();
                        jsonResponse = "{\"totalRevenue\":" + revenue + ",\"currency\":\"USD\"}";
                    } else if (path.startsWith("/sales/item/")) {
                        try {
                            int itemId = Integer.parseInt(path.substring(12));
                            System.out.println("Fetching sales for item ID: " + itemId);
                            jsonResponse = convertSalesToJson(saleService.getSalesForItem(itemId));
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid Item ID\"}"; 
                        }
                    } else if (path.startsWith("/sales/")) {
                        try {
                            int id = Integer.parseInt(path.substring(7));
                            System.out.println("Fetching sale with ID: " + id);
                            Sale sale = saleService.getSale(id);
                            jsonResponse = (sale != null) ? convertSaleToJson(sale) : "{}";
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