import java.util.List;

/**
 * SERVICE LAYER: BookService
 * * This layer acts as the public API for the Nocturne Archive System.
 * It invokes the Business Layer to process rare book data while maintaining 
 * architectural separation from the Data Layer.
 * * HOSTING AND DEPLOYMENT INFORMATION:
 * -------------------------------------------
 * PLATFORM: Render.com (Web Service)
 * DATABASE: Aiven.io (Managed MySQL)
 * * HOSTING INSTRUCTIONS:
 * 1. Containerization: The project uses a 'Dockerfile' to define an Amazon 
 * Corretto OpenJDK 17 environment.
 * 2. CI/CD: Render is connected to the GitHub repository for automatic 
 * rebuilds on push.
 * 3. Secrets Management: Database credentials are never hardcoded and are 
 * injected via Render Environment Variables (DB_URL, DB_USER, DB_PASSWORD).
 * 4. Build Command: javac -cp ".:lib/*" $(find . -name "*.java") -d bin.
 */

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
