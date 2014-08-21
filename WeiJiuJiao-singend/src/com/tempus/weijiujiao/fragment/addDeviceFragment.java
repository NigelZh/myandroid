package com.tempus.weijiujiao.fragment;

import org.json.JSONException;

import com.tempus.weijiujiao.MainActivity;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * 添加设备
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class addDeviceFragment extends Fragment {
	private EditText ed_id, ed_code;
	private Button btn_ok;
	private int deviceType = -1;
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		deviceType = b.getInt("deviceType");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (deviceType != -1) {
			View rootView = inflater.inflate(
					R.layout.add_gradevin_and_winecellar_layout, null);
			findViwes(rootView);
			return rootView;
		} else {
			return null;
		}
	}

	private void findViwes(View rootView) {
		// TODO Auto-generated method stub
		ed_id = (EditText) rootView.findViewById(R.id.addDevice_ed_id);
		if (deviceType == 2) {
			ed_id.setHint("请输入酒柜编号");
		} else {
			ed_id.setHint("请输入酒窖编号");
		}
		ed_code = (EditText) rootView.findViewById(R.id.addDevice_ed_code);
		ed_code.setHint("请输入正确的授权码");
		btn_ok = (Button) rootView.findViewById(R.id.addDevice_ed_btnOk);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				requestForAddDevice();
			}
		});
	}

	Handler uihandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtils.toastMessage("绑定成功");
				Intent intent = new Intent(
						Constant.Action.ACTION_ADDDEVICE_DONE);
				intent.putExtra("deviceType", deviceType);
				MyApplication.getinstance().getApplicationContext().sendBroadcast(intent);
				Debug.Out("adddevice" + "", "successed!!");
				getActivity().finish();
				break;
			case 1:
				ToastUtils.toastMessage("网络异常");
				break;
			case 2:
				ToastUtils.toastMessage("绑定失败，没有找到该设备！！");
				break;
			case 3:
				ToastUtils.toastMessage("绑定失败，授权码失效或错误！！");
				break;
			case 4:
				ToastUtils.toastMessage("绑定失败，服务器未知错误！！");
				break;
			case 5:
				ToastUtils.toastMessage("绑定失败，服务器返回数据有误，无法识别！！");
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

	protected void requestForAddDevice() {
		// TODO Auto-generated method stub
		String id = ed_id.getText().toString();
		String code = ed_code.getText().toString();
		if (StringUtils.isNull(id)) {
			ToastUtils.toastMessage("请输入设备编号！");
			return;
		}
		if (StringUtils.isNull(code)) {
			ToastUtils.toastMessage("请输入正确的授权码！");
			return;
		}
		Service.getInstance(getActivity()).aadDevice(
				MyApplication.getinstance().getUser().getUserID(), deviceType,
				id, code, new onResultListener() {
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						try {
							ParserResult presult = JsonParser
									.parserAddDevice(result.getJsonBody());
							if (presult.getCode() == 0) {
								uihandler.sendEmptyMessage(0);
							} else if (presult.getCode() == 1) {
								uihandler.sendEmptyMessage(2);
							} else if (presult.getCode() == 2) {
								uihandler.sendEmptyMessage(3);
							} else {
								uihandler.sendEmptyMessage(4);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							uihandler.sendEmptyMessage(5);
						}
					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.obj = result.getErrorInfo();
						msg.what = 1;
						uihandler.sendMessage(msg);
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
	 * 
	 * @return Back键管理
	 */
	public boolean onKeyBack() {
		return false;
	}

}
