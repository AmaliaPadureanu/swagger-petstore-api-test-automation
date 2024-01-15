package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import models.ApiResponse;
import models.User;
import config.PetStoreEndpoints;
import testConfig.TestConfig;
import org.testng.annotations.Test;
import testUtils.DataGenerationUtils;
import java.util.List;

import static io.restassured.RestAssured.given;

@Epic("User")
public class UserTests extends TestConfig {

    User testUser = DataGenerationUtils.generateNewRandomUser();
    ApiResponse apiResponse;

    @Test(priority = 1)
    @Description("Login Test")
    public void login() {
         apiResponse = given()
                .queryParam("username", "test")
                .queryParam("password", "abc123")
        .when()
                .get(PetStoreEndpoints.LOGIN)
        .then()
                .extract().response().as(ApiResponse.class);
        assert apiResponse.getType().equals("unknown");
        assert apiResponse.getMessage().contains("logged in user session");
    }

    @Test(priority = 2)
    @Description("Create login Test")
    public void createUser() {
        apiResponse = given()
                .body(testUser)
        .when()
                .post(PetStoreEndpoints.CREATE_USER)
        .then()
                .extract().response().as(ApiResponse.class);
        assert apiResponse.getMessage().equals(testUser.getId().toString());
    }

    @Test(priority = 2)
    @Description("Create multiple users using a list Test")
    public void createUsersWithList() {
        User testUser1 = DataGenerationUtils.generateNewRandomUser();
        User testUser2 = DataGenerationUtils.generateNewRandomUser();
        User testUser3 = DataGenerationUtils.generateNewRandomUser();
        List<User> users = List.of(testUser1, testUser2, testUser3);

        apiResponse = given()
                .body(users)
        .when()
                .post(PetStoreEndpoints.CREATE_WITH_LIST)
        .then()
                .extract().response().as(ApiResponse.class);
        assert apiResponse.getType().equals("unknown");
        assert apiResponse.getMessage().equals("ok");

        for (User user : users) {
            given()
                    .pathParam("username", user.getUsername())
            .when()
                    .get(PetStoreEndpoints.USER_BY_NAME)
            .then();
        }
    }

    @Test(priority = 2)
    @Description("Create multiple users using an array Test")
    public void createUsersWithArray() {
        User testUser4 = DataGenerationUtils.generateNewRandomUser();
        User testUser5 = DataGenerationUtils.generateNewRandomUser();
        User testUser6 = DataGenerationUtils.generateNewRandomUser();
        User[] users = new User[]{testUser4, testUser5, testUser6};

        apiResponse = given()
                .body(users)
        .when()
                .post(PetStoreEndpoints.CREATE_WITH_ARRAY)
        .then()
                .extract().response().as(ApiResponse.class);
        assert apiResponse.getType().equals("unknown");
        assert apiResponse.getMessage().equals("ok");

        for (User user : users) {
            given()
                    .pathParam("username", user.getUsername())
            .when()
                    .get(PetStoreEndpoints.USER_BY_NAME)
            .then();
        }
    }

    @Test(priority = 3)
    @Description("Get user by username Test")
    public void getUserByUsername() {
        String username = given()
                .pathParam("username", testUser.getUsername())
        .when()
                .get(PetStoreEndpoints.USER_BY_NAME)
        .then()
                .extract().response().as(User.class).getUsername();
        assert username.equals(testUser.getUsername());
    }

    @Test(priority = 3)
    @Description("Update user's email address Test")
    public void updateUserEmailAddress() {
        String newEmailAddress = DataGenerationUtils.generateRandomEmailAddress();
        testUser.setEmail(newEmailAddress);

        given()
                .pathParam("username", testUser.getUsername())
                .body(testUser)
        .when().put(PetStoreEndpoints.USER_BY_NAME)
        .then();

        String email = given()
                .pathParam("username", testUser.getUsername())
        .when()
                .get(PetStoreEndpoints.USER_BY_NAME)
        .then().
                extract().response().as(User.class).getEmail();
        assert email.equals(newEmailAddress);
    }

    @Test(priority = 4)
    @Description("Delete user Test")
    public void deleteUser() {
        given()
                .pathParam("username", testUser.getUsername())
        .when()
                .delete(PetStoreEndpoints.USER_BY_NAME)
        .then();

        apiResponse = given()
                .pathParam("username", testUser.getUsername())
                .expect().statusCode(404)
        .when()
                .get(PetStoreEndpoints.USER_BY_NAME)
        .then()
                .extract().response().as(ApiResponse.class);
        assert apiResponse.getType().equals("error");
        assert apiResponse.getMessage().equals("User not found");
    }

    @Test(priority = 5)
    @Description("Logout test")
    public void logout() {
        apiResponse = given()
                .queryParam("username", "test")
                .queryParam("password", "abc123")
        .when()
                .get(PetStoreEndpoints.LOGOUT)
        .then()
                .extract().response().as(ApiResponse.class);
    assert apiResponse.getType().equals("unknown");
    assert apiResponse.getMessage().equals("ok");
    }
}
