package com.tempus.weijiujiao.fragment;

import org.json.JSONException;

import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.bean.ProductInfo;
import com.tempus.weijiujiao.bean.ProductInfo.TasteInfo;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 商品品鉴信息
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class ProductTasteInfoFrament extends Fragment {
	private String code;
	private int codeType;
	private TasteInfo tasteInfo;
	private TextView tv_soberTime, tv_Sampletemperature, tv_Fragrance,
			tv_WithDishes, tv_RobertScore, tv_Expert;
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		code = getArguments().getString("code");
		codeType = getArguments().getInt("codeType", -1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		ScrollView svProductBasic = (ScrollView) inflater.inflate(
				R.layout.product_evluation_guide_info_layout, container, false);
		findViews(svProductBasic);
		return svProductBasic;
	}

	private void findViews(ScrollView svProductBasic) {
		// TODO Auto-generated method stub
		tv_soberTime = (TextView) svProductBasic
				.findViewById(R.id.product_tasteInfo_sobertime_tv);
		tv_Sampletemperature = (TextView) svProductBasic
				.findViewById(R.id.product_tasteInfo_sampletempperature_tv);
		tv_Fragrance = (TextView) svProductBasic
				.findViewById(R.id.product_tasteInfo_fragrance_tv);
		tv_WithDishes = (TextView) svProductBasic
				.findViewById(R.id.product_tasteInfo_withdishes_tv);
		tv_RobertScore = (TextView) svProductBasic
				.findViewById(R.id.product_tasteInfo_robertscore_tv);
		tv_Expert = (TextView) svProductBasic
				.findViewById(R.id.product_tasteInfo_expert_tv);
	}

	public void onResume() {
		super.onResume();
		if (tasteInfo == null) {
			requestTasteInfo();
		} else {
			setViews();
		}
	}

	Handler uihandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				setViews();
				break;
			case 1:
				ToastMessage("查无该商品");
				break;
			case 2:
				ToastMessage("服务器返回数据异常，无法解析");
				break;
			case 3:
				ToastMessage("网络异常");
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

	private void ToastMessage(String message) {
		ToastUtils.toastMessage(message);
	}

	private void requestTasteInfo() {

		// TODO Auto-generated method stub
		int type = -1;
		switch (codeType) {
		case Constant.Tag.TAG_CODE_TYPE_BARCODE:
			type = 0;
			break;
		case Constant.Tag.TAG_CODE_TYPE_CODE:
			type = 1;
			break;
		case Constant.Tag.TAG_CODE_TYPE_PRODUCTID:
			type = 3;
			break;
		case Constant.Tag.TAG_CODE_TYPE_RFID:
			type = 2;
			break;

		default:
			break;
		}
		Service.getInstance(getActivity()).requestGetProductInfo(code, type, 4,
				new onResultListener() {
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						try {
							ParserResult presult = JsonParser
									.parserProductInfo(result.getJsonBody());
							if (presult.getCode() == 0) {
								ProductInfo pinfo = (ProductInfo) presult
										.getObj();
								if (pinfo.getTeasteinfo() != null) {
									tasteInfo = pinfo.getTeasteinfo();
									uihandler.sendEmptyMessage(0);
								}
							} else if (presult.getCode() == 1) {
								uihandler.sendEmptyMessage(1);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							uihandler.sendEmptyMessage(2);
						}
					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub
						uihandler.sendEmptyMessage(3);
						Debug.Out("reuslt info="+result.getErrorInfo());
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

	private void setViews() {
		// TODO Auto-generated method stub
		if (tasteInfo != null) {
			tv_Expert.setText(tasteInfo.getExpert());
			tv_Fragrance.setText(tasteInfo.getFragrance());
			tv_RobertScore.setText(tasteInfo.getRobertScore());
			tv_Sampletemperature.setText(tasteInfo.getSampletemperature());
			tv_soberTime.setText(tasteInfo.getSoberTime());
			tv_WithDishes.setText(tasteInfo.getWithDishes());
		}

	}
}
