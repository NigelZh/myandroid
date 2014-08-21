package com.tempus.weijiujiao;

import java.io.File;
import java.util.HashMap;

import org.json.JSONException;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.ParamsUtils;
import com.tempus.weijiujiao.bean.User;
import com.tempus.weijiujiao.db.DBManager;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap.Config;
import android.os.Environment;

/**
 * 自主管理的application，全局操作
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class MyApplication extends Application {

	private HashMap<String, Activity> activityMap;
	private ParamsUtils pmus;
	private SharedPreferences sp_user, sp_setting;
	private User user;
	private static MyApplication application;
	private File cacheFile;
	private ImageLoader imageLoader;
	private DBManager dbmanager;

	@Override
	public void onCreate() {
		super.onCreate();
		if (activityMap == null) {
			activityMap = new HashMap<String, Activity>();
		}
		if (pmus == null) {
			pmus = ParamsUtils.getInstance(this.getApplicationContext());
		}
		application = this;
		if (getSDPath() != null) {
			cacheFile = new File(getSDPath() + "/iamgeCache");
		}
		dbmanager = DBManager.getInstance(getApplicationContext());
		initImageLoader();
	}

	public DBManager getDbmanager() {
		return this.dbmanager;
	}

	private void loginFortest() {
		// TODO Auto-generated method stub
		// 13345674444 888888
		Service.getInstance(getApplicationContext()).requestLogin(
				"13345674444", "888888", new onResultListener() {
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						try {
							Debug.Out("Login done=" + result.getJsonBody());
							ParserResult presult = JsonParser
									.parserLogin(result.getJsonBody());
							setUser((User) presult.getObj());

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub
						Debug.Out("Login error=" + result.getErrorInfo());
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						
					}
				});

	}

	public File getSdcacheFile() {
		return cacheFile;
	}

	private void initImageLoader() {
		// TODO Auto-generated method stub
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(cacheFile == null ? false : true)
				.bitmapConfig(Config.RGB_565).build();
		Builder loadBuilder = new ImageLoaderConfiguration.Builder(
				getApplicationContext());
		loadBuilder.memoryCacheExtraOptions(480, 800)
				.diskCacheExtraOptions(480, 800, null)
				.memoryCache(new LruMemoryCache(10 * 1024 * 1024))
				.defaultDisplayImageOptions(options);
		if (cacheFile != null) {
			loadBuilder.diskCache(new UnlimitedDiscCache(cacheFile)).diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100);
		}
		ImageLoader.getInstance().init(loadBuilder.build());
	}

	public ImageLoader getImageLoader() {
		if(imageLoader==null){
			imageLoader=ImageLoader.getInstance();
		}
		return imageLoader;
	}

	public ParamsUtils getParamsUtils() {
		return this.pmus;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void ApplicationExit() {
		System.exit(0);
		System.gc();
	}

	public SharedPreferences getSp_user() {
		if (sp_user == null) {
			sp_user = this.getSharedPreferences("local_user", 0);
		}
		return sp_user;
	}

	public SharedPreferences getSp_setting() {
		if (sp_setting == null) {
			sp_setting = this.getSharedPreferences("local_setting", 0);
		}
		return sp_setting;
	}

	public void clearSettingSp() {

		this.getSp_setting();

		sp_setting.edit().clear().commit();
	}

	public void clearUserSp() {
		this.getSp_user();
		sp_user.edit().clear().commit();
	}

	public void clearAllSp() {
		this.clearUserSp();
		this.clearSettingSp();
	}

	public User getUser() {
		if (user == null) {
			user = User.getUser();
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static MyApplication getinstance() {
		return application;
	}

	private String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.toString();
		}
		return null;
	}
}
