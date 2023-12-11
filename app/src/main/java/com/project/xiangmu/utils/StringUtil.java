package com.project.xiangmu.utils;

public class StringUtil {
    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str) && !"null".equals(str)) {
            return false;
        }
        return true;
    }

    public static String isEmptyReturnString(String str) {
        if (str != null && !"null".equals(str)) {
            return str;
        }
        return "";
    }

    public static String toStringHex(String str) {
        if (!StringUtil.isEmpty(str)) {
            byte[] baKeyword = new byte[str.length() / 2];
            for (int i = 0; i < baKeyword.length; i++) {
                try {
                    baKeyword[i] = (byte) (0xff & Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                str = new String(baKeyword, "utf-8");// UTF-16le:Not
            } catch (Exception e1) {

            }
        }
        return str;
    }
}
