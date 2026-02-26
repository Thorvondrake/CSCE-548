import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
// This looks for a "DB_URL" environment variable on Render.
    // If it doesn't find one (like on your laptop), it defaults to your local MySQL.
    private final String url = System.getenv("DB_URL") != null 
                         ? System.getenv("DB_URL") 
                         : "mysql://avnadmin:AVNS_lqHtEMxKIOMm3uWH_GY@mysql-nocture-archive-nocturne-archive.a.aivencloud.com:11433/defaultdb?ssl-mode=REQUIRED";

    private final String username = System.getenv("DB_USER") != null 
                              ? System.getenv("DB_USER") 
                              : "nocturne_admin";

    // Use System.getenv to pull the secret from Render's environment
    private final String password = System.getenv("DB_PASSWORD") != null 
                            ? System.getenv("DB_PASSWORD") 
                            : "Qazwsx123";

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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace(); 
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}