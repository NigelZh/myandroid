package com.tempus.weijiujiao.Socket;

import org.json.JSONException;

import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.constant.Constant;

import android.content.Intent;

public class MessageHandler {

	public static void handleMessage(String socketMessage) {
		try {
			String cmd = JsonParser.parserSocketMessageCMD(socketMessage)
					.trim();
			if (cmd != null) {
				switch (cmd) {
				case Constant.Socket.CMD_CONNECT:
					broadCastMessage(Constant.Action.ACTION_CONNECT,
							JsonParser.parserSocketConnectState(socketMessage));
					break;
				case Constant.Socket.CMD_DISCONNECT:
					broadCastMessage(Constant.Action.ACTION_DISCONNECT, null);
					break;
				case Constant.Socket.CMD_INFO:
					handleTemper(socketMessage);
					handleHumidity(socketMessage);
					handleStock(socketMessage);
					handleLamp(socketMessage);
					handleLock(socketMessage);
					handleInHumidity(socketMessage);
					handleInTemper(socketMessage);
					break;
				case Constant.Socket.CMD_TEMPER:
					handleTemper(socketMessage);
					break;
				case Constant.Socket.CMD_HUMIDITY:
					handleHumidity(socketMessage);
					break;
				case Constant.Socket.CMD_STOCK:
					handleStock(socketMessage);
					break;
				case Constant.Socket.CMD_LAMP:
					handleLamp(socketMessage);
					break;
				case Constant.Socket.CMD_LOCK:
					handleLock(socketMessage);
					break;
				case Constant.Socket.CMD_PASSWORD:
					handlePassword(socketMessage);
					break;
				case Constant.Socket.CMD_STATE:
					handleDeviceState(socketMessage);
					break;
				case Constant.Socket.CMD_SERVER_ERROR:
					broadCastSocketError("SERVER ERROR");
				default:
					broadCastSocketError("CMD IS NOT AVAIABLE");
					break;
				}
			} else {
				broadCastSocketError("CMD IS NULL");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			broadCastSocketError("JSON ERROR");
			e.printStackTrace();
		}
	}

	private static void handleInTemper(String socketMessage)
			throws JSONException {
		// TODO Auto-generated method stub
		broadCastMessage(Constant.Action.ACTION_INTEMPER,
				JsonParser.parserSocketInTemper(socketMessage));
	}

	private static void handleInHumidity(String socketMessage)
			throws JSONException {
		// TODO Auto-generated method stub
		broadCastMessage(Constant.Action.ACTION_INHUMIDITY,
				JsonParser.parserSocketInHumidity(socketMessage));
	}

	private static void handleDeviceState(String socketMessage)
			throws JSONException {
		// TODO Auto-generated method stub
		broadCastMessage(Constant.Action.ACTION_STATE,
				JsonParser.parserSocketDeviceState(socketMessage));
	}

	private static void handlePassword(String socketMessage)
			throws JSONException {
		// TODO Auto-generated method stub
		broadCastMessage(Constant.Action.ACTION_PASSWORD,
				JsonParser.parserSocketSetPassWord(socketMessage));
	}

	private static void handleLock(String socketMessage) throws JSONException {
		// TODO Auto-generated method stub
		broadCastMessage(Constant.Action.ACTION_LOCK,
				JsonParser.parserSocketLock(socketMessage));
	}

	private static void handleLamp(String socketMessage) throws JSONException {
		// TODO Auto-generated method stub
		String[] lamp = JsonParser.parserSocketLamp(socketMessage);
		broadCastLampMessage(lamp[0], Integer.parseInt(lamp[1]));
	}

	private static void handleStock(String socketMessage) throws JSONException {
		// TODO Auto-generated method stub
		int[] stock = JsonParser.parserSocketStock(socketMessage);
		broadCastMessage(stock[0], stock[1]);
	}

	private static void handleHumidity(String socketMessage)
			throws JSONException {
		// TODO Auto-generated method stub
		broadCastMessage(Constant.Action.ACTION_HUMIDITY,
				JsonParser.parserSocketHumidity(socketMessage));
	}

	private static void handleTemper(String socketMessage) throws JSONException {
		// TODO Auto-generated method stub
		broadCastMessage(Constant.Action.ACTION_TEMPER,
				JsonParser.parserSocketTemper(socketMessage));
	}

	/**
	 * 广播 一般信息
	 * 
	 * @param action
	 * @param data
	 */
	public static void broadCastMessage(String action, String data) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (!StringUtils.isNull(data)) {
			intent.putExtra("data", data);
		}
		MyApplication.getinstance().getApplicationContext()
				.sendBroadcast(intent);
	}

	public static void broadCastMessage(String action, int data) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtra("data", data);
		MyApplication.getinstance().getApplicationContext()
				.sendBroadcast(intent);
	}

	/**
	 * 广播 存储信息
	 * 
	 * @param stock
	 * @param totalsize
	 */
	public static void broadCastMessage(int stock, int totalsize) {
		Intent intent = new Intent();
		intent.setAction(Constant.Action.ACTION_STOCK);
		intent.putExtra("stock", stock);
		intent.putExtra("totalsize", totalsize);
		MyApplication.getinstance().getApplicationContext()
				.sendBroadcast(intent);
	}

	/**
	 * 广播 灯源信息
	 * 
	 * @param lampid
	 * @param lampState
	 */
	public static void broadCastLampMessage(String lampid, int lampState) {
		Intent intent = new Intent();
		intent.setAction(Constant.Action.ACTION_LAMP);
		intent.putExtra("lampId", lampid);
		intent.putExtra("lampState", lampState);
		MyApplication.getinstance().getApplicationContext().sendBroadcast(intent);
	}

	/**
	 * 广播 错误信息
	 * 
	 * @param errorInfo
	 */
	public static void broadCastSocketError(String errorInfo) {
		Intent intent = new Intent();
		intent.setAction(Constant.Action.SOCKET_BROADCAST_ACTION);
		intent.putExtra("errorInfo", errorInfo);
		MyApplication.getinstance().getApplicationContext()
				.sendBroadcast(intent);
	}
}
