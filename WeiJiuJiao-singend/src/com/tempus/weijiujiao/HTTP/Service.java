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
 * ���������װ��
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class Service {
	private Context context;
	private static ExecutorService service;
	private static Service myService;

	/**
	 * ����ģʽ
	 * 
	 * @param context
	 * @return Service����
	 */
	public static Service getInstance(Context context) {
		if (myService == null) {
			myService = new Service();
		}
		return myService;
	}

	/**
	 * init����
	 */
	static {
		if (service == null) {
			service = Executors.newFixedThreadPool(2);
		}
	}

	/**
	 * ��ȡ��Դ�б�
	 * 
	 * @param type
	 *            �豸��� 1 ��ѯ����2�ƹ�3�ƽ�
	 * @param deviceID
	 *            �豸���
	 * @param listener
	 *            ����
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
	 * �ж��豸�Ƿ����ڸ��û�
	 * 
	 * @param userID
	 *             �û�ID
	 * @param type
	 *             �豸��� 1 ��ѯ����2�ƹ�3�ƽ�
	 * @param deviceID
	 *             �豸���
	 * @param listener
	 *             ����
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
	 * �ϴ��豸ͼƬ
	 * 
	 * @param userId
	 *            �û�ʶ���
	 * @param deviceId
	 *            �豸���
	 * @param deviceType
	 *            �豸��� 2�ƹ�3�ƽ�
	 * @param imageArray
	 *            ͼƬ����������
	 * @param listener
	 *            �������
	 */
	public void uploadDeviceImage(String userId, String deviceId,int deviceType, byte[] imageArray, onResultListener listener) {
		requestUploadImage(userId, deviceId, deviceType, imageArray, listener);
	}

	/**
	 * �ϴ��û�ͷ��
	 * 
	 * @param userID
	 *            �û�ʶ���
	 * @param imageArray
	 *            ͼƬ����������
	 * @param listener
	 *            �������
	 */
	public void uploadUserImage(String userID, byte[] imageArray,
			onResultListener listener) {
		requestUploadImage(userID, null, 0, imageArray, listener);
	}

	/**
	 * ͼƬ�ϴ�
	 * 
	 * @param userID
	 *            �û�ʶ���
	 * @param deviceID
	 *            �豸���
	 * @param deviceType
	 *            �豸��� 2�ƹ�3�ƽ� ��0�û�
	 * @param imageByteArray
	 *            ͼƬ����������
	 * @param listener
	 *            �������
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
	 * �汾����
	 * 
	 * @param localVersionCode
	 *            ���ذ汾��
	 * @param type
	 *            Ӧ����� 1android�ֻ��� 2tablet,3����
	 * @param listener
	 *            ����
	 */
	public void requestUpgrade(int localVersionCode, onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_versionCode, localVersionCode + "");
		bodyMap.put(Constant.Key.key_Type, 1 + "");
		excuse(Constant.WebSate.INTERFACE_APP_UPGRADE, bodyMap, listener);
	}

	/**
	 * �������
	 * 
	 * @param userID
	 *            �û�ʶ���
	 * @param content
	 *            ��������
	 * @param listener
	 *            �������
	 */
	public void requestFeedBack(String userID, String content,
			onResultListener listener) {
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put(Constant.Key.key_userId, userID);
		bodyMap.put(Constant.Key.key_feedBackContent, content);
		excuse(Constant.WebSate.INTERFACE_APP_FEEDBACK, bodyMap, listener);
	}

	/**
	 * �ƽ������б�
	 * 
	 * @param userID
	 *            �û�ʶ���
	 * @param deviceID
	 *            �ƽѱ��
	 * @param listener
	 *            �������
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
	 * ���󱨱�
	 * 
	 * @param userId
	 *            �û�ʶ���
	 * @param deviceType
	 *            �豸��� 2�ƹ�3�ƽ�
	 * @param ids
	 *            �豸�������
	 * @param startTime
	 *            ��ʼʱ��
	 * @param endTime
	 *            ����ʱ��
	 * @param listener
	 *            �������
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
	 * �豸��Ϣ����
	 * 
	 * @param userID
	 *            �û�ʶ���
	 * @param deviceID
	 *            �豸���
	 * @param deviceType
	 *            �豸��� 1 ��ѯ����2�ƹ�3�ƽ�
	 * @param type
	 *            ������Ϣ���0�����ǳƣ�1�����ֻ����룬2���������ַ��3���µ�ַ 4 ��ע����
	 * @param updateString
	 *            ��������
	 * @param listener
	 *            �������
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
	 * �û���Ϣ����
	 * 
	 * @param userID
	 *            �û�id
	 * @param type
	 *            ������Ϣ���0�����ǳƣ�1�����ֻ����룬2���������ַ��3���µ�ַ
	 * @param updateString
	 *            ��������
	 * @param listener
	 *            �������
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
	 * ��Ϣ����
	 * 
	 * @param userID
	 *            �û�id
	 * @param isUser
	 *            �����������true�û����£�false�豸����
	 * @param deviceId
	 *            �豸���
	 * @param deviceType
	 *            �豸��� 1 ��ѯ����2�ƹ�3�ƽ�
	 * @param name
	 *            �ǳƸ���
	 * @param number
	 *            �ֻ��������
	 * @param email
	 *            �����ַ����
	 * @param address
	 *            ��ַ����
	 * @param remark
	 *            ��ע����
	 * @param listener
	 *            �������
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
	 * ����豸
	 * 
	 * @param userID
	 *            �û�ʶ���
	 * @param deviceType
	 *            �豸��� 1 ��ѯ����2�ƹ�3�ƽ�
	 * @param deviceId
	 *            �豸���
	 * @param code
	 *            ��Ȩ��
	 * @param listener
	 *            �������
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
	 * ��ѯ�豸�б�
	 * 
	 * @param userID
	 *            �û�ʶ���
	 * @param deviceType
	 *            �豸���1 ��ѯ����2�ƹ�3�ƽ�
	 * @param pageIndex
	 *            ҳ��
	 * @param listener
	 *            �������
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
	 * ȫ�ּ���
	 * 
	 * @param pageIndex
	 *            ҳ��
	 * @param key
	 *            �ؼ���
	 * @param listener
	 *            �������
	 */
	public void requestSearch(int pageIndex, String key,
			onResultListener listener) {
		requestSearch(key, null, pageIndex, -1, null, listener);
	}

	/**
	 * �豸�� ��ѯȫ��
	 * 
	 * @param deviceId
	 *            �豸���
	 * @param pageIndex
	 *            ҳ��
	 * @param type
	 *            �豸��� 2�ƹ�3�ƽ�
	 * @param listener
	 *            �������
	 */
	public void requestSearch(String deviceId, int pageIndex, int type,
			onResultListener listener) {
		requestSearch(null, deviceId, pageIndex, type, null, listener);
	}

	/**
	 * �豸�ڣ��ؼ��ּ���
	 * 
	 * @param deviceId
	 *            �豸���
	 * @param key
	 *            �ؼ���
	 * @param pageIndex
	 *            ҳ��
	 * @param listener
	 *            �������
	 */
	public void requestSearch(String deviceId, String key, int pageIndex,
			onResultListener listener) {
		requestSearch(key, deviceId, pageIndex, -1, null, listener);
	}

	/**
	 * �ƽ��ڣ������ѯȫ��
	 * 
	 * @param deviceId
	 *            �豸���
	 * @param pageIndex
	 *            ҳ��
	 * @param type
	 *            �豸��� 2�ƹ�3�ƽ�
	 * @param listener
	 *            �������
	 */
	public void requestSearch(String deviceId, int pageIndex, String areaId,
			onResultListener listener) {
		requestSearch(null, deviceId, pageIndex, 3, areaId, listener);
	}

	/**
	 * �ƽ��ڣ�ĳ�����ڣ��ؼ��ּ���
	 * 
	 * @param key
	 *            �ؼ���
	 * @param deviceId
	 *            �ƽѱ��
	 * @param areaId
	 *            ������
	 * @param pageIndex
	 *            ҳ��
	 * @param listener
	 *            �������
	 */
	public void requestSearch(String key, String deviceId, String areaId,
			int pageIndex, onResultListener listener) {
		requestSearch(key, deviceId, pageIndex, 3, areaId, listener);
	}

	/**
	 * ��Ʒ����
	 * 
	 * @param key
	 *            �����ؼ���
	 * @param deviceId
	 *            �豸���
	 * @param pageIndex
	 *            ҳ��
	 * @param type
	 *            �豸��� 2�ƹ�3�ƽ�
	 * @param areaId
	 *            ������ �ƽ��������
	 * @param listener
	 *            �������
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
	 * ��ȡ��Ʒ��Ϣ
	 * 
	 * @param code
	 *            ��ѯ�ؼ���
	 * @param codeType
	 *            �ؼ������ ��0��ά�룬1�����룬2RFID��ǩ��3ProductID
	 * @param contentType
	 *            ��ѯ��Ϣ��� ��0ȫ����Ϣ��1������Ϣ��2��ׯ��Ϣ��3����Ʒ����Ϣ��4Ʒ����Ϣ
	 * @param listener
	 *            �������
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
	 * �û���¼
	 * 
	 * @param id
	 *            �û�ʶ��ID����������������ֻ�����
	 * @param pw
	 *            �û�����
	 * @param listener
	 *            �������
	 */
	public void requestLogin(String id, String pw, onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		bodymap.put(Constant.Key.key_userId, id);
		bodymap.put(Constant.Key.key_PW, pw);
		excuse(Constant.WebSate.INTERFACE_USER_LOGIN, bodymap, listener);
	}

	/**
	 * �û���������
	 * 
	 * @param userId
	 *            �û�ʶ���
	 * @param newpw
	 *            ������
	 * @param listener
	 *            ����
	 */
	public void resetUserPassword(String userId, String newpw,
			onResultListener listener) {
		requestResetPassWord(userId, null, newpw, 2, listener);
	}

	/**
	 * �޸�����
	 * 
	 * @param userId
	 *            �û�ʶ���
	 * @param pw
	 *            ����
	 * @param newpw
	 *            ������
	 * @param listener
	 *            ����
	 */
	public void resetUserPassword(String userId, String pw, String newpw,
			onResultListener listener) {
		requestResetPassWord(userId, pw, newpw, 1, listener);
	}

	/**
	 * �������룬�һ�����
	 * 
	 * @param userId
	 *            �û�ʶ���
	 * @param pw
	 *            ԭʼ����
	 * @param newpw
	 *            ������
	 * @param type
	 *            ���1�޸����룬2�һ�����
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
	 * ��֤
	 * 
	 * @param phoneNumber
	 *            �ֻ�����
	 * @param code
	 *            ��֤��
	 * @param type
	 *            ��֤����;��� 0�û�ע�ᣬ1�豸ת�ã�2�һ�����
	 * @param listener
	 *            �������
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
	 * ע��
	 * 
	 * @param phoneNumer
	 *            �ֻ�����
	 * @param password
	 *            ����
	 * @param listener
	 *            �������
	 */
	public void requestRegister(String phoneNumer, String password,
			onResultListener listener) {
		HashMap<String, String> bodymap = new HashMap<String, String>();
		bodymap.put(Constant.Key.key_Number, phoneNumer);
		bodymap.put(Constant.Key.key_PassWord, password);
		excuse(Constant.WebSate.INTERFACE_USER_REGISTER, bodymap, listener);
	}

	/**
	 * ������֤��
	 * 
	 * @param phoneNumer
	 *            �ֻ�����
	 * @param type
	 *            ������� 0 ע�ᣬ2 �һ�����
	 * @param onResultListener
	 *            �������
	 */
	public void requestCode(String phoneNumer, int type,
			onResultListener listener) {
		requestCode(null,phoneNumer, type, null, listener);
	}

	/**
	 * 
	 * @param phoneNumer
	 *            �ֻ�����
	 * @param type
	 *            ������� 1 �豸ת��
	 * @param id
	 *            �豸id
	 * @param onResultListener
	 *            �������
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
	 * apiִ�з���
	 * 
	 * @param url
	 *            �ӿڵ�ַ
	 * @param bodymap
	 *            �ӿ���صĲ���map
	 * @param listener
	 *            ����ص�����
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
	 * �ϴ�ͼƬִ�з���
	 * 
	 * @param url
	 *            �ӿڵ�ַ
	 * @param bodymap
	 *            ������
	 * @param listener
	 *            �������
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
