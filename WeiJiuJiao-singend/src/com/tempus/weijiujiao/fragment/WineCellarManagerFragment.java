package com.tempus.weijiujiao.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.qq.wx.voice.recognizer.VoiceRecognizerDialog;
import com.qq.wx.voice.recognizer.VoiceRecognizerListener;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult;
import com.qq.wx.voice.recognizer.VoiceRecordState;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult.Word;
import com.tempus.weijiujiao.DetailActivity;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Socket.SocketService;
import com.tempus.weijiujiao.Socket.SocketService.MyBinder;
import com.tempus.weijiujiao.Utils.BitmapUtils;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.adapter.GradevinInforListViewAdapter;
import com.tempus.weijiujiao.adapter.GradevinLocationShowAdapter;
import com.tempus.weijiujiao.adapter.WinecallerRegionalAdapter;
import com.tempus.weijiujiao.adapter.WinecellarManagerGridviewAdapter;
import com.tempus.weijiujiao.bean.Device;
import com.tempus.weijiujiao.bean.Light;
import com.tempus.weijiujiao.bean.ProductInfo;
import com.tempus.weijiujiao.bean.Regional;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.view.ChartView;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

public class WineCellarManagerFragment extends Fragment {
	private ViewFlipper viewFlipper = null;
	private LayoutInflater inflater;
	private GridView gv;
	private DetailActivity dActivity;
	private int currnetIndex = 0;
	private TextView tv_deviceid, tv_temp, tv_intemp, tv_setemp, tv_humidity,
			tv_inhumidity, tv_sethumidity;
	private List<CheckBox> listCheckbox;
	private String frontTitle, frontAndUpTitle;// 前一级标题，和上上级标题
	private Device device;
	private SocketService socketService;
	private ServiceConnection conn;
	BroadcastReceiver receiver;
	WinecellarManagerGridviewAdapter gridViewAdapter;
	boolean isLightControlPage = false;
	ProgressDialog pDialog;
	Handler uihandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 区域列表获取成功
				regionalAdapter.notifyDataSetChanged();
				break;
			case 1:// 没有查到区域
				ToastUtils.toastMessage("没有找到区域");
				break;
			case 2:// JSON解析失败
				ToastUtils.toastMessage("数据解析失败");
				break;
			case 3:// 网络异常
				ToastUtils.toastMessage("网络异常");
				break;
			case 4:// status!=0
				ToastUtils.toastMessage("获取酒窖区域列表失败");
				break;
			case 5:// 获取酒位列表成功
				locationAdapter.notifyDataSetChanged();
				break;
			case 6:// 获取酒位列表失败
				ToastUtils.toastMessage("获取酒位信息失败");
			case 7:// 获取灯源列表成功
				updateLightContent();
			case 8:// 获取灯源列表失败了
				ToastUtils.toastMessage("获取灯源列表失败了");
				break;
			case 9:// 头像更新成功了
				ToastUtils.toastMessage("头像更新成功了");
				updateDeviceImage(msg.obj.toString());
				break;
			case 10:// 头像更新失败了
				ToastUtils.toastMessage("头像更新失败了");
				break;
			case 11:// 信息更新成功
				ToastUtils.toastMessage("设备信息更新成功");
				update(msg.obj.toString(), msg.arg1);
				break;
			case 12:// 信息更新识别
				ToastUtils.toastMessage("更新失败了");
				break;
			case 999:
				if(pDialog==null){
					pDialog=CustomDialog.createProgressDialog(getActivity());
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		device = (Device) b.getSerializable("device");
	}

	protected void update(String newValue, int updatePosition) {
		// TODO Auto-generated method stub
		switch (updatePosition) {
		case 1:
			device.setName(newValue);
			break;
		case 2:
			MyApplication.getinstance().getUser().setUserNumber(newValue);
			break;
		case 3:
			MyApplication.getinstance().getUser().setUserEmail(newValue);
			break;
		case 5:
			device.setAddress(newValue);
			break;
		case 6:
			device.setRemark(newValue);
			break;
		default:
			break;
		}
		wineCallerAdapter.notifyDataSetChanged();
		sendBroadCast();
		onKeyBack();
	}

