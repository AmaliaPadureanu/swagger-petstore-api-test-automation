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
    String LOGIN = "user/login/";
    String USER_BY_NAME = "/user/{username}";
}
