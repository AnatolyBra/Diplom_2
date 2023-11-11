package api.model.courier;

public class LoginCourierRequest {
    private String email;
    private String password;

    public LoginCourierRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LoginCourierRequest() {
    }
}
