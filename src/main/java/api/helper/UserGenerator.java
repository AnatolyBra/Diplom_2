package api.helper;

import api.model.user.CreateUserRequest;
import com.github.javafaker.Faker;

public class UserGenerator {
    public static CreateUserRequest getRandomCourier() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.animal().name();
        String name = faker.name().firstName();
        return new CreateUserRequest(email, password, name);
    }
}
