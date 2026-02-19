/**
 * HOSTING INSTRUCTIONS:
 * To host this as a microservice, use Spring Boot.
 * 1. Add 'spring-boot-starter-web' to your dependencies.
 * 2. Annotate this class with @RestController.
 * 3. Deploy to Heroku, AWS, or Render.com by connecting your GitHub Repo.
 * For this college project, we are simulating the service invocation.
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