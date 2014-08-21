package com.tempus.weijiujiao.Utils;

import java.io.File;
import android.content.Context;

public class DataCleanManager {
	static float result = 0.0f;
	static boolean isFirst = true;

	public static void clearTotalCache(Context context, File sdCacheFile) {
		clearDataCache(context);
		clearSdCache(sdCacheFile);
	}

	public static void clearDataCache(Context context) {
		deleteFile(context.getCacheDir());
	}

	public static void clearSdCache(File sdCacheFile) {
		deleteFile(sdCacheFile);
	}

	public static float getTotalCache(Context context, File sdCacheFile) {
		return getSdCacheSize(sdCacheFile) + getDataCacheSize(context);
	}

	public static float getSdCacheSize(File cacheFile) {
		isFirst = true;
		return getFileSize(cacheFile);
	}

	public static float getDataCacheSize(Context context) {
		isFirst = true;
		return getFileSize(context.getCacheDir());
	}

	public static void deleteFile(File file) {
		if (!file.exists()) {
			return;
		}
		if (!file.isFile()) {
			for (File f : file.listFiles()) {
				deleteFile(f);
			}
		} else {
			file.delete();
		}
	}

	public static float getFileSize(File file) {
		if (isFirst) {
			result = 0.0f;
		}
		if (!file.exists()) {
			return 0.0f;
		}
		if (!file.isFile()) {
			for (File f : file.listFiles()) {
				getFileSize(f);
				isFirst = false;
			}
		} else {
			result += (float) file.length() / 1024 / 1024;
		}
		return Math.round(result*100)/100;
	}
}
