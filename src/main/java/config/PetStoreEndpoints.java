package config;

public interface PetStoreEndpoints {
    //pet endpoints
    String PET_BY_ID = "/pet/{petId}";
    String PET_BY_STATUS = "pet/findByStatus";
    String PET_UPLOAD_IMAGE = "pet/{petId}/uploadImage";
    String CREATE_PET = "/pet";
    String UPDATE_PET = "/pet";
    String UPDATE_PET_FORM_DATA = "/pet/{petId}";
    String DELETE_PET = "/pet/{petId}";

    //user endpoints
    String CREATE_USER = "/user";
    String CREATE_WITH_LIST = "/user/createWithList";
    String CREATE_WITH_ARRAY = "/user/createWithArray";
    String LOGIN = "user/login";
    String USER_BY_NAME = "/user/{username}";
    String LOGOUT = "user/logout";

    //store endpoints
    String PLACE_ORDER = "/store/order";
    String ORDER_BY_ID = "/store/order/{orderId}";
    String PET_INVENTORY_BY_STATUS = "/store/inventory";

}
