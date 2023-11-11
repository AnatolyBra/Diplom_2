package api.client;

import api.model.courier.CreateCourierRequest;
import api.model.courier.DeleteCourierRequest;
import api.model.courier.LoginCourierRequest;
import api.model.courier.UserResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static api.config.ConfigApp.BASE_URL;

public class CourierApiClient extends BaseApiClient{
    @Step("Создание курьера")
    public Response createCourier(CreateCourierRequest createCourierRequest) {
        return getPostSpec()
                .body(createCourierRequest)
                .when()
                .post(BASE_URL + "/api/auth/register");
    }
    @Step("Авторизация курьера")
    public Response loginCourier(LoginCourierRequest loginCourierRequest) {
        return getPostSpec()
                .body(loginCourierRequest)
                .when()
                .post(BASE_URL + "/api/auth/login");
    }

    @Step("Изменение данных курьера")
    public Response updateCourier(UserResponse user, String accessToken) {
        return getPostSpec()
                .header("Authorization", accessToken)
                .body(user)
                .patch(BASE_URL + "/api/auth/user");
    }

    @Step("Изменение данных курьера")
    public Response updateCourierWrong(UserResponse user) {
        return getPostSpec()
                .body(user)
                .patch(BASE_URL + "/api/auth/user");
    }

    @Step("Удаление курьера")
    public Response deleteCourier(DeleteCourierRequest deleteCourierRequest, String accessToken) {
        return getPostSpec()
                .header("Authorization", accessToken)
                .body(deleteCourierRequest)
                .delete(BASE_URL + "/api/auth/user");
    }
}
