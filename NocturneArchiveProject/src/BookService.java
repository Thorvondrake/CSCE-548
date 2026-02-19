import java.util.List;

public class BookService {
    private BookBusinessLogic business = new BookBusinessLogic();

    public List<BookItem> getArchive() {
        return business.fetchAllInventory();
    }

    public String postBook(int tid, String cond, double p, boolean s) {
        business.acquireNewBook(tid, cond, p, s);
        return "Success: Book added to Nocturne Archive Service.";
    }

    public String putPriceChange(int id, double p) {
        business.adjustPrice(id, p);
        return "Success: Service updated item " + id;
    }

    public String deleteBook(int id) {
        business.removeRecord(id);
        return "Success: Item purged from service.";
    }
}
