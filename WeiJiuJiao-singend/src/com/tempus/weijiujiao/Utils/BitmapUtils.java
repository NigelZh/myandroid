package com.tempus.weijiujiao.Utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

public class BitmapUtils {

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

}
