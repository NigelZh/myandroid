package com.tempus.weijiujiao.Socket;

import java.io.Serializable;
import java.util.List;

import com.tempus.weijiujiao.bean.Light;

public class SocketMessage implements Serializable {
	/**
	 * ��װ��Message
	 */
	private static final long serialVersionUID = 1001110000L;
	private String cmd; // ָ��
	private int connectState; // ���ӷ�����״̬ 1�����ӣ�0�Ͽ�����
	private int setPassWord;//������������1�ɹ���0ʧ��
	private String Temper;// �¶�
	private String InTemper;// �����¶�
	private String Humidity;// ʪ��
	private String InHumidity;// ����
	private Light Lamp;// ��Դ״̬ 1����0��
	private String Lock;// ��״̬ 1�� 0��
	private String StockCount; // �洢��
	private String TotalStock;// ����
	private int deviceState;//�豸״̬��1���ߣ�0������
	private List<Position> positionList;

	public String toMyString(){
		return "SocketMessage:"+"cmd="+this.cmd+"��"+"connectState="+this.connectState+"��"+
	"setPassWord="+this.setPassWord+"��"+
	"Temper="+this.Temper+"��"+
	"InTemper="+this.InTemper+"��"+
	"Humidity="+this.Humidity+"��"+
	"InHumidity="+this.InHumidity+"��"+
	"Lock="+this.Lock+"��"+
	"TotalStock="+this.TotalStock+"��"+
	"deviceState="+this.deviceState+"��"+
	"positionList="+(positionList==null?"null":"not null");
		
	
	
	}
	public String getTemper() {
		return Temper;
	}

	public void setTemper(String temper) {
		Temper = temper;
	}

	public String getInTemper() {
		return InTemper;
	}

	public void setInTemper(String inTemper) {
		InTemper = inTemper;
	}

	public String getHumidity() {
		return Humidity;
	}

	public void setHumidity(String humidity) {
		Humidity = humidity;
	}

	public String getInHumidity() {
		return InHumidity;
	}

	public void setInHumidity(String inHumidity) {
		InHumidity = inHumidity;
	}

	public String getLock() {
		return Lock;
	}

	public void setLock(String lock) {
		Lock = lock;
	}

	public String getTotalStock() {
		return TotalStock;
	}

	public void setTotalStock(String totalStock) {
		TotalStock = totalStock;
	}

	public String getStockCount() {
		return StockCount;
	}

	public void setStockCount(String stockCount) {
		StockCount = stockCount;
	}

	public int getConnectState() {
		return connectState;
	}

	public void setConnectState(int connectState) {
		this.connectState = connectState;
	}

	public List<Position> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Position> positionList) {
		this.positionList = positionList;
	}

	public int getSetPassWord() {
		return setPassWord;
	}

	public void setSetPassWord(int setPassWord) {
		this.setPassWord = setPassWord;
	}

	public int getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(int deviceState) {
		this.deviceState = deviceState;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public Light getLamp() {
		return Lamp;
	}
	public void setLamp(Light lamp) {
		Lamp = lamp;
	}
}
