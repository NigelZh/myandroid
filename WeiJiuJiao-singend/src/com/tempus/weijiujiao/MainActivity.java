package com.tempus.weijiujiao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.TitleLayout.CustomTitleLayout;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.bean.User;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.fragment.DeviceListFrament;
import com.tempus.weijiujiao.fragment.TaceBackFragment;
import com.tempus.weijiujiao.view.CustomDialog;
import com.viewpagerindicator.TabPageIndicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 程序主页面，入口页面
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class MainActivity extends FragmentActivity {
	public static MainActivity mainActivityInstance = null;
	private TabPageIndicator indicator;
	private ViewPager vp;
	private ImageView welcome_iv;
	private List<Fragment> fragment_list = new ArrayList<Fragment>();
	private String[] TittleArray = { "防伪追溯", "酒柜", "酒窖" };
	private MyApplication application;
	CustomTitleLayout titleLayout;
//	ProgressDialog pDialog;
	Handler uihandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// login done;
				Debug.Out("get 00000");
				hideWelcomeImageview();
				initData();
				initView();
				break;
			case 1:
				Debug.Out("get 11111111");
				initAndshowWelcomeImageView();
				break;
//			case 999:
//				if(pDialog==null){
//					pDialog=CustomDialog.createProgressDialog(MainActivity.this);
//				}
//				pDialog.show();
//				break;
//			case 998:
//				CustomDialog.hideProgressDialog(pDialog);
//				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mainActivityInstance = MainActivity.this;
		application = (MyApplication) getApplication();

		autoLogin();
	}

	private void autoLogin() {
		// TODO Auto-generated method stub
		uihandler.sendEmptyMessage(1);
		if (application.getSp_setting().getBoolean("autoLogin", false)) {
			Debug.Out("aaaaaaaaaaaaa");
			final SharedPreferences sp_user = application.getSp_user();
			String id = sp_user.getString("id", null);
			String pw = sp_user.getString("pw", null);
			if (id != null && pw != null) {
				Service.getInstance(getApplicationContext()).requestLogin(id,
						pw, new onResultListener() {
							@Override
							public void onResult(Result result) {
								// TODO Auto-generated method stub
								try {
									ParserResult presult = JsonParser.parserLogin(result.getJsonBody());
									application.setUser((User) presult.getObj());
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							@Override
							public void onNetError(Result result) {
								// TODO Auto-generated method stub
								sp_user.edit().clear().commit();
							}
							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								
							}
							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								Debug.Out("bbbbbbbbbbbbbb");
								uihandler.sendEmptyMessageDelayed(0, 2000);
							}
						});
			}else{
				uihandler.sendEmptyMessageDelayed(0, 3000);
			}
		}else{
			Debug.Out("ccccccccccc");
			uihandler.sendEmptyMessageDelayed(0, 3000);
		}
	}
	private void initData() {
		// TODO Auto-generated method stub
		Fragment f1 = new TaceBackFragment();
		Fragment f2 = new DeviceListFrament();
		Bundle b = new Bundle();
		b.putInt("tag", Constant.Tag.TAG_GARVIDEN_LIST);
		f2.setArguments(b);
		Fragment f3 = new DeviceListFrament();
		Bundle b1 = new Bundle();
		b1.putInt("tag", Constant.Tag.TAG_WINLLER_LIST);
		f3.setArguments(b1);
		fragment_list.add(f1);
		fragment_list.add(f2);
		fragment_list.add(f3);
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		titleLayout = (CustomTitleLayout) findViewById(R.id.mainpage_title_layout);
		//titleLayout.notifyUserSateChanged();
		indicator = (TabPageIndicator) findViewById(R.id.tab_indicator);
		vp = (ViewPager) findViewById(R.id.pager);
		vp.setOffscreenPageLimit(3);
		vp.setAdapter(new mViewpagerAdapter(getSupportFragmentManager()));
		indicator.setViewPager(vp);
	}
	class mViewpagerAdapter extends FragmentStatePagerAdapter {

		public mViewpagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TittleArray[position] == null ? null : TittleArray[position];
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragment_list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragment_list.size();
		}

	}

	private void initAndshowWelcomeImageView() {
		// TODO Auto-generated method stub
		welcome_iv = new ImageView(this);
	    welcome_iv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
	    welcome_iv.setScaleType(ScaleType.FIT_XY);
	    welcome_iv.setAdjustViewBounds(true);
	    welcome_iv.setBackgroundResource(R.drawable.welcome_page_bg);
	    setContentView(welcome_iv);
	}

	public void hideWelcomeImageview(){
		if(welcome_iv!=null&&welcome_iv.getVisibility()==View.VISIBLE){
			welcome_iv.setVisibility(View.GONE);
		}
	}
	public static MainActivity getInstance() {
		if (mainActivityInstance != null) {
			return mainActivityInstance;
		} else {
			return null;
		}
	}

	/**
	 * 启动用户信息与登录页面
	 */
	public void startUserInforAndLogin() {
		Intent i = new Intent(MainActivity.this, DetailActivity.class);
		if (application.getUser().isLogin()) {
			i.putExtra("tag", Constant.Tag.TAG_USER_INFOR);
		} else {
			i.putExtra("tag", Constant.Tag.TAG_USER_LOGIN);
		}
		MainActivity.this.startActivity(i);
	}
	


	/**
	 * 启动详情页面
	 * 
	 * @param tag
	 *            详情页面标识
	 */
	public void startDetailActivity(int tag) {
		Intent i = new Intent(MainActivity.this, DetailActivity.class);
		i.putExtra("tag", tag);
		MainActivity.this.startActivity(i);
	}
	/**
	 * 
	 * @param tag
	 */
	public void startDetailActivityForProduct(String productID) {
		Intent i = new Intent(MainActivity.this, DetailActivity.class);
		i.putExtra("tag", Constant.Tag.TAG_PRODUCT_INFO);
		i.putExtra("code", productID);
		i.putExtra("codeType", Constant.Tag.TAG_CODE_TYPE_PRODUCTID);
		MainActivity.this.startActivity(i);
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { 
			showDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDialog() {
		// TODO Auto-generated method stub
		CustomDialog.createDialog(MainActivity.this, "提示", "确定退出么？", true, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if(arg1==DialogInterface.BUTTON_POSITIVE){
					MainActivity.this.finish();
					System.exit(0);
					System.gc();
				}
			}
		}).show();
	}

	@Override
	public void onResume() {
		super.onResume();

	}
}
