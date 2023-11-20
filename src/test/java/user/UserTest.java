package user;

import api.client.UserApiClient;
import api.model.user.CreateUserRequest;
import api.model.user.CreateUserResponse;
import api.model.user.DeleteUserRequest;
import api.model.user.DeleteUserResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static api.helper.UserGenerator.getRandomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@Epic(value = "Создание пользователя")
public class UserTest {
    private CreateUserRequest createUserRequest;
    private UserApiClient userApiClient;

    @Before
    public void setUp() {
        userApiClient = new UserApiClient();
        createUserRequest = getRandomCourier();
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void createCourierSuccess() {
        Response createResponse = userApiClient.createUser(createUserRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertTrue(createUserResponse.getSuccess());
        String token = createUserResponse.getAccessToken();

        Response deleteResponse = userApiClient.deleteUser(token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteUserResponse deleteUserResponse = deleteResponse.as(DeleteUserResponse.class);
        assertTrue(deleteUserResponse.getSuccess());
    }

    @Test
    @DisplayName("Создание курьера который уже существует")
    public void createCourierSuccessDouble() {
        Response createResponse = userApiClient.createUser(createUserRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertTrue(createUserResponse.getSuccess());
        String token = createUserResponse.getAccessToken();

        createResponse = userApiClient.createUser(createUserRequest);
        assertEquals(SC_FORBIDDEN, createResponse.statusCode());
        createUserResponse = createResponse.as(CreateUserResponse.class);
        assertFalse(createUserResponse.getSuccess());
        assertEquals("User already exists", createUserResponse.getMessage());

        Response deleteResponse = userApiClient.deleteUser(token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteUserResponse deleteUserResponse = deleteResponse.as(DeleteUserResponse.class);
        assertTrue(deleteUserResponse.getSuccess());
    }

    @Test
    @DisplayName("Создание курьера без одного из полей")
    public void createCourierSuccessWithoutField() {
        createUserRequest.setEmail("");
        Response createResponse = userApiClient.createUser(createUserRequest);
        assertEquals(SC_FORBIDDEN, createResponse.statusCode());
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertFalse(createUserResponse.getSuccess());
        assertEquals("Email, password and name are required fields", createUserResponse.getMessage());
    }

}
