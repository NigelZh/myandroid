package com.tempus.weijiujiao.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.TraceBackImageActivity;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.bean.ProductInfo;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;
import com.tempus.weijiujiao.bean.TracePoint;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.db.DBManager;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductBasicInfoFragment extends Fragment {
	private String code;
	private int codeType;
	private LayoutInflater inflater;
	private BasicInfo basicInfo;
	private TextView tv_name, tv_name_en, tv_year, tv_area,tv_alcoholic_strength, tv_volume,tv_chateau,tv_assortment;
	private ImageView iv;
	private LinearLayout ll_traceback;
	private List<TracePoint> list = new ArrayList<TracePoint>();
	private List<String> retrospectTitle = new ArrayList<String>();
	private DBManager dbManager;
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		code = getArguments().getString("code");
		codeType = getArguments().getInt("codeType", -1);
		dbManager=MyApplication.getinstance().getDbmanager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater =inflater;
		View view = inflater.inflate(R.layout.product_basic_info_layout,container, false);	
		initTitle();
		findviews(view);
		return view;
	}		
	public void initTitle() {
		retrospectTitle.add("收获");
		retrospectTitle.add("陈酿");
		retrospectTitle.add("灌装");
		retrospectTitle.add("采购");
		retrospectTitle.add("运输");
		retrospectTitle.add("检查、检疫");
		retrospectTitle.add("报关");
		retrospectTitle.add("入库");
		retrospectTitle.add("销售订单");
		retrospectTitle.add("配送");
	}

	private void findviews(View rootView) {
		// TODO Auto-generated method stub
		tv_name = (TextView) rootView
				.findViewById(R.id.product_basicInfo_name_tv);
		tv_name_en = (TextView) rootView
				.findViewById(R.id.product_basicInfo_name_en_tv);
		tv_year = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_year);
		tv_area = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_producing_area);
		tv_alcoholic_strength = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_alcoholic_strength);
		tv_volume = (TextView) rootView
				.findViewById(R.id.tv_product_basic_infor_content);
		tv_chateau = (TextView)rootView
		.findViewById(R.id.tv_product_basic_infor_chateau);
		tv_assortment = (TextView)rootView
		.findViewById(R.id.tv_product_basic_infor_assortment);
		ll_traceback = (LinearLayout)rootView
				.findViewById(R.id.ll_product_basic_infor);	
		
	
		iv=(ImageView)rootView.findViewById(R.id.product_basicInfo_image);
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
		if (basicInfo == null) {
			Service.getInstance(getActivity()).requestGetProductInfo(code,
					type, 1, new onResultListener() {
						@Override
						public void onResult(Result result) {
							// TODO Auto-generated method stub
							try {
								ParserResult presult=JsonParser.parserProductInfo(result.getJsonBody());
								if(presult.getCode()==0){
									ProductInfo pinfo = (ProductInfo) presult.getObj();
									if (pinfo.getBasicinfo() != null) {
										basicInfo = pinfo.getBasicinfo();
										uihandler.sendEmptyMessage(0);
									}
								}else if(presult.getCode()==1){
									uihandler.sendEmptyMessage(1);
								}
							
							} catch (JSONException e) {
								e.printStackTrace();
								uihandler.sendEmptyMessage(2);
							}
						}

						@Override
						public void onNetError(Result result) {
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
				if(codeType==Constant.Tag.TAG_CODE_TYPE_BARCODE){
					insertBasicInfoToSqlite();
				}
				setViews();
				break;
			case 1:
				ToastUtils.toastMessage("您所扫描的防伪码不存在，谨防假冒！");
				break;
			case 2:
				ToastUtils.toastMessage("服务器返回数据异常，无法解析");
				break;
			case 3:
				ToastUtils.toastMessage("网络异常");
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
	protected void setViews() {
		tv_name.setText(basicInfo.getName());
		tv_name_en.setText(basicInfo.getName_en());
		tv_year.setText(basicInfo.getYear());
		tv_area.setText(basicInfo.getArea());
		tv_volume.setText(basicInfo.getVolume());
		tv_alcoholic_strength.setText(basicInfo.getAlcoholStrength());
		//8-1pxc:增加葡萄酒品种和酒庄
		tv_chateau.setText(basicInfo.getBrandName());
		tv_assortment.setText(basicInfo.getSubCategory());
		//traceMapView.Init(basicInfo.getTraceList());
		if(!StringUtils.isNull(basicInfo.getImageURL())){
			MyApplication.getinstance().getImageLoader().displayImage(basicInfo.getImageURL(), iv);
		}
		if(basicInfo.getTraceList()!=null){
			list = basicInfo.getTraceList();
			initTraceBackLinearLayout();	
		}		
	}

	protected void insertBasicInfoToSqlite() {
		dbManager.add(basicInfo);
	}
	
	private void initTraceBackLinearLayout() {		
		for (int i = 0; i < retrospectTitle.size()&&i<list.size(); i++) {
			TracePoint tp = list.get(i);
			String strTitle = retrospectTitle.get(i);
			LinearLayout columLayout = new LinearLayout(getActivity());
			columLayout.setLayoutParams(new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
			columLayout.setOrientation(LinearLayout.HORIZONTAL);
			columLayout.setPadding(20, 0, 20, 0);
			RelativeLayout rlItem = (RelativeLayout)inflater.inflate(R.layout.traceback_list_item, null);
			ImageView imagPointPic = (ImageView)rlItem.findViewById(R.id.img_traceback_point_pic);
			TextView tvTile = (TextView)rlItem.findViewById(R.id.tv_traceback_title);
			TextView tvTime= (TextView)rlItem.findViewById(R.id.tv_traceback_time);
			ImageView imagSpectacle = (ImageView)rlItem.findViewById(R.id.img_traceback_spectacle_pic);
			tvTile.setText(strTitle);
			if(!StringUtils.isNull(tp.getTime())){
				tvTime.setText(tp.time);
				imagPointPic.setBackgroundResource(R.drawable.point_have);
				if(!StringUtils.isNull(tp.imgUrl)){					
					MyApplication.getinstance().getImageLoader().displayImage(tp.imgUrl, imagSpectacle);
					imagSpectacle.setTag(i);	
					imagSpectacle.setOnClickListener(listener);
				}
			}	
			columLayout.addView(rlItem);					
			ll_traceback.addView(columLayout);
		}
	}
	private OnClickListener listener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			int i = (Integer) arg0.getTag();
			imageBrower(list.get(i).getImgUrl());			
		}};

	private void imageBrower(String url) {
		Intent intent = new Intent(getActivity(), TraceBackImageActivity.class);
		intent.putExtra(TraceBackImageActivity.EXTRA_IMAGE_URLS, url);
		getActivity().startActivity(intent);		
	}
}
