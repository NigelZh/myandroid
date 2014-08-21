package com.tempus.weijiujiao.HTTP;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.client.ClientProtocolException;
import android.content.Context;
import com.tempus.weijiujiao.Utils.HttpUtils;
import com.tempus.weijiujiao.Utils.JsonUtils;
import com.tempus.weijiujiao.Utils.ParamsUtils;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.constant.Constant;

/**
 * 网络操作封装类
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class Service {
	private Context context;
	private static ExecutorService service;
	private static Service myService;

	/**
	 * 单例模式
	 * 
	 * @param context
	 * @return Service对象
	 */
	public static Service getInstance(Context context) {
		if (myService == null) {
			myService = new Service();
		}
		return myService;
	}

	/**
	 * init方法
	 */
	static {
		if (service == null) {
			service = Executors.newFixedThreadPool(2);
		}
	}

	/**
	 * 获取灯源列表
	 * 
	 * @param type
	 *            设备类别 1 查询机，2酒柜，3酒窖
	 * @param deviceID
	 *            设备编号
	 * @param listener
	 *            监听
	 */
	public void requestLightList(int type, String deviceID,
			onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_searchDeviceId, deviceID);
		bodyMap.put(Constant.Key.key_Type, type + "");
		excuse(Constant.WebSate.INTERFACE_ENVIRONMENT_LIGHT_LIST, bodyMap,
				listener);
	}
	
	/**
	 * 判断设备是否属于该用户
	 * 
	 * @param userID
	 *             用户ID
	 * @param type
	 *             设备类别 1 查询机，2酒柜，3酒窖
	 * @param deviceID
	 *             设备编号
	 * @param listener
	 *             监听
	 */
	public void requestIsUserDevice(String userID,String deviceID,
			onResultListener listener){
		isUserDevice(userID,2,deviceID,listener);
	}
	private void isUserDevice(String userID,int type,String deviceID,
			onResultListener listener){
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_searchDeviceId, deviceID);
		bodyMap.put(Constant.Key.key_Type, type + "");
		bodyMap.put(Constant.Key.key_userId, userID);
		excuse(Constant.WebSate.INTERFACE_DEVICE_ISOWNER, bodyMap,
				listener);
	}

	/**
	 * 上传设备图片
	 * 
	 * @param userId
	 *            用户识别号
	 * @param deviceId
	 *            设备编号
	 * @param deviceType
	 *            设备类别 2酒柜，3酒窖
	 * @param imageArray
	 *            图片二进制数据
	 * @param listener
	 *            结果监听
	 */
	public void uploadDeviceImage(String userId, String deviceId,int deviceType, byte[] imageArray, onResultListener listener) {
		requestUploadImage(userId, deviceId, deviceType, imageArray, listener);
	}

	/**
	 * 上传用户头像
	 * 
	 * @param userID
	 *            用户识别号
	 * @param imageArray
	 *            图片二进制数组
	 * @param listener
	 *            结果监听
	 */
	public void uploadUserImage(String userID, byte[] imageArray,
			onResultListener listener) {
		requestUploadImage(userID, null, 0, imageArray, listener);
	}

	/**
	 * 图片上传
	 * 
	 * @param userID
	 *            用户识别号
	 * @param deviceID
	 *            设备编号
	 * @param deviceType
	 *            设备类别 2酒柜，3酒窖 ，0用户
	 * @param imageByteArray
	 *            图片二进制数组
	 * @param listener
	 *            结果监听
	 * 
	 */
	private void requestUploadImage(String userID, String deviceID,
			int deviceType, byte[] imageByteArray, onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_userId, userID);
		if (deviceType != -1) {
			bodyMap.put(Constant.Key.key_Type, deviceType + "");
		} else {
			bodyMap.put(Constant.Key.key_Type, 0 + "");
		}
		if (deviceID != null) {
			bodyMap.put(Constant.Key.key_searchDeviceId, deviceID);
		}
		excuseUpload(Constant.WebSate.INTERFACE_USER_UPLOAD, imageByteArray,bodyMap, listener);
	}

	/**
	 * 版本更新
	 * 
	 * @param localVersionCode
	 *            本地版本号
	 * @param type
	 *            应用类别 1android手机， 2tablet,3其他
	 * @param listener
	 *            监听
	 */
	public void requestUpgrade(int localVersionCode, onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_versionCode, localVersionCode + "");
		bodyMap.put(Constant.Key.key_Type, 1 + "");
		excuse(Constant.WebSate.INTERFACE_APP_UPGRADE, bodyMap, listener);
	}

	/**
	 * 意见反馈
	 * 
	 * @param userID
	 *            用户识别号
	 * @param content
	 *            反馈内容
	 * @param listener
	 *            结果监听
	 */
	public void requestFeedBack(String userID, String content,
			onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_userId, userID);
		bodyMap.put(Constant.Key.key_feedBackContent, content);
		excuse(Constant.WebSate.INTERFACE_APP_FEEDBACK, bodyMap, listener);
	}

	/**
	 * 酒窖区域列表
	 * 
	 * @param userID
	 *            用户识别号
	 * @param deviceID
	 *            酒窖编号
	 * @param listener
	 *            结果监听
	 */
	public void requestRegionalList(String userID, String deviceID,
			onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_userId, userID);
		bodyMap.put(Constant.Key.key_searchDeviceId, deviceID);
		excuse(Constant.WebSate.INTERFACE_DEVICE_REGIONAL_LIST, bodyMap,
				listener);
	}

	/**
	 * 请求报表
	 * 
	 * @param userId
	 *            用户识别号
	 * @param deviceType
	 *            设备编号 2酒柜，3酒窖
	 * @param ids
	 *            设备编号数组
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param listener
	 *            结果监听
	 */
	public void requestReport(String userId, int deviceType, String[] ids,
			long startTime, long endTime, onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_userId, userId);
		bodyMap.put(Constant.Key.key_Type, deviceType + "");
		bodyMap.put(Constant.Key.key_searchDeviceId,
				StringUtils.arrayConvertString(ids));
		bodyMap.put(Constant.Key.key_reportStartTime, startTime + "");
		bodyMap.put(Constant.Key.key_reportEndTime, endTime + "");
		excuse(Constant.WebSate.INTERFACE_DEVICE_REPORT, bodyMap, listener);
	}

	/**
	 * 设备信息更新
	 * 
	 * @param userID
	 *            用户识别号
	 * @param deviceID
	 *            设备编号
	 * @param deviceType
	 *            设备类别 1 查询机，2酒柜，3酒窖
	 * @param type
	 *            更新信息类别，0更新昵称，1更新手机号码，2更新邮箱地址，3更新地址 4 备注更新
	 * @param updateString
	 *            更新内容
	 * @param listener
	 *            结果监听
	 */
	public void deviceUpdate(String userID, String deviceID, int deviceType,
			int type, String updateString, onResultListener listener) {
		switch (type) {
		case 0:
			infoUpdate(userID, false, deviceID, deviceType, updateString, null,
					null, null, null, listener);
			break;
		case 1:
			infoUpdate(userID, false, deviceID, deviceType, null, updateString,
					null, null, null, listener);
			break;
		case 2:
			infoUpdate(userID, false, deviceID, deviceType, null, null,
					updateString, null, null, listener);
			break;
		case 3:
			infoUpdate(userID, false, deviceID, deviceType, null, null, null,
					updateString, null, listener);
			break;
		case 4:
			infoUpdate(userID, false, deviceID, deviceType, null, null, null,
					null, updateString, listener);
			break;
		default:
			break;
		}
	}

	/**
	 * 用户信息更新
	 * 
	 * @param userID
	 *            用户id
	 * @param type
	 *            更新信息类别，0更新昵称，1更新手机号码，2更新邮箱地址，3更新地址
	 * @param updateString
	 *            更新内容
	 * @param listener
	 *            结果监听
	 */
	public void userUpdate(String userID, int type, String updateString,
			onResultListener listener) {
		switch (type) {
		case 0:
			infoUpdate(userID, true, null, -1, updateString, null, null, null,null, listener);
			break;
		case 1:
			infoUpdate(userID, true, null, -1, null, updateString, null, null,null, listener);
			break;
		case 2:
			infoUpdate(userID, true, null, -1, null, null, updateString, null,null, listener);
			break;
		case 3:
			infoUpdate(userID, true, null, -1, null, null, null, updateString, null,listener);
			break;
		default:
			break;
		}
	}

	/**
	 * 信息更新
	 * 
	 * @param userID
	 *            用户id
	 * @param isUser
	 *            更新主体类别，true用户更新，false设备更新
	 * @param deviceId
	 *            设备编号
	 * @param deviceType
	 *            设备类别 1 查询机，2酒柜，3酒窖
	 * @param name
	 *            昵称更新
	 * @param number
	 *            手机号码更新
	 * @param email
	 *            邮箱地址更新
	 * @param address
	 *            地址更新
	 * @param remark
	 *            备注更新
	 * @param listener
	 *            结果监听
	 */
	private void infoUpdate(String userID, boolean isUser, String deviceId,
			int deviceType, String name, String number, String email,
			String address, String remark, onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_userId, userID);
		bodyMap.put(Constant.Key.key_updateIsUser, (isUser ? 0 : 1) + "");
		if (deviceId != null) {
			bodyMap.put(Constant.Key.key_searchDeviceId, deviceId);
		}
		if (deviceType != -1) {
			bodyMap.put(Constant.Key.key_searchDeviceType, deviceType + "");
		}
		if (name != null) {
			bodyMap.put(Constant.Key.key_Name, name);
		}
		if (number != null) {
			bodyMap.put(Constant.Key.key_Number, number);
		}
		if (email != null) {
			bodyMap.put(Constant.Key.key_Email, email);
		}
		if (address != null) {
			bodyMap.put(Constant.Key.key_Address, address);
		}
		if (remark != null) {
			bodyMap.put(Constant.Key.key_Remark, remark);
		}
		excuse(Constant.WebSate.INTERFACE_DEVICE_UPDATE, bodyMap, listener);
	}

	/**
	 * 添加设备
	 * 
	 * @param userID
	 *            用户识别号
	 * @param deviceType
	 *            设备类别 1 查询机，2酒柜，3酒窖
	 * @param deviceId
	 *            设备编号
	 * @param code
	 *            授权码
	 * @param listener
	 *            结果监听
	 */
	public void aadDevice(String userID, int deviceType, String deviceId,
			String code, onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_userId, userID);
		bodyMap.put(Constant.Key.key_Type, deviceType + "");
		bodyMap.put(Constant.Key.key_searchDeviceId, deviceId);
		bodyMap.put(Constant.Key.key_VerifyCode, code);
		excuse(Constant.WebSate.INTERFACE_DEVICE_ADD, bodyMap, listener);
	}

	/**
	 * 查询设备列表
	 * 
	 * @param userID
	 *            用户识别号
	 * @param deviceType
	 *            设备类别。1 查询机，2酒柜，3酒窖
	 * @param pageIndex
	 *            页卡
	 * @param listener
	 *            结果监听
	 */
	public void getDeviceList(String userID, int deviceType, int pageIndex,
			onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_userId, userID);
		bodyMap.put(Constant.Key.key_Type, deviceType + "");
		bodyMap.put(Constant.Key.key_pageIndex, pageIndex + "");
		excuse(Constant.WebSate.INTERFACE_DEVICE_LIST, bodyMap, listener);
	}

	/**
	 * 全局检索
	 * 
	 * @param pageIndex
	 *            页卡
	 * @param key
	 *            关键字
	 * @param listener
	 *            结果监听
	 */
	public void requestSearch(int pageIndex, String key,
			onResultListener listener) {
		requestSearch(key, null, pageIndex, -1, null, listener);
	}

	/**
	 * 设备内 查询全部
	 * 
	 * @param deviceId
	 *            设备编号
	 * @param pageIndex
	 *            页卡
	 * @param type
	 *            设备类别 2酒柜，3酒窖
	 * @param listener
	 *            结果监听
	 */
	public void requestSearch(String deviceId, int pageIndex, int type,
			onResultListener listener) {
		requestSearch(null, deviceId, pageIndex, type, null, listener);
	}

	/**
	 * 设备内，关键字检索
	 * 
	 * @param deviceId
	 *            设备编号
	 * @param key
	 *            关键字
	 * @param pageIndex
	 *            页卡
	 * @param listener
	 *            结果监听
	 */
	public void requestSearch(String deviceId, String key, int pageIndex,
			onResultListener listener) {
		requestSearch(key, deviceId, pageIndex, -1, null, listener);
	}

	/**
	 * 酒窖内，区域查询全部
	 * 
	 * @param deviceId
	 *            设备编号
	 * @param pageIndex
	 *            页卡
	 * @param type
	 *            设备类别 2酒柜，3酒窖
	 * @param listener
	 *            结果监听
	 */
	public void requestSearch(String deviceId, int pageIndex, String areaId,
			onResultListener listener) {
		requestSearch(null, deviceId, pageIndex, 3, areaId, listener);
	}

	/**
	 * 酒窖内，某区域内，关键字检索
	 * 
	 * @param key
	 *            关键字
	 * @param deviceId
	 *            酒窖编号
	 * @param areaId
	 *            区域编号
	 * @param pageIndex
	 *            页卡
	 * @param listener
	 *            结果监听
	 */
	public void requestSearch(String key, String deviceId, String areaId,
			int pageIndex, onResultListener listener) {
		requestSearch(key, deviceId, pageIndex, 3, areaId, listener);
	}

	/**
	 * 商品检索
	 * 
	 * @param key
	 *            检索关键字
	 * @param deviceId
	 *            设备编号
	 * @param pageIndex
	 *            页卡
	 * @param type
	 *            设备类别 2酒柜，3酒窖
	 * @param areaId
	 *            区域编号 酒窖区域检索
	 * @param listener
	 *            结果监听
	 */
	private void requestSearch(String key, String deviceId, int pageIndex,
			int type, String areaId, onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		if (key != null) {
			bodymap.put(Constant.Key.key_searchKey, key);
		}
		if (deviceId != null) {
			bodymap.put(Constant.Key.key_searchDeviceId, deviceId);
		}
		if (type != -1) {
			bodymap.put(Constant.Key.key_searchDeviceType, type + "");
		}
		if (areaId != null) {
			bodymap.put(Constant.Key.key_searchAreaId, areaId);
		}
		bodymap.put(Constant.Key.key_pageIndex, pageIndex + "");
		excuse(Constant.WebSate.INTERFACE_RRODUCT_SEARCH, bodymap, listener);
	}

	/**
	 * 获取商品信息
	 * 
	 * @param code
	 *            查询关键字
	 * @param codeType
	 *            关键字类别 ：0二维码，1条形码，2RFID标签，3ProductID
	 * @param contentType
	 *            查询信息类别 ：0全部信息，1基本信息，2酒庄信息，3葡萄品种信息，4品鉴信息
	 * @param listener
	 *            结果监听
	 */
	public void requestGetProductInfo(String code, int codeType,
			int contentType, onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		bodymap.put(Constant.Key.key_getCode, code);
		bodymap.put(Constant.Key.key_getCodeType, codeType + "");
		bodymap.put(Constant.Key.key_getContentType, contentType + "");
		excuse(Constant.WebSate.INTERFACE_PRODUCT_INFO, bodymap, listener);
	}

	/**
	 * 用户登录
	 * 
	 * @param id
	 *            用户识别ID，可以是邮箱或者手机号码
	 * @param pw
	 *            用户密码
	 * @param listener
	 *            结果监听
	 */
	public void requestLogin(String id, String pw, onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		bodymap.put(Constant.Key.key_userId, id);
		bodymap.put(Constant.Key.key_PW, pw);
		excuse(Constant.WebSate.INTERFACE_USER_LOGIN, bodymap, listener);
	}

	/**
	 * 用户重置密码
	 * 
	 * @param userId
	 *            用户识别号
	 * @param newpw
	 *            新密码
	 * @param listener
	 *            监听
	 */
	public void resetUserPassword(String userId, String newpw,
			onResultListener listener) {
		requestResetPassWord(userId, null, newpw, 2, listener);
	}

	/**
	 * 修改密码
	 * 
	 * @param userId
	 *            用户识别号
	 * @param pw
	 *            密码
	 * @param newpw
	 *            新密码
	 * @param listener
	 *            监听
	 */
	public void resetUserPassword(String userId, String pw, String newpw,
			onResultListener listener) {
		requestResetPassWord(userId, pw, newpw, 1, listener);
	}

	/**
	 * 重置密码，找回密码
	 * 
	 * @param userId
	 *            用户识别号
	 * @param pw
	 *            原始密码
	 * @param newpw
	 *            新密码
	 * @param type
	 *            类别，1修改密码，2找回密码
	 * @param listener
	 */
	private void requestResetPassWord(String userId, String pw, String newpw,
			int type, onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		bodymap.put(Constant.Key.key_userId, userId);
		if(pw!=null){
			bodymap.put(Constant.Key.key_PW, pw);
		}
		bodymap.put(Constant.Key.key_newPW, newpw);
		bodymap.put(Constant.Key.key_Type, type + "");
		excuse(Constant.WebSate.INTERFACE_USER_RESET_PASSWORD, bodymap,
				listener);
	}

	/**
	 * 验证
	 * 
	 * @param phoneNumber
	 *            手机号码
	 * @param code
	 *            验证码
	 * @param type
	 *            验证码用途类别 0用户注册，1设备转让，2找回密码
	 * @param listener
	 *            结果监听
	 */
	public void requestVerify(String phoneNumber, String code, int type,
			onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		bodymap.put(Constant.Key.key_Number, phoneNumber);
		bodymap.put(Constant.Key.key_VerifyCode, code);
		bodymap.put(Constant.Key.key_Type, type + "");
		excuse(Constant.WebSate.INTERFACE_USER_VerifyCode, bodymap, listener);
	}

	/**
	 * 注册
	 * 
	 * @param phoneNumer
	 *            手机号码
	 * @param password
	 *            密码
	 * @param listener
	 *            结果监听
	 */
	public void requestRegister(String phoneNumer, String password,
			onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		bodymap.put(Constant.Key.key_Number, phoneNumer);
		bodymap.put(Constant.Key.key_PassWord, password);
		excuse(Constant.WebSate.INTERFACE_USER_REGISTER, bodymap, listener);
	}

	/**
	 * 请求验证码
	 * 
	 * @param phoneNumer
	 *            手机号码
	 * @param type
	 *            请求类别 0 注册，2 找回密码
	 * @param onResultListener
	 *            结果监听
	 */
	public void requestCode(String phoneNumer, int type,
			onResultListener listener) {
		requestCode(null,phoneNumer, type, null, listener);
	}

	/**
	 * 
	 * @param phoneNumer
	 *            手机号码
	 * @param type
	 *            请求类别 1 设备转让
	 * @param id
	 *            设备id
	 * @param onResultListener
	 *            结果监听
	 */
	public void requestCode(String userID,String phoneNumer, int type, String id,
			onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		if(userID!=null){
			bodymap.put(Constant.Key.key_userId, userID);
		}
		bodymap.put(Constant.Key.key_Number, phoneNumer);
		bodymap.put(Constant.Key.key_Type, type + "");
		bodymap.put(Constant.Key.key_Id, id);
		excuse(Constant.WebSate.INTERFACE_USER_RandomCode, bodymap, listener);
	}

	/**
	 * api执行方法
	 * 
	 * @param url
	 *            接口地址
	 * @param bodymap
	 *            接口相关的参数map
	 * @param listener
	 *            结果回调监听
	 */
	private void excuse(final String url,
			final HashMap<String, String> bodymap,
			final onResultListener listener) {
		Runnable ra = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.onStart();
				}
				StringBuffer sb = new StringBuffer();
				boolean isError = true;
				Result r = new Result();
				try {
					sb.append("{\"head\":");
					sb.append(JsonUtils.map2Json(ParamsUtils.getInstance(
							context).getHeadMap()));
					sb.append(",\"body\":");
					sb.append(JsonUtils.map2Json(bodymap));
					sb.append("}");
					String resultJson = HttpUtils.httpPost(url, sb.toString());
					if (resultJson != null) {
						isError = false;
						r.setStatucCode(Result.CODE_OK);
						r.setJsonBody(resultJson);
					} else {
						isError = true;
						r.setStatucCode(Result.CODE_REQUEST_ERROR);
						r.setErrorInfo("the result body is null");
					}
				} catch (NetWorkUnAvaiableException e) {
					// TODO Auto-generated catch block
					isError = true;
					e.printStackTrace();
					r.setStatucCode(Result.CDOE_NETWORK_UNAVAIABLE);
					r.setErrorInfo("The network is unavailable!");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					isError = true;
					e.printStackTrace();
					r.setStatucCode(Result.CODE_REQUEST_ERROR);
					r.setErrorInfo(e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					isError = true;
					e.printStackTrace();
					r.setStatucCode(Result.CODE_REQUEST_ERROR);
					r.setErrorInfo(e.toString());
				} finally {
					if (listener != null) {
						listener.onFinish();
						if (isError) {
							listener.onNetError(r);
						} else {
							listener.onResult(r);
						}

					}
				}
			}
		};
		service.execute(ra);
	}

	/**
	 * 上传图片执行方法
	 * 
	 * @param url
	 *            接口地址
	 * @param bodymap
	 *            参数表
	 * @param listener
	 *            结果监听
	 */
	private void excuseUpload(final String url, final byte[] bitmapArray,
			final HashMap<String, String> bodymap,
			final onResultListener listener) {
		Runnable ra = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.onStart();
				}
				boolean isError = true;
				StringBuffer sb = new StringBuffer();
				Result r = new Result();
				try {
					sb.append("{\"head\":");
					sb.append(JsonUtils.map2Json(ParamsUtils.getInstance(
							context).getHeadMap()));
					sb.append(",\"body\":");
					sb.append(JsonUtils.map2Json(bodymap));
					sb.append("}");
					String resultJson = HttpUtils.uploadFile(url, bitmapArray,
							sb.toString());
					if (resultJson != null) {
						isError = false;
						r.setStatucCode(Result.CODE_OK);
						r.setJsonBody(resultJson);
					} else {
						isError = true;
						r.setStatucCode(Result.CODE_REQUEST_ERROR);
						r.setErrorInfo("the result body is null");
					}
				} catch (NetWorkUnAvaiableException e) {
					// TODO Auto-generated catch block
					isError = true;
					e.printStackTrace();
					r.setStatucCode(Result.CDOE_NETWORK_UNAVAIABLE);
					r.setErrorInfo("The network is unavailable!");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					isError = true;
					e.printStackTrace();
					r.setStatucCode(Result.CODE_REQUEST_ERROR);
					r.setErrorInfo(e.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					isError = true;
					e.printStackTrace();
					r.setStatucCode(Result.CODE_REQUEST_ERROR);
					r.setErrorInfo(e.toString());
				} finally {
					if (listener != null) {
						listener.onFinish();
						if (isError) {
							listener.onNetError(r);
						} else {
							listener.onResult(r);
						}

					}
				}
			}
		};
		service.execute(ra);
	}

	public void requestDownLoadFile(final File saveDir, final String fileUrl,
			final onProgressListner listener) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpUtils.downLoadFile(saveDir, fileUrl, listener);
			}
		};
		service.execute(r);
	}
}
