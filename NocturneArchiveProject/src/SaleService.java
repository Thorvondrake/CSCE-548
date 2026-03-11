import java.sql.Date;
import java.util.List;

/**
 * SERVICE LAYER: SaleService
 * This service provides REST-like API operations for Sale management.
 * It acts as the public interface for the Nocturne Archive Sales operations.
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
public class SaleService {
    private final SaleBusinessLogic business = new SaleBusinessLogic();

    /**
     * GET /sales - Retrieve all sales
     */
    public List<Sale> getSales() {
        return business.fetchAllSales();
    }

    /**
     * GET /sales/{id} - Retrieve specific sale
     */
    public Sale getSale(int id) {
        return business.findSaleById(id);
    }

    /**
     * GET /sales/item/{itemId} - Get sales for specific item
     */
    public List<Sale> getSalesForItem(int itemId) {
        return business.fetchSalesForItem(itemId);
    }

    /**
     * GET /sales/revenue - Get total revenue
     */
    public double getTotalRevenue() {
        return business.calculateTotalRevenue();
    }

    /**
     * POST /sales - Record new sale
     */
    public String postSale(int itemId, Date saleDate, double finalPrice, String buyerName) {
        business.recordNewSale(itemId, saleDate, finalPrice, buyerName);
        return "Success: Sale recorded in Nocturne Archive Service.";
    }

    /**
     * POST /sales - Record new sale with current date
     */
    public String postSale(int itemId, double finalPrice, String buyerName) {
        Date currentDate = new Date(System.currentTimeMillis());
        business.recordNewSale(itemId, currentDate, finalPrice, buyerName);
        return "Success: Sale recorded in Nocturne Archive Service.";
    }

    /**
     * PUT /sales/{id} - Update existing sale
     */
    public String putSale(int id, int itemId, Date saleDate, double finalPrice, String buyerName) {
        business.updateSaleDetails(id, itemId, saleDate, finalPrice, buyerName);
        return "Success: Sale " + id + " updated in service.";
    }

    /**
     * DELETE /sales/{id} - Remove sale record
     */
    public String deleteSale(int id) {
        business.removeSale(id);
        return "Success: Sale record purged from service.";
    }
}