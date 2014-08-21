package com.tempus.weijiujiao.HTTP;

public interface onResultListener {
	public void onResult(Result result);
	public void onNetError(Result result);
	public void onStart();
	public void onFinish();
}
