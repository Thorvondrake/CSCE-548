import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
// This looks for a "DB_URL" environment variable on Render.
    // If it doesn't find one (like on your laptop), it defaults to your local MySQL.
    private final String url = System.getenv("DB_URL") != null 
                         ? System.getenv("DB_URL") 
                         : "jdbc:mysql://mysql-nocture-archive-nocturne-archive.a.aivencloud.com:11433/defaultdb?ssl-mode=REQUIRED";

    private final String username = System.getenv("DB_USER") != null 
                              ? System.getenv("DB_USER") 
                              : "avnadmin";

    // Use System.getenv to pull the secret from Render's environment
    private final String password = System.getenv("DB_PASSWORD") != null 
                            ? System.getenv("DB_PASSWORD") 
                            : "AVNS_lqHtEMxKIOMm3uWH_GY";

    // Helper method to get a connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // --- CREATE ---
    public void addItem(int titleId, String condition, double price, boolean signed) {
        String sql = "INSERT INTO Inventory_Items (TitleID, Condition_Grade, Asking_Price, Is_Signed) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, titleId);
            stmt.setString(2, condition);
            stmt.setDouble(3, price);
            stmt.setBoolean(4, signed);
            stmt.executeUpdate();
            System.out.println("Item added successfully to The Nocturne Archive.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // --- READ ---
    public List<BookItem> getAllItems() {
        List<BookItem> items = new ArrayList<>();
        String sql = "SELECT i.ItemID, t.TitleName, i.Condition_Grade, i.Asking_Price, i.Is_Signed " +
                     "FROM Inventory_Items i JOIN Titles t ON i.TitleID = t.TitleID";
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new BookItem(
                    rs.getInt("ItemID"),
                    rs.getString("TitleName"),
                    rs.getString("Condition_Grade"),
                    rs.getDouble("Asking_Price"),
                    rs.getBoolean("Is_Signed")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving all items: " + e.getMessage());
        }
        return items;
    }

    public BookItem getItemById(int id) {
        // JOIN with Titles to get the String 'TitleName' instead of just the int 'TitleID'
        String sql = "SELECT i.ItemID, t.TitleName, i.Condition_Grade, i.Asking_Price, i.Is_Signed " +
                    "FROM Inventory_Items i JOIN Titles t ON i.TitleID = t.TitleID " +
                    "WHERE i.ItemID = ?";
        
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new BookItem(
                        rs.getInt("ItemID"),
                        rs.getString("TitleName"), // Now this is a String, matching the constructor!
                        rs.getString("Condition_Grade"),
                        rs.getDouble("Asking_Price"),
                        rs.getBoolean("Is_Signed")
                    );
                }
            }
        } catch (SQLException e) { 
            System.err.println("Database error: " + e.getMessage()); 
        }
        return null;
    }

    // --- UPDATE ---
    public void updatePrice(int itemId, double newPrice) {
        String sql = "UPDATE Inventory_Items SET Asking_Price = ? WHERE ItemID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newPrice);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
            System.out.println("Price updated.");
        } catch (SQLException e) {
            System.err.println("Database error updating price: " + e.getMessage());
        }
    }

    // --- DELETE ---
    public void deleteItem(int itemId) {
        String sql = "DELETE FROM Inventory_Items WHERE ItemID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
            System.out.println("Item removed from archives.");
        } catch (SQLException e) {
            System.err.println("Database error deleting item: " + e.getMessage());
        }
    }

    // ====================================================================
    // AUTHORS CRUD OPERATIONS
    // ====================================================================

    // --- CREATE Author ---
    public void addAuthor(String fullName, String nationality, int birthYear) {
        String sql = "INSERT INTO Authors (FullName, Nationality, BirthYear) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, nationality);
            stmt.setInt(3, birthYear);
            stmt.executeUpdate();
            System.out.println("Author added successfully to The Nocturne Archive.");
        } catch (SQLException e) {
            System.err.println("Database error adding author: " + e.getMessage());
        }
    }

    // --- READ Authors ---
    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT AuthorID, FullName, Nationality, BirthYear FROM Authors";
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                authors.add(new Author(
                    rs.getInt("AuthorID"),
                    rs.getString("FullName"),
                    rs.getString("Nationality"),
                    rs.getInt("BirthYear")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database error getting all authors: " + e.getMessage());
        }
        return authors;
    }

    public Author getAuthorById(int authorId) {
        String sql = "SELECT AuthorID, FullName, Nationality, BirthYear FROM Authors WHERE AuthorID = ?";
        
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, authorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Author(
                        rs.getInt("AuthorID"),
                        rs.getString("FullName"),
                        rs.getString("Nationality"),
                        rs.getInt("BirthYear")
                    );
                }
            }
        } catch (SQLException e) { 
            System.err.println("Database error: " + e.getMessage()); 
        }
        return null;
    }

    // --- UPDATE Author ---
    public void updateAuthor(int authorId, String fullName, String nationality, int birthYear) {
        String sql = "UPDATE Authors SET FullName = ?, Nationality = ?, BirthYear = ? WHERE AuthorID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, nationality);
            stmt.setInt(3, birthYear);
            stmt.setInt(4, authorId);
            stmt.executeUpdate();
            System.out.println("Author updated successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // --- DELETE Author ---
    public void deleteAuthor(int authorId) {
        String sql = "DELETE FROM Authors WHERE AuthorID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            stmt.executeUpdate();
            System.out.println("Author removed from archives.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // ====================================================================
    // TITLES CRUD OPERATIONS
    // ====================================================================

    // --- CREATE Title ---
    public void addTitle(int authorId, String titleName, String genre, int originalPublicationYear) {
        String sql = "INSERT INTO Titles (AuthorID, TitleName, Genre, OriginalPublicationYear) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            stmt.setString(2, titleName);
            stmt.setString(3, genre);
            stmt.setInt(4, originalPublicationYear);
            stmt.executeUpdate();
            System.out.println("Title added successfully to The Nocturne Archive.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // --- READ Titles ---
    public List<Title> getAllTitles() {
        List<Title> titles = new ArrayList<>();
        String sql = "SELECT TitleID, AuthorID, TitleName, Genre, OriginalPublicationYear FROM Titles";
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                titles.add(new Title(
                    rs.getInt("TitleID"),
                    rs.getInt("AuthorID"),
                    rs.getString("TitleName"),
                    rs.getString("Genre"),
                    rs.getInt("OriginalPublicationYear")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return titles;
    }

    public Title getTitleById(int titleId) {
        String sql = "SELECT TitleID, AuthorID, TitleName, Genre, OriginalPublicationYear FROM Titles WHERE TitleID = ?";
        
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, titleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Title(
                        rs.getInt("TitleID"),
                        rs.getInt("AuthorID"),
                        rs.getString("TitleName"),
                        rs.getString("Genre"),
                        rs.getInt("OriginalPublicationYear")
                    );
                }
            }
        } catch (SQLException e) { 
            System.err.println("Database error: " + e.getMessage()); 
        }
        return null;
    }

    // --- UPDATE Title ---
    public void updateTitle(int titleId, int authorId, String titleName, String genre, int originalPublicationYear) {
        String sql = "UPDATE Titles SET AuthorID = ?, TitleName = ?, Genre = ?, OriginalPublicationYear = ? WHERE TitleID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            stmt.setString(2, titleName);
            stmt.setString(3, genre);
            stmt.setInt(4, originalPublicationYear);
            stmt.setInt(5, titleId);
            stmt.executeUpdate();
            System.out.println("Title updated successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // --- DELETE Title ---
    public void deleteTitle(int titleId) {
        String sql = "DELETE FROM Titles WHERE TitleID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, titleId);
            stmt.executeUpdate();
            System.out.println("Title removed from archives.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // ====================================================================
    // SALES_LOG CRUD OPERATIONS
    // ====================================================================

    // --- CREATE Sale ---
    public void addSale(int itemId, Date saleDate, double finalPrice, String buyerName) {
        String sql = "INSERT INTO Sales_Log (ItemID, Sale_Date, Final_Price, Buyer_Name) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.setDate(2, saleDate);
            stmt.setDouble(3, finalPrice);
            stmt.setString(4, buyerName);
            stmt.executeUpdate();
            System.out.println("Sale recorded successfully in The Nocturne Archive.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // --- READ Sales ---
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT SaleID, ItemID, Sale_Date, Final_Price, Buyer_Name FROM Sales_Log";
        
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("SaleID"),
                    rs.getInt("ItemID"),
                    rs.getDate("Sale_Date"),
                    rs.getDouble("Final_Price"),
                    rs.getString("Buyer_Name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return sales;
    }

    public Sale getSaleById(int saleId) {
        String sql = "SELECT SaleID, ItemID, Sale_Date, Final_Price, Buyer_Name FROM Sales_Log WHERE SaleID = ?";
        
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, saleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Sale(
                        rs.getInt("SaleID"),
                        rs.getInt("ItemID"),
                        rs.getDate("Sale_Date"),
                        rs.getDouble("Final_Price"),
                        rs.getString("Buyer_Name")
                    );
                }
            }
        } catch (SQLException e) { 
            System.err.println("Database error: " + e.getMessage()); 
        }
        return null;
    }

    // --- UPDATE Sale ---
    public void updateSale(int saleId, int itemId, Date saleDate, double finalPrice, String buyerName) {
        String sql = "UPDATE Sales_Log SET ItemID = ?, Sale_Date = ?, Final_Price = ?, Buyer_Name = ? WHERE SaleID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.setDate(2, saleDate);
            stmt.setDouble(3, finalPrice);
            stmt.setString(4, buyerName);
            stmt.setInt(5, saleId);
            stmt.executeUpdate();
            System.out.println("Sale updated successfully.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // --- DELETE Sale ---
    public void deleteSale(int saleId) {
        String sql = "DELETE FROM Sales_Log WHERE SaleID = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saleId);
            stmt.executeUpdate();
            System.out.println("Sale record removed from archives.");
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
