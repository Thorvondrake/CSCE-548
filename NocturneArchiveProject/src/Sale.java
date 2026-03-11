import java.sql.Date;

public class Sale {
    public int saleId;
    public int itemId;
    public Date saleDate;
    public double finalPrice;
    public String buyerName;

    public Sale(int saleId, int itemId, Date saleDate, double finalPrice, String buyerName) {
        this.saleId = saleId;
        this.itemId = itemId;
        this.saleDate = saleDate;
        this.finalPrice = finalPrice;
        this.buyerName = buyerName;
    }

    @Override
    public String toString() {
        return String.format("SaleID: %-3d | ItemID: %-3d | Date: %s | $%.2f | Buyer: %s", 
            saleId, itemId, saleDate != null ? saleDate.toString() : "Unknown", 
            finalPrice, buyerName != null ? buyerName : "Anonymous");
    }
}