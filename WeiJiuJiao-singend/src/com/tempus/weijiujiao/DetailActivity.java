package com.tempus.weijiujiao;

import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.bean.Device;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.fragment.FeedbackFragment;
import com.tempus.weijiujiao.fragment.GradevinManagerFragment;
import com.tempus.weijiujiao.fragment.ProductInfoFragment;
import com.tempus.weijiujiao.fragment.SaleReportFragment;
import com.tempus.weijiujiao.fragment.ShareAppFragment;
import com.tempus.weijiujiao.fragment.UserInforFragment;
import com.tempus.weijiujiao.fragment.UserLoginFragment;
import com.tempus.weijiujiao.fragment.UserSetupFragment;
import com.tempus.weijiujiao.fragment.WineCellarManagerFragment;
import com.tempus.weijiujiao.fragment.addDeviceFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailActivity extends FragmentActivity {
	private int currentTag = -1;
	private Fragment contentFragment;
	private ImageButton img_btn_back;
	private TextView title_tv;
	private boolean isAddingGraviden = false, isAddingWineCaller = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail);
		intBaseView();
		if (switchIntentTag()) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.detial_contenter, contentFragment).commit();
		} 
	}

	/**
	 * 这个方法应该优化下，不同的子页面点击它应该返回到对应的上级页面，类似抽象方法
	 */
	private void intBaseView() {
		// TODO Auto-generated method stub
		img_btn_back = (ImageButton) findViewById(R.id.detail_img_btn_back);
		img_btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onKeyDown();
			}
		});
		title_tv = (TextView) findViewById(R.id.detail_title_tv);
	}

	public void setTitle(String title) {
		title_tv.setText(title);
	}

	public String getActivityTitle() {
		String a = title_tv.getText().toString().trim();
		return a;
	}

	private boolean switchIntentTag() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		boolean status = false;
		if (i != null) {
			currentTag = i.getIntExtra("tag", -1);
			if (currentTag != -1) {
				switch (currentTag) {
				case Constant.Tag.TAG_PRODUCT_INFO:
					title_tv.setText("商品详情");// DONE
					String code = i.getStringExtra("code");
					int codeType = i.getIntExtra("codeType", -1);
					if (code == null || codeType == -1) {
						status = false;
					} else {
						contentFragment = new ProductInfoFragment();
						Bundle b = new Bundle();
						b.putString("code", code);
						b.putInt("codeType", codeType);
						b.putBoolean("history", i.getBooleanExtra("history", false));
						contentFragment.setArguments(b);
						status = true;
					}
					break;
				case Constant.Tag.TAG_GRADEVIN_MANAGER:
					title_tv.setText("酒柜管理");
					Device d = (Device) i.getSerializableExtra("device");
					if (d == null) {
						status = false;
					} else {
						contentFragment = new GradevinManagerFragment();
						Bundle b1 = new Bundle();
						b1.putSerializable("device", d);
						contentFragment.setArguments(b1);
						status = true;
					}

					break;
				case Constant.Tag.TAG_WINECELLAR_MANAGER:
					title_tv.setText("酒窖管理");
					Device d1 = (Device) i.getSerializableExtra("device");
					if (d1 == null) {
						status = false;
					} else {
						contentFragment = new WineCellarManagerFragment();
						Bundle b2 = new Bundle();
						b2.putSerializable("device", d1);
						contentFragment.setArguments(b2);
						status = true;
					}
					break;
				case Constant.Tag.TAG_USER_FEEDBACK:
					title_tv.setText("意见反馈");// DONE
					contentFragment = new FeedbackFragment();
					status = true;
					break;
				case Constant.Tag.TAG_USER_SETUP:
					title_tv.setText("设置");// DONE
					contentFragment = new UserSetupFragment();
					status = true;
					break;
				case Constant.Tag.TAG_USER_SHAREAPP:
					title_tv.setText("分享APP");// DONE
					contentFragment = new ShareAppFragment();
					status = true;
					break;
				case Constant.Tag.TAG_USER_ADDGRADEVIN:
					title_tv.setText("添加酒柜");// done
					Bundle b2 = new Bundle();
					b2.putInt("deviceType", 2);
					contentFragment = new addDeviceFragment();
					contentFragment.setArguments(b2);
					isAddingGraviden = true;
					status = true;
					break;
				case Constant.Tag.TAG_USER_ADDWINECELLAR:
					title_tv.setText("添加酒窖");// done
					Bundle b1 = new Bundle();
					b1.putInt("deviceType", 3);
					contentFragment = new addDeviceFragment();
					contentFragment.setArguments(b1);
					isAddingWineCaller = true;
					status = true;
					break;
				case Constant.Tag.TAG_USER_INFOR:
					title_tv.setText("用户信息");
					contentFragment = new UserInforFragment();
					status = true;
					break;
				case Constant.Tag.TAG_USER_LOGIN:
					title_tv.setText("用户登录");
					contentFragment = new UserLoginFragment();
					status = true;
					break;
				case Constant.Tag.TAG_SALE_REPORT:
					title_tv.setText("消费报表");
					contentFragment = new SaleReportFragment();
					status = true;
					break;

				default:
					status = false;
					break;
				}
			}
		}
		return status;
	}

	public boolean isAddgraviden() {
		return this.isAddingGraviden;
	}

	public void setIsAddGraviden(boolean isadding) {
		this.isAddingGraviden = isadding;
	}

	public boolean isAddwineCaller() {
		return this.isAddingWineCaller;
	}

	public void setIsAddWinecaller(boolean isadding) {
		this.isAddingWineCaller = isadding;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Debug.Out("detail onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 按下的如果是BACK，同时没有重复
			return onKeyDown();
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean onKeyDown() {
		boolean canFinish = false;
		switch (currentTag) {
		case Constant.Tag.TAG_GRADEVIN_MANAGER:
			canFinish = ((GradevinManagerFragment) contentFragment).onKeyBack();
			break;
		case Constant.Tag.TAG_WINECELLAR_MANAGER:
			canFinish = ((WineCellarManagerFragment) contentFragment)
					.onKeyBack();
			break;
		case Constant.Tag.TAG_USER_FEEDBACK:
			canFinish = ((FeedbackFragment) contentFragment).onKeyBack();
			break;
		case Constant.Tag.TAG_USER_SETUP:
			canFinish = ((UserSetupFragment) contentFragment).onKeyBack();
			break;
		case Constant.Tag.TAG_USER_SHAREAPP:
			canFinish = ((ShareAppFragment) contentFragment).onKeyBack();
			break;
		case Constant.Tag.TAG_USER_ADDGRADEVIN:
			canFinish = ((addDeviceFragment) contentFragment).onKeyBack();
			break;
		case Constant.Tag.TAG_USER_ADDWINECELLAR:
			canFinish = ((addDeviceFragment) contentFragment).onKeyBack();
			break;
		case Constant.Tag.TAG_USER_LOGIN:
			canFinish = ((UserLoginFragment) contentFragment).onKeyBack();
			break;
		case Constant.Tag.TAG_USER_INFOR:
			canFinish = ((UserInforFragment) contentFragment).onKeyBack();
			break;
		case Constant.Tag.TAG_SALE_REPORT:
			canFinish = ((SaleReportFragment) contentFragment).onKeyBack();
			break;
		default:
			break;
		}
		if (!canFinish) {
			this.finish();
		}
		return canFinish;
	}
}
