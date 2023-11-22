package config;

public interface PetStoreEndpoints {
    String PET = "/pet/{petId}";
    String PET_BY_STATUS = "pet/findByStatus";
    String PET_UPLOAD_IMAGE = "pet/{petId}/uploadImage";


    String CREATE_USER = "/user";
    String LOGIN = "user/login/";
    String USER_BY_NAME = "/user/{username}";
}
