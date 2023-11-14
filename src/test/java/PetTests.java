import config.PetStoreEndpoints;
import config.TestConfig;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class PetTests extends TestConfig {

    @Test
    public void getPet() {
        given()
                .pathParam("petId", 9223372036854775807L)
        .when()
                .get(PetStoreEndpoints.PET)
        .then();
    }
}
