package com.tempus.weijiujiao.Utils;

import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer {
	private Button btn_view;
	private String currentText;

	public TimeCount(long millisInFuture, long countDownInterval,
			Button btn_view) {
		super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		this.btn_view = btn_view;
		this.currentText = btn_view.getText().toString();
	}

	@Override
	public void onFinish() {
		btn_view.setText(currentText);
		btn_view.setEnabled(true);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		btn_view.setEnabled(false);
		btn_view.setText(currentText+"("+millisUntilFinished/1000+"s)");
	}
}
