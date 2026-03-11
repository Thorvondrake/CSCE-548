public class Title {
    public int titleId;
    public int authorId;
    public String titleName;
    public String genre;
    public int originalPublicationYear;

    public Title(int titleId, int authorId, String titleName, String genre, int originalPublicationYear) {
        this.titleId = titleId;
        this.authorId = authorId;
        this.titleName = titleName;
        this.genre = genre;
        this.originalPublicationYear = originalPublicationYear;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | %-30s | %-20s | %d | AuthorID: %d", 
            titleId, titleName, genre != null ? genre : "Unknown", originalPublicationYear, authorId);
    }
}