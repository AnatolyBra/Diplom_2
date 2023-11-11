package api.model.courier;

public class DeleteCourierResponse {
    private Boolean success;
    private String message;

    public DeleteCourierResponse() {
    }

    public DeleteCourierResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
