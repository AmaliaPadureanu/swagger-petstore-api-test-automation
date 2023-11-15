import config.PetStoreEndpoints;
import config.TestConfig;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class PetTests extends TestConfig {

    @Test
    public void getPetById() {
        given()
                .pathParam("petId", 9223372036854775609L)
        .when()
                .get(PetStoreEndpoints.PET)
        .then();
    }

    @Test
    public void getPetsByStatus() {
        given()
                .queryParam("status", "available")
        .when()
                .get(PetStoreEndpoints.PET_BY_STATUS)
        .then();
    }

    @Test
    public void getPetAndValidateName() {
        given()
                .pathParam("petId", 9223372036854775609L)
        .when()
                .get(PetStoreEndpoints.PET)
        .then()
                .body("name", equalTo("fish"));
    }

    @Test
    public void getPetAndValidateCategoryName() {
        given()
                .pathParam("petId", 9223372036854774840L)
        .when()
                .get(PetStoreEndpoints.PET)
        .then()
                .body("category.name", equalTo("pets"));
    }

    @Test
    public void validateResponseContainsAllData() {
        Set<String> expectedData = Set.of("id", "category", "name", "photoUrls", "tags", "status");
        Response response =
                given()
                        .pathParam("petId", 9223372036854774840L)
                .when()
                        .get(PetStoreEndpoints.PET)
                .then()
                        .extract().response();
        assert response.as(Map.class).keySet().equals(expectedData);
    }

    @Test
    public void validatePetHasPhotoUrl() {
        Response response =
                given()
                        .pathParam("petId", 9223372036854774931L)
                .when()
                        .get(PetStoreEndpoints.PET)
                .then()
                        .extract().response();

        List<String> photoUrls = response.path("photoUrls");
        assert !photoUrls.isEmpty();
    }
}
