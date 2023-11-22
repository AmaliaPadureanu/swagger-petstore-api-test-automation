import Models.User;
import config.PetStoreEndpoints;
import config.TestConfig;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests extends TestConfig {

    @Test
    public void login() {

        given()
                .queryParam("username", "test")
                .queryParam("password", "abc123")
        .when()
                .get(PetStoreEndpoints.LOGIN)
        .then();
    }

    @Test
    public void createUser() {
        User newUser = new User(1919, "BabyShark", "Baby", "Shark",
                "bshark@gmail.com", "blabla123", "399948394", 0);

        given()
                .body(newUser)
        .when()
                .post(PetStoreEndpoints.CREATE_USER)
        .then();
    }

    @Test
    public void getUserById() {
        given()
                .pathParam("username", "BabyShark")
                .when()
                .get(PetStoreEndpoints.USER_BY_NAME)
                .then();

    }
}
