package api.model.user;

public class DeleteUserResponse {
    private Boolean success;
    private String message;

    public DeleteUserResponse() {
    }

    public DeleteUserResponse(Boolean success, String message) {
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
