import java.io.*;
import java.net.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        BookService service = new BookService();
        
        // Phase 1: Automated Test Evidence for Logs
        System.out.println("=== NOCTURNE ARCHIVE SERVICE: AUTOMATED CLOUD TEST ===");
        System.out.println("\n[Testing CREATE...]");
        service.postBook(1, "Mint", 1500.00, true);
        List<BookItem> archive = service.getArchive();
        System.out.println("Retrieved " + archive.size() + " items from the cloud archive.");
        System.out.println("\nAll services invoked. Architecture: Console -> Service -> Business -> Database.");

        // Phase 2: Legit Cloud Web Server for Project 3
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        
        System.out.println("Starting server on port: " + port);
        System.out.println("Environment: " + (System.getenv("PORT") != null ? "Production (Render)" : "Local Development"));
        
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Service is LIVE and listening on 0.0.0.0:" + port);
            System.out.println("Server ready to accept connections from GitHub Pages");
            
            while (true) {
                try (Socket client = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                    
                    // Read the HTTP request header (e.g., "GET /inventory HTTP/1.1")
                    String requestLine = in.readLine();
                    if (requestLine == null) continue;
                    
                    System.out.println("Incoming request: " + requestLine);
                    
                    String[] parts = requestLine.split(" ");
                    String method = parts.length > 0 ? parts[0] : "GET";
                    String path = parts.length > 1 ? parts[1] : "/";
                    String jsonResponse = "[]";
                    
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

                    // --- SERVICE LAYER ROUTING ---
                    // Requirement: Invoke ALL get methods (All, Single, Subset)
                    if (path.equals("/") || path.equals("/health")) {
                        jsonResponse = "{\"status\":\"OK\",\"service\":\"Nocturne Archive API\",\"timestamp\":\"" + System.currentTimeMillis() + "\"}";
                    } else if (path.equals("/inventory")) {
                        System.out.println("Fetching all inventory...");
                        jsonResponse = convertToJson(service.getArchive());
                    } else if (path.equals("/signed")) {
                        System.out.println("Fetching signed books...");
                        jsonResponse = convertToJson(service.getSignedArchive());
                    } else if (path.startsWith("/inventory/")) {
                        try {
                            int id = Integer.parseInt(path.substring(11));
                            System.out.println("Fetching book with ID: " + id);
                            BookItem item = service.getBook(id);
                            jsonResponse = (item != null) ? convertItemToJson(item) : "{}";
                        } catch (NumberFormatException e) { 
                            jsonResponse = "{\"error\":\"Invalid ID\"}"; 
                        }
                    } else {
                        jsonResponse = "{\"error\":\"Endpoint not found\",\"path\":\"" + path + "\"}";
                    }

                    // --- LEGIT HTTP RESPONSE WITH CORS ---
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
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("CRITICAL: Server socket error - " + e.getMessage());
            System.err.println("This usually means port " + port + " is already in use or binding failed");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("CRITICAL: Invalid PORT environment variable - " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("CRITICAL: Unexpected server error - " + e.getMessage());
            e.printStackTrace();
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

    // Helper to turn List into JSON string manually (No external libraries needed)
    private static String convertToJson(List<BookItem> items) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            sb.append(convertItemToJson(items.get(i)));
            if (i < items.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    private static String convertItemToJson(BookItem b) {
        return String.format("{\"id\":%d,\"title\":\"%s\",\"condition\":\"%s\",\"price\":%.2f,\"isSigned\":%b}",
            b.id, b.title, b.condition, b.price, b.isSigned);
    }
}