package api.model.user;

public class DeleteUserRequest {
    private String email;
    private String password;

    public DeleteUserRequest(String email, String password) {
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
