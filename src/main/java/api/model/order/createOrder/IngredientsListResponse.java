package api.model.order.createOrder;

public class IngredientsListResponse {
    private boolean success;
    private IngredientsResponse[] data;

    public IngredientsListResponse(boolean success, IngredientsResponse[] data) {
        this.success = success;
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public IngredientsResponse[] getData() {
        return data;
    }
}
