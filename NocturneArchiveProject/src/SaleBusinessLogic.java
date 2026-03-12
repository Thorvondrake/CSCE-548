import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * BUSINESS LAYER: SaleBusinessLogic
 * Handles all business rules and operations related to Sale entities.
 * Acts as an intermediary between the Service Layer and Data Layer.
 */
public class SaleBusinessLogic {
    private final DatabaseManager db = new DatabaseManager();

    public List<Sale> fetchAllSales() {
        return db.getAllSales();
    }

    public Sale findSaleById(int id) {
        return db.getSaleById(id);
    }

    public List<Sale> findSalesByBuyer(String buyerName) {
        // Business rule: Validate buyer name is not null or empty
        if (buyerName == null || buyerName.trim().isEmpty()) {
            System.out.println("Error: Buyer name cannot be empty.");
            return new ArrayList<>();
        }
        return db.getSalesByBuyer(buyerName.trim());
    }

    public void recordNewSale(int itemId, Date saleDate, double finalPrice, String buyerName) {
        // Business rule: Validate item exists and is available
        BookItem item = db.getItemById(itemId);
        if (item == null) {
            System.out.println("Error: Item with ID " + itemId + " does not exist.");
            return;
        }
        
        // Business rule: Final price must be positive
        if (finalPrice <= 0) {
            System.out.println("Error: Sale price must be greater than 0.");
            return;
        }
        
        // Business rule: Sale date cannot be null
        if (saleDate == null) {
            saleDate = new Date(System.currentTimeMillis()); // Use current date if null
        }
        
        // Business rule: Buyer name should not be empty (though anonymous is allowed)
        if (buyerName != null && buyerName.trim().isEmpty()) {
            buyerName = null; // Convert empty string to null for "Anonymous"
        }
        
        db.addSale(itemId, saleDate, finalPrice, buyerName);
    }

    public void updateSaleDetails(int saleId, int itemId, Date saleDate, double finalPrice, String buyerName) {
        // Validate sale exists before updating
        Sale existing = db.getSaleById(saleId);
        if (existing == null) {
            System.out.println("Error: Sale with ID " + saleId + " not found.");
            return;
        }
        
        // Apply same business rules as creation
        BookItem item = db.getItemById(itemId);
        if (item == null) {
            System.out.println("Error: Item with ID " + itemId + " does not exist.");
            return;
        }
        
        if (finalPrice <= 0) {
            System.out.println("Error: Sale price must be greater than 0.");
            return;
        }
        
        if (saleDate == null) {
            System.out.println("Error: Sale date cannot be null for updates.");
            return;
        }
        
        if (buyerName != null && buyerName.trim().isEmpty()) {
            buyerName = null;
        }
        
        db.updateSale(saleId, itemId, saleDate, finalPrice, buyerName);
    }

    public void removeSale(int saleId) {
        // Business rule: Check if sale exists before deletion
        Sale existing = db.getSaleById(saleId);
        if (existing == null) {
            System.out.println("Error: Sale with ID " + saleId + " not found.");
            return;
        }
        db.deleteSale(saleId);
    }

    // Business method: Calculate total revenue
    public double calculateTotalRevenue() {
        List<Sale> allSales = db.getAllSales();
        double total = 0.0;
        for (Sale sale : allSales) {
            total += sale.finalPrice;
        }
        return total;
    }

    // Business method: Get sales for a specific item
    public List<Sale> fetchSalesForItem(int itemId) {
        List<Sale> allSales = db.getAllSales();
        List<Sale> itemSales = new java.util.ArrayList<>();
        for (Sale sale : allSales) {
            if (sale.itemId == itemId) {
                itemSales.add(sale);
            }
        }
        return itemSales;
    }
}