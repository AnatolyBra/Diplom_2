package api.client;

import api.model.order.createOrder.IngredientsRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static api.config.ConfigApp.BASE_URL;

public class OrderApiClient extends BaseApiClient {
    @Step("Создание заказа")
    public Response createOrder(IngredientsRequest ingredientsRequest, String accessToken) {
        return getPostSpec()
                .header("Authorization", accessToken)
                .body(ingredientsRequest)
                .post(BASE_URL + "/orders");
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(IngredientsRequest ingredientsRequest) {
        return getPostSpec()
                .body(ingredientsRequest)
                .post(BASE_URL + "/orders");
    }

    @Step("Создание заказа без ингредиента")
    public Response createOrder() {
        return getPostSpec()
                .post(BASE_URL + "/orders");
    }

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return getPostSpec()
                .get(BASE_URL + "/ingredients");
    }

    @Step("Получить заказы без авторизации")
    public Response getOrders() {
        return getPostSpec()
                .get(BASE_URL + "/orders");
    }

    @Step("Получить заказы без авторизации")
    public Response getOrders(String accessToken) {
        return getPostSpec()
                .header("Authorization", accessToken)
                .get(BASE_URL + "/orders");
    }

}
