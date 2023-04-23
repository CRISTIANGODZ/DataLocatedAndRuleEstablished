package com.cqupt.extraction_rules_build.utils;

/**
 * @auther DyingZhang
 * @Create 2022-10-15 上午 11:37
 * @Discriptioon
 */
public class SeparatorUtil {
    public static String  getSeparator(String str){
        String separator = "：";
        if (str.contains(":")){
            separator = ":";
        }
        return separator;
    }
}
