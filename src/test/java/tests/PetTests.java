package tests;

import config.PetStoreEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Category;
import models.Pet;
import models.PetStatus;
import models.Tag;
import org.testng.annotations.Test;
import testConfig.TestConfig;
import testUtils.DataGenerationUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@Epic("Pet")
public class PetTests extends TestConfig {

    Category testCategory = new Category(DataGenerationUtils.generateRandomId(), "dogs");
    List<String> photos = List.of("src/test/java/cute-puppy.jpg");
    Tag newTag1 = new Tag(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString());
    Tag newTag2 = new Tag(DataGenerationUtils.generateRandomId(), DataGenerationUtils.generateRandomAlphaString());
    List<Tag> tags = List.of(newTag1, newTag2);
    Pet testPet = new Pet(DataGenerationUtils.generateRandomId(), testCategory, DataGenerationUtils.generateRandomAlphaString(), photos, tags, PetStatus.available.toString());

    @Test(priority = 1)
    @Description("Create Pet Test")
    public void createPet() {
        given()
                .body(testPet)
        .when()
                .post(PetStoreEndpoints.CREATE_PET)
        .then().
                assertThat().body("id", equalTo(testPet.getId()));
    }

    @Test(priority = 2)
    @Description("Get pet by Id Test")
    public void getPetById() {
        given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then();
    }

    @Test(priority = 2)
    @Description("Get pets by status Test")
    public void getPetsByStatus() {
        List<Pet> pets = given()
                .queryParam("status", PetStatus.sold.toString())
        .when()
                .get(PetStoreEndpoints.PET_BY_STATUS)
        .then()
                .extract().body().jsonPath().getList(".", Pet.class);
        assert !pets.stream().allMatch(pet -> Objects.equals(pet.getStatus(), PetStatus.available.toString())
                || Objects.equals(pet.getStatus(), PetStatus.pending.toString()));
    }

    @Test(priority = 2)
    @Description("Get pet and validate its name Test")
    public void getPetAndValidateName() {
        String petName = given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then().
                extract().response().as(Pet.class).getName();
        assert petName.equals(testPet.getName());
    }

    @Test(priority = 2)
    @Description("Get pet and validate its category name Test")
    public void getPetAndValidateCategoryName() {
        Category petCategory = given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then()
                .extract().response().as(Pet.class).getCategory();
        assert petCategory.getId().equals(testCategory.getId());
        assert petCategory.getName().equals(testCategory.getName());
    }

    @Test(priority = 2)
    @Description("Validate the response contains all data Test")
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
    @Description("Validate the pet has a photo url Test")
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

    @Test(priority = 2)
    @Description("Upload an image to an existing pet Test")
    public void uploadPetImage() {
        File petImage = new File("src/test/java/testUtils/cute-puppy.jpg");

        given()
                .contentType("multipart/form-data")
                .pathParam("petId", testPet.getId())
                .multiPart("file", petImage)
        .when()
                .post(PetStoreEndpoints.PET_UPLOAD_IMAGE)
        .then();
    }

    @Test(priority = 3)
    @Description("Update pet Test")
    public void updatePet() {
        String newStatus = PetStatus.pending.toString();
        testPet.setStatus(newStatus);

        given()
                .body(testPet)
        .when()
                .put(PetStoreEndpoints.UPDATE_PET)
        .then()
                .assertThat().body("status", equalTo(newStatus));
    }

    @Test(priority = 3)
    @Description("Update pet using form data Test")
    public void updatePetFormData() {
        String newName = DataGenerationUtils.generateRandomAlphaString();
        String newStatus = PetStatus.sold.toString();

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
        .then()
                .extract().response().as(Pet.class).getName();
        assert name.equals(newName);

        String status = given()
                .pathParam("petId", testPet.getId())
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then()
                .extract().response().as(Pet.class).getStatus();
        assert status.equals(newStatus);
    }

    @Test(priority = 4)
    @Description("Delete pet Test")
    public void deletePet() {
        given()
                .pathParam("petId", testPet.getId())
        .when()
                .delete(PetStoreEndpoints.DELETE_PET)
        .then();

        int statusCode = given()
                .pathParam("petId", testPet.getId())
                .expect().statusCode(404)
        .when()
                .get(PetStoreEndpoints.PET_BY_ID)
        .then()
                .extract().response().getStatusCode();
        assert statusCode == 404;
    }
}
