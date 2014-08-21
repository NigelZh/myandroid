package com.tempus.weijiujiao.constant;

/**
 * ������
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class Constant {
	/**
	 * �ӿڵ�ַ
	 * 
	 * @author _blank :24611015@qq.com
	 * 
	 */
	public class WebSate {
		/**
		 * �ӿڸ���ַ
		 */
		//private static final String BaseServiceURL = "http://guizu.sziotech.com/NobleAPP/";
		//private static final String BaseServiceURL = "http://172.16.60.18/NobleAPP/";
		private static final String BaseServiceURL = "http://172.16.60.199:7090/NobleAPP/";
		//private static final String BaseServiceURL = "http://guizu.sziotech.com/NobleAPP/";
		/**
		 * ע��
		 */
		public static final String INTERFACE_USER_REGISTER = BaseServiceURL
				+ "User/Register?";

		/**
		 * ��ȡ��֤�� ע�ᣬ�豸��Ȩ���һ�����
		 */
		public static final String INTERFACE_USER_RandomCode = BaseServiceURL
				+ "User/RandomCode?";
		/**
		 * ��֤
		 */
		public static final String INTERFACE_USER_VerifyCode = BaseServiceURL
				+ "User/VerifyCode?";
		/**
		 * ���� �������룬��������
		 */
		public static final String INTERFACE_USER_RESET_PASSWORD = BaseServiceURL
				+ "User/ResetPassWord?";
		/**
		 * ��¼
		 */
		public static final String INTERFACE_USER_LOGIN = BaseServiceURL
				+ "User/Login?";
		/**
		 * ��ȡ��Ʒ��Ϣ
		 */
		public static final String INTERFACE_PRODUCT_INFO = BaseServiceURL
				+ "Product/getProductInfo?";
		/**
		 * ���� ȫ�ּ��������豸�������ƽ��������
		 */
		public static final String INTERFACE_RRODUCT_SEARCH = BaseServiceURL
				+ "Product/Search?";
		/**
		 * �豸�б� �ƹ��б��ƽ��б���ѯ���б�
		 */
		public static final String INTERFACE_DEVICE_LIST = BaseServiceURL
				+ "Device/List?";
		/**
		 * ����豸 ��Ӿƹ���Ӿƽѣ���Ӳ�ѯ��
		 */
		public static final String INTERFACE_DEVICE_ADD = BaseServiceURL
				+ "Device/Add?";
		/**
		 * �ж��豸�Ƿ����ڸ��û�
		 */
		public static final String INTERFACE_DEVICE_ISOWNER = BaseServiceURL
				+ "Device/Isowner?";
		/**
		 * �豸��Ϣ����
		 */
		public static final String INTERFACE_DEVICE_UPDATE = BaseServiceURL
				+ "Device/Update?";
		/**
		 * �豸���� ֧�ֶ��豸
		 */
		public static final String INTERFACE_DEVICE_REPORT = BaseServiceURL
				+ "Device/Report?";
		/**
		 * �ƽ������б�
		 */
		public static final String INTERFACE_DEVICE_REGIONAL_LIST = BaseServiceURL
				+ "Device/RegionalList?";
		/**
		 * ��Դ�б� ���豸
		 */
		public static final String INTERFACE_ENVIRONMENT_LIGHT_LIST = BaseServiceURL
				+ "Environment/LightList?";
		/**
		 * ��Դ����
		 */
		public static final String INTERFACE_ENVIRONMENT_LIGHT_CONTROL = BaseServiceURL
				+ "Environment/LightControl?";
		/**
		 * �¶ȿ���
		 */
		public static final String INTERFACE_ENVIRONMENT_TEMPERATURE_CONTROL = BaseServiceURL
				+ "Environment/TemperatureControl?";
		/**
		 * app����
		 */
		public static final String INTERFACE_APP_UPGRADE = BaseServiceURL
				+ "App/Upgrade?";
		/**
		 * app����
		 */
		public static final String INTERFACE_APP_FEEDBACK = BaseServiceURL
				+ "App/FeedBack?";
		/**
		 * ͼƬ�ϴ�
		 */
		public static final String INTERFACE_USER_UPLOAD = BaseServiceURL
				+ "User/UploadImage?";

	}

	/**
	 * ΢����������ƽ̨ key
	 * 
	 * @author _blank :24611015@qq.com
	 * 
	 */
	public class WXVoice {
		public static final String WXVOICE_KEY = "1ae7ffbb3aba2b642d65239c3743946f2284477daf59a233";
	}
	/**
	 * ΢�ſ���ƽ̨ 
	 * @author _blank :24611015@qq.com
	 *
	 */
	public class WXShare {
		public static final String appid = "wxc74c4753c1d904c1";
		//CC:E4:C6:9E:2A:D6:FB:20:A7:18:E4:7C:71:21:12:04
	}

	/**
	 * ������keyֵ
	 * 
	 * @author _blank :24611015@qq.com
	 * 
	 */
	public class Key {

		public static final String key_pageIndex = "index";
		public static final String key_Name = "name";
		public static final String key_Email = "email";
		public static final String key_Address = "address";
		public static final String key_Remark = "remark";
		public static final String key_Number = "number";
		public static final String key_Type = "type";
		public static final String key_Id = "id";
		public static final String key_PassWord = "passWord";
		public static final String key_VerifyCode = "code";
		public static final String key_PW = "PW";
		public static final String key_newPW = "newPW";
		public static final String key_userId = "userId";
		public static final String key_getCode = "Code";
		public static final String key_getCodeType = "codeType";
		public static final String key_getContentType = "contentType";
		public static final String key_searchKey = "key";
		public static final String key_searchDeviceId = "deviceId";
		public static final String key_searchDeviceType = "deviceType";
		public static final String key_searchAreaId = "areaId";
		public static final String key_updateIsUser = "isUser";
		public static final String key_reportStartTime = "starTime";
		public static final String key_reportEndTime = "endTime";
		public static final String key_feedBackContent = "content";
		public static final String key_versionCode = "localVesion";
	}

	/**
	 * ��ǩ
	 * 
	 * @author _blank :24611015@qq.com
	 * 
	 */
	public class Tag {
		public static final int TAG_GRADEVIN_MANAGER = 1;// �ƹ����
		public static final int TAG_WINECELLAR_MANAGER = 2;// �ƽѹ���
		public static final int TAG_USER_FEEDBACK = 4;// �û��������
		public static final int TAG_USER_SETUP = 5;// �û�����
		public static final int TAG_USER_SHAREAPP = 6;// ����APP
		public static final int TAG_USER_ADDGRADEVIN = 7;// ��Ӿƹ�
		public static final int TAG_USER_ADDWINECELLAR = 8;// ��Ӿƽ�
		public static final int TAG_USER_INFOR = 9;// �û���Ϣ
		public static final int TAG_USER_LOGIN = 10;// �û���¼
		public static final int TAG_SALE_REPORT = 11;// ���۱���/���ѱ���
		public static final int TAG_PRODUCT_INFO = 3;// ��Ʒ����,Productid
		public static final int TAG_CODE_TYPE_PRODUCTID = 12;// ��Ʒ����,BARCODE
		public static final int TAG_CODE_TYPE_BARCODE = 13;// ��Ʒ����,RFID
		public static final int TAG_CODE_TYPE_CODE = 14;// ��Ʒ����,һά��
		public static final int TAG_CODE_TYPE_RFID = 15;// ��Ʒ����,һά��
		public static final int TAG_GARVIDEN_LIST = 16;// �ƹ��б�
		public static final int TAG_WINLLER_LIST = 17;// �ƽ��б�
	}

	public class Socket {
		/**
		 * socket ip��ַ
		 */
		public static final String SOCKET_ADDRESS_GRAVIDEN = "stock.sziotech.com";
		public static final String SOCKET_ADDRESS_WINECALLER = "cellar.sziotech.com";
		// public static final String SOCKET_ADDRESS = "172.16.60.17";
		/**
		 * socket �˿�
		 */
		public static final int SOCKET_PORT_GRAVIDEN = 2015;
		public static final int SOCKET_PORT_WINECALLER = 9999;
		/**
		 * ��ͷ
		 */
		public static final String PACKAGE_HEAD = "0x00";
		/**
		 * ��β
		 */
		public static final String PACKAGE_FOOT = "0xFF";
		/**
		 * ָ���������
		 */
		public static final String CMD_CONNECT = "0x00";
		/**
		 * ָ��ƹ���Ϣ
		 */
		public static final String CMD_INFO = "0x01";
		/**
		 * ָ�����/��ȡ�¶�
		 */
		public static final String CMD_TEMPER = "0x02";
		/**
		 * ָ�����/��ȡʪ��
		 */
		public static final String CMD_HUMIDITY = "0x03";
		/**
		 * ָ��洢
		 */
		public static final String CMD_STOCK = "0x04";
		/**
		 * ָ���Դ���ƣ�����
		 */
		public static final String CMD_LAMP = "0x05";
		/**
		 * ָ������ƣ�����
		 */
		public static final String CMD_LOCK = "0x06";
		/**
		 * ָ���λ��ѯ
		 */
		public static final String CMD_POSITION = "0x07";
		/**
		 * ָ���������
		 */
		public static final String CMD_PASSWORD = "0x08";
		/**
		 * ָ��ƹ�����״̬
		 */
		public static final String CMD_STATE = "0x09";
		/**
		 * ָ��ƹ����λ��
		 */
		public static final String CMD_LOCATION = "0x10";
		/**
		 * ָ��Ͽ�����
		 */
		public static final String CMD_DISCONNECT = "0x0a";
		/**
		 * ָ��������쳣
		 */
		public static final String CMD_SERVER_ERROR = "0xaa";
		/**
		 * ָ�����
		 */
		public static final String CMD_BREATH = "0xbb";
	}

	public class Action {
		public static final String SOCKET_BROADCAST_ACTION = "COM.TEMPUS.ACTIONS_";
		/**
		 * action����������
		 */
		public static final String ACTION_CONNECT = SOCKET_BROADCAST_ACTION
				+ "CONNECT ";
		/**
		 * action���ƹ���Ϣ
		 */
		public static final String ACTION_INFO = SOCKET_BROADCAST_ACTION
				+ "INFO";
		/**
		 * action������/��ȡ�¶�
		 */
		public static final String ACTION_TEMPER = SOCKET_BROADCAST_ACTION
				+ "TEMPER";
		/**
		 * action����ȡ�����¶�
		 */
		public static final String ACTION_INTEMPER = SOCKET_BROADCAST_ACTION
				+ "INTEMPER";
		/**
		 * action������/��ȡʪ��
		 */
		public static final String ACTION_HUMIDITY = SOCKET_BROADCAST_ACTION
				+ "HUMIDITY";
		/**
		 * action����ȡ����ʪ��
		 */
		public static final String ACTION_INHUMIDITY = SOCKET_BROADCAST_ACTION
				+ "INHUMIDITY";
		/**
		 * action���洢
		 */
		public static final String ACTION_STOCK = SOCKET_BROADCAST_ACTION
				+ "STOCK";
		/**
		 * action����Դ���ƣ�����
		 */
		public static final String ACTION_LAMP = SOCKET_BROADCAST_ACTION
				+ "LAMP";
		/**
		 * action�������ƣ�����
		 */
		public static final String ACTION_LOCK = SOCKET_BROADCAST_ACTION
				+ "LOCK";
		/**
		 * action����λ��ѯ
		 */
		public static final String ACTION_POSITION = SOCKET_BROADCAST_ACTION
				+ "POSITION";
		/**
		 * action����������
		 */
		public static final String ACTION_PASSWORD = SOCKET_BROADCAST_ACTION
				+ "PASSWORD";
		/**
		 * action���ƹ�����״̬
		 */
		public static final String ACTION_STATE = SOCKET_BROADCAST_ACTION
				+ "STATE";
		/**
		 * action���ƹ����λ��
		 */
		public static final String ACTION_LOCATION = SOCKET_BROADCAST_ACTION
				+ "LOCATION";
		/**
		 * action���Ͽ�����
		 */
		public static final String ACTION_DISCONNECT = SOCKET_BROADCAST_ACTION
				+ "DISCONNECT";
		/**
		 * action���������쳣
		 */
		public static final String ACTION_SERVER_ERROR = SOCKET_BROADCAST_ACTION
				+ "SERVER_ERROR";
		/**
		 * action������
		 */
		public static final String ACTION_BREATH = SOCKET_BROADCAST_ACTION
				+ "BREATH ";
		public static final String ACTION_ADDDEVICE_DONE = SOCKET_BROADCAST_ACTION
				+ "ADDDEVICE ";
	}
}
