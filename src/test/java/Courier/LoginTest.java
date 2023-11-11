package Courier;

import api.client.CourierApiClient;
import api.model.courier.*;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static api.helper.CourierGenerator.getRandomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
@Epic(value = "Авторизация курьера")
public class LoginTest {
    private DeleteCourierRequest deleteCourierRequest;
    private LoginCourierRequest loginCourierRequest;
    private CourierApiClient courierApiClient;
    private String token;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        CreateCourierRequest createCourierRequest = getRandomCourier();
        deleteCourierRequest = new DeleteCourierRequest(createCourierRequest.getEmail(), createCourierRequest.getPassword());
        Response createResponse = courierApiClient.createCourier(createCourierRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateCourierResponse createCourierResponse = createResponse.as(CreateCourierResponse.class);
        assertTrue(createCourierResponse.getSuccess());
        token = createCourierResponse.getAccessToken();
        loginCourierRequest = new LoginCourierRequest(createCourierRequest.getEmail(), createCourierRequest.getPassword());
    }

    @After
    public void down() {
        Response deleteResponse = courierApiClient.deleteCourier(deleteCourierRequest, token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteCourierResponse deleteCourierResponse = deleteResponse.as(DeleteCourierResponse.class);
        assertTrue(deleteCourierResponse.getSuccess());
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginCourierSuccess() {
        Response loginResponse = courierApiClient.loginCourier(loginCourierRequest);
        assertEquals(SC_OK, loginResponse.statusCode());
        LoginCourierResponse loginCourierResponse = loginResponse.as(LoginCourierResponse.class);
        assertTrue(loginCourierResponse.getSuccess());
    }

    @Test
    @DisplayName("Логин c ошибкой")
    public void loginCourierWrong() {
        loginCourierRequest.setEmail("");
        Response loginResponse = courierApiClient.loginCourier(loginCourierRequest);
        assertEquals(SC_UNAUTHORIZED, loginResponse.statusCode());
        LoginCourierResponse loginCourierResponse = loginResponse.as(LoginCourierResponse.class);
        assertFalse(loginCourierResponse.getSuccess());
        assertEquals("email or password are incorrect", loginCourierResponse.getMessage());
    }
}
