package api.model.courier;

public class DeleteCourierRequest {
    private String email;
    private String password;

    public DeleteCourierRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
