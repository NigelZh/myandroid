package com.tempus.weijiujiao.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.tempus.weijiujiao.DetailActivity;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onProgressListner;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.DataCleanManager;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.ParamsUtils;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.adapter.SetupListViewAdapter;
import com.tempus.weijiujiao.bean.Version;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 意见反馈
 * 
 * @author lenovo
 * 
 */
public class UserSetupFragment extends Fragment {
	private int currnetIndex = 0;
	private LayoutInflater inflater;
	private ListView lv_setup;
	private List<String> setupInforList;
	private String frontTitle, frontAndUpTitle = null;// 返回时记录之前的标题
	private ViewFlipper viewFlipper = null;
	private DetailActivity aActivity;
	private CheckBox cb_set;
	private TextView tv_cacheSize;
	TextView rl_about_founctions;
	RelativeLayout rl_upgrade;
	private ProgressDialog progressDialog,pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		aActivity = (DetailActivity) getActivity();
		// 获取一级标题
		frontAndUpTitle = aActivity.getActivityTitle();
		viewFlipper = (ViewFlipper) inflater.inflate(R.layout.setup_layout,null);
		initData();
		lv_setup = (ListView) viewFlipper.findViewById(R.id.lv_setup);
		lv_setup.setAdapter(new SetupListViewAdapter(inflater, setupInforList));
		lv_setup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int positon, long arg3) {
				// TODO Auto-generated method stub
				if (positon == 0) {
					aActivity.setTitle("系统设置");
					frontTitle=aActivity.getActivityTitle();
					initSystemSutup();
				} else if (positon == 1) {
					aActivity.setTitle("关于我们");
					frontTitle=aActivity.getActivityTitle();
					initAboutUS();
				} else {

				}
			}
		});

		return viewFlipper;
	}

	/**
	 * 初始化关于我们页面
	 */
	protected void initAboutUS() {
		// TODO Auto-generated method stub
		LinearLayout lyAboutUS = (LinearLayout) inflater.inflate(
				R.layout.setup_about_us_layout, null);
		rl_about_founctions = (TextView) lyAboutUS
				.findViewById(R.id.tv_about_fouctions);
		rl_about_founctions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(), "页面暂缺，敬请期待！",
				// Toast.LENGTH_SHORT).show();
				toFunctionsGuidePage();
			}
		});
		rl_upgrade = (RelativeLayout) lyAboutUS.findViewById(R.id.rl_upgrade);
		rl_upgrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				requestForUpgrade();
			}
		});
		changeVp(1, lyAboutUS);
	}

	protected void toFunctionsGuidePage() {
		// TODO Auto-generated method stub
		aActivity.setTitle("功能介绍");
		View guideview = inflater.inflate(R.layout.functions_guide_layou, null);
		changeVp(2, guideview);
	}

	protected void requestForUpgrade() {
		// TODO Auto-generated method stub
		Service.getInstance(getActivity()).requestUpgrade(
				ParamsUtils.getInstance(getActivity()).getLocalVersionCode(),
				new onResultListener() {
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						try {
							if (result.getStatucCode() == 0) {
								ParserResult presult = JsonParser
										.parserAppUpgrade(result.getJsonBody());
								if (presult.getCode() == 0) {
									Message msg = new Message();
									msg.what = 0;
									msg.obj = presult.getObj();
									handler.sendMessage(msg);
								} else if (presult.getCode() == 1) {
									handler.sendEmptyMessage(8);
								} else if (presult.getCode() == 2) {
									handler.sendEmptyMessage(9);
								} else {
									handler.sendEmptyMessage(10);
								}
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							handler.sendEmptyMessage(10);
						}
					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = 2;
						msg.obj = result.getErrorInfo();
						handler.sendMessage(msg);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(999);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(998);
					}
				});
	}

	/**
	 * 
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				doUpgrade((Version) msg.obj);
				break;
			case 2:
				ToastUtils.toastMessage("网络异常！！");
				break;
			case 3:
				ToastUtils.toastMessage("您取消了更新");
				break;
			case 4:
				// startDownload
				showProgressDialog();
				break;
			case 5:
				// progress changed
				updateProgressDialog(Float.parseFloat(msg.obj.toString()));
				break;
			case 6:
				// done download
				progressDialog.dismiss();
				installAPP((File) msg.obj);
				break;
			case 7:
				// down error
				downLoadError(msg.obj.toString());
				break;
			case 8:
				ToastUtils.toastMessage("已经是最新版本了！");

				break;
			case 9:

				ToastUtils.toastMessage("请求失败，服务器异常！");
				break;
			case 10:
				ToastUtils.toastMessage("服务器返回数据异常，无法识别！");
				break;
			case 999:
				if(pDialog==null){
					pDialog=	CustomDialog.createProgressDialog(getActivity());
				}
				pDialog.show();
				break;
			case 998:
				CustomDialog.hideProgressDialog(pDialog);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化关于系统设置
	 */
	protected void initSystemSutup() {
		// TODO Auto-generated method stub
		LinearLayout lySystemSutup = (LinearLayout) inflater.inflate(
				R.layout.setup_systemsetup_layout, null);
		cb_set = (CheckBox) lySystemSutup.findViewById(R.id.setUp_check);
		cb_set.setChecked(MyApplication.getinstance().getSp_setting()
				.getBoolean("downloadAll", true));
		tv_cacheSize = (TextView) lySystemSutup
				.findViewById(R.id.setUp_cahceSize_tv);
		tv_cacheSize.setText(DataCleanManager.getTotalCache(getActivity(),
				MyApplication.getinstance().getSdcacheFile()) + " MB");
		RelativeLayout rlClearCache = (RelativeLayout) lySystemSutup
				.findViewById(R.id.setUp_clearCache);
		cb_set.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				editSp(isChecked);
			}
		});
		rlClearCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog();

			}
		});
		changeVp(1, lySystemSutup);
	}

	protected void showDialog() {
		// TODO Auto-generated method stub
		CustomDialog.createDialog(getActivity(), "提示",
				"是否要删除所有缓存数据？其中包含了图片缓存，历史记录以及您的个人设置信息！该操作无法复原！！", true,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

						// TODO Auto-generated method stub
						if (arg1 == Dialog.BUTTON_POSITIVE) {
							DataCleanManager.clearTotalCache(getActivity(),
									MyApplication.getinstance()
											.getSdcacheFile());
							tv_cacheSize.setText("0 MB");
						} else {
							arg0.dismiss();
						}

					}
				}).show();
	}

	protected void downLoadError(String string) {
		// TODO Auto-generated method stub
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		ToastUtils.toastMessage("下载失败!");
	}

	protected void installAPP(File appFile) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(appFile),"application/vnd.android.package-archive");
		startActivity(intent);
	}

	protected void updateProgressDialog(float progress) {
		// TODO Auto-generated method stub
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.setMessage(progress * 100 + "%");
		}
	}

	protected void showProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setCancelable(false);
			progressDialog.setTitle("正在下载");
			progressDialog.setMessage("0%");
		}
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	protected void doUpgrade(final Version newVersion) {
		// TODO Auto-generated method stub
		rl_upgrade.findViewById(R.id.iv_upgrade_new)
				.setVisibility(View.VISIBLE);
		((TextView) rl_upgrade.findViewById(R.id.tv_upgrade_versionName))
				.setText(newVersion.getVersionName());
		CustomDialog.createDialog(getActivity(), "提示", "发现新版本，是否马上更新？", true,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						if (arg1 == DialogInterface.BUTTON_POSITIVE) {
							startMyupgrade(newVersion);
						} else {
							handler.sendEmptyMessage(3);
						}
					}
				}).show();
	}

	protected void startMyupgrade(Version newVersion) {
		// TODO
		handler.sendEmptyMessage(4);
		Service.getInstance(getActivity()).requestDownLoadFile(
				MyApplication.getinstance().getSdcacheFile(),
				newVersion.getAppUrl(), new onProgressListner() {

					@Override
					public void onProgress(float progress) {
						// TODO Auto-generated method stub
						Debug.Out("onProgress----->" + progress);
						Message msg = new Message();
						msg.what = 5;
						msg.obj = progress;
						handler.sendMessage(msg);
					}

					@Override
					public void onFailed(String errorInfo) {
						// TODO Auto-generated method stub
						Debug.Out("onFailed----->" + errorInfo);
						Message msg = new Message();
						msg.what = 7;
						msg.obj = errorInfo;
						handler.sendMessage(msg);
					}

					@Override
					public void onDone(File newFile) {
						// TODO Auto-generated method stub
						Message msg = new Message();
						Debug.Out("onDone----->" + newFile.getPath().toString());
						msg.what = 6;
						msg.obj = newFile;
						handler.sendMessage(msg);
					}
				});
	}

	protected void editSp(boolean isChecked) {
		// TODO Auto-generated method stub
		SharedPreferences sp_setting = MyApplication.getinstance()
				.getSp_setting();
		sp_setting.edit().putBoolean("downloadAll", isChecked).commit();
	}

	private void initData() {
		// TODO Auto-generated method stub
		setupInforList = new ArrayList<String>();
		if (setupInforList == null || setupInforList.size() <= 0) {
			for (int i = 0; i < 2; i++) {
				setupInforList.add("" + i);
			}
		}

	}

	/**
	 * vp切换
	 * 
	 * @param index
	 *            要添加view的index
	 * @param scendview
	 *            要添加的view
	 */
	private void changeVp(int index, View scendview) {
		// TODO Auto-generated method stub
		int count = viewFlipper.getChildCount();
		while (count > index) {
			// 把之前的view删除掉
			viewFlipper.removeViewAt(count - 1);
			count = viewFlipper.getChildCount();
		}
		viewFlipper.addView(scendview);
		showViewFillperNext();
	}

	/**
	 * 判断是否还有下一页
	 * */
	private boolean showViewFillperNext() {
		// TODO Auto-generated method stub
		if (currnetIndex < viewFlipper.getChildCount() - 1) {
			viewFlipper.clearAnimation();
//			viewFlipper.setInAnimation(aActivity, R.anim.in_right);
//			viewFlipper.setOutAnimation(aActivity, R.anim.out_left);
			viewFlipper.showNext();
			currnetIndex++;
			return true;
		}
		return false;
	}

	/**
	 * 判断是否还有上一页
	 * */
	private boolean showViewFillperPerious() {
		// TODO Auto-generated method stub
		if (currnetIndex > 0) {
			viewFlipper.clearAnimation();
//			viewFlipper.setInAnimation(aActivity, R.anim.in_left);
//			viewFlipper.setOutAnimation(aActivity, R.anim.out_right);
			viewFlipper.showPrevious();
			currnetIndex--;
			return true;
		}
		return false;
	}

	public boolean onKeyBack() {
		if (showViewFillperPerious()) {
			if (currnetIndex > 0) {
				aActivity.setTitle(frontTitle);
			} else {
				aActivity.setTitle(frontAndUpTitle);
			}
			return true;
		} else {
			return false;
		}
	}

}
