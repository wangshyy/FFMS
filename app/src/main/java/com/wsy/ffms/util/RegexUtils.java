package com.wsy.ffms.util;

import java.util.regex.Pattern;

/**
 * author : wsy
 * date   : 2023/3/1
 * desc   :
 */
public class RegexUtils {

/**
 * 必须要有一个小写字母，一个大写字母和一个数字，并且是8-16位(java 正则)
 * @param pasword
 * @return
 */
    public static boolean validatePassword(String pasword) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{8,16}$");
        return pattern.matcher(pasword).matches();
    }
}
