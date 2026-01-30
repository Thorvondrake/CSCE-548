public class BookItem {
    public int id;
    public String title;
    public String condition;
    public double price;
    public boolean isSigned;

    public BookItem(int id, String title, String condition, double price, boolean isSigned) {
        this.id = id;
        this.title = title;
        this.condition = condition;
        this.price = price;
        this.isSigned = isSigned;
    }

    @Override
    public String toString() {
        return String.format("ID: %-3d | %-20s | %-10s | $%.2f %s", 
            id, title, condition, price, (isSigned ? "[SIGNED]" : ""));
    }
}