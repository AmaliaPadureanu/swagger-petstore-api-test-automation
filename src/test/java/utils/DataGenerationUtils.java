package utils;

import java.nio.charset.Charset;
import java.util.Random;

public class DataGenerationUtils {

    public static Integer generateRandomId() {
        Random random = new Random();
        return random.nextInt(1000000);
    }

    public static String generateRandomAlphaString() {
        String alphaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
        StringBuilder stringBuilder = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = (int) (alphaString.length() * Math.random());
            stringBuilder.append(alphaString.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static String generateRandomNumbercString() {
        String numericString = "123456789";
        StringBuilder stringBuilder = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = (int) (numericString.length() * Math.random());
            stringBuilder.append(numericString.charAt(index));
        }
        return stringBuilder.toString();
    }

    public static String generateRandomEmailAddress() {
        String alphaString = "abcdefghijklmnopqrstuvxyz";
        StringBuilder stringBuilder = new StringBuilder(7);

        for (int i = 0; i < 7; i++) {
            int index = (int) (alphaString.length() * Math.random());
            stringBuilder.append(alphaString.charAt(index));
        }
        return stringBuilder + "@gmail.com";
    }
}


