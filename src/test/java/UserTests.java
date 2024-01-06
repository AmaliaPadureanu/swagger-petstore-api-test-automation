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
    public void getUserById() {
        given()
                .pathParam("username", testUser.getUsername())
        .when()
                .get(PetStoreEndpoints.USER_BY_NAME)
        .then();
    }
}
