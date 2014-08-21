package com.tempus.weijiujiao.Utils;

import java.util.HashMap;

import com.tempus.weijiujiao.HTTP.NetWorkUnAvaiableException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

/**
 * 参数工具集
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class ParamsUtils {
	private static ParamsUtils params;
	private static final String key_imei = "IMEI";
	private static final String key_netType = "NetType";
	private static final String key_macAddress = "MAC_Address";
	private static final String key_mobileMode = "MobileMode";
	private static final String key_currentTime = "CurrentTime";
	private Context context;
	private HashMap<String, String> HeadMap;
	private int ScreenH=0;
	private int ScreenW=0;
	/**
	 * 单例实例
	 * 
	 * @return ParamsUtils
	 */
	public static ParamsUtils getInstance(Context context) {
		if (params == null) {
			params = new ParamsUtils(context);
		}
		return params;
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public ParamsUtils(Context context) {
		this.context = context;
		if (HeadMap == null) {
			initHeadMap();
		}
	}

	/**
	 * head初始化
	 * 
	 * @param context
	 */
	private void initHeadMap() {
		// TODO Auto-generated method stub
		HeadMap = new HashMap<String, String>();
		checkNet();
		getImeiANDMode();
		getMAC();
		getCurrentTime();
	}

	/**
	 * 获取当前系统时间
	 */
	private void getCurrentTime() {
		// TODO Auto-generated method stub
		String currentTimeString = System.currentTimeMillis() + "";
		HeadMap.put(key_currentTime, currentTimeString);
	}

	/**
	 * 获取无线网卡的物理地址
	 */
	private void getMAC() {
		// TODO Auto-generated method stub
		WifiManager vm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (vm != null) {
			WifiInfo wf = vm.getConnectionInfo();
			if (wf != null) {
				String mac = wf.getMacAddress();
				if (mac != null) {
					HeadMap.put(key_macAddress, mac);
				}
			}
		}
	}

	/**
	 * 获取设备串号和型号
	 */
	private void getImeiANDMode() {
		// TODO Auto-generated method stub
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			String imei = tm.getDeviceId();
			String mode = Build.MODEL;
			if (imei != null) {
				HeadMap.put(key_imei, imei);
			}
			if (mode != null) {
				HeadMap.put(key_mobileMode, mode);
			}
		}
	}

	/**
	 * 检查网络是否可用
	 */
	private boolean checkNet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					HeadMap.put(key_netType, info.getTypeName());
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取网络时候可用
	 * 
	 * @return true可用，false不可用
	 */
	public boolean isNetAvaiable() {
		return checkNet();
	}

	/**
	 * 获取head
	 * 
	 * @return
	 * @throws NetWorkUnAvaiableException 
	 */
	public HashMap<String, String> getHeadMap() throws NetWorkUnAvaiableException {
		if(!checkNet()){
			throw new NetWorkUnAvaiableException();
		}
		return HeadMap;
	}
	/**
	 * 获取json参数
	 * @param bodyMap 实质参数map
	 * @return json
	 */
	public String getJsonParams(HashMap<String, String>bodyMap){
		return JsonUtils.map2Json(bodyMap);
	}
	/**
	 * 获取本地版本号 versionCode
	 * @return
	 */
	public  int getLocalVersionCode(){
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return 0;
	}
	/**
	 * 获取本地版本号 versionCode
	 * @return
	 */
	public  String getLocalVersionName(){
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return null;
	}
	/**
	 * 获取socket的请求参数表
	 * @param requestUnitId 请求识别号
	 * @param cmd 指令
	 * @param deviceID 设备编号
	 * @param content JsonString 内容
	 * @return 参数表HashMap
	 */
	public HashMap<String, String>getSocketRequestMap(String requestUnitId,String cmd,String deviceID,String content){
		HashMap<String, String> hasMap=new HashMap<String, String>();
		byte[] b=content.getBytes();
		hasMap.put("requestUnitId", requestUnitId);
		hasMap.put("CmdWord", cmd);
		hasMap.put("Content", content);
		hasMap.put("StockSn", deviceID);
		hasMap.put("Length", b.length+"");
		hasMap.put("Check",b.length==0?0+"":(b[0]^b[b.length-1])+"" );
		return hasMap;
	}
	
	/**
	 * 获取屏幕宽度
	 * @return
	 */

	public int getScreenH(){
		if(ScreenH==0){
			WindowManager wm = (WindowManager) context
	                .getSystemService(Context.WINDOW_SERVICE);
			ScreenW = wm.getDefaultDisplay().getWidth();     
			ScreenH = wm.getDefaultDisplay().getHeight(); 
		}
		return ScreenH;
	}
	/**
	 * 获取屏幕高度
	 * @return
	 */
	public int getScreenW(){
		if(ScreenW==0){
			WindowManager wm = (WindowManager) context
	                .getSystemService(Context.WINDOW_SERVICE);
			ScreenW = wm.getDefaultDisplay().getWidth();     
			ScreenH = wm.getDefaultDisplay().getHeight(); 
		}
		return ScreenW;
	}
}
