package com.tempus.weijiujiao.Utils;

import com.tempus.weijiujiao.MyApplication;

import android.widget.Toast;

public class ToastUtils {

	public static void toastMessage(String message) {
		try {
			Toast.makeText(MyApplication.getinstance().getApplicationContext(),
					message, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
