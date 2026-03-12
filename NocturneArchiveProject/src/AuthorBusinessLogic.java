import java.util.ArrayList;
import java.util.List;

/**
 * BUSINESS LAYER: AuthorBusinessLogic
 * Handles all business rules and operations related to Author entities.
 * Acts as an intermediary between the Service Layer and Data Layer.
 */
public class AuthorBusinessLogic {
    private final DatabaseManager db = new DatabaseManager();

    public List<Author> fetchAllAuthors() {
        return db.getAllAuthors();
    }

    public Author findAuthorById(int id) {
        return db.getAuthorById(id);
    }

    public List<Author> findAuthorsByNationality(String nationality) {
        // Business rule: Validate nationality is not null or empty
        if (nationality == null || nationality.trim().isEmpty()) {
            System.out.println("Error: Nationality cannot be empty.");
            return new ArrayList<>();
        }
        return db.getAuthorsByNationality(nationality.trim());
    }

    public void registerNewAuthor(String fullName, String nationality, int birthYear) {
        // Business rule: Validate birth year is reasonable
        if (birthYear < 1000 || birthYear > 2024) {
            System.out.println("Error: Invalid birth year. Must be between 1000-2024.");
            return;
        }
        // Business rule: Full name is required
        if (fullName == null || fullName.trim().isEmpty()) {
            System.out.println("Error: Author name cannot be empty.");
            return;
        }
        db.addAuthor(fullName, nationality, birthYear);
    }

    public void updateAuthorDetails(int authorId, String fullName, String nationality, int birthYear) {
        // Validate author exists before updating
        Author existing = db.getAuthorById(authorId);
        if (existing == null) {
            System.out.println("Error: Author with ID " + authorId + " not found.");
            return;
        }
        // Apply same business rules as creation
        if (birthYear < 1000 || birthYear > 2024) {
            System.out.println("Error: Invalid birth year. Must be between 1000-2024.");
            return;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            System.out.println("Error: Author name cannot be empty.");
            return;
        }
        db.updateAuthor(authorId, fullName, nationality, birthYear);
    }

    public void removeAuthor(int authorId) {
        // Business rule: Check if author exists before deletion
        Author existing = db.getAuthorById(authorId);
        if (existing == null) {
            System.out.println("Error: Author with ID " + authorId + " not found.");
            return;
        }
        db.deleteAuthor(authorId);
    }
}