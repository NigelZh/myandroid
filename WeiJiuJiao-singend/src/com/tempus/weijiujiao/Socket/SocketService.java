package com.tempus.weijiujiao.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonUtils;
import com.tempus.weijiujiao.Utils.ParamsUtils;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.constant.Constant;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class SocketService extends Service {
	private String deviceID = null;
	private int deviceType = -1;
	private ExecutorService excutor = Executors.newFixedThreadPool(3);
	private Socket socket;
	private ParamsUtils paramsutils;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Debug.Out("onBind");
		deviceID = intent.getStringExtra("id");
		deviceType = intent.getIntExtra("deviceType", -1);
		Debug.Out("SocketService onBind ", "deviceID=" + deviceID+ "&&deviceType=" + deviceType);
		paramsutils = ParamsUtils.getInstance(getApplicationContext());
		if (deviceID != null && deviceType != -1) {
			connectToServer();
		}
		return myBinder;

	}

	/**
	 * 设置密码
	 * 
	 * @param uid
	 *            请求识别号
	 * @param deviceid
	 *            设备编号
	 * @param oldPw
	 *            原始密码
	 * @param newPw
	 *            新密码
	 */
	public void setPassowrd(String uid, String deviceid, String oldPw,
			String newPw) {
		String content = "{\\\"" + "OldPwd\\\":\\\"" + oldPw + "\\\",\\\""
				+ "Newpwd\\\":\\\"" + newPw + "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_PASSWORD, deviceid, content));
	}

	/**
	 * 酒位查询
	 * 
	 * @param uid
	 *            请求识别号
	 * @param deviceid
	 *            设备编号
	 */
	public void getPosition(String uid, String deviceid) {
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_POSITION, deviceid, ""));
	}

	/**
	 * 开关锁
	 * 
	 * @param uid
	 *            请求识别号
	 * @param deviceid
	 *            设备编号
	 * @param isOpen
	 *            开True,关False;
	 */
	public void setLock(String uid, String deviceid, boolean islockl) {
		String content = "{\\\"" + "Lock\\\":\\\"" + (islockl ? 0 : 1)
				+ "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_LOCK, deviceid, content));
	}

	/**
	 * 开关灯
	 * 
	 * @param uid
	 *            请求识别
	 * @param deviceid
	 *            设备编号
	 * @param lampId
	 *            灯编号
	 * @param isOpen
	 *            开True，关False
	 */
	public void setLamper(String uid, String deviceid, String lampId,
			boolean isOpen) {
		String content = "{\\\"" + "LampId\\\":\\\"" + lampId + "\\\",\\\""
				+ "Lamp\\\":\\\"" + (isOpen ? 1 : 0) + "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_LAMP, deviceid, content));
	}

	/**
	 * 读取酒柜存储比例
	 * 
	 * @param uid
	 *            请求识别号
	 * @param deviceid
	 *            设备编号
	 */
	public void getStock(String uid, String deviceid) {
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_STOCK, deviceid, ""));
	}

	/**
	 * 设置酒柜湿度
	 * 
	 * @param uid
	 *            请求识别号
	 * @param deviceid
	 *            设备编号
	 * @param humidity
	 *            要设置的湿度值
	 */
	public void setHumidity(String uid, String deviceid, int humidity) {
		String content = "{\\\"" + "Humidity\\\":\\\"" + humidity + "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_HUMIDITY, deviceid, content));
	}

	/**
	 * 设置酒柜温度
	 * 
	 * @param uid
	 *            请求识别号
	 * @param deviceid
	 *            设备编号
	 * @param temp
	 *            要设置的温度
	 */
	public void setTemper(String uid, String deviceid, int temp) {
		String content = "{\\\"" + "Temper\\\":\\\"" + temp + "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_TEMPER, deviceid, content));
	}

	/**
	 * 请求酒柜实时信息
	 * 
	 * @param uid
	 *            请求识别号
	 * @param Deviceid
	 *            设备编号
	 */
	public void getDeviceInfo(String uid, String Deviceid) {
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_INFO, Deviceid, ""));
	}

	/**
	 * 连接服务器
	 */
	private void connectToServer() {
		// TODO Auto-generated method stub
		Runnable rb = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (deviceType == 2) {
						socket = new Socket(
								Constant.Socket.SOCKET_ADDRESS_GRAVIDEN,
								Constant.Socket.SOCKET_PORT_GRAVIDEN);
					} else {
						socket = new Socket(
								Constant.Socket.SOCKET_ADDRESS_WINECALLER,
								Constant.Socket.SOCKET_PORT_WINECALLER);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					socket = null;
					e.printStackTrace();
				} finally {
					if (socket != null) {
						requestConnect();
						startReadTask();
					}
				}
			}
		};
		excutor.execute(rb);
	}

	/**
	 * 请求连接
	 */
	public void requestConnect() {
		sendRequstMessage(paramsutils.getSocketRequestMap(
				System.currentTimeMillis() + "", Constant.Socket.CMD_CONNECT,
				deviceID, ""));
	}

	/**
	 * 启动读线程
	 */
	protected void startReadTask() {
		// TODO Auto-generated method stub
		Runnable rb = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(socket.getInputStream(),
									"utf-8"));
					new StringBuilder();
					String temp;
					while ((temp = br.readLine()) != null) {
						if (temp.startsWith(Constant.Socket.PACKAGE_HEAD)
								&& temp.endsWith(Constant.Socket.PACKAGE_FOOT)) {
							temp = StringUtils.deepclearMystring(temp
									.substring(4, temp.length() - 4));
							Debug.Out("socket get message", temp);
							MessageHandler.handleMessage(temp);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		excutor.execute(rb);
	}

	/**
	 * 向服务器发起一条指令
	 * 
	 * @param contentMap
	 *            内容集合 。包含 singleRequestID 请求ID； Length 报文长度； StockSn 酒柜编号；
	 *            CmdWord 指令； Content 指令内容； Check 校验
	 */
	private void sendRequstMessage(final HashMap<String, String> paramsMap) {
		Runnable rb = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (socket != null && socket.isConnected()) {
					try {
						Writer writer = new OutputStreamWriter(
								socket.getOutputStream());
						String str = Constant.Socket.PACKAGE_HEAD
								+ JsonUtils.map2Json(paramsMap)
								+ Constant.Socket.PACKAGE_FOOT + "\r\n";
						Debug.Out("socket write", str);
						writer.write(str);
						writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		excutor.execute(rb);
	}

	public class MyBinder extends Binder {
		public SocketService getService() {
			return SocketService.this;
		}
	}

	private MyBinder myBinder = new MyBinder();

	@Override
	public void onDestroy() {
		super.onDestroy();
		Debug.Out("service onDestroy ");
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
