package order;

import api.client.UserApiClient;
import api.client.OrderApiClient;
import api.model.user.CreateUserRequest;
import api.model.user.CreateUserResponse;
import api.model.user.DeleteUserRequest;
import api.model.user.DeleteUserResponse;
import api.model.order.createorder.CreateOrderResponse;
import api.model.order.createorder.IngredientsListResponse;
import api.model.order.createorder.IngredientsRequest;
import api.model.order.getorders.GetOrdersResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static api.helper.UserGenerator.getRandomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@Epic(value = "Получить заказ")
public class GetOrdersTest {
    private UserApiClient userApiClient;
    private OrderApiClient orderApiClient;
    private String token;

    @Before
    public void setUp() {
        userApiClient = new UserApiClient();
        orderApiClient = new OrderApiClient();
        CreateUserRequest createUserRequest = getRandomCourier();
        Response createResponse = userApiClient.createUser(createUserRequest);
        assertEquals(SC_OK, createResponse.statusCode());
        CreateUserResponse createUserResponse = createResponse.as(CreateUserResponse.class);
        assertTrue(createUserResponse.getSuccess());
        token = createUserResponse.getAccessToken();

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
        Response deleteResponse = userApiClient.deleteUser(token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteUserResponse deleteUserResponse = deleteResponse.as(DeleteUserResponse.class);
        assertTrue(deleteUserResponse.getSuccess());
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
