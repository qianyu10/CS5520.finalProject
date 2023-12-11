package com.project.xiangmu.utils;

/**
 * @author hao
 * @date 2015-9-18
 * @description 字符串工具类
 */
public class StringUtil {
	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return false:非null、非""、非"null"
	 */
	public static boolean isEmpty(String str) {
		if (str != null && !"".equals(str) && !"null".equals(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return 字符串
	 */
	public static String isEmptyReturnString(String str) {
		if (str != null && !"null".equals(str)) {
			return str;
		}
		return "";
	}


	/**
	 * 十六进制转字符串
	 * 
	 * @param str
	 * @return
	 */
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
