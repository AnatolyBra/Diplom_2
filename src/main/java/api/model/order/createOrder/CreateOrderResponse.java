package api.model.order.createOrder;

public class CreateOrderResponse {
    private String name;
    private OrderResponse order;
    private boolean success;
    private String message;

    public CreateOrderResponse(String name, OrderResponse order, boolean success) {
        this.name = name;
        this.order = order;
        this.success = success;
    }

    public CreateOrderResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public OrderResponse getOrder() {
        return order;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
