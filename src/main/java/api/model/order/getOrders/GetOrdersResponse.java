package api.model.order.getOrders;

public class GetOrdersResponse {
    private boolean success;
    private OrdersResponse[] orders;
    private String message;
    private int total;
    private int totalToday;

    public GetOrdersResponse(boolean success, OrdersResponse[] orders, int total, int totalToday) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    public GetOrdersResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
