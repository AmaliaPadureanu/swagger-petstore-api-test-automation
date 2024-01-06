package utils;

import java.util.Random;

public class DataGenerationUtils {

    public static Integer generateRandomId() {
        Random random = new Random();
        return random.nextInt(1000000);
    }

}


