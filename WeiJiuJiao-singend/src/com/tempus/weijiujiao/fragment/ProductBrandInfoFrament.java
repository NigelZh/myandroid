package com.tempus.weijiujiao.fragment;

import org.json.JSONException;

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
import com.tempus.weijiujiao.bean.ProductInfo;
import com.tempus.weijiujiao.bean.ProductInfo.BrandInfo;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 商品酒庄信息
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class ProductBrandInfoFrament extends Fragment {
	private String code;
	private int codeType;
	private TextView tv_name, tv_name_en, tv_area, tv_rank, tv_Region,
			tv_average, tv_varietites, tv_bestYear, tv_webstate, tv_detialDesc;
	private ImageView img_chateatu;
	private BrandInfo brandInfo;
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
		View rootView = inflater.inflate(R.layout.product_chateau_info_layout,
				null);
		findviews(rootView);
		return rootView;
	}

	private void findviews(View rootView) {
		tv_name = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_name);
		tv_name_en = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_name_en);
		tv_area = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_area);
		tv_rank = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_rank);
		tv_Region = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_region);
		tv_average = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_average);
		tv_varietites = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_varietites);
		tv_bestYear = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_bestyear);
		tv_webstate = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_Webstate);
		tv_detialDesc = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_chateatu_detial);
		img_chateatu = (ImageView) rootView
				.findViewById(R.id.img_product_basic_infor_chateatu_img);
	}

	public void onResume() {
		super.onResume();
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
		if (brandInfo == null) {
			Service.getInstance(getActivity()).requestGetProductInfo(code,
					type, 2, new onResultListener() {
						@Override
						public void onResult(Result result) {
							// TODO Auto-generated method stub
							try {
								ParserResult presult = JsonParser
										.parserProductInfo(result.getJsonBody());
								if (presult.getCode() == 0) {
									ProductInfo pinfo = (ProductInfo) presult
											.getObj();
									if (pinfo.getBrandinfo() != null) {
										brandInfo = pinfo.getBrandinfo();
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
							Debug.Out("reuslt info="+result.getErrorInfo());
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
				toastMessage("查无该商品");
				break;
			case 2:
				toastMessage("服务器返回数据异常，无法解析");
				break;
			case 3:
				toastMessage("网络异常");
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

	public void toastMessage(String message) {
		ToastUtils.toastMessage(message);
	}

	private void setViews() {
		tv_name.setText(brandInfo.getName());
		tv_name_en.setText(brandInfo.getNameEn());
		tv_area.setText(brandInfo.getArea());
		tv_rank.setText(brandInfo.getRank());
		tv_Region.setText(brandInfo.getRegion());
		tv_average.setText(brandInfo.getAverage());
		tv_varietites.setText(brandInfo.getVarietites());
		tv_bestYear.setText(brandInfo.getBestYear());
		tv_webstate.setText(brandInfo.getWebstate());
		tv_detialDesc.setText(Html.fromHtml(brandInfo.getDetialDesc()));
		// 下载图片还没有处理
		if (!StringUtils.isNull(brandInfo.getImageURL())) {
			MyApplication.getinstance().getImageLoader()
					.displayImage(brandInfo.getImageURL(), img_chateatu);
		}
	}
}
