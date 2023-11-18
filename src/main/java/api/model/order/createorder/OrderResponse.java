package api.model.order.createorder;

import java.util.Date;

public class OrderResponse {
    private int number;
    private IngredientsResponse[] ingredients;
    private String _id;
    private OwnerResponse owner;

    private String status;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private int price;
}
