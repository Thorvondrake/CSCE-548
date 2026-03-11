public class Author {
    public int authorId;
    public String fullName;
    public String nationality;
    public int birthYear;

    public Author(int authorId, String fullName, String nationality, int birthYear) {
        this.authorId = authorId;
        this.fullName = fullName;
        this.nationality = nationality;
        this.birthYear = birthYear;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | %-25s | %-15s | Born: %d", 
            authorId, fullName, nationality != null ? nationality : "Unknown", birthYear);
    }
}