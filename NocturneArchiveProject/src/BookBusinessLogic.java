import java.util.List;

public class BookBusinessLogic {
    private DatabaseManager db = new DatabaseManager();

    // READ
    public List<BookItem> fetchAllInventory() {
        return db.getAllItems();
    }

    // CREATE with Validation
    public void acquireNewBook(int titleId, String condition, double price, boolean signed) {
        if (price < 1.00) {
            System.out.println("Business Rule Error: Rare books must be valued at $1.00 or more.");
            return;
        }
        db.addItem(titleId, condition, price, signed);
    }

    // UPDATE
    public void adjustPrice(int itemId, double newPrice) {
        db.updatePrice(itemId, newPrice);
    }

    // DELETE
    public void removeRecord(int itemId) {
        db.deleteItem(itemId);
    }
}