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

@Epic(value = "Авторизация пользователя")
public class LoginTest {
    private LoginUserRequest loginUserRequest;
    private UserApiClient userApiClient;
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
        loginUserRequest = new LoginUserRequest(createUserRequest.getEmail(), createUserRequest.getPassword());
    }

    @After
    public void down() {
        Response deleteResponse = userApiClient.deleteUser(token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteUserResponse deleteUserResponse = deleteResponse.as(DeleteUserResponse.class);
        assertTrue(deleteUserResponse.getSuccess());
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginCourierSuccess() {
        Response loginResponse = userApiClient.loginUser(loginUserRequest);
        assertEquals(SC_OK, loginResponse.statusCode());
        LoginUserResponse loginUserResponse = loginResponse.as(LoginUserResponse.class);
        assertTrue(loginUserResponse.getSuccess());
    }

    @Test
    @DisplayName("Логин c ошибкой")
    public void loginCourierWrong() {
        loginUserRequest.setEmail("");
        Response loginResponse = userApiClient.loginUser(loginUserRequest);
        assertEquals(SC_UNAUTHORIZED, loginResponse.statusCode());
        LoginUserResponse loginUserResponse = loginResponse.as(LoginUserResponse.class);
        assertFalse(loginUserResponse.getSuccess());
        assertEquals("email or password are incorrect", loginUserResponse.getMessage());
    }
}
