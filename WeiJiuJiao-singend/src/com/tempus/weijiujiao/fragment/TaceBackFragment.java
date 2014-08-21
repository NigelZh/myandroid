package com.tempus.weijiujiao.fragment;

import java.util.ArrayList;
import java.util.List;

import com.tempus.weijiujiao.BarcodeScannerActivity;
import com.tempus.weijiujiao.DetailActivity;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.adapter.traceBackListViewAdapter;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.db.DBManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 防伪追溯页面对应Fragment
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class TaceBackFragment extends Fragment implements OnClickListener {
	public final static int SCANNIN_GREQUEST_CODE = 1;
	private ListView lv;
	private List<BasicInfo> historyList = new ArrayList<BasicInfo>();
	private ImageButton img_btn_scan;
	private String tag = "TaceBackFragment";
	private int currentIndex = 0;
	private DBManager dbmanager;
	private traceBackListViewAdapter adapter;
	private RelativeLayout historyControllerLayout;
	private TextView tex_clear_history,tv_toscan;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debug.Log(tag, "onCreate");
		dbmanager=MyApplication.getinstance().getDbmanager();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Debug.Log(tag, "onDestroy");
	}

	@Override
	public void onResume() {
		super.onResume();
		Debug.Log(tag, "onResume");
		currentIndex = 0;
		if (historyList != null) {
			historyList.clear();
		}
		addData();
	}
	private void addData() {
		// TODO Auto-generated method stub
		List<BasicInfo> infoList = dbmanager.getBasicList(currentIndex);
		if (infoList == null || infoList.size() <= 0) {
			return;
		} else {
			currentIndex++;
			for (BasicInfo info : infoList) {
				historyList.add(info);
			}
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			if (historyList.size() > 0) {
				historyControllerLayout.setVisibility(View.VISIBLE);
				tv_toscan.setVisibility(View.GONE);
			} else {
				historyControllerLayout.setVisibility(View.GONE);
				tv_toscan.setVisibility(View.VISIBLE);
			}
		}
	

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.traceback_fragment_layout,container, false);
		img_btn_scan = (ImageButton) rootView.findViewById(R.id.img_btn_scan);
		img_btn_scan.setOnClickListener(this);
		historyControllerLayout = (RelativeLayout) rootView
				.findViewById(R.id.history_list_controller);
		tex_clear_history = (TextView) rootView
				.findViewById(R.id.trace_back_clear_history);
		tex_clear_history.setOnClickListener(this);
		tv_toscan=(TextView)rootView.findViewById(R.id.trace_back_toscan_tv);
		tv_toscan.setOnClickListener(this);
		lv = (ListView) rootView.findViewById(R.id.traceback_history_listview);
		adapter = new traceBackListViewAdapter(getActivity(), inflater,historyList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				toDetailActivity(Constant.Tag.TAG_CODE_TYPE_PRODUCTID, historyList.get(arg2).getId(),true);
			}
		});
		lv.setOnScrollListener(new OnScrollListener() {
			private int first, count, total;

			@Override
			public void onScrollStateChanged(AbsListView view, int state) {
				// TODO Auto-generated method stub
				if (state == SCROLL_STATE_IDLE) {
					if (first + count >= total) {
						addData();
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
		});
		return rootView;
	}

	protected void toDetailActivity(int codeType, String code,boolean ishistory) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getActivity(), DetailActivity.class);
		i.putExtra("tag", Constant.Tag.TAG_PRODUCT_INFO);
		i.putExtra("code", code);
		i.putExtra("codeType", codeType);
		if(ishistory){
			i.putExtra("history", true);
		}
		getActivity().startActivity(i);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.img_btn_scan:
			startQRcodeScan();
			break;
		case R.id.trace_back_clear_history:
			clearHistory();
			break;
		case R.id.trace_back_toscan_tv:
			startQRcodeScan();
			break;
		default:
			break;
		}
	}

	private void clearHistory() {
		// TODO Auto-generated method stub
		dbmanager.clearAllBsics();
		historyList.clear();
		adapter.notifyDataSetChanged();
		if(historyControllerLayout.getVisibility()==View.VISIBLE){
			historyControllerLayout.setVisibility(View.GONE);
		}
		if(tv_toscan.getVisibility()==View.GONE){
			tv_toscan.setVisibility(View.VISIBLE);
		}
	}

	private void startQRcodeScan() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getActivity(), BarcodeScannerActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				if (scanResult != null) {
					String barcode = StringUtils.getBarCode(scanResult);
					if (barcode == null) {
						ToastUtils.toastMessage("非法二维码，请确认！！");
					} else {
						toDetailActivity(Constant.Tag.TAG_CODE_TYPE_BARCODE,barcode,false);
					}
				}
			}
			break;
		default:
			break;
		}
	}
}
