package testUtils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestLogger {

    public static void main(String[] args) {
        logger.info("hello");
    }

    private static final Logger logger = LogManager.getLogger(TestLogger.class);


    public static void info(String message) {
        logger.info(message);
        attachLogToAllure("INFO: " + message);
    }

    public static void error(String message) {
        logger.error(message);
        attachLogToAllure("ERROR: " + message);
    }

    @Attachment(value = "{logMessage}", type = "text/plain")
    private static void attachLogToAllure(String logMessage) {
        Allure.addAttachment("Attached log", logMessage);
    }
}
