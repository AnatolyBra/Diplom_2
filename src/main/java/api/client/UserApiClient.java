package api.client;

import api.model.user.CreateUserRequest;
import api.model.user.LoginUserRequest;
import api.model.user.UserResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserApiClient extends BaseApiClient {
    @Step("Создание пользователя")
    public Response createUser(CreateUserRequest createUserRequest) {
        return getPostSpec()
                .body(createUserRequest)
                .when()
                .post("/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response loginUser(LoginUserRequest loginUserRequest) {
        return getPostSpec()
                .body(loginUserRequest)
                .when()
                .post("/auth/login");
    }

    @Step("Изменение данных пользователя")
    public Response updateUser(UserResponse user, String accessToken) {
        return getPostSpec()
                .header("Authorization", accessToken)
                .body(user)
                .patch("/auth/user");
    }

    @Step("Изменение данных пользователя")
    public Response updateUserWrong(UserResponse user) {
        return getPostSpec()
                .body(user)
                .patch("/auth/user");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return getPostSpec()
                .header("Authorization", accessToken)
                .delete("/auth/user");
    }
}
