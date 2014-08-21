package com.tempus.weijiujiao.constant;

/**
 * 配置类
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class Constant {
	/**
	 * 接口地址
	 * 
	 * @author _blank :24611015@qq.com
	 * 
	 */
	public class WebSate {
		/**
		 * 接口根地址
		 */
		//private static final String BaseServiceURL = "http://guizu.sziotech.com/NobleAPP/";
		//private static final String BaseServiceURL = "http://172.16.60.18/NobleAPP/";
		private static final String BaseServiceURL = "http://172.16.60.199:7090/NobleAPP/";
		//private static final String BaseServiceURL = "http://guizu.sziotech.com/NobleAPP/";
		/**
		 * 注册
		 */
		public static final String INTERFACE_USER_REGISTER = BaseServiceURL
				+ "User/Register?";

		/**
		 * 获取验证码 注册，设备授权，找回密码
		 */
		public static final String INTERFACE_USER_RandomCode = BaseServiceURL
				+ "User/RandomCode?";
		/**
		 * 验证
		 */
		public static final String INTERFACE_USER_VerifyCode = BaseServiceURL
				+ "User/VerifyCode?";
		/**
		 * 密码 设置密码，重置密码
		 */
		public static final String INTERFACE_USER_RESET_PASSWORD = BaseServiceURL
				+ "User/ResetPassWord?";
		/**
		 * 登录
		 */
		public static final String INTERFACE_USER_LOGIN = BaseServiceURL
				+ "User/Login?";
		/**
		 * 获取商品信息
		 */
		public static final String INTERFACE_PRODUCT_INFO = BaseServiceURL
				+ "Product/getProductInfo?";
		/**
		 * 检索 全局检索，单设备检索，酒窖区域检索
		 */
		public static final String INTERFACE_RRODUCT_SEARCH = BaseServiceURL
				+ "Product/Search?";
		/**
		 * 设备列表 酒柜列表，酒窖列表，查询机列表
		 */
		public static final String INTERFACE_DEVICE_LIST = BaseServiceURL
				+ "Device/List?";
		/**
		 * 添加设备 添加酒柜，添加酒窖，添加查询机
		 */
		public static final String INTERFACE_DEVICE_ADD = BaseServiceURL
				+ "Device/Add?";
		/**
		 * 判断设备是否属于该用户
		 */
		public static final String INTERFACE_DEVICE_ISOWNER = BaseServiceURL
				+ "Device/Isowner?";
		/**
		 * 设备信息更新
		 */
		public static final String INTERFACE_DEVICE_UPDATE = BaseServiceURL
				+ "Device/Update?";
		/**
		 * 设备报表 支持多设备
		 */
		public static final String INTERFACE_DEVICE_REPORT = BaseServiceURL
				+ "Device/Report?";
		/**
		 * 酒窖区域列表
		 */
		public static final String INTERFACE_DEVICE_REGIONAL_LIST = BaseServiceURL
				+ "Device/RegionalList?";
		/**
		 * 灯源列表 单设备
		 */
		public static final String INTERFACE_ENVIRONMENT_LIGHT_LIST = BaseServiceURL
				+ "Environment/LightList?";
		/**
		 * 灯源控制
		 */
		public static final String INTERFACE_ENVIRONMENT_LIGHT_CONTROL = BaseServiceURL
				+ "Environment/LightControl?";
		/**
		 * 温度控制
		 */
		public static final String INTERFACE_ENVIRONMENT_TEMPERATURE_CONTROL = BaseServiceURL
				+ "Environment/TemperatureControl?";
		/**
		 * app升级
		 */
		public static final String INTERFACE_APP_UPGRADE = BaseServiceURL
				+ "App/Upgrade?";
		/**
		 * app反馈
		 */
		public static final String INTERFACE_APP_FEEDBACK = BaseServiceURL
				+ "App/FeedBack?";
		/**
		 * 图片上传
		 */
		public static final String INTERFACE_USER_UPLOAD = BaseServiceURL
				+ "User/UploadImage?";

	}

	/**
	 * 微信语音开放平台 key
	 * 
	 * @author _blank :24611015@qq.com
	 * 
	 */
	public class WXVoice {
		public static final String WXVOICE_KEY = "1ae7ffbb3aba2b642d65239c3743946f2284477daf59a233";
	}
	/**
	 * 微信开放平台 
	 * @author _blank :24611015@qq.com
	 *
	 */
	public class WXShare {
		public static final String appid = "wxc74c4753c1d904c1";
		//CC:E4:C6:9E:2A:D6:FB:20:A7:18:E4:7C:71:21:12:04
	}

	/**
	 * 参数表key值
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
	 * 标签
	 * 
	 * @author _blank :24611015@qq.com
	 * 
	 */
	public class Tag {
		public static final int TAG_GRADEVIN_MANAGER = 1;// 酒柜管理
		public static final int TAG_WINECELLAR_MANAGER = 2;// 酒窖管理
		public static final int TAG_USER_FEEDBACK = 4;// 用户意见反馈
		public static final int TAG_USER_SETUP = 5;// 用户设置
		public static final int TAG_USER_SHAREAPP = 6;// 分享APP
		public static final int TAG_USER_ADDGRADEVIN = 7;// 添加酒柜
		public static final int TAG_USER_ADDWINECELLAR = 8;// 添加酒窖
		public static final int TAG_USER_INFOR = 9;// 用户信息
		public static final int TAG_USER_LOGIN = 10;// 用户登录
		public static final int TAG_SALE_REPORT = 11;// 销售报表/消费报表
		public static final int TAG_PRODUCT_INFO = 3;// 商品详情,Productid
		public static final int TAG_CODE_TYPE_PRODUCTID = 12;// 商品详情,BARCODE
		public static final int TAG_CODE_TYPE_BARCODE = 13;// 商品详情,RFID
		public static final int TAG_CODE_TYPE_CODE = 14;// 商品详情,一维码
		public static final int TAG_CODE_TYPE_RFID = 15;// 商品详情,一维码
		public static final int TAG_GARVIDEN_LIST = 16;// 酒柜列表
		public static final int TAG_WINLLER_LIST = 17;// 酒窖列表
	}

	public class Socket {
		/**
		 * socket ip地址
		 */
		public static final String SOCKET_ADDRESS_GRAVIDEN = "stock.sziotech.com";
		public static final String SOCKET_ADDRESS_WINECALLER = "cellar.sziotech.com";
		// public static final String SOCKET_ADDRESS = "172.16.60.17";
		/**
		 * socket 端口
		 */
		public static final int SOCKET_PORT_GRAVIDEN = 2015;
		public static final int SOCKET_PORT_WINECALLER = 9999;
		/**
		 * 包头
		 */
		public static final String PACKAGE_HEAD = "0x00";
		/**
		 * 包尾
		 */
		public static final String PACKAGE_FOOT = "0xFF";
		/**
		 * 指令：请求连接
		 */
		public static final String CMD_CONNECT = "0x00";
		/**
		 * 指令：酒柜信息
		 */
		public static final String CMD_INFO = "0x01";
		/**
		 * 指令：设置/读取温度
		 */
		public static final String CMD_TEMPER = "0x02";
		/**
		 * 指令：设置/读取湿度
		 */
		public static final String CMD_HUMIDITY = "0x03";
		/**
		 * 指令：存储
		 */
		public static final String CMD_STOCK = "0x04";
		/**
		 * 指令：灯源控制，开关
		 */
		public static final String CMD_LAMP = "0x05";
		/**
		 * 指令：锁控制，开关
		 */
		public static final String CMD_LOCK = "0x06";
		/**
		 * 指令：酒位查询
		 */
		public static final String CMD_POSITION = "0x07";
		/**
		 * 指令：密码设置
		 */
		public static final String CMD_PASSWORD = "0x08";
		/**
		 * 指令：酒柜连接状态
		 */
		public static final String CMD_STATE = "0x09";
		/**
		 * 指令：酒柜地理位置
		 */
		public static final String CMD_LOCATION = "0x10";
		/**
		 * 指令：断开连接
		 */
		public static final String CMD_DISCONNECT = "0x0a";
		/**
		 * 指令：服务器异常
		 */
		public static final String CMD_SERVER_ERROR = "0xaa";
		/**
		 * 指令：心跳
		 */
		public static final String CMD_BREATH = "0xbb";
	}

	public class Action {
		public static final String SOCKET_BROADCAST_ACTION = "COM.TEMPUS.ACTIONS_";
		/**
		 * action：请求连接
		 */
		public static final String ACTION_CONNECT = SOCKET_BROADCAST_ACTION
				+ "CONNECT ";
		/**
		 * action：酒柜信息
		 */
		public static final String ACTION_INFO = SOCKET_BROADCAST_ACTION
				+ "INFO";
		/**
		 * action：设置/读取温度
		 */
		public static final String ACTION_TEMPER = SOCKET_BROADCAST_ACTION
				+ "TEMPER";
		/**
		 * action：读取室内温度
		 */
		public static final String ACTION_INTEMPER = SOCKET_BROADCAST_ACTION
				+ "INTEMPER";
		/**
		 * action：设置/读取湿度
		 */
		public static final String ACTION_HUMIDITY = SOCKET_BROADCAST_ACTION
				+ "HUMIDITY";
		/**
		 * action：读取室内湿度
		 */
		public static final String ACTION_INHUMIDITY = SOCKET_BROADCAST_ACTION
				+ "INHUMIDITY";
		/**
		 * action：存储
		 */
		public static final String ACTION_STOCK = SOCKET_BROADCAST_ACTION
				+ "STOCK";
		/**
		 * action：灯源控制，开关
		 */
		public static final String ACTION_LAMP = SOCKET_BROADCAST_ACTION
				+ "LAMP";
		/**
		 * action：锁控制，开关
		 */
		public static final String ACTION_LOCK = SOCKET_BROADCAST_ACTION
				+ "LOCK";
		/**
		 * action：酒位查询
		 */
		public static final String ACTION_POSITION = SOCKET_BROADCAST_ACTION
				+ "POSITION";
		/**
		 * action：密码设置
		 */
		public static final String ACTION_PASSWORD = SOCKET_BROADCAST_ACTION
				+ "PASSWORD";
		/**
		 * action：酒柜连接状态
		 */
		public static final String ACTION_STATE = SOCKET_BROADCAST_ACTION
				+ "STATE";
		/**
		 * action：酒柜地理位置
		 */
		public static final String ACTION_LOCATION = SOCKET_BROADCAST_ACTION
				+ "LOCATION";
		/**
		 * action：断开连接
		 */
		public static final String ACTION_DISCONNECT = SOCKET_BROADCAST_ACTION
				+ "DISCONNECT";
		/**
		 * action：服务器异常
		 */
		public static final String ACTION_SERVER_ERROR = SOCKET_BROADCAST_ACTION
				+ "SERVER_ERROR";
		/**
		 * action：心跳
		 */
		public static final String ACTION_BREATH = SOCKET_BROADCAST_ACTION
				+ "BREATH ";
		public static final String ACTION_ADDDEVICE_DONE = SOCKET_BROADCAST_ACTION
				+ "ADDDEVICE ";
	}
}
