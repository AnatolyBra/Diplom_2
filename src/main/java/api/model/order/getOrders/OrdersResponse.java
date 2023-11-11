package api.model.order.getOrders;

import java.util.Date;

public class OrdersResponse {
    private String _id;
    private OrderIngredients[] orderIngredients;
    private String status;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private int number;

    public OrdersResponse(String _id, OrderIngredients[] orderIngredients, String status, String name, Date createdAt, Date updatedAt, int number) {
        this._id = _id;
        this.orderIngredients = orderIngredients;
        this.status = status;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.number = number;
    }

}
