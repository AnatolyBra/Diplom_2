package Courier;

import api.client.CourierApiClient;
import api.model.courier.CreateCourierRequest;
import api.model.courier.CreateCourierResponse;
import api.model.courier.DeleteCourierRequest;
import api.model.courier.DeleteCourierResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static api.helper.CourierGenerator.getRandomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@Epic(value = "Создание курьера")
public class CourierTest {
    private CreateCourierRequest createCourierRequest;
    private DeleteCourierRequest deleteCourierRequest;
    private CourierApiClient courierApiClient;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        createCourierRequest = getRandomCourier();
        deleteCourierRequest = new DeleteCourierRequest(createCourierRequest.getEmail(), createCourierRequest.getPassword());
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void createCourierSuccess() {
        Response createResponse = courierApiClient.createCourier(createCourierRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateCourierResponse createCourierResponse = createResponse.as(CreateCourierResponse.class);
        assertTrue(createCourierResponse.getSuccess());
        String token = createCourierResponse.getAccessToken();

        Response deleteResponse = courierApiClient.deleteCourier(deleteCourierRequest, token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteCourierResponse deleteCourierResponse = deleteResponse.as(DeleteCourierResponse.class);
        assertTrue(deleteCourierResponse.getSuccess());
    }

    @Test
    @DisplayName("Создание курьера который уже существует")
    public void createCourierSuccessDouble() {
        Response createResponse = courierApiClient.createCourier(createCourierRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateCourierResponse createCourierResponse = createResponse.as(CreateCourierResponse.class);
        assertTrue(createCourierResponse.getSuccess());
        String token = createCourierResponse.getAccessToken();

        createResponse = courierApiClient.createCourier(createCourierRequest);
        assertEquals(SC_FORBIDDEN, createResponse.statusCode());
        createCourierResponse = createResponse.as(CreateCourierResponse.class);
        assertFalse(createCourierResponse.getSuccess());
        assertEquals("User already exists", createCourierResponse.getMessage());

        Response deleteResponse = courierApiClient.deleteCourier(deleteCourierRequest, token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteCourierResponse deleteCourierResponse = deleteResponse.as(DeleteCourierResponse.class);
        assertTrue(deleteCourierResponse.getSuccess());
    }

    @Test
    @DisplayName("Создание курьера без одного из полей")
    public void createCourierSuccessWithoutField() {
        createCourierRequest.setEmail("");
        Response createResponse = courierApiClient.createCourier(createCourierRequest);
        assertEquals(SC_FORBIDDEN, createResponse.statusCode());
        CreateCourierResponse createCourierResponse = createResponse.as(CreateCourierResponse.class);
        assertFalse(createCourierResponse.getSuccess());
        assertEquals("Email, password and name are required fields", createCourierResponse.getMessage());
    }

}
