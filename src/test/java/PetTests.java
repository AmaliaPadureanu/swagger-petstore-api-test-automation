import Models.Category;
import Models.Pet;
import Models.Status;
import Models.Tag;
import config.PetStoreEndpoints;
import config.TestConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.DataGenerationUtils;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class PetTests extends TestConfig {

    Category testCategory = new Category(DataGenerationUtils.generateRandomId(), "dogs");
    List<String> photos = List.of("src/test/java/cute-puppy.jpg");
    Tag newTag1 = new Tag(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString());
    Tag newTag2 = new Tag(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString());
    List<Tag> tags = List.of(newTag1, newTag2);
    Pet testPet = new Pet(DataGenerationUtils.generateRandomId(), testCategory, DataGenerationUtils.generateRandomAlphaString(), photos, tags, Status.available.toString());

    @Test(priority = 1)
    public void createPet() {
        Pet pet = given()
                .body(testPet)
        .when()
                .post(PetStoreEndpoints.CREATE_PET)
        .then().extract().response().as(Pet.class);
        assert pet.getId().equals(testPet.getId());
    }

    @Test(priority = 2)
    public void getPetById() {
        given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then();
    }

    @Test (priority = 2)
    public void getPetsByStatus() {
        given()
                .queryParam("status", Status.sold.toString())
        .when()
                .get(PetStoreEndpoints.PET_BY_STATUS)
        .then();
    }

    @Test (priority = 2)
    public void getPetAndValidateName() {
        String petName = given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then().extract().response().as(Pet.class).getName();
        assert petName.equals(testPet.getName());
    }

    @Test (priority = 2)
    public void getPetAndValidateCategoryName() {
        Category petCategory = given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then().extract().as(Pet.class).getCategory();
        assert petCategory.getId().equals(testCategory.getId());
        assert petCategory.getName().equals(testCategory.getName());
    }

    @Test(priority = 2)
    public void validateResponseContainsAllData() {
        Set<String> expectedData = Set.of("id", "category", "name", "photoUrls", "tags", "status");
        Response response =
                given()
                        .pathParam("petId", testPet.getId())
                .when()
                        .get(PetStoreEndpoints.PET_BY_ID)
                .then()
                        .extract().response();
        assert response.as(Map.class).keySet().equals(expectedData);
    }

    @Test(priority = 2)
    public void validatePetHasPhotoUrl() {
        Response response =
                given()
                        .pathParam("petId", testPet.getId())
                .when()
                        .get(PetStoreEndpoints.PET_BY_ID)
                .then()
                        .extract().response();

        List<String> photoUrls = response.path("photoUrls");
        assert !photoUrls.isEmpty();
    }

    @Test (priority = 2)
    public void uploadPetImage() {
        File petImage = new File("src/test/java/cute-puppy.jpg");

        given()
                .contentType("multipart/form-data")
                .pathParam("petId", testPet.getId())
                .multiPart("file", petImage)
        .when()
                .post(PetStoreEndpoints.PET_UPLOAD_IMAGE)
        .then();
    }

    @Test(priority = 3)
    public void updatePet() {
        testPet.setStatus(Status.pending.toString());

        given()
                .body(testPet)
        .when()
                .put(PetStoreEndpoints.UPDATE_PET)
        .then();
    }

    @Test(priority = 3)
    public void updatePetFormData() {
        String newName = DataGenerationUtils.generateRandomAlphaString();
        String newStatus = Status.sold.toString();

        given()
                .contentType(ContentType.URLENC)
                .pathParam("petId", testPet.getId())
                .formParam("name", newName)
                .formParam("status", newStatus)
        .when()
                .post(PetStoreEndpoints.UPDATE_PET_FORM_DATA)
        .then();

        String name = given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then().extract().response().as(Pet.class).getName();
        assert name.equals(newName);

        String status = given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then().extract().response().as(Pet.class).getStatus();
        assert status.equals(newStatus);
    }

    @Test(priority = 4)
    public void deletePet() {
        given()
                .pathParam("petId", testPet.getId())
        .when()
                .delete(PetStoreEndpoints.DELETE_PET)
        .then();
    }
}
