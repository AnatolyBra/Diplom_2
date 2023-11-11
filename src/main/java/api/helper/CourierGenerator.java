package api.helper;

import api.model.courier.CreateCourierRequest;
import com.github.javafaker.Faker;

public class CourierGenerator {
    public static CreateCourierRequest getRandomCourier() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.animal().name();
        String name = faker.name().firstName();
        return new CreateCourierRequest(email, password, name);
    }
}
