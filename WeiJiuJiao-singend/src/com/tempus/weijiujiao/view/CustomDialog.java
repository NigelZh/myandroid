package com.tempus.weijiujiao.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomDialog {

	public static Dialog createDialog(Context context, String title,
			String conotent, boolean cancleAble,
			DialogInterface.OnClickListener onclickListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(conotent);
		builder.setTitle(title);
		builder.setCancelable(cancleAble);
		builder.setPositiveButton("确认", onclickListener);
		builder.setNegativeButton("取消", onclickListener);
		return builder.create();
	}

	public static ProgressDialog  createProgressDialog(Context context) {
		ProgressDialog pd = new ProgressDialog(context);
		pd.setCancelable(false);
		pd.setMessage("加载中...");
		return pd;
	}

	public static void hideProgressDialog(ProgressDialog dialog) {
		if (dialog == null) {
			return;
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
