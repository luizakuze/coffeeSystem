package engtelecom.bcd.Models;

/**
 * Classe que representa uma fatura de compra de um produto.
 */
public class Invoice {
    private String userId;
    private String transactionId;
    private String date;
    private String time;
    private String productId;
    private double price;

    public Invoice() {
    }

    public Invoice(String userId, String transactionId, String date, String time, String productId, double price) {
        this.userId = userId;
        this.transactionId = transactionId;
        this.date = date;
        this.time = time;
        this.productId = productId;
        this.price = price;
    }
    

    public String getUserId() {
        return userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "🧾 " + date + " " + time + " | Produto: " + productId + " | Valor: R$ " + String.format("%.2f", price);
    }
}
