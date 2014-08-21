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
	 * ��������
	 * 
	 * @param uid
	 *            ����ʶ���
	 * @param deviceid
	 *            �豸���
	 * @param oldPw
	 *            ԭʼ����
	 * @param newPw
	 *            ������
	 */
	public void setPassowrd(String uid, String deviceid, String oldPw,
			String newPw) {
		String content = "{\\\"" + "OldPwd\\\":\\\"" + oldPw + "\\\",\\\""
				+ "Newpwd\\\":\\\"" + newPw + "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_PASSWORD, deviceid, content));
	}

	/**
	 * ��λ��ѯ
	 * 
	 * @param uid
	 *            ����ʶ���
	 * @param deviceid
	 *            �豸���
	 */
	public void getPosition(String uid, String deviceid) {
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_POSITION, deviceid, ""));
	}

	/**
	 * ������
	 * 
	 * @param uid
	 *            ����ʶ���
	 * @param deviceid
	 *            �豸���
	 * @param isOpen
	 *            ��True,��False;
	 */
	public void setLock(String uid, String deviceid, boolean islockl) {
		String content = "{\\\"" + "Lock\\\":\\\"" + (islockl ? 0 : 1)
				+ "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_LOCK, deviceid, content));
	}

	/**
	 * ���ص�
	 * 
	 * @param uid
	 *            ����ʶ��
	 * @param deviceid
	 *            �豸���
	 * @param lampId
	 *            �Ʊ��
	 * @param isOpen
	 *            ��True����False
	 */
	public void setLamper(String uid, String deviceid, String lampId,
			boolean isOpen) {
		String content = "{\\\"" + "LampId\\\":\\\"" + lampId + "\\\",\\\""
				+ "Lamp\\\":\\\"" + (isOpen ? 1 : 0) + "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_LAMP, deviceid, content));
	}

	/**
	 * ��ȡ�ƹ�洢����
	 * 
	 * @param uid
	 *            ����ʶ���
	 * @param deviceid
	 *            �豸���
	 */
	public void getStock(String uid, String deviceid) {
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_STOCK, deviceid, ""));
	}

	/**
	 * ���þƹ�ʪ��
	 * 
	 * @param uid
	 *            ����ʶ���
	 * @param deviceid
	 *            �豸���
	 * @param humidity
	 *            Ҫ���õ�ʪ��ֵ
	 */
	public void setHumidity(String uid, String deviceid, int humidity) {
		String content = "{\\\"" + "Humidity\\\":\\\"" + humidity + "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_HUMIDITY, deviceid, content));
	}

	/**
	 * ���þƹ��¶�
	 * 
	 * @param uid
	 *            ����ʶ���
	 * @param deviceid
	 *            �豸���
	 * @param temp
	 *            Ҫ���õ��¶�
	 */
	public void setTemper(String uid, String deviceid, int temp) {
		String content = "{\\\"" + "Temper\\\":\\\"" + temp + "\\\"}";
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_TEMPER, deviceid, content));
	}

	/**
	 * ����ƹ�ʵʱ��Ϣ
	 * 
	 * @param uid
	 *            ����ʶ���
	 * @param Deviceid
	 *            �豸���
	 */
	public void getDeviceInfo(String uid, String Deviceid) {
		sendRequstMessage(paramsutils.getSocketRequestMap(uid,
				Constant.Socket.CMD_INFO, Deviceid, ""));
	}

	/**
	 * ���ӷ�����
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
	 * ��������
	 */
	public void requestConnect() {
		sendRequstMessage(paramsutils.getSocketRequestMap(
				System.currentTimeMillis() + "", Constant.Socket.CMD_CONNECT,
				deviceID, ""));
	}

	/**
	 * �������߳�
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
	 * �����������һ��ָ��
	 * 
	 * @param contentMap
	 *            ���ݼ��� ������ singleRequestID ����ID�� Length ���ĳ��ȣ� StockSn �ƹ��ţ�
	 *            CmdWord ָ� Content ָ�����ݣ� Check У��
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
