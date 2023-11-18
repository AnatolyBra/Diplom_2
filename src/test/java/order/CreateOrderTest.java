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
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static api.helper.UserGenerator.getRandomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@Epic(value = "Создание заказов")
public class CreateOrderTest {
    private UserApiClient userApiClient;
    private OrderApiClient orderApiClient;
    private IngredientsRequest ingredientsRequest;
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
        ingredientsRequest = new IngredientsRequest(ingredients);
    }

    @After
    public void down() {
        Response deleteResponse = userApiClient.deleteUser(token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteUserResponse deleteUserResponse = deleteResponse.as(DeleteUserResponse.class);
        assertTrue(deleteUserResponse.getSuccess());
    }

    @Test
    @DisplayName(value = "Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        Response orderResponse = orderApiClient.createOrderWithoutAuth(ingredientsRequest);
        assertEquals(SC_OK, orderResponse.statusCode());
        CreateOrderResponse createOrderResponse = orderResponse.as(CreateOrderResponse.class);
        assertTrue(createOrderResponse.getSuccess());
    }


    @Test
    @DisplayName("Создать заказ с не существующим ингредиентом")
    public void createOrderWithWrongIngredients() {
        String[] ingredients = {"61c0"};
        ingredientsRequest = new IngredientsRequest(ingredients);
        Response orderResponse = orderApiClient.createOrderWithoutAuth(ingredientsRequest);
        assertEquals(SC_INTERNAL_SERVER_ERROR, orderResponse.statusCode());
    }

    @Test
    @DisplayName("Создать заказ")
    public void createOrder() {
        Response orderResponse = orderApiClient.createOrder(ingredientsRequest, token);
        assertEquals(SC_OK, orderResponse.statusCode());
        CreateOrderResponse createOrderResponse = orderResponse.as(CreateOrderResponse.class);
        assertTrue(createOrderResponse.getSuccess());
    }

    @Test
    @DisplayName("Создать заказ без ингредиента")
    public void createOrderWithoutIngredient() {
        Response orderResponse = orderApiClient.createOrder();
        assertEquals(SC_BAD_REQUEST, orderResponse.statusCode());
        CreateOrderResponse createOrderResponse = orderResponse.as(CreateOrderResponse.class);
        assertFalse(createOrderResponse.getSuccess());
        assertEquals("Ingredient ids must be provided", createOrderResponse.getMessage());
    }
}
