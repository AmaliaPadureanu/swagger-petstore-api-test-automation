import Models.Category;
import Models.Pet;
import Models.Status;
import Models.Tag;
import config.PetStoreEndpoints;
import config.TestConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class PetTests extends TestConfig {

    Category newCategory = new Category(23445, "dogs");
    List<String> photos = List.of("src/test/java/cute-puppy.jpg");
    Tag newTag1 = new Tag(23433, "TAG1");
    Tag newTag2 = new Tag(23333, "TAG2");
    List<Tag> tags = List.of(newTag1, newTag2);
    Pet newPet = new Pet(5345765, newCategory, "Max", photos, tags, Status.available.toString());

    @Test
    public void getPetById() {
        given()
                .pathParam("petId", 5345765)
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then();
    }

    @Test
    public void getPetsByStatus() {

        given()
                .queryParam("status", Status.sold.toString())
        .when()
                .get(PetStoreEndpoints.PET_BY_STATUS)
        .then();
    }

    @Test
    public void getPetAndValidateName() {
        given()
                .pathParam("petId", 9223372036854773147L)
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then()
                .body("name", equalTo("fish"));
    }

    @Test
    public void getPetAndValidateCategoryName() {
        given()
                .pathParam("petId", 9223372036854773147L)
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then()
                .body("category.name", equalTo("string"));
    }

    @Test
    public void validateResponseContainsAllData() {
        Set<String> expectedData = Set.of("id", "category", "name", "photoUrls", "tags", "status");
        Response response =
                given()
                        .pathParam("petId", 9223372036854773147L)
                .when()
                        .get(PetStoreEndpoints.PET_BY_ID)
                .then()
                        .extract().response();
        assert response.as(Map.class).keySet().equals(expectedData);
    }

    @Test
    public void validatePetHasPhotoUrl() {
        Response response =
                given()
                        .pathParam("petId", 9223372036854773147L)
                .when()
                        .get(PetStoreEndpoints.PET_BY_ID)
                .then()
                        .extract().response();

        List<String> photoUrls = response.path("photoUrls");
        assert !photoUrls.isEmpty();
    }

    @Test
    public void uploadPetImage() {
        File petImage = new File("src/test/java/cute-puppy.jpg");

        given()
                .contentType("multipart/form-data")
                .pathParam("petId", 9223372036854773147L)
                .multiPart("file", petImage)
        .when()
                .post(PetStoreEndpoints.PET_UPLOAD_IMAGE)
        .then();
    }

    @Test
    public void createPet() {
        given()
                .body(newPet)
        .when()
                .post(PetStoreEndpoints.CREATE_PET)
        .then();
    }

    @Test
    public void updatePet() {
        newPet.setStatus(Status.sold.toString());

        given()
                .body(newPet)
        .when()
                .put(PetStoreEndpoints.UPDATE_PET)
        .then();
    }

    @Test
    public void updatePetFormData() {
        given()
                .contentType(ContentType.URLENC)
                .pathParam("petId", "5345765")
                .formParam("name", "Bobitza")
                .formParam("status", Status.sold.toString())
        .when()
                .post(PetStoreEndpoints.UPDATE_PET_FORM_DATA)
        .then();
    }

    @Test
    public void deletePet() {
        given()
                .pathParam("petId", "5345765")
        .when()
                .delete(PetStoreEndpoints.DELETE_PET)
        .then();
    }
}
