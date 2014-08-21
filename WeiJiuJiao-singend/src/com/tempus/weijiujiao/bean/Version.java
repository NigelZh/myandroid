package com.tempus.weijiujiao.bean;

public class Version {

	private int versionCode;
	private String versionName;
	private String appUrl;
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return this.versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getAppUrl() {
		return this.appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
}
