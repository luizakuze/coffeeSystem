package engtelecom.bcd.Systems;

import engtelecom.bcd.Models.CoffeeType;
import engtelecom.bcd.Models.Invoice;
import engtelecom.bcd.Models.User;
import engtelecom.bcd.Repository.RepositoryCSV;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe que implementa um sistema de notas fiscais.
 */
public class InvoiceSystem {

    private final RepositoryCSV<Invoice> repository;

    public InvoiceSystem() {
        this.repository = new RepositoryCSV<>("db//invoices.csv", Invoice.class, "transactionId");
    }

    /**
     * Cria e armazena uma nova nota fiscal com a data e hora atuais.
     * Também debita o valor do café do saldo do usuário no arquivo CSV.
     *
     * @param user Usuário que realizou a compra
     * @param coffeeType Tipo de café comprado
     */
    public void addInvoice(User user, CoffeeType coffeeType) {
        String transactionId = "TRA-" + Math.abs((user.getUserId() + System.currentTimeMillis()).hashCode());
    
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    
        String productId = coffeeType.getProductId();
        double price = coffeeType.getPrice();
    
        // 💰 Debita o saldo do usuário
        double novoSaldo = user.getAccountBalance() - price;
        user.setAccountBalance(novoSaldo);
    
        // Atualiza o saldo diretamente no CSV, sem guardar RepositoryCSV como atributo
        RepositoryCSV<User> userRepo = new RepositoryCSV<>("db/users.csv", User.class, "userId");
        userRepo.updateField(user.getUserId(), "accountBalance", String.valueOf(novoSaldo));
    
        // 🧾 Cria e salva a nota fiscal
        Invoice invoice = new Invoice(user.getUserId(), transactionId, date, time, productId, price);
        repository.insert(invoice);
    }
    
    
    /**
     * Exibe o histórico de compras do usuário.
     * Se o mês for informado, o filtro será aplicado (com base no formato "MM").
     *
     * @param userId ID do usuário
     * @param month (opcional) Mês em formato "MM" (ex: "03" para março)
     * @return true se pelo menos uma transação foi encontrada, false caso contrário
     */
    public boolean history(String userId, String month) {
        List<Invoice> invoices = repository.selectAll();
        boolean found = false;

        System.out.println("📄 Histórico de compras:");
        for (Invoice inv : invoices) {
            if (inv.getUserId().equals(userId)) {
                if (month == null || inv.getDate().substring(5, 7).equals(month)) {
                    System.out.println(inv);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("❌ Nenhuma transação encontrada.");
        }
        
        return found;
    }
}
