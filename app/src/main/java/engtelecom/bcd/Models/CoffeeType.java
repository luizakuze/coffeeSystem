package engtelecom.bcd.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa um tipo de café com seus ingredientes, preço e identificador.
 */
public class CoffeeType {
    private String productId;
    private String typeName;
    private int waterQty;
    private int coffeQty;
    private int milkQty;
    private int price;

    public CoffeeType() {
        // Construtor padrão necessário para reflexão do RepositoryCSV
    }

    public CoffeeType(String productId, String typeName, int waterQty, int coffeQty, int milkQty, int price) {
        this.productId = productId;
        this.typeName = typeName;
        this.waterQty = waterQty;
        this.coffeQty = coffeQty;
        this.milkQty = milkQty;
        this.price = price;
    }

    public String getId() {
        return productId;
    }

    public String getName() {
        return typeName;
    }

    public int getPrice() {
        return price;
    }

    /**
     * Retorna uma tabela hash com os ingredientes e suas quantidades.
     *
     * @return tabela hash com chaves "coffee", "milk", "water"
     */
    public Map<String, Integer> getIngredients() {
        Map<String, Integer> ingredients = new HashMap<>();
        ingredients.put("coffee", coffeQty);
        ingredients.put("milk", milkQty);
        ingredients.put("water", waterQty);
        return ingredients;
    }

    public String getTypeId() {
        return productId;
    }

    public void setTypeId(String productId) {
        this.productId = productId;
    }

    public String getType_name() {
        return typeName;
    }

    public void setType_name(String typeName) {
        this.typeName = typeName;
    }

    public int getwaterQty() {
        return waterQty;
    }

    public void setwaterQty(int waterQty) {
        this.waterQty = waterQty;
    }

    public int getCoffeQty() {
        return coffeQty;
    }

    public void setCoffeQty(int coffeQty) {
        this.coffeQty = coffeQty;
    }

    public int getMilkQty() {
        return milkQty;
    }

    public void setMilkQty(int milkQty) {
        this.milkQty = milkQty;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }
}