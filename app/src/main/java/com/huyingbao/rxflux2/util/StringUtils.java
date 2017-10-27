package com.huyingbao.rxflux2.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liujunfeng on 2017/1/1.
 */
public class StringUtils {
    /**
     * 判断手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    /**
     * 判断是否是6-16 的字母或数字
     *
     * @param value1
     * @return true 是
     */
    public static boolean isPassword(String value1) {
        Pattern p = Pattern.compile("^[0-9A-Za-z]{6,16}$");
        // .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");
        // .compile("((?=.*[0-9])(?=.*[a-z])
        // (?=.*[A-Z])(?=.*[@#*=])(?=[\\S]+$).{5,10})");
        Matcher m = p.matcher(value1);
        return m.matches();
    }

    /**
     * 转为MD5加密字符串
     *
     * @param s 字符串
     * @return MD5
     */
    public static String toMD5(String s) {
        if (s == null) return null;
        return toHex(toMD5(s.getBytes()));
    }

    /**
     * 转为MD5加密字节流
     *
     * @param b 字节流
     * @return MD5
     */
    private static byte[] toMD5(byte[] b) {
        if (b == null) return null;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return b;
        }
        md5.update(b);
        return md5.digest();
    }

    /**
     * 把byte数组转为16进制字符串
     *
     * @param b byte数组
     * @return 16进制字符串
     */
    private static String toHex(byte[] b) {
        String r = "";
        if (b == null || b.length == 0) return r;
        try {
            for (int i = 0; i < b.length; i++) {
                r += Character.forDigit(b[i] >> 4 & 0xf, 16);
                r += Character.forDigit(b[i] & 0xf, 16);
            }
        } catch (Exception e) {
        }
        return r;
    }

    /**
     * 判断字符串是否是json格式
     *
     * @param content
     * @return
     */
    public static boolean isJson(String content) {
        try {
            new JsonParser().parse(content);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }


//    /**
//     * 获取拼音
//     *
//     * @param inputString
//     * @return
//     */
//    public static String getPingYin(String inputString)
//    {
//        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        format.setVCharType(HanyuPinyinVCharType.WITH_V);
//        char[] input = inputString.trim().toCharArray();
//        String output = "";
//        try
//        {
//            for (char curchar : input)
//            {
//                if (Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+"))
//                {
//                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
//                    output += temp[0];
//                } else
//                {
//                    output += Character.toString(curchar);
//                }
//            }
//        } catch (BadHanyuPinyinOutputFormatCombination e)
//        {
//            e.printStackTrace();
//        }
//        return output;
//    }
}
