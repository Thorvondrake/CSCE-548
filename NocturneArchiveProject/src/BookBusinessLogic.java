import java.util.ArrayList;
import java.util.List;

public class BookBusinessLogic {
    private final DatabaseManager db = new DatabaseManager();

    public List<BookItem> fetchAllInventory() {
        return db.getAllItems();
    }

    // Resolves "method findItemById(int) is undefined"
    public BookItem findItemById(int id) {
        return db.getItemById(id);
    }

    // Resolves "method fetchSignedBooks() is undefined"
    public List<BookItem> fetchSignedBooks() {
        List<BookItem> all = db.getAllItems();
        List<BookItem> signed = new ArrayList<>();
        for (BookItem item : all) {
            if (item.isSigned) signed.add(item);
        }
        return signed;
    }

    public void acquireNewBook(int titleId, String condition, double price, boolean signed) {
        if (price < 1.00) return;
        db.addItem(titleId, condition, price, signed);
    }

    public void adjustPrice(int itemId, double newPrice) {
        db.updatePrice(itemId, newPrice);
    }

    public void removeRecord(int itemId) {
        db.deleteItem(itemId);
    }
}