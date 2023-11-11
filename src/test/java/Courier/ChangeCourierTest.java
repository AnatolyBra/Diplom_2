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
@Epic(value = "Изменение данных курьера")
public class ChangeCourierTest {
    private DeleteCourierRequest deleteCourierRequest;
    private CourierApiClient courierApiClient;
    private UserResponse user;
    private String token;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        CreateCourierRequest createCourierRequest = getRandomCourier();
        Response createResponse = courierApiClient.createCourier(createCourierRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateCourierResponse createCourierResponse = createResponse.as(CreateCourierResponse.class);
        assertTrue(createCourierResponse.getSuccess());
        token = createCourierResponse.getAccessToken();
        user = new UserResponse(createCourierRequest.getEmail(), "Дишольд");

        deleteCourierRequest = new DeleteCourierRequest(createCourierRequest.getEmail(), createCourierRequest.getPassword());
    }

    @After
    public void down() {
        Response deleteResponse = courierApiClient.deleteCourier(deleteCourierRequest, token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteCourierResponse deleteCourierResponse = deleteResponse.as(DeleteCourierResponse.class);
        assertTrue(deleteCourierResponse.getSuccess());
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    public void updateCourier() {
        Response updateResponse = courierApiClient.updateCourier(user, token);
        assertEquals(SC_OK, updateResponse.statusCode());
        LoginCourierResponse loginCourierResponse = updateResponse.as(LoginCourierResponse.class);
        assertTrue(loginCourierResponse.getSuccess());
    }

    @Test
    @DisplayName("Обновление данных пользователя c ошибкой")
    public void updateCourierWrong() {
        Response updateResponse = courierApiClient.updateCourierWrong(user);
        assertEquals(SC_UNAUTHORIZED, updateResponse.statusCode());
        LoginCourierResponse loginCourierResponse = updateResponse.as(LoginCourierResponse.class);
        assertFalse(loginCourierResponse.getSuccess());
        assertEquals("You should be authorised", loginCourierResponse.getMessage());
    }
}
