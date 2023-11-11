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
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static api.helper.CourierGenerator.getRandomCourier;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
@Epic(value = "Создание заказов")
public class CreateOrderTest {
    private DeleteCourierRequest deleteCourierRequest;
    private CourierApiClient courierApiClient;
    private OrderApiClient orderApiClient;
    private IngredientsRequest ingredientsRequest;
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
        ingredients[0]= String.valueOf(listResponse.getData()[ingredientIndex].get_id());
        ingredientsRequest = new IngredientsRequest(ingredients);
    }

    @After
    public void down() {
        Response deleteResponse = courierApiClient.deleteCourier(deleteCourierRequest, token);
        assertEquals(SC_ACCEPTED, deleteResponse.statusCode());
        DeleteCourierResponse deleteCourierResponse = deleteResponse.as(DeleteCourierResponse.class);
        assertTrue(deleteCourierResponse.getSuccess());
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