	private void sendBroadCast() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Constant.Action.ACTION_ADDDEVICE_DONE);
		intent.putExtra("deviceType", 3);
		MyApplication.getinstance().getApplicationContext()
				.sendBroadcast(intent);
	}

	protected void updateDeviceImage(String imageurl) {
		// TODO Auto-generated method stub
		if (deviceImageView != null) {
			MyApplication
					.getinstance()
					.getImageLoader()
					.loadImage(device.getImgURL(),
							new SimpleImageLoadingListener() {
								@SuppressWarnings("deprecation")
								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									deviceImageView
											.setBackgroundDrawable(new BitmapDrawable(
													arg2));
								}
							});

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		bindMyService();
	}

	@Override
	public void onPause() {
		super.onPause();
		unBindMyservice();
	}

	public void onDestroy() {
		super.onDestroy();
		unBindMyservice();
	}

	private void registerMyReciver() {
		// TODO Auto-generated method stub
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent intent) {
				// TODO Auto-generated method stub
				switch (intent.getAction()) {
				case Constant.Action.SOCKET_BROADCAST_ACTION:
					// Toast.makeText(getActivity(),
					// intent.getStringExtra("data"), Toast.LENGTH_SHORT)
					// .show();
					break;
				case Constant.Action.ACTION_CONNECT:
					int state = intent.getIntExtra("data", -1);
					ToastUtils.toastMessage(state == 1 ? "连接成功" : "连接失败");
					if (state == 1) {
						socketService.getDeviceInfo(System.currentTimeMillis()
								+ "", device.getId());
					}
					break;
				case Constant.Action.ACTION_TEMPER:
					int temp = intent.getIntExtra("data", -1);
					if (temp != -1) {
						updateTemp(temp);
					}
					break;
				case Constant.Action.ACTION_INTEMPER:
					int intemp = intent.getIntExtra("data", -1);
					if (intemp != -1) {
						updateInTemp(intemp);
					}
					break;
				case Constant.Action.ACTION_HUMIDITY:
					int humidity = intent.getIntExtra("data", -1);
					if (humidity != -1) {
						updateHumidity(humidity);
					}
				case Constant.Action.ACTION_INHUMIDITY:
					int inhumidity = intent.getIntExtra("data", -1);
					if (inhumidity != -1) {
						updateInHumidity(inhumidity);
					}
					break;
				case Constant.Action.ACTION_LAMP:
					String lampid = intent.getStringExtra("lampId");
					int lampstate = intent.getIntExtra("lampState", -1);
					if (lampstate != -1) {
						updateLampState(lampid, lampstate == 1 ? true : false);
					}
					break;
				case Constant.Action.ACTION_LOCK:
					int loctState = intent.getIntExtra("data", -1);
					if (loctState != -1) {
						updateLockState(loctState == 1 ? false : true);
					}
					break;
				case Constant.Action.ACTION_PASSWORD:
					int setState = intent.getIntExtra("data", -1);
					if (setState != -1) {
						ToastUtils.toastMessage("修改密码"
								+ (setState == 1 ? "成功" : "失败"));
					}
					break;
				case Constant.Action.ACTION_STATE:
					int deviceState = intent.getIntExtra("data", -1);
					if (deviceState != -1) {

						ToastUtils.toastMessage("设备已"
								+ (deviceState == 1 ? "连接" : "断开"));
					}
					break;
				default:
					break;
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.Action.SOCKET_BROADCAST_ACTION);
		intentFilter.addAction(Constant.Action.ACTION_CONNECT);
		intentFilter.addAction(Constant.Action.ACTION_INFO);
		intentFilter.addAction(Constant.Action.ACTION_TEMPER);
		intentFilter.addAction(Constant.Action.ACTION_INTEMPER);
		intentFilter.addAction(Constant.Action.ACTION_HUMIDITY);
		intentFilter.addAction(Constant.Action.ACTION_INHUMIDITY);
		intentFilter.addAction(Constant.Action.ACTION_LAMP);
		intentFilter.addAction(Constant.Action.ACTION_LOCK);
		intentFilter.addAction(Constant.Action.ACTION_PASSWORD);
		intentFilter.addAction(Constant.Action.ACTION_STATE);
		intentFilter.addAction(Constant.Action.ACTION_SERVER_ERROR);
		intentFilter.addAction(Constant.Action.ACTION_DISCONNECT);
		getActivity().registerReceiver(receiver, intentFilter);
	}

	private void unBindMyservice() {
		try {
			getActivity().unbindService(conn);
			conn = null;
			socketService = null;
			getActivity().unregisterReceiver(receiver);
			receiver = null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	protected void updateLampState(String lightid, boolean isOn) {
		// TODO Auto-generated method stub
		int position = findLampPositon(lightid);
		if (position != -1) {
			checkList.get(position).setChecked(isOn);
			lightList.get(position).setON(isOn);
		}
	}

	private int findLampPositon(String lightid) {
		int position = -1;
		if (lightList != null && lightList.size() > 0) {
			for (int i = 0; i < lightList.size(); i++) {
				if (lightList.get(i).getId().equals(lightid)) {
					position = i;
				}
			}
		}
		return position;
	}

	protected void updateLockState(boolean isLocked) {
		// TODO Auto-generated method stub
		if (gridViewAdapter != null) {
			gridViewAdapter.updateLockState(isLocked);
		}
	}

	protected void updateInHumidity(int inHumidity) {
		// TODO Auto-generated method stub
		if (tv_inhumidity != null) {
			tv_inhumidity.setText(inHumidity + "%");
		}
	}

	protected void updateHumidity(int humidity) {
		// TODO Auto-generated method stub
		if (tv_humidity != null) {
			tv_humidity.setText(humidity + "%");
		}
	}

	protected void updateInTemp(int inTemper) {
		// TODO Auto-generated method stub
		if (tv_intemp != null) {
			tv_intemp.setText(inTemper + "℃");
		}
	}

	protected void updateTemp(int temper) {
		// TODO Auto-generated method stub
		if (tv_temp != null) {
			tv_temp.setText(temper + "℃");
		}
	}

	private void bindMyService() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), SocketService.class);
		intent.putExtra("id", device.getId());
		intent.putExtra("deviceType", 3);
		if (conn == null) {
			conn = new ServiceConnection() {

				@Override
				public void onServiceDisconnected(ComponentName name) {
					// TODO Auto-generated method stub
					socketService = null;
					conn = null;
				}

				@Override
				public void onServiceConnected(ComponentName name,
						IBinder service) {
					// TODO Auto-generated method stub
					MyBinder binder = (MyBinder) service;
					socketService = binder.getService();
				}
			};

		}
		getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
		if (receiver == null) {
			registerMyReciver();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		viewFlipper = (ViewFlipper) inflater.inflate(
				R.layout.winecellar_manager_layout, null);
		dActivity = (DetailActivity) getActivity();
		frontAndUpTitle = dActivity.getActivityTitle();
		LinearLayout llGradevinManager = (LinearLayout) viewFlipper
				.findViewById(R.id.ll_winecellar_manager);
		findviews(llGradevinManager);
		return viewFlipper;
	}

	private void findviews(LinearLayout managerRootView) {
		// TODO Auto-generated method stub
		tv_deviceid = (TextView) managerRootView
				.findViewById(R.id.winecaller_manager_deviceid_tv);
		tv_deviceid.setText(device.getId());
		tv_temp = (TextView) managerRootView
				.findViewById(R.id.winecaller_manager_temp_tv);
		tv_intemp = (TextView) managerRootView
				.findViewById(R.id.winecaller_manager_intemp_tv);
		tv_setemp = (TextView) managerRootView
				.findViewById(R.id.winecaller_manager_settemp_tv);
		tv_humidity = (TextView) managerRootView
				.findViewById(R.id.winecaller_manager_humidity_tv);
		tv_inhumidity = (TextView) managerRootView
				.findViewById(R.id.winecaller_manager_inhumidity_tv);
		tv_sethumidity = (TextView) managerRootView
				.findViewById(R.id.winecaller_manager_sethumidity_tv);
		gv = (GridView) managerRootView
				.findViewById(R.id.gv_winecellar_manager);
		gridViewAdapter = new WinecellarManagerGridviewAdapter(inflater,
				getActivity().getApplicationContext());
		gv.setAdapter(gridViewAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				addScenndView(arg2);
			}
		});
	}

	private void addScenndView(int position) {
		switch (position) {
		case 0:
			initWinecellarRegionalShow();
			dActivity.setTitle("区域展示");
			frontTitle = dActivity.getActivityTitle();
			break;
		case 1:
			if (device.isOwn()) {
				initWinecellarLightControl();
				dActivity.setTitle("灯光控制");
				frontTitle = dActivity.getActivityTitle();
			} else {

				ToastUtils.toastMessage("您没有权限这么做！");
			}
			break;
		case 2:
			initWinecellarStorage();
			dActivity.setTitle("储存情况");
			frontTitle = dActivity.getActivityTitle();
			break;
		case 3:
			if (device.isOwn()) {
				requestForLockOrUnlock();
			} else {
				ToastUtils.toastMessage("您没有权限这么做！");
			}
			break;
		case 4:
			if (device.isOwn()) {
				initWinecellarSetUp();
				dActivity.setTitle("酒窖设置");
				frontTitle = dActivity.getActivityTitle();
			} else {
				ToastUtils.toastMessage("您没有权限这么做！");
			}
			break;
		case 5:
			initWinecellarInfor();
			dActivity.setTitle("酒窖信息");
			frontTitle = dActivity.getActivityTitle();
			break;
		default:
			break;
		}
		frontTitle = dActivity.getActivityTitle();
	}

	/**
	 * 酒窖信息
	 */
	GradevinInforListViewAdapter wineCallerAdapter;
	ImageView deviceImageView;

	private void initWinecellarInfor() {
		LinearLayout lyWinecellarInfor = (LinearLayout) inflater.inflate(
				R.layout.winecellar_infor_layout, null);
		deviceImageView = (ImageView) lyWinecellarInfor
				.findViewById(R.id.winecaller_manager_deviceid_image);
		if (!StringUtils.isNull(device.getImgURL())) {
			MyApplication
					.getinstance()
					.getImageLoader()
					.loadImage(device.getImgURL(),
							new SimpleImageLoadingListener() {

								@SuppressWarnings("deprecation")
								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									deviceImageView
											.setBackgroundDrawable(new BitmapDrawable(
													arg2));
								}
							});
		}
		// MyApplication.getinstance().getImageLoader()
		// .displayImage(device.getImgURL(), deviceImageView);
		LinearLayout lLayout = (LinearLayout) lyWinecellarInfor
				.findViewById(R.id.ll_winecellar_infor_changerpic);
		lLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (device.isOwn()) {
					UpdateDeviceImage();
				} else {
					ToastUtils.toastMessage("您没有权限这么做！");
				}
			}
		});
		ListView lvWinecellarInfor = (ListView) lyWinecellarInfor
				.findViewById(R.id.lv_winecellar_infor);
		wineCallerAdapter = new GradevinInforListViewAdapter(inflater, device,
				3);
		lvWinecellarInfor.setAdapter(wineCallerAdapter);
		lvWinecellarInfor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View itemView,
					int position, long arg3) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				if (device.isOwn()) {
					String lastStr = ((TextView) itemView
							.findViewById(R.id.tv_gradevin_infor_list_otheritem_num))
							.getText().toString();
					toDeviceInfoPage(lastStr, position);
				} else {
					ToastUtils.toastMessage("您没有权限这么做！");
				}

			}
		});
		changeVp(1, lyWinecellarInfor);
	}

	private void toDeviceInfoPage(String lastStr, int position) {
		// TODO Auto-generated method stub
		DetailActivity dActivity = (DetailActivity) getActivity();
		String[] InfotitleArray = { "昵称修改", "手机修改", "邮箱修改", "酒柜位置修改", "地址修改",
				"备注信息" };
		if (position == 1 || position == 5 || position == 6) {
			dActivity.setTitle(InfotitleArray[position - 1]);
			textChanged(lastStr, position);
		} else {
			return;
		}
	}

	private void textChanged(final String lastStr, final int position) {
		LinearLayout llChangerStr = (LinearLayout) inflater.inflate(
				R.layout.user_login_changer_infro_layout, null);
		final EditText etv = (EditText) llChangerStr
				.findViewById(R.id.ed_user_login_changer_infro);
		etv.setText(lastStr);
		Button bt = (Button) llChangerStr
				.findViewById(R.id.btn_user_login_changer_infro);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String newstr = etv.getText().toString().trim();
				etv.setFocusable(false);
				if (newstr.equals(lastStr)) {
					ToastUtils.toastMessage("您并未对当前信息做任何修改！");
					onKeyBack();
				} else {
					handUpdateInfo(newstr, position);
				}
			}
		});
		changeVp(2, llChangerStr);
	}

	protected void handUpdateInfo(final String newstr, final int position) {
		// TODO Auto-generated method stub
		int updateType = -1;
		switch (position) {
		case 1:
			updateType = 0;
			break;
		case 2:
			updateType = 1;
			break;
		case 3:
			updateType = 2;
			break;
		case 5:
			updateType = 3;
			break;
		case 6:
			updateType = 4;
			break;
		default:
			break;
		}
		if (updateType != -1) {
			Service.getInstance(dActivity).deviceUpdate(
					MyApplication.getinstance().getUser().getUserID(),
					device.getId(), 3, updateType, newstr,
					new onResultListener() {
						@Override
						public void onResult(Result result) {
							// TODO Auto-generated method stub
							if (result.getStatucCode() == 0) {
								Message msg = new Message();
								msg.what = 11;
								msg.arg1 = position;
								msg.obj = newstr;
								uihandler.sendMessage(msg);
							} else {
								uihandler.sendEmptyMessage(12);
							}
						}

						@Override
						public void onNetError(Result result) {
							// TODO Auto-generated method stub
							uihandler.sendEmptyMessage(12);
						}

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							uihandler.sendEmptyMessage(999);
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							uihandler.sendEmptyMessage(998);
						}
					});
		}
	}

	protected void UpdateDeviceImage() {
		// TODO Auto-generated method stub
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		innerIntent.putExtra("crop", "true");
		innerIntent.putExtra("aspectX", 1);
		innerIntent.putExtra("aspectY", 1);
		innerIntent.setType("image/*");
		innerIntent.putExtra("outputX", 200);
		innerIntent.putExtra("outputY", 200);
		startActivityForResult(innerIntent, 200);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode == 200) {
			Bitmap bmap = data.getParcelableExtra("data");
			if (bmap != null) {
				requestForUpdateImage(bmap);
			}
		}
	}

	private void requestForUpdateImage(Bitmap bitmap) {
		// TODO Auto-generated method stub
		Service.getInstance(dActivity).uploadDeviceImage(
				MyApplication.getinstance().getUser().getUserID(),
				device.getId(), 2, BitmapUtils.Bitmap2Bytes(bitmap),
				new onResultListener() {

					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						if (result.getStatucCode() == 0) {
							try {
								ParserResult presult = JsonParser
										.parserUploadImage(result.getJsonBody());
								if (presult.getCode() == 0) {
									Message msg = new Message();
									msg.what = 9;
									msg.obj = presult.getObj().toString();
									uihandler.sendMessage(msg);
								} else {
									uihandler.sendEmptyMessage(10);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								uihandler.sendEmptyMessage(10);
							}
						} else {
							uihandler.sendEmptyMessage(10);
						}
					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub
						Debug.Out(result.getErrorInfo());
						uihandler.sendEmptyMessage(9);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						uihandler.sendEmptyMessage(999);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						uihandler.sendEmptyMessage(998);
					}
				});
	}

	/**
	 * 酒窖设置
	 */
	SeekBar barTemp, barHumidity;
	TextView tv_current_t;
	TextView tv_current_h;

	private void initWinecellarSetUp() {
		LinearLayout lyWinecellarSetUp = (LinearLayout) inflater.inflate(
				R.layout.winecellar_setup_layout, null);
		((RelativeLayout) lyWinecellarSetUp
				.findViewById(R.id.rl_winecellar_manager_setup_password))
				.setOnClickListener(onclickListenr);
		((ImageButton) lyWinecellarSetUp
				.findViewById(R.id.img_btn_winecellar_manager_setup_t_add))
				.setOnClickListener(onclickListenr);
		((ImageButton) lyWinecellarSetUp
				.findViewById(R.id.img_btn_winecellar_manager_setup_h_add))
				.setOnClickListener(onclickListenr);
		((ImageButton) lyWinecellarSetUp
				.findViewById(R.id.img_btn_winecellar_manager_setup_h_reduce))
				.setOnClickListener(onclickListenr);
		((ImageButton) lyWinecellarSetUp
				.findViewById(R.id.img_btn_winecellar_manager_setup_t_reduce))
				.setOnClickListener(onclickListenr);
		barTemp = (SeekBar) lyWinecellarSetUp
				.findViewById(R.id.seekbar_winecellar_manager_setup_temp);
		barTemp.setProgress(15);
		barTemp.setOnSeekBarChangeListener(seekbarChangeListner);
		barHumidity = (SeekBar) lyWinecellarSetUp
				.findViewById(R.id.seekbar_winecellar_manager_setup_humidity);
		barHumidity.setProgress(80);
		barHumidity.setOnSeekBarChangeListener(seekbarChangeListner);
		tv_current_t = (TextView) lyWinecellarSetUp
				.findViewById(R.id.tv_winecellar_manager_setup_temp_num);
		tv_current_h = (TextView) lyWinecellarSetUp
				.findViewById(R.id.tv_winecellar_manager_setup_humidity_num);
		changeVp(1, lyWinecellarSetUp);
	}

	OnSeekBarChangeListener seekbarChangeListner = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub

			switch (seekBar.getId()) {
			case R.id.seekbar_winecellar_manager_setup_temp: {

				if (seekBar.getProgress() < 8) {
					seekBar.setProgress(8);
					// Toast.makeText(dActivity,
					// "您不能将温度设置到10℃以下！",Toast.LENGTH_SHORT).show();
				} else {
					tv_current_t
							.setLayoutParams(new AbsoluteLayout.LayoutParams(
									android.widget.AbsoluteLayout.LayoutParams.WRAP_CONTENT,
									android.widget.AbsoluteLayout.LayoutParams.WRAP_CONTENT,
									seekBar.getThumb().getBounds().centerX() - 10,(int) tv_current_t.getY()));
					tv_current_t.setText(String.valueOf(seekBar.getProgress())
							+ "°C");
					requestUpdateTempOrHumidity(true, seekBar.getProgress());
				}

				break;
			}
			case R.id.seekbar_winecellar_manager_setup_humidity: {

				if (seekBar.getProgress() < 20) {
					seekBar.setProgress(20);
				}
				tv_current_h
						.setLayoutParams(new AbsoluteLayout.LayoutParams(
								android.widget.AbsoluteLayout.LayoutParams.WRAP_CONTENT,
								android.widget.AbsoluteLayout.LayoutParams.WRAP_CONTENT,
								seekBar.getThumb().getBounds().centerX() - 10,
								(int) tv_current_h.getY()));
				tv_current_h.setText(String.valueOf(seekBar.getProgress())
						+ "%");
				requestUpdateTempOrHumidity(false, seekBar.getProgress());
				break;
			}
			default:
				break;
			}
		}
	};

	public void requestUpdateTempOrHumidity(boolean istemp, int progress) {
		if (istemp) {
			socketService.setTemper(System.currentTimeMillis() + "",
					device.getId(), progress);
		} else {
			socketService.setHumidity(System.currentTimeMillis() + "",
					device.getId(), progress);
		}
	}

	OnClickListener onclickListenr = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			switch (arg0.getId()) {
			case R.id.rl_winecellar_manager_setup_password:
				dActivity.setTitle("密码修改");
				toRestPassWordPage();
				break;
			case R.id.img_btn_winecellar_manager_setup_t_add: {
				int CurrentProress = barTemp.getProgress();
				CurrentProress++;
				barTemp.setProgress(CurrentProress);
			}
				break;
			case R.id.img_btn_winecellar_manager_setup_t_reduce: {
				int CurrentProress = barTemp.getProgress();

				CurrentProress--;
				barTemp.setProgress(CurrentProress);

			}
				break;
			case R.id.img_btn_winecellar_manager_setup_h_add: {
				int CurrentProress = barHumidity.getProgress();
				CurrentProress++;
				barHumidity.setProgress(CurrentProress);
			}
				break;
			case R.id.img_btn_winecellar_manager_setup_h_reduce: {

				int CurrentProress = barHumidity.getProgress();
				CurrentProress--;
				barHumidity.setProgress(CurrentProress);
			}
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 酒窖门禁控制
	 */
	private void requestForLockOrUnlock() {

		if (gridViewAdapter != null) {
			socketService.setLock(System.currentTimeMillis() + "",
					device.getId(), !gridViewAdapter.getLockState());
		}
	}

	protected void toRestPassWordPage() {
		// TODO Auto-generated method stub
		LinearLayout llChangerPassword = (LinearLayout) inflater.inflate(
				R.layout.user_login_changer_password_layout, null);
		changeVp(2, llChangerPassword);
	}

	/**
	 * 酒窖存储状况
	 */
	private void initWinecellarStorage() {
		// TODO Auto-generated method stub
		ChartView chartView = new ChartView(getActivity());
		chartView.init(device.getSize(), device.getStock());
		changeVp(1, chartView);
	}

	/**
	 * 酒窖灯光控制
	 */
	List<CheckBox> checkList = new ArrayList<CheckBox>();
	List<Light> lightList = new ArrayList<Light>();
	LinearLayout lightContentlayout;

	private void initWinecellarLightControl() {
		LinearLayout lyWinecellarLightControl = (LinearLayout) inflater
				.inflate(R.layout.winecellar_lightcontrol_layout, null);
		RadioGroup rg = (RadioGroup) lyWinecellarLightControl
				.findViewById(R.id.rg_winecellar_lightcontrol);
		final RadioButton bdLight = (RadioButton) lyWinecellarLightControl
				.findViewById(R.id.rbtn_winecellar_lightcontrol_total);
		final RadioButton bdLightNo = (RadioButton) lyWinecellarLightControl
				.findViewById(R.id.rbtn_winecellar_lightcontrol_no);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedButtonid) {
				if (checkedButtonid == R.id.rbtn_winecellar_lightcontrol_total) {
					allControl(true);
					bdLight.setTextColor(Color.RED);
					bdLightNo.setTextColor(Color.WHITE);
				} else {
					allControl(false);
					bdLight.setTextColor(Color.WHITE);
					bdLightNo.setTextColor(Color.RED);
				}
			}
		});
		lightContentlayout = (LinearLayout) lyWinecellarLightControl
				.findViewById(R.id.light_contenter);
		changeVp(1, lyWinecellarLightControl);
		requestForWinecallerLightList();
		isLightControlPage = true;
	}

	protected void updateLightContent() {
		// TODO Auto-generated method stub
		if (checkList.size() > 0) {
			for (CheckBox b : checkList) {
				b.setBackgroundDrawable(null);
				b = null;
			}
			listCheckbox = null;
			checkList.clear();
			System.gc();
		}
		LinearLayout columLayout = null;
		for (int i = 0; i < lightList.size(); i++) {
			if (i % 3 == 0) {
				columLayout = new LinearLayout(getActivity());
				columLayout.setLayoutParams(new LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
				columLayout.setWeightSum(3);
				columLayout.setOrientation(LinearLayout.HORIZONTAL);
				columLayout.setPadding(20, 20, 20, 20);
				lightContentlayout.addView(columLayout);
			}
			LinearLayout itemLayout = new LinearLayout(getActivity());
			itemLayout.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1));
			itemLayout.setOrientation(LinearLayout.HORIZONTAL);
			itemLayout.setGravity(Gravity.CENTER);
			CheckBox cb = (CheckBox) inflater.inflate(R.layout.custom_checkbox,
					null);
			itemLayout.addView(cb);
			listCheckbox.add(cb);
			columLayout.addView(itemLayout);
			cb.setTag(i);
			if (lightList.get(i).isUseable()) {
				cb.setChecked(lightList.get(i).isON());
			} else {
				cb.setEnabled(true);
			}
			cb.setOnCheckedChangeListener(LightcheckedListener);
		}
	}

	OnCheckedChangeListener LightcheckedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton checkbox, boolean ischecked) {
			// TODO Auto-generated method stub
			int position = Integer.parseInt(checkbox.getTag().toString());
			requestForSingleLightControl(lightList.get(position), ischecked);
		}
	};

	private void requestForWinecallerLightList() {
		// TODO Auto-generated method stub
		if (lightList.size() <= 0) {
			dorequestForLightList();
		} else {
			uihandler.sendEmptyMessage(7);
		}
	}

	protected void requestForSingleLightControl(Light light, boolean isopen) {
		// TODO Auto-generated method stub
		if (socketService != null && light.isUseable()) {
			socketService.setLamper(System.currentTimeMillis() + "",
					device.getId(), light.getId(), isopen);
		}
	}

	private void dorequestForLightList() {
		// TODO Auto-generated method stub
		Service.getInstance(getActivity()).requestLightList(3, device.getId(),
				new onResultListener() {

					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						try {
							ParserResult presult = JsonParser
									.parserLightList(result.getJsonBody());
							if (presult.getCode() == 0) {
								lightList = (List<Light>) presult.getObj();
								uihandler.sendEmptyMessage(7);
							} else {
								uihandler.sendEmptyMessage(8);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							uihandler.sendEmptyMessage(8);
						}

					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub
						uihandler.sendEmptyMessage(8);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						uihandler.sendEmptyMessage(999);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						uihandler.sendEmptyMessage(998);
					}
				});
	}

	protected void allControl(boolean isOpen) {
		// TODO Auto-generated method stub
		if (listCheckbox != null && listCheckbox.size() > 0) {
			for (CheckBox box : listCheckbox) {
				if (box.isChecked() != isOpen) {
					box.setChecked(isOpen);
				}
			}
		}

	}

	/**
	 * 酒窖区域展示
	 */
	private List<Regional> regionalList = new ArrayList<Regional>();
	WinecallerRegionalAdapter regionalAdapter;

	private void initWinecellarRegionalShow() {
		GridView gridView = new GridView(getActivity());
		gridView.setNumColumns(3);
		regionalAdapter = new WinecallerRegionalAdapter(inflater, regionalList);
		gridView.setAdapter(regionalAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				initGradevinLocationShow(regionalList.get(arg2));
				DetailActivity dActivity = (DetailActivity) getActivity();
				dActivity.setTitle("酒位展示");
			}
		});
		changeVp(1, gridView);
		requestForWineCallerRegional();
	}

	private void requestForWineCallerRegional() {
		// TODO Auto-generated method stub
		if (regionalList.size() <= 0) {
			Service.getInstance(getActivity()).requestRegionalList(
					MyApplication.getinstance().getUser().getUserID(),
					device.getId(), new onResultListener() {
						@Override
						public void onResult(Result result) {
							// TODO Auto-generated method stub
							if (result.getStatucCode() == 0) {
								try {
									List<Regional> list = (List<Regional>) JsonParser
											.parserRegionalList(
													result.getJsonBody())
											.getObj();
									if (list != null && list.size() > 0) {
										for (Regional r : list) {
											regionalList.add(r);
										}
										uihandler.sendEmptyMessage(0);
									} else {
										uihandler.sendEmptyMessage(1);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									uihandler.sendEmptyMessage(2);
								}
							} else {
								uihandler.sendEmptyMessage(4);
							}
						}

						@Override
						public void onNetError(Result result) {
							// TODO Auto-generated method stub
							uihandler.sendEmptyMessage(3);
						}

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							uihandler.sendEmptyMessage(999);
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							uihandler.sendEmptyMessage(998);
						}
					});
		} else {
			uihandler.sendEmptyMessage(0);
		}

	}

	/**
	 * 酒窖的酒位展示页面
	 */
	GradevinLocationShowAdapter locationAdapter;
	List<BasicInfo> locaitonBasicsList = new ArrayList<BasicInfo>();
	int basicInfoPageIndex = 0;
	ImageButton speachSearch;
	ImageButton search_del;
	Button btn_search;
	EditText et_search;
	VoiceRecognizerDialog mVoiceRecognizerDialog;
	Regional CurrentRegional;

	private void initGradevinLocationShow(Regional regional) {
		// TODO Auto-generated method stub
		CurrentRegional = regional;
		LinearLayout lyGradevinLocationShow = (LinearLayout) inflater.inflate(
				R.layout.gradevin_locationshow_layout, null);
		ListView listview = (ListView) lyGradevinLocationShow
				.findViewById(R.id.lv_gradevin_locationshow);
		speachSearch = (ImageButton) lyGradevinLocationShow
				.findViewById(R.id.img_btn_gradevin_speachsearch);
		speachSearch.setOnClickListener(listener);
		search_del = (ImageButton) lyGradevinLocationShow
				.findViewById(R.id.img_btn_gradevin_locationshow_search_del);
		search_del.setOnClickListener(listener);
		btn_search = (Button) lyGradevinLocationShow
				.findViewById(R.id.btn_gradevin_locationshow_doSearch);
		btn_search.setOnClickListener(listener);
		et_search = (EditText) lyGradevinLocationShow
				.findViewById(R.id.et_gradevin_locationshow_search);
		et_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				int length = et_search.getText().toString().length();
				if (length > 0) {
					if (search_del.getVisibility() == View.GONE) {
						search_del.setVisibility(View.VISIBLE);
					}
					if (speachSearch.getVisibility() == View.VISIBLE) {
						speachSearch.setVisibility(View.GONE);
					}
				} else {
					if (speachSearch.getVisibility() == View.GONE) {
						speachSearch.setVisibility(View.VISIBLE);
					}
					if (search_del.getVisibility() == View.VISIBLE) {
						search_del.setVisibility(View.GONE);
					}
				}

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}
		});
		locationAdapter = new GradevinLocationShowAdapter(inflater,
				locaitonBasicsList);
		listview.setAdapter(locationAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 进入商品详情页面
				if(!isUnKownProducut(locaitonBasicsList.get(arg2))){
					Intent i = new Intent(getActivity(), DetailActivity.class);
					i.putExtra("tag", Constant.Tag.TAG_PRODUCT_INFO);
					i.putExtra("code", locaitonBasicsList.get(arg2).getId());
					i.putExtra("codeType", Constant.Tag.TAG_CODE_TYPE_PRODUCTID);
					getActivity().startActivity(i);
				}
			}
		});
		listview.setOnScrollListener(new MylocationListScrollListener());
		changeVp(2, lyGradevinLocationShow);
		requestForWineCallerRegionalLocation(CurrentRegional, null, true);
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (null != arg0.getTag()) {
				return;
			}
			switch (arg0.getId()) {
			case R.id.btn_gradevin_locationshow_doSearch:
				doTextSearch();
				break;
			case R.id.img_btn_gradevin_speachsearch:
				doSpeachSearch();
				break;
			case R.id.img_btn_gradevin_locationshow_search_del:
				clearEd();
				break;
			default:
				break;
			}
		}
	};
	protected boolean isUnKownProducut(BasicInfo basicInfo) {
		// TODO Auto-generated method stub
		if(basicInfo!=null){
			if(!StringUtils.isNull(basicInfo.getId())){
				if(basicInfo.getId().equals("-1")){
					return true;
				}
			}
			if(!StringUtils.isNull(basicInfo.getName())){
				if(basicInfo.getName().trim().equals("未知酒")){
					return true;
				}
			}
		}
		return false;
	}
	protected void doSpeachSearch() {
		if (mVoiceRecognizerDialog == null) {
			preInitVoiceRecognizer();
		}
		mVoiceRecognizerDialog.show();
	}

	private void preInitVoiceRecognizer() {
		mVoiceRecognizerDialog = new VoiceRecognizerDialog(getActivity(), -1);
		mVoiceRecognizerDialog.setSilentTime(1000);
		mVoiceRecognizerDialog
				.setOnRecognizerResultListener(new VoiceRecognizerListener() {
					@Override
					public void onVolumeChanged(int arg0) {
					}

					@Override
					public void onGetVoiceRecordState(VoiceRecordState arg0) {
					}

					@Override
					public void onGetResult(VoiceRecognizerResult result) {
						// TODO Auto-generated method stub
						String mRecognizerResult = "";
						if (result != null && result.words != null) {
							int wordSize = result.words.size();
							StringBuilder results = new StringBuilder();
							for (int i = 0; i < wordSize; ++i) {
								Word word = (Word) result.words.get(i);
								if (word != null && word.text != null) {
									results.append(word.text.replace(" ", ""));
								}
							}
							mRecognizerResult = results.toString();
						}
						et_search.setText(mRecognizerResult);
						mVoiceRecognizerDialog.onDestroy();
						mVoiceRecognizerDialog = null;
					}

					@Override
					public void onGetError(int arg0) {
						mVoiceRecognizerDialog.onDestroy();
						mVoiceRecognizerDialog = null;
					}
				});
		int ret = mVoiceRecognizerDialog.init(Constant.WXVoice.WXVOICE_KEY);
		if (0 != ret) {
			mVoiceRecognizerDialog.onDestroy();
			mVoiceRecognizerDialog = null;
		}
	}

	protected void clearEd() {
		if (et_search != null) {
			et_search.setText("");
		}
	}

	protected void doTextSearch() {
		if (StringUtils.isNull(et_search.getText().toString())) {
			ToastUtils.toastMessage("检索关键字不能为空！");
			return;
		}
		requestForWineCallerRegionalLocation(CurrentRegional, et_search
				.getText().toString().trim(), true);
	}

	class MylocationListScrollListener implements OnScrollListener {

		private int first, count, total;

		@Override
		public void onScrollStateChanged(AbsListView view, int state) {
			// TODO Auto-generated method stub
			if (state == SCROLL_STATE_IDLE) {
				if (first + count >= total) {
					if (StringUtils.isNull(et_search.getText().toString()
							.trim())) {
						requestForWineCallerRegionalLocation(CurrentRegional,
								null, false);
					} else {
						requestForWineCallerRegionalLocation(CurrentRegional,
								et_search.getText().toString().trim(), false);
					}

				}
			}
		}

		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			first = firstVisibleItem;
			count = visibleItemCount;
			total = totalItemCount;
		}

	}

	private void requestForWineCallerRegionalLocation(Regional regional,
			String key, boolean isFistRequest) {
		// TODO Auto-generated method stub
		// if (locaitonBasicsList.size() > 0) {
		//
		// }
		if (isFistRequest) {
			locaitonBasicsList.clear();
			basicInfoPageIndex = 0;
		}
		if (key == null) {
			Service.getInstance(getActivity()).requestSearch(device.getId(),
					basicInfoPageIndex, regional.getId(),
					requestForLocationShowListener);
		} else {
			Service.getInstance(getActivity()).requestSearch(key,
					device.getId(), regional.getId(), basicInfoPageIndex,
					requestForLocationShowListener);
		}
	}

	onResultListener requestForLocationShowListener = new onResultListener() {

		@Override
		public void onResult(Result result) {
			// TODO Auto-generated method stub
			if (result.getStatucCode() == 0) {
				Debug.Out(result.getJsonBody());
				try {
					List<ProductInfo> pinfoList = (List<ProductInfo>) JsonParser
							.parserSearch(result.getJsonBody()).getObj();
					if (pinfoList != null && pinfoList.size() > 0) {
						for (ProductInfo pinfo : pinfoList) {
							locaitonBasicsList.add(pinfo.getBasicinfo());
						}
						uihandler.sendEmptyMessage(5);
					} else {
						uihandler.sendEmptyMessage(6);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					uihandler.sendEmptyMessage(6);
				}
			} else {
				uihandler.sendEmptyMessage(6);
			}
		}

		@Override
		public void onNetError(Result result) {
			// TODO Auto-generated method stub
			uihandler.sendEmptyMessage(6);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			uihandler.sendEmptyMessage(999);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			uihandler.sendEmptyMessage(998);
		}
	};

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
			// viewFlipper.setInAnimation(getActivity(), R.anim.in_right);
			// viewFlipper.setOutAnimation(getActivity(), R.anim.out_left);
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
			// viewFlipper.setInAnimation(getActivity(), R.anim.in_left);
			// viewFlipper.setOutAnimation(getActivity(), R.anim.out_right);
			viewFlipper.showPrevious();
			currnetIndex--;
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return Back键管理
	 */
	public boolean onKeyBack() {
		if (showViewFillperPerious()) {
			DetailActivity dActivity = (DetailActivity) getActivity();
			if (currnetIndex > 0) {
				dActivity.setTitle(frontTitle);
				if (isLightControlPage) {
					isLightControlPage = false;
				}
			} else {
				dActivity.setTitle(frontAndUpTitle);
			}
			return true;
		} else {
			return false;
		}
	}

}
