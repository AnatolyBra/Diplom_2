package api.client;

import api.model.order.createorder.IngredientsRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static api.config.ConfigApp.BASE_URL;

public class OrderApiClient extends BaseApiClient {
    @Step("Создание заказа")
    public Response createOrder(IngredientsRequest ingredientsRequest, String accessToken) {
        return getPostSpec()
                .header("Authorization", accessToken)
                .body(ingredientsRequest)
                .post("/orders");
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(IngredientsRequest ingredientsRequest) {
        return getPostSpec()
                .body(ingredientsRequest)
                .post("/orders");
    }

    @Step("Создание заказа без ингредиента")
    public Response createOrder() {
        return getPostSpec()
                .post("/orders");
    }

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return getPostSpec()
                .get("/ingredients");
    }

    @Step("Получить заказы без авторизации")
    public Response getOrders() {
        return getPostSpec()
                .get("/orders");
    }

    @Step("Получить заказы без авторизации")
    public Response getOrders(String accessToken) {
        return getPostSpec()
                .header("Authorization", accessToken)
                .get("/orders");
    }

}
