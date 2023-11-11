package order;

import api.client.CourierApiClient;
import api.client.OrderApiClient;
import api.model.courier.CreateCourierRequest;
import api.model.courier.CreateCourierResponse;
import api.model.courier.DeleteCourierRequest;
import api.model.courier.DeleteCourierResponse;
import api.model.order.createOrder.CreateOrderResponse;
import api.model.order.createOrder.IngredientsListResponse;
import api.model.order.createOrder.IngredientsRequest;
import api.model.order.getOrders.GetOrdersResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static api.helper.CourierGenerator.getRandomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@Epic(value = "Получить заказ")
public class GetOrdersTest {
    private DeleteCourierRequest deleteCourierRequest;
    private CourierApiClient courierApiClient;
    private OrderApiClient orderApiClient;
    private String token;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        orderApiClient = new OrderApiClient();
        CreateCourierRequest createCourierRequest = getRandomCourier();
        Response createResponse = courierApiClient.createCourier(createCourierRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateCourierResponse createCourierResponse = createResponse.as(CreateCourierResponse.class);
        assertTrue(createCourierResponse.getSuccess());
        token = createCourierResponse.getAccessToken();

        deleteCourierRequest = new DeleteCourierRequest(createCourierRequest.getEmail(), createCourierRequest.getPassword());

        Response ingredientsResponse = orderApiClient.getIngredients();
        IngredientsListResponse listResponse = ingredientsResponse.as(IngredientsListResponse.class);
        assertTrue(listResponse.getSuccess());
        int size = listResponse.getData().length;
        var ingredientIndex = (int) (Math.random() * size);

        String[] ingredients = new String[size];
        ingredients[0] = String.valueOf(listResponse.getData()[ingredientIndex].get_id());
        IngredientsRequest ingredientsRequest = new IngredientsRequest(ingredients);

        Response orderResponse = orderApiClient.createOrder(ingredientsRequest, token);
        assertEquals(SC_OK, orderResponse.statusCode());
        CreateOrderResponse createOrderResponse = orderResponse.as(CreateOrderResponse.class);
        assertTrue(createOrderResponse.getSuccess());
    }

    @After
    public void down() {
        Response deleteResponse = courierApiClient.deleteCourier(deleteCourierRequest, token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteCourierResponse deleteCourierResponse = deleteResponse.as(DeleteCourierResponse.class);
        assertTrue(deleteCourierResponse.getSuccess());
    }

    @Test
    @DisplayName("Получить заказ с авторизацией")
    public void getOrders() {
        Response orderResponse = orderApiClient.getOrders(token);
        assertEquals(SC_OK, orderResponse.statusCode());
        GetOrdersResponse getOrdersResponse = orderResponse.as(GetOrdersResponse.class);
        assertTrue(getOrdersResponse.getSuccess());
    }

    @Test
    @DisplayName("Получить заказ без авторизации")
    public void getOrderWithoutAuth() {
        Response orderResponse = orderApiClient.getOrders();
        assertEquals(SC_UNAUTHORIZED, orderResponse.statusCode());
        GetOrdersResponse getOrdersResponse = orderResponse.as(GetOrdersResponse.class);
        assertFalse(getOrdersResponse.getSuccess());
        assertEquals("You should be authorised", getOrdersResponse.getMessage());
    }
}
