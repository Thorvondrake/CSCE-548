import java.util.List;

/**
 * SERVICE LAYER: TitleService
 * This service provides REST-like API operations for Title management.
 * It acts as the public interface for the Nocturne Archive Title operations.
 * 
 * HOSTING AND DEPLOYMENT INFORMATION:
 * -------------------------------------------
 * PLATFORM: Render.com (Web Service) 
 * DATABASE: Aiven.io (Managed MySQL)
 * 
 * HOSTING INSTRUCTIONS:
 * 1. Containerization: Uses Dockerfile with Amazon Corretto OpenJDK 17
 * 2. CI/CD: Connected to GitHub for automatic deployment on push
 * 3. Environment Variables: DB_URL, DB_USER, DB_PASSWORD injected by Render
 * 4. Build: javac -cp ".:lib/*" $(find . -name "*.java") -d bin
 * 5. Run: java -cp "bin:lib/*" App (main application entry point)
 */
public class TitleService {
    private final TitleBusinessLogic business = new TitleBusinessLogic();

    /**
     * GET /titles - Retrieve all titles
     */
    public List<Title> getTitles() {
        return business.fetchAllTitles();
    }

    /**
     * GET /titles/{id} - Retrieve specific title
     */
    public Title getTitle(int id) {
        return business.findTitleById(id);
    }

    /**
     * GET /titles/author/{authorId} - Get titles by author
     */
    public List<Title> getTitlesByAuthor(int authorId) {
        return business.fetchTitlesByAuthor(authorId);
    }

    /**
     * POST /titles - Create new title
     */
    public String postTitle(int authorId, String titleName, String genre, int originalPublicationYear) {
        business.registerNewTitle(authorId, titleName, genre, originalPublicationYear);
        return "Success: Title added to Nocturne Archive Service.";
    }

    /**
     * PUT /titles/{id} - Update existing title
     */
    public String putTitle(int id, int authorId, String titleName, String genre, int originalPublicationYear) {
        business.updateTitleDetails(id, authorId, titleName, genre, originalPublicationYear);
        return "Success: Title " + id + " updated in service.";
    }

    /**
     * DELETE /titles/{id} - Remove title
     */
    public String deleteTitle(int id) {
        business.removeTitle(id);
        return "Success: Title purged from service.";
    }
}