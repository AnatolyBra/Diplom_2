package user;

import api.client.UserApiClient;
import api.model.user.*;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static api.helper.UserGenerator.getRandomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
@Epic(value = "Изменение данных пользователя")
public class ChangeUserTest {
    private UserApiClient userApiClient;
    private UserResponse user;
    private String token;

    @Before
    public void setUp() {
        userApiClient = new UserApiClient();
        CreateUserRequest createUserRequest = getRandomCourier();
        Response createResponse = userApiClient.createUser(createUserRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertTrue(createUserResponse.getSuccess());
        token = createUserResponse.getAccessToken();
        user = new UserResponse(createUserRequest.getEmail(), "Дишольд");
    }

    @After
    public void down() {
        Response deleteResponse = userApiClient.deleteUser(token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteUserResponse deleteUserResponse = deleteResponse.as(DeleteUserResponse.class);
        assertTrue(deleteUserResponse.getSuccess());
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    public void updateCourier() {
        Response updateResponse = userApiClient.updateUser(user, token);
        assertEquals(SC_OK, updateResponse.statusCode());
        LoginUserResponse loginUserResponse = updateResponse.as(LoginUserResponse.class);
        assertTrue(loginUserResponse.getSuccess());
    }

    @Test
    @DisplayName("Обновление данных пользователя c ошибкой")
    public void updateCourierWrong() {
        Response updateResponse = userApiClient.updateUserWrong(user);
        assertEquals(SC_UNAUTHORIZED, updateResponse.statusCode());
        LoginUserResponse loginUserResponse = updateResponse.as(LoginUserResponse.class);
        assertFalse(loginUserResponse.getSuccess());
        assertEquals("You should be authorised", loginUserResponse.getMessage());
    }
}
