import java.util.List;

/**
 * BUSINESS LAYER: TitleBusinessLogic
 * Handles all business rules and operations related to Title entities.
 * Acts as an intermediary between the Service Layer and Data Layer.
 */
public class TitleBusinessLogic {
    private final DatabaseManager db = new DatabaseManager();

    public List<Title> fetchAllTitles() {
        return db.getAllTitles();
    }

    public Title findTitleById(int id) {
        return db.getTitleById(id);
    }

    public void registerNewTitle(int authorId, String titleName, String genre, int originalPublicationYear) {
        // Business rule: Validate author exists
        Author author = db.getAuthorById(authorId);
        if (author == null) {
            System.out.println("Error: Author with ID " + authorId + " does not exist.");
            return;
        }
        
        // Business rule: Title name is required
        if (titleName == null || titleName.trim().isEmpty()) {
            System.out.println("Error: Title name cannot be empty.");
            return;
        }
        
        // Business rule: Publication year should be reasonable
        if (originalPublicationYear < 1000 || originalPublicationYear > 2024) {
            System.out.println("Error: Invalid publication year. Must be between 1000-2024.");
            return;
        }
        
        db.addTitle(authorId, titleName, genre, originalPublicationYear);
    }

    public void updateTitleDetails(int titleId, int authorId, String titleName, String genre, int originalPublicationYear) {
        // Validate title exists before updating
        Title existing = db.getTitleById(titleId);
        if (existing == null) {
            System.out.println("Error: Title with ID " + titleId + " not found.");
            return;
        }
        
        // Apply same business rules as creation
        Author author = db.getAuthorById(authorId);
        if (author == null) {
            System.out.println("Error: Author with ID " + authorId + " does not exist.");
            return;
        }
        
        if (titleName == null || titleName.trim().isEmpty()) {
            System.out.println("Error: Title name cannot be empty.");
            return;
        }
        
        if (originalPublicationYear < 1000 || originalPublicationYear > 2024) {
            System.out.println("Error: Invalid publication year. Must be between 1000-2024.");
            return;
        }
        
        db.updateTitle(titleId, authorId, titleName, genre, originalPublicationYear);
    }

    public void removeTitle(int titleId) {
        // Business rule: Check if title exists before deletion
        Title existing = db.getTitleById(titleId);
        if (existing == null) {
            System.out.println("Error: Title with ID " + titleId + " not found.");
            return;
        }
        db.deleteTitle(titleId);
    }

    // Business method: Get all titles by a specific author
    public List<Title> fetchTitlesByAuthor(int authorId) {
        List<Title> allTitles = db.getAllTitles();
        List<Title> authorTitles = new java.util.ArrayList<>();
        for (Title title : allTitles) {
            if (title.authorId == authorId) {
                authorTitles.add(title);
            }
        }
        return authorTitles;
    }
}