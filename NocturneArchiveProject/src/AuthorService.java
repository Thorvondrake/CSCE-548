import java.util.List;

/**
 * SERVICE LAYER: AuthorService
 * This service provides REST-like API operations for Author management.
 * It acts as the public interface for the Nocturne Archive Author operations.
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
public class AuthorService {
    private final AuthorBusinessLogic business = new AuthorBusinessLogic();

    /**
     * GET /authors - Retrieve all authors
     */
    public List<Author> getAuthors() {
        return business.fetchAllAuthors();
    }

    /**
     * GET /authors/{id} - Retrieve specific author
     */
    public Author getAuthor(int id) {
        return business.findAuthorById(id);
    }

    /**
     * GET /authors/nationality/{nationality} - Retrieve authors by nationality
     */
    public List<Author> getAuthorsByNationality(String nationality) {
        return business.findAuthorsByNationality(nationality);
    }

    /**
     * POST /authors - Create new author
     */
    public String postAuthor(String fullName, String nationality, int birthYear) {
        business.registerNewAuthor(fullName, nationality, birthYear);
        return "Success: Author added to Nocturne Archive Service.";
    }

    /**
     * PUT /authors/{id} - Update existing author
     */
    public String putAuthor(int id, String fullName, String nationality, int birthYear) {
        business.updateAuthorDetails(id, fullName, nationality, birthYear);
        return "Success: Author " + id + " updated in service.";
    }

    /**
     * DELETE /authors/{id} - Remove author
     */
    public String deleteAuthor(int id) {
        business.removeAuthor(id);
        return "Success: Author purged from service.";
    }
}