import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class MyFirstTest  {
    @Test
    public void getPet() {
        given()
                .when()
                .get("pet/9223372036854775807")
                .then();

    }

    @Test
    public void addNewPet() {

        String petBodyJson = "{\n" +
                "  \"id\": 0,\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"string\"\n" +
                "  },\n" +
                "  \"name\": \"doggie\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";

        given()
                .body(petBodyJson)
                .when()
                .post("pet")
                .then();

    }

    @Test
    public void updatePet() {

        String petBodyJson = "{\n" +
                "  \"id\": 9223372036854775807,\n" +
                "  \"category\": {\n" +
                "    \"id\": 9223372036854775807,\n" +
                "    \"name\": \"string\"\n" +
                "  },\n" +
                "  \"name\": \"doggie\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"doggie\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"sold\"\n" +
                "}";

        given()
                .body(petBodyJson)
                .when()
                .post("pet")
                .then();
    }

    @Test
    public void login() {
        baseURI = "https://petstore.swagger.io/";
        basePath = "v2/";

        Response response = given().queryParam("username", "test")
                .queryParam("password", "abc123")
                .when()
                .get("user/login")
                .then().extract().response();

        System.out.println(response.asString());
    }
}
