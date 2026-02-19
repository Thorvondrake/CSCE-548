import java.util.List;

public class App {
    public static void main(String[] args) {

        BookService service = new BookService();
                
                System.out.println("=== NOCTURNE ARCHIVE SERVICE: AUTOMATED CLOUD TEST ===");

                // 1. CREATE
                System.out.println("\n[Testing CREATE...]");
                service.postBook(1, "Mint", 1500.00, true);

                // 2. READ
                System.out.println("\n[Testing READ...]");
                List<BookItem> archive = service.getArchive();
                System.out.println("Retrieved " + archive.size() + " items from the cloud archive.");

                // 3. UPDATE
                if(!archive.isEmpty()) {
                    System.out.println("\n[Testing UPDATE...]");
                    service.putPriceChange(archive.get(0).id, 2000.00);
                }

                // 4. DELETE
                if(!archive.isEmpty()) {
                    System.out.println("\n[Testing DELETE...]");
                    service.deleteBook(archive.get(archive.size()-1).id);
                }
                
                System.out.println("\nAll services invoked. Architecture: Console -> Service -> Business -> Database.");

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
}