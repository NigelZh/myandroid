package com.tempus.weijiujiao.Utils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class JsonUtils {
	/**
	 * hasmap×ªJson×Ö·û´®
	 * @param map
	 * @return
	 */
	public static String map2Json(HashMap<String, String> map) {
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		while (iter.hasNext()) {	
			Entry<String, String> entry = iter.next();
			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\":\"");
			sb.append(entry.getValue());
			sb.append("\",");
		}
		sb.append('}');
		sb.deleteCharAt(sb.lastIndexOf(","));
		return sb.toString();
	}

	/**
	 * ²âÊÔ
	 * @param args
	 */
	public static void main(String[] args){
		HashMap<String , String>map=new HashMap<String, String>();
		for(int i=0;i<10;i++){
			map.put("key "+i, "value "+i);	
		}
		System.out.println(map2Json(map));
	}
}
