package com.tempus.weijiujiao.fragment;

import org.json.JSONException;

import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 意见反馈
 * 
 * @author lenovo
 * 
 */
public class FeedbackFragment extends Fragment {

	private Button btn_ok;
	private EditText ed_message;
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = (LinearLayout) inflater.inflate(R.layout.feedback_layout, null);
		btn_ok = (Button) rootview.findViewById(R.id.feedback_btn_ok);
		ed_message = (EditText) rootview.findViewById(R.id.feedback_ed_message);
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkMessage();
			}
		});
		return rootview;
	}

	protected void checkMessage() {
		// TODO Auto-generated method stub
		String message = ed_message.getText().toString();
		String alertMessage = null;
		if (message == null || message.equals("")) {
			alertMessage = "反馈信息不能为空！";
		} else if (message.length() < 10) {
			alertMessage = "至少要输入10个字吧？";
		} else {
			sendMessage(message);
		}
		if (alertMessage != null) {
			ToastMessage(alertMessage);
		}
	}

	private void sendMessage(String message) {
		// TODO Auto-generated method stub
		Service.getInstance(getActivity()).requestFeedBack(
				MyApplication.getinstance().getUser().getUserID(), message,
				new onResultListener() {
					Message msg = new Message();
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						try {
							ParserResult presult=JsonParser.parserFeedBakc(result.getJsonBody());
							if(presult.getCode()==0){
								handler.sendEmptyMessage(0);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

							handler.sendEmptyMessage(2);
						}
					}
					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub

						handler.sendEmptyMessage(1);
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

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastMessage("信息反馈成功，感谢您对我们的支持！");
				goBack();
				break;
			case 1:
				ToastMessage("网络异常！");
				break;
			case 2:
				ToastMessage("服务器返回数据异常，无法解析！");
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

	protected void goBack() {
		// TODO Auto-generated method stub
		getActivity().finish();
	}

	public void ToastMessage(String message) {
		ToastUtils.toastMessage(message);
	}

	/**
	 * 
	 * @return Back键管理
	 */
	public boolean onKeyBack() {
		return false;
	}

}
