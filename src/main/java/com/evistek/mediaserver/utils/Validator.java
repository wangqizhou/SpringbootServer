package com.evistek.mediaserver.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/19.
 */
public class Validator {

    /*
        用于验证邮箱地址合法性的正则表达式
     */
    private final static String REG_EMAIL = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";


    /**
     * 验证邮箱地址的合法性
     *
     * @param email 待验证的邮箱地址
     * @return 验证通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return match(REG_EMAIL, email);
    }

    private static boolean match(String regex, String str) {
        if (str == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
