package api.model.order.createorder;

import java.util.Date;

public class OwnerResponse {
    private String name;
    private String email;
    private Date createdAt;
    private Date updatedAt;

    public OwnerResponse() {
    }

    public OwnerResponse(String name, String email, Date createdAt, Date updatedAt) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

}
