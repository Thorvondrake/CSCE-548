import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("      THE NOCTURNE ARCHIVE SYSTEM        ");
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
                    System.out.println("\n--- CURRENT ARCHIVE INVENTORY ---");
                    List<BookItem> items = db.getAllItems();
                    for (BookItem item : items) System.out.println(item);
                    break;
                case 2:
                    System.out.print("Enter Title ID (1-5): ");
                    int tid = scanner.nextInt();
                    System.out.print("Enter Condition (Fine/Mint/Good): ");
                    String cond = scanner.next();
                    System.out.print("Enter Asking Price: ");
                    double price = scanner.nextDouble();
                    db.addItem(tid, cond, price, false);
                    break;
                case 3:
                    System.out.print("Enter Item ID to update: ");
                    int upId = scanner.nextInt();
                    System.out.print("Enter New Price: ");
                    double newPrice = scanner.nextDouble();
                    db.updatePrice(upId, newPrice);
                    break;
                case 4:
                    System.out.print("Enter Item ID to remove: ");
                    int delId = scanner.nextInt();
                    db.deleteItem(delId);
                    break;
                case 5:
                    running = false;
                    System.out.println("Closing the Archive. Farewell.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }
}