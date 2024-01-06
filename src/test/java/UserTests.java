import Models.User;
import config.PetStoreEndpoints;
import config.TestConfig;
import org.testng.annotations.Test;
import utils.DataGenerationUtils;

import java.util.List;

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

    @Test(priority = 2)
    public void createUsersWithList() {
        User testUser1 = new User(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomAlphaString(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomEmailAddress(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomNumbercString(), 1);

        User testUser2 = new User(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomAlphaString(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomEmailAddress(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomNumbercString(), 2);

        User testUser3 = new User(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomAlphaString(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomEmailAddress(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomNumbercString(), 3);

        List<User> users = List.of(testUser1, testUser2, testUser3);

        given()
                .body(users)
        .when()
                .post(PetStoreEndpoints.CREATE_WITH_LIST)
        .then();
    }

    @Test(priority = 2)
    public void createUsersWithArray() {
        User testUser4 = new User(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomAlphaString(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomEmailAddress(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomNumbercString(), 4);

        User testUser5 = new User(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomAlphaString(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomEmailAddress(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomNumbercString(), 5);

        User testUser6 = new User(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomAlphaString(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomEmailAddress(), DataGenerationUtils.generateRandomAlphaString(),
                DataGenerationUtils.generateRandomNumbercString(), 6);

        User[] users = new User[] {testUser4, testUser5, testUser6};

        given()
                .body(users)
        .when()
                .post(PetStoreEndpoints.CREATE_WITH_ARRAY)
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
