package engtelecom.bcd.Systems;

import engtelecom.bcd.Models.CoffeeType;
import engtelecom.bcd.Models.User;
import engtelecom.bcd.Repository.RepositoryCSV;

import java.util.List;
import java.util.Map;

/**
 * Classe que implementa um sistema de cafeteira.
 */
public class CoffeeSystem {
    private final RepositoryCSV<CoffeeType> repository;
    private int coffeeStock;
    private int milkStock;
    private int waterStock;
    private boolean isWorking;

    public CoffeeSystem() {
        this.repository = new RepositoryCSV<>("db/coffeetypes.csv", CoffeeType.class, "productId");
        this.coffeeStock = 10;
        this.milkStock = 10;
        this.waterStock = 10;
        this.isWorking = true;
    }

    /**
     * Exibe no console o menu com os cafés disponíveis e seus preços.
     * Os cafés são numerados para facilitar a escolha.
     */
    public void getCoffeeMenu() {
        System.out.println("📋 Menu de Cafés:");
        List<CoffeeType> coffeeTypes = repository.selectAll();
        for (int i = 0; i < coffeeTypes.size(); i++) {
            CoffeeType coffeeType = coffeeTypes.get(i);
            System.out.println((i + 1) + ". " + coffeeType.getName() + " (R$" + coffeeType.getPrice() + ")");
        }
    }

    /**
     * Prepara um café caso haja ingredientes suficientes
     * e saldo disponível na conta do usuário.
     *
     * @param coffeeTypeId ID do tipo de café (ex: "CAF-001")
     * @param user Usuário que está solicitando o café
     * @return true se o café foi preparado com sucesso, false caso contrário
     */
    public boolean makeCoffee(String coffeeTypeId, User user) {
        CoffeeType coffee = repository.selectById(coffeeTypeId);
    
        if (!isWorking || coffee == null || !theresIngredients(coffee)) {
            return false;
        }
    
        double preco = coffee.getPrice();
        if (user.getAccountBalance() < preco) {
            return false;
        }
    
        // Desconta ingredientes
        Map<String, Integer> ingredientes = coffee.getIngredients();
        coffeeStock -= ingredientes.getOrDefault("coffee", 0);
        milkStock -= ingredientes.getOrDefault("milk", 0);
        waterStock -= ingredientes.getOrDefault("water", 0);
    
        // Desconta saldo do usuário
        double novoSaldo = user.getAccountBalance() - preco;
        user.setAccountBalance(novoSaldo);
    
        return true;
    }

    /**
     * Verifica se há ingredientes suficientes para preparar o tipo de café.
     *
     * @param coffeeType Tipo de café desejado
     * @return true se há ingredientes suficientes, false caso contrário
     */
    private boolean theresIngredients(CoffeeType coffeeType) {
        Map<String, Integer> ingredients = coffeeType.getIngredients();
        return coffeeStock >= ingredients.getOrDefault("coffee", 0) &&
               milkStock >= ingredients.getOrDefault("milk", 0) &&
               waterStock >= ingredients.getOrDefault("water", 0);
    }

    /**
     * Retorna o tipo de café com base na posição do menu.
     *
     * @param number Número correspondente à posição no menu
     * @return Objeto CoffeeType correspondente, ou null se número inválido
     */
    public CoffeeType getCoffeeByMenuNumber(int number) {
        List<CoffeeType> lista = repository.selectAll();
        if (number >= 1 && number <= lista.size()) {
            return lista.get(number - 1);
        }
        return null;
    }

    public int getCoffeeStock() {
        return coffeeStock;
    }

    public int getMilkStock() {
        return milkStock;
    }

    public int getWaterStock() {
        return waterStock;
    }

    /**
     * Retorna uma string com o status atual da cafeteira,
     * incluindo níveis de ingredientes e se está funcionando.
     *
     * @return String com status completo da cafeteira
     */
    public String getStatus() {
        return "\n📊 Status da Cafeteira:\n" +
               "- Café: " + coffeeStock +
               "\n- Leite: " + milkStock +
               "\n- Água: " + waterStock +
               "\n- Funcionando: " + (isWorking ? "Sim ✅" : "Não ❌");
    }

    /**
     * Recupera um tipo de café com base no ID (ex: "CAF-001").
     *
     * @param cafeId ID do tipo de café
     * @return Objeto CoffeeType correspondente ou null
     */
    public CoffeeType getCoffeeById(String cafeId) {

        
        return repository.selectById(cafeId);
    }

    /**
     * Reabastece o estoque de ingredientes da cafeteira ao valor cheio (10).
     */
    public void refillCoffee() {
        this.coffeeStock = 10;
        this.milkStock = 10;
        this.waterStock = 10;
    }
}
