package tests;

import config.PetStoreEndpoints;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import models.ApiResponse;
import models.Order;
import models.OrderStatus;
import org.testng.annotations.Test;
import testConfig.TestConfig;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@Epic("Store")
public class StoreTests extends TestConfig {

    Order testOrder = new Order(9, 42520, 1,
            "2024-01-07T15:50:46.121Z", OrderStatus.placed.toString(), true);

    @Test(priority = 1)
    @Description("Place order Test")
    public void placeOrder() {
        given()
                .body(testOrder)
        .when()
                .post(PetStoreEndpoints.PLACE_ORDER)
        .then()
                .assertThat().body("id", equalTo(testOrder.getId()))
                .assertThat().body("status", equalTo(testOrder.getStatus()));
    }

    @Test(priority = 2)
    @Description("Find purchase order by Id Test")
    public void findPurchaseOrderById() {
        given()
                .pathParam("orderId", testOrder.getId())
        .when()
                .get(PetStoreEndpoints.ORDER_BY_ID)
        .then()
                .assertThat().body("petId", equalTo(testOrder.getPetId()))
                .assertThat().body("status", equalTo(testOrder.getStatus()));
    }

    @Test(priority = 3)
    @Description("Delete purchase order by Id Test")
    public void deletePurchaseOrderById() {
        ApiResponse apiResponse = given()
                .pathParam("orderId", testOrder.getId())
        .when()
                .delete(PetStoreEndpoints.ORDER_BY_ID)
        .then()
                .extract().response().as(ApiResponse.class);
        assert apiResponse.getType().equals("unknown");
        assert apiResponse.getMessage().equals("9");
    }

    @Test
    @Description("Get store inventory Test")
    public void getStoreInventory() {
        Response response = given()
        .when()
                .get(PetStoreEndpoints.PET_INVENTORY_BY_STATUS)
        .then()
                .extract().response();
        assert response.as(Map.class).containsKey("sold");
    }
}
