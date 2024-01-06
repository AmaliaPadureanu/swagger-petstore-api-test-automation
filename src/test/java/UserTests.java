import Models.User;
import config.PetStoreEndpoints;
import config.TestConfig;
import org.testng.annotations.Test;
import utils.DataGenerationUtils;

import static io.restassured.RestAssured.given;

public class UserTests extends TestConfig {

    User testUser = new User(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString(),
            DataGenerationUtils.generateRandomAlphaString(), DataGenerationUtils.generateRandomAlphaString(),
            DataGenerationUtils.generateRandomEmailAddress(), DataGenerationUtils.generateRandomAlphaString(),
            DataGenerationUtils.generateRandomNumbercString(), 0);


    @Test(priority = 1)
    public void login() {
        given()
                .queryParam("username", "test")
                .queryParam("password", "abc123")
        .when()
                .get(PetStoreEndpoints.LOGIN)
        .then();
    }

    @Test (priority = 2)
    public void createUser() {
        given()
                .body(testUser)
        .when()
                .post(PetStoreEndpoints.CREATE_USER)
        .then();
    }

    @Test (priority = 3)
    public void getUserByUsername() {
        String username = given()
                .pathParam("username", testUser.getUsername())
        .when()
                .get(PetStoreEndpoints.USER_BY_NAME)
        .then().extract().response().as(User.class).getUsername();
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
        .then().extract().response().as(User.class).getEmail();
        assert email.equals(newEmailAddress);
    }

    @Test (priority = 4)
    public void deleteUser() {
        given()
                .pathParam("username", testUser.getUsername())
        .when()
                .delete(PetStoreEndpoints.USER_BY_NAME)
        .then();

        int statusCode = given()
                .pathParam("username", testUser.getUsername())
                .expect().statusCode(404)
        .when()
                .get(PetStoreEndpoints.USER_BY_NAME)
        .then()
                .extract().response().getStatusCode();
        assert statusCode == 404;
    }

    @Test(priority = 5)
    public void logout() {
        given()
                .queryParam("username", "test")
                .queryParam("password", "abc123")
        .when()
                .get(PetStoreEndpoints.LOGOUT)
        .then();
    }
}
