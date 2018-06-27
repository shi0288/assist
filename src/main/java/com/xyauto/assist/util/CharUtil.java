package com.xyauto.assist.util;

/**
 * Created by shiqm on 2018-01-19.
 */
public class CharUtil {

    //首字母大写
    public static String captureUpper(String name) {
        char[] cs = name.toCharArray();
        if (cs[0] >= 97 && cs[0] <= 122) {
            cs[0] -= 32;
        }
        return String.valueOf(cs);
    }

    //首字母小写
    public static String captureLower(String name) {
        char[] cs = name.toCharArray();
        if (cs[0] >= 65 && cs[0] <= 90) {
            cs[0] += 32;
        }
        return String.valueOf(cs);
    }


}
