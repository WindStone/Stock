package stock.common.util;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

/**
 * Created by songyuanren on 2016/7/28.
 */
public class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger("DEFAULT_LOGGER");

    public static void info(String str, Object... params) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageFormat.format(str, params));
        }
    }

    public static void warn(String str, Object... params) {
        if (params[params.length - 1] instanceof Throwable) {
            LOGGER.warn(MessageFormat.format(str, params), (Throwable) params[params.length - 1]);
        } else {
            LOGGER.warn(MessageFormat.format(str, params));
        }
    }

    public static void error(String str, Object... params) {
        if (params[params.length - 1] instanceof Throwable) {
            LOGGER.error(MessageFormat.format(str, params), (Throwable) params[params.length - 1]);
        } else {
            LOGGER.error(MessageFormat.format(str, params));
        }
    }
}
