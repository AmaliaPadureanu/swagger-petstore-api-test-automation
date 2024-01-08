import Models.ApiResponse;
import Models.User;
import config.PetStoreEndpoints;
import config.TestConfig;
import org.testng.annotations.Test;
import utils.DataGenerationUtils;
import java.util.List;

import static io.restassured.RestAssured.given;

public class UserTests extends TestConfig {

    User testUser = DataGenerationUtils.generateNewRandomUser();
    ApiResponse apiResponse;

    @Test(priority = 1)
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

    @Test (priority = 2)
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

    @Test (priority = 3)
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

    @Test (priority = 4)
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
