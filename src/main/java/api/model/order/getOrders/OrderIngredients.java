package api.model.order.getOrders;

public class OrderIngredients {
    private String ingredients;

    public OrderIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }
}