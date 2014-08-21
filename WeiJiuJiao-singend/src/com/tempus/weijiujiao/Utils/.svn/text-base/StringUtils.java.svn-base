package com.tempus.weijiujiao.Utils;

/**
 * 字符串工具集
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class StringUtils {
	/**
	 * 数组转换成字符串，以“,”分割
	 * 
	 * @param stringArray
	 *            要分割的数组
	 * @return 结果字符串
	 */
	public static String arrayConvertString(String[] stringArray) {
		StringBuffer sb = new StringBuffer();
		for (String s : stringArray) {
			sb.append(s + ",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}

	/**
	 * 去除转义字符
	 * 
	 * @param s
	 * @return
	 */
	public static String clearMystring(String s) {
		s = s.replace("\"{", "{");
		s = s.replace("}\"", "}");
//		s = s.replace("\\", "");
		return s;
	}
	public static String deepclearMystring(String s) {
		s = s.replace("\"{", "{");
		s = s.replace("}\"", "}");
	s = s.replace("\\", "");
		return s;
	}
	public static String getFileNameFormUrl(String url) {
		return url.substring(url.lastIndexOf("/"));
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (str == null) {
			return true;
		}
		if (str.equals("")) {
			return true;
		}
		if (str.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}

	public static boolean isPhoneNumber(String mobiles) {
		String telRegex = "^((13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$";
		if (isNull(mobiles)) {
			return false;
		} else {
			return mobiles.matches(telRegex);
		}
	}

	public static boolean isMail(String mail) {
		String mailRegex = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{1,3}$";
		if (isNull(mail)) {
			return false;
		} else {
			return mail.matches(mailRegex);
		}
	}

	public static boolean isNumLet(String nub) {
		String numRegex = "[a-zA-Z0-9]*";
		if (isNull(nub)) {
			return false;
		} else {
			return nub.matches(numRegex);
		}
	}

	public static boolean chackLenth(String lenth) {
		if (lenth.length() >= 6 && lenth.length() <= 20) {
			return true;
		} else {
			return false;
		}
	}

	// http://zs.sziotech.com/Query/Index?LaserCode=0PH86NP0VBJJFP6J842BDR6H&Web=yes&CodeType=2
	public static String getBarCode(String scanResult) {
		String barcode = null;
		if (scanResult.trim().length() == 24 && checkBarCode(scanResult)) {
			barcode = scanResult;
		} else if (scanResult.startsWith("http://zs.sziotech.com")
				&& scanResult.contains("LaserCode=")) {
			String sub = scanResult.substring(
					scanResult.lastIndexOf("LaserCode=") + 10,
					scanResult.indexOf("&Web"));
			if (sub.length()==24&&checkBarCode(sub)) {
				barcode = sub;
			}
		}
		return barcode;
	}

	private static boolean checkBarCode(String barcode) {
		String strExp = "^[A-Za-z0-9]+$";
		return barcode.matches(strExp);
	}
}
