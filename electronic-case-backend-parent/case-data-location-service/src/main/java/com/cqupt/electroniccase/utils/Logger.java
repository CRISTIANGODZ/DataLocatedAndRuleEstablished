package com.cqupt.electroniccase.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @auther DyingZhang
 * @Create 2022-11-18 下午 5:59
 * @Discriptioon
 */
@Component
public final class Logger {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("CaseDataLocationLogger");

    public static void info(String info){
        logger.info(info);
    }

    public static void debug(String debug){
        logger.debug(debug);
    }

    public static void error(String error){
        logger.error(error);
    }
}
