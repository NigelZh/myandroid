package com.tempus.weijiujiao.HTTP;

/**
 * ������������װ
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class Result {
	/**
	 * �ɹ�
	 */
	public static final int CODE_OK = 0;
	/**
	 * ʧ��  �������
	 */
	public static final int CODE_REQUEST_ERROR = 1;
	/**
	 * ʧ�ܣ����粻����
	 */
	public static final int CDOE_NETWORK_UNAVAIABLE=2;
	private String errorInfo;
	private String jsonBody;
	private int StatucCode;

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public int getStatucCode() {
		return StatucCode;
	}

	public void setStatucCode(int statucCode) {
		StatucCode = statucCode;
	}

	public String getJsonBody() {
		return jsonBody;
	}

	public void setJsonBody(String jsonBody) {
		this.jsonBody = jsonBody;
	}
}
