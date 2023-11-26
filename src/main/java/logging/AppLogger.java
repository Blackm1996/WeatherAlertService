package logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger
{
    private static final Logger ALERTS_LOGGER = LoggerFactory.getLogger("alerts");
    private static final Logger ERRORS_LOGGER = LoggerFactory.getLogger("errors");
    private static final Logger UNIVERSAL_LOGGER = LoggerFactory.getLogger("personalDebugger");

    public static void logAlert(String message) {

        ALERTS_LOGGER.info(message);
    }

    public static void logError(String message) {
        ERRORS_LOGGER.error(message);
    }

    public static void logDebug(String message) {
        UNIVERSAL_LOGGER.debug(message);
    }
}
