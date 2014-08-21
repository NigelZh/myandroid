package com.tempus.weijiujiao.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;

import com.tempus.weijiujiao.DetailActivity;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.DateUtil;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.adapter.SaleReportCheckedImgAdapter;
import com.tempus.weijiujiao.adapter.SaleReportDeviceAdapter;
import com.tempus.weijiujiao.bean.Device;
import com.tempus.weijiujiao.bean.ReportEntity;
import com.tempus.weijiujiao.view.CustomDialog;
import com.tempus.weijiujiao.view.SaleReportHistogramView;
import com.tempus.weijiujiao.view.SaleReportHorizontalListView;
import com.tempus.weijiujiao.wheelView.DatePopupWindow;
import com.tempus.weijiujiao.wheelView.DatePopupWindow.onDateChangedListener;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 意见反馈
 * 
 * @author lenovo
 * 
 */
public class SaleReportFragment extends Fragment {
	private int currnetIndex = 0;
	private LayoutInflater inflater;
	private ListView deviceListView;
	private DetailActivity aActivity;
	private ArrayList<Device> checkedList;
	private Button makesureBtn;
	private String frontTitle, frontAndUpTitle = null;// 返回时记录之前的标题
	private ViewFlipper viewFlipper = null;
	private SaleReportCheckedImgAdapter checkedAdapter;
	private SaleReportHorizontalListView checkedContack;
	private RelativeLayout startTimerelativeLayout, endTimerelativeLayout;
	private TextView startTextView, endTextView, stratTimeTextView,
			endTimeTextView;
	private DatePopupWindow datePopupWindow;
	private int deviceType = 0, timeType = 0;
	private long currentTimeMillis;
	private long selectTimeStart = 0, selectTimeEnd = 0;
	private SaleReportDeviceAdapter adapterForGradevin, adapterForWineCaller;
	private int gradevinIndex = 0, winecallerIndex = 0;
	private List<Device> gradevinList, winecallerList;
	private List<ReportEntity> reportEntityList;
	private RadioButton rd_gradevin, rd_winecellar, rd_day, rd_month, rd_year,
			rd_time;
	ProgressDialog pDialog;
	boolean isPasue=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentTimeMillis = System.currentTimeMillis();
		selectTimeStart = currentTimeMillis;
		selectTimeEnd = currentTimeMillis;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		aActivity = (DetailActivity) getActivity();
		frontAndUpTitle = aActivity.getActivityTitle();
		viewFlipper = (ViewFlipper) inflater.inflate(
				R.layout.sale_report_layout, null);
		initView();
		return viewFlipper;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(!isPasue){
			deviceType = 2;
			handler.sendEmptyMessage(4);
		}
		isPasue=false;
	}

	@Override
	public void onPause(){
		super.onPause();
		this.isPasue=true;
	}
	/**
	 * 初始化视图
	 */
	private void initView() {
		initListView();
		initBootomCheckedShowView();
		RadioGroup deviceTypeGroup = (RadioGroup) viewFlipper
				.findViewById(R.id.rg_sale_report_gradevin_and_winecellar);
		rd_gradevin = (RadioButton) viewFlipper
				.findViewById(R.id.rbtn_sale_report_gradevin);
		rd_winecellar = (RadioButton) viewFlipper
				.findViewById(R.id.rbtn_sale_report_winecellar);
		deviceTypeGroup.setOnCheckedChangeListener(deviceTypeGroupListner);
		RadioGroup selectTypeGroup = (RadioGroup) viewFlipper
				.findViewById(R.id.rg_sale_report_time);
		rd_day = (RadioButton) viewFlipper
				.findViewById(R.id.rbtn_sale_report_day);
		rd_month = (RadioButton) viewFlipper
				.findViewById(R.id.rbtn_sale_report_month);
		rd_year = (RadioButton) viewFlipper
				.findViewById(R.id.rbtn_sale_report_year);
		rd_time = (RadioButton) viewFlipper
				.findViewById(R.id.rbtn_sale_report_time);
		selectTypeGroup.setOnCheckedChangeListener(selectTypeGroupListener);
		startTimerelativeLayout = (RelativeLayout) viewFlipper
				.findViewById(R.id.rl_sale_report_start_time);
		endTimerelativeLayout = (RelativeLayout) viewFlipper
				.findViewById(R.id.rl_sale_report_end_time);
		startTextView = (TextView) viewFlipper
				.findViewById(R.id.tv_sale_report_start);
		endTextView = (TextView) viewFlipper
				.findViewById(R.id.tv_sale_report_end);
		stratTimeTextView = (TextView) viewFlipper
				.findViewById(R.id.tv_sale_report_start_time);
		stratTimeTextView.setText(DateUtil.FormatDay(currentTimeMillis));
		endTimeTextView = (TextView) viewFlipper
				.findViewById(R.id.tv_sale_report_end_time);
		endTimeTextView.setText(DateUtil.FormatDay(currentTimeMillis));
		endTimerelativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (datePopupWindow != null) {
					if (datePopupWindow.isShowing()) {
						datePopupWindow.dismiss();
					}
					datePopupWindow = null;
				}
				datePopupWindow = new DatePopupWindow(aActivity,
						DatePopupWindow.Type_FOR_DAY);
				datePopupWindow.showPopupWindow(viewFlipper);
				datePopupWindow
						.setOnDateChangedListener(new onDateChangedListener() {
							@Override
							public void onDateChanged(int type, long newTime) {
								// TODO Auto-generated method stub
								selectTimeEnd = newTime;
								endTimeTextView.setText(DateUtil
										.FormatDay(newTime));
							}
						});
			}
		});
		startTimerelativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (datePopupWindow != null) {
					if (datePopupWindow.isShowing()) {
						datePopupWindow.dismiss();
					}
					datePopupWindow = null;
				}
				switch (timeType) {
				case 0:
					datePopupWindow = new DatePopupWindow(aActivity,
							DatePopupWindow.Type_FOR_DAY);
					break;
				case 1:
					datePopupWindow = new DatePopupWindow(aActivity,
							DatePopupWindow.Type_FOR_MOUNTH);
					break;
				case 2:
					datePopupWindow = new DatePopupWindow(aActivity,
							DatePopupWindow.Type_FOR_YEAR);
					break;
				case 3:
					datePopupWindow = new DatePopupWindow(aActivity,
							DatePopupWindow.Type_FOR_DAY);
					break;
				default:
					break;
				}
				datePopupWindow.showPopupWindow(viewFlipper);
				datePopupWindow
						.setOnDateChangedListener(new onDateChangedListener() {
							@Override
							public void onDateChanged(int type, long newTime) {
								// TODO Auto-generated method stub
								String formatResult = "";
								selectTimeStart = newTime;
								switch (timeType) {
								case 0:
									formatResult = DateUtil.FormatDay(newTime);
									break;
								case 1:
									formatResult = DateUtil
											.FormatMounth(newTime);
									break;
								case 2:
									formatResult = DateUtil.FormatYear(newTime);
									break;
								case 3:
									formatResult = DateUtil.FormatDay(newTime);
									break;
								default:
									break;
								}
								stratTimeTextView.setText(formatResult);
							}
						});
			}
		});
	}

	private void initBootomCheckedShowView() {
		// TODO Auto-generated method stub
		checkedContack = (SaleReportHorizontalListView) viewFlipper
				.findViewById(R.id.imgList_sale_report);
		checkedList = new ArrayList<Device>();
		checkedAdapter = new SaleReportCheckedImgAdapter(inflater, checkedList);
		makesureBtn = (Button) viewFlipper
				.findViewById(R.id.btn_sale_report_makesureBtn);
		makesureBtn.setOnClickListener(sureOnClickListener);
		checkedContack.setAdapter(checkedAdapter);
		checkedContack.setOnItemClickListener(picOnItemClickListener);
	}

	private void initListView() {
		deviceListView = (ListView) viewFlipper
				.findViewById(R.id.lv_sale_report_Gradevin_and_winecellar);
		if (gradevinList == null) {
			gradevinList = new ArrayList<Device>();
		}
		if (adapterForGradevin == null) {
			adapterForGradevin = new SaleReportDeviceAdapter(inflater,
					gradevinList, 2);
		}
		deviceListView.setAdapter(adapterForGradevin);
		deviceListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View itemview,
					int position, long arg3) {
				// TODO Auto-generated method stub
				setCheckedSateChanged(position);
			}
		});
		deviceListView.setOnScrollListener(new OnScrollListener() {
			private int first, count, total;

			@Override
			public void onScrollStateChanged(AbsListView view, int state) {
				// TODO Auto-generated method stub
				if (state == SCROLL_STATE_IDLE) {
					if (first + count >= total) {
						if (deviceType == 2) {
							handler.sendEmptyMessage(4);
						} else {
							handler.sendEmptyMessage(5);
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
		});

	}

	protected void setCheckedSateChanged(int position) {
		// TODO Auto-generated method stub
		if (deviceType == 2) {
			boolean before = adapterForGradevin.getCheckedMap().get(position);
			adapterForGradevin.getCheckedMap().put(position, !before);
			adapterForGradevin.notifyDataSetChanged();
			if (before) {
				checkedAdapter.removeCheckedDecive(gradevinList.get(position));
			} else {
				checkedAdapter.addCheckedDevice(gradevinList.get(position));
			}

		} else {
			boolean before = adapterForWineCaller.getCheckedMap().get(position);
			adapterForWineCaller.getCheckedMap().put(position, !before);
			adapterForWineCaller.notifyDataSetChanged();
			if (before) {
				checkedAdapter
						.removeCheckedDecive(winecallerList.get(position));
			} else {
				checkedAdapter.addCheckedDevice(winecallerList.get(position));
			}
		}
		updateBtnText();
	}

	/**
	 * 选择酒柜或者酒窖的radio的监听器
	 */
	private OnCheckedChangeListener deviceTypeGroupListner = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup arg0, int checkedButtonid) {
			if (checkedList != null && checkedList.size() > 0) {
				checkedList.clear();
				updateBtnText();
				checkedAdapter.notifyDataSetChanged();
			}
			switch (checkedButtonid) {
			case R.id.rbtn_sale_report_gradevin:
				changeAdapter(2);
				deviceType = 2;
				rd_gradevin.setTextColor(Color.RED);
				rd_winecellar.setTextColor(Color.WHITE);
//				if (adapterForGradevin == null) {
//					handler.sendEmptyMessage(4);
//				} else {
//					adapterForGradevin.getCheckedMap().clear();
//					handler.sendEmptyMessage(0);
//				}
				adapterForGradevin.getCheckedMap().clear();
				if(gradevinList!=null&&gradevinList.size()>0){
					handler.sendEmptyMessage(0);
				}else{
					handler.sendEmptyMessage(4);
				}
				break;
			case R.id.rbtn_sale_report_winecellar:
				changeAdapter(3);
				deviceType = 3;
				rd_gradevin.setTextColor(Color.WHITE);
				rd_winecellar.setTextColor(Color.RED);
				adapterForWineCaller.getCheckedMap().clear();
				if(winecallerList!=null&&winecallerList.size()>0){
					handler.sendEmptyMessage(1);
				}else{
					handler.sendEmptyMessage(5);
				}
				break;
			default:
				break;
			}
		}

	};
	/**
	 * 选择时间段的radio的监听器
	 */
	private OnCheckedChangeListener selectTypeGroupListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup arg0, int checkedButtonid) {
			// TODO Auto-generated method stub
			boolean hideEndLayout = true;
			switch (checkedButtonid) {
			case R.id.rbtn_sale_report_day:
				hideEndLayout = true;
				timeType = 0;
				rd_day.setTextColor(Color.RED);
				rd_month.setTextColor(Color.BLACK);
				rd_year.setTextColor(Color.BLACK);
				rd_time.setTextColor(Color.BLACK);
				break;
			case R.id.rbtn_sale_report_month:
				hideEndLayout = true;
				timeType = 1;
				rd_day.setTextColor(Color.BLACK);
				rd_month.setTextColor(Color.RED);
				rd_year.setTextColor(Color.BLACK);
				rd_time.setTextColor(Color.BLACK);
				break;
			case R.id.rbtn_sale_report_year:
				hideEndLayout = true;
				timeType = 2;
				rd_day.setTextColor(Color.BLACK);
				rd_month.setTextColor(Color.BLACK);
				rd_year.setTextColor(Color.RED);
				rd_time.setTextColor(Color.BLACK);
				break;
			case R.id.rbtn_sale_report_time:
				hideEndLayout = false;
				timeType = 3;
				rd_day.setTextColor(Color.BLACK);
				rd_month.setTextColor(Color.BLACK);
				rd_year.setTextColor(Color.BLACK);
				rd_time.setTextColor(Color.RED);
				break;
			default:
				break;
			}
			if (!hideEndLayout) {
				endTimerelativeLayout.setVisibility(View.VISIBLE);
				startTextView.setText("开始日期");
				selectTimeStart = System.currentTimeMillis();
				selectTimeEnd = System.currentTimeMillis();
			} else {
				if (endTimerelativeLayout.getVisibility() == View.VISIBLE) {
					endTimerelativeLayout.setVisibility(View.GONE);
				}
				startTextView.setText("选择日期");
				selectTimeEnd = System.currentTimeMillis();
			}
			setTextDate(currentTimeMillis);
		}
	};

	/**
	 * 选择图片时的监听器
	 */
	private OnItemClickListener picOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position < checkedList.size()) {
				Device d = checkedList.get(position);
				if (deviceType == 2) {
					adapterForGradevin.removeCheckedDevice(d);
				} else {
					adapterForWineCaller.removeCheckedDevice(d);
				}
				checkedAdapter.removeCheckedDecive(checkedList.get(position));
				updateBtnText();
			}
		}
	};
	/**
	 * 选择图片后确定按钮的监听器
	 */
	private OnClickListener sureOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (checkedList != null && checkedList.size() > 0) {
				String[] ids = new String[checkedList.size()];
				for (int i = 0; i < checkedList.size(); i++) {
					ids[i] = checkedList.get(i).getId();
				}
				long startTime = getStartTime();
				long endTime = getEndTime();
				System.out.println("start time="
						+ DateUtil.FormatSeconds(startTime));
				System.out.println("end time="
						+ DateUtil.FormatSeconds(endTime));
				if (checkTime(startTime, endTime)) {
					Service.getInstance(aActivity).requestReport(
							MyApplication.getinstance().getUser().getUserID(),
							deviceType, ids, startTime, endTime,
							reportResultListener);
				}
			}
		}
	};

	onResultListener reportResultListener = new onResultListener() {

		@Override
		public void onResult(Result result) {
			// TODO Auto-generated method stub
			if (result.getStatucCode() == 0) {
				if (reportEntityList == null) {
					reportEntityList = new ArrayList<ReportEntity>();
				}
				if (reportEntityList.size() > 0) {
					reportEntityList.clear();
				}
				try {
					ParserResult parserR = JsonParser
							.parserReportEntityList(result.getJsonBody());
					if (parserR.getCode() == 0) {
						reportEntityList = (List<ReportEntity>) parserR
								.getObj();
						handler.sendEmptyMessage(6);
					} else if (parserR.getCode() == 1) {
						Debug.Out(" 失败，没有找到设备");
						handler.sendEmptyMessage(7);
					} else if (parserR.getCode() == 2) {
						Debug.Out(" 失败，时间区域溢出");
						handler.sendEmptyMessage(7);
					} else {
						Debug.Out(" 失败，服务器异常");
						handler.sendEmptyMessage(7);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				}
			}
		}

		@Override
		public void onNetError(Result result) {
			// TODO Auto-generated method stub
			Debug.Out(" 失败，请求服务器失败！" + result.getErrorInfo());
			handler.sendEmptyMessage(2);
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
	};

	/**
	 * 
	 * @param arg0
	 *            进入消费报表详情页面
	 */
	protected void addScenndView() {
		// TODO Auto-generated method stub
		aActivity.setTitle("消费详情");
		SaleReportHistogramView sRHistogramView = new SaleReportHistogramView(
				aActivity);
		sRHistogramView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		sRHistogramView.Init(reportEntityList);
		changeVp(1, sRHistogramView);
	}

	private boolean checkTime(long start, long end) {
		// TODO Auto-generated method stub
		long currentTime = System.currentTimeMillis();
		if (timeType == 0 || timeType == 3) {
			if (end > System.currentTimeMillis()) {
				System.out.println("您不能选择当天或者当天以后的时间！！");
				return false;
			}
			if (currentTime - start > (long) 90 * 24 * 60 * 60 * 1000) {
				System.out.println("减去之后得==" + (currentTime - start) + "");
				System.out.println("您不能选择距离当天90天以上的时间！！");
				return false;
			}
			if (end < start) {
				System.out.println("您不能选择开始大于结束的时间！！");
				return false;
			}
			return true;
		}
		return true;
	}

	private long getEndTime() {
		// TODO Auto-generated method stub
		Calendar cl = Calendar.getInstance();
		cl.clear();
		cl.setTimeInMillis(selectTimeStart);
		if (timeType == 0) {
			cl.set(Calendar.HOUR_OF_DAY, 23);
			cl.set(Calendar.MINUTE, 59);
			cl.set(Calendar.SECOND, 59);
		} else if (timeType == 3) {
			cl.setTimeInMillis(selectTimeEnd);
			System.out.println("select end time="
					+ DateUtil.FormatSeconds(selectTimeEnd));
			cl.set(Calendar.HOUR_OF_DAY, 23);
			cl.set(Calendar.MINUTE, 59);
			cl.set(Calendar.SECOND, 59);
		} else if (timeType == 1) {
			int maxDay = DateUtil.getMonthMaxDays(cl.get(Calendar.MONTH) + 1,
					cl.get(Calendar.YEAR));
			cl.set(Calendar.DAY_OF_MONTH, maxDay);
			cl.set(Calendar.HOUR_OF_DAY, 23);
			cl.set(Calendar.MINUTE, 59);
			cl.set(Calendar.SECOND, 59);
		} else {
			cl.set(Calendar.MONTH, 11);
			int maxDay = DateUtil.getMonthMaxDays(cl.get(Calendar.MONTH) + 1,
					cl.get(Calendar.YEAR));
			cl.set(Calendar.DAY_OF_MONTH, maxDay);
			cl.set(Calendar.HOUR_OF_DAY, 23);
			cl.set(Calendar.MINUTE, 59);
			cl.set(Calendar.SECOND, 59);
		}
		return cl.getTimeInMillis();
	}

	private long getStartTime() {
		// TODO Auto-generated method stub
		Calendar cl = Calendar.getInstance();
		cl.clear();
		cl.setTimeInMillis(selectTimeStart);
		if (timeType == 0 || timeType == 3) {
			cl.set(Calendar.HOUR_OF_DAY, 0);
			cl.set(Calendar.MINUTE, 0);
			cl.set(Calendar.SECOND, 0);
		} else if (timeType == 1) {
			cl.set(Calendar.DAY_OF_MONTH, 1);
			cl.set(Calendar.HOUR_OF_DAY, 0);
			cl.set(Calendar.MINUTE, 0);
			cl.set(Calendar.SECOND, 0);
		} else {
			cl.set(Calendar.MONTH, 0);
			cl.set(Calendar.DAY_OF_MONTH, 1);
			cl.set(Calendar.HOUR_OF_DAY, 0);
			cl.set(Calendar.MINUTE, 0);
			cl.set(Calendar.SECOND, 0);
		}
		return cl.getTimeInMillis();
	}

	public void requestForDeviceList(final int deviceType) {
		int requestIndex = (deviceType == 2 ? gradevinIndex : winecallerIndex);
		Service.getInstance(getActivity()).getDeviceList(
				MyApplication.getinstance().getUser().getUserID(), deviceType,
				requestIndex, new onResultListener() {
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						if (result.getStatucCode() == 0) {
							try {
								ParserResult parserResult = JsonParser
										.parserDeviceList(result.getJsonBody());
								if (parserResult.getCode() == 0) {
									List<Device> list = (List<Device>) parserResult
											.getObj();
									if (list != null && list.size() > 0) {
										for (Device d : list) {
											if (deviceType == 2) {
												gradevinList.add(d);
											} else {
												winecallerList.add(d);
											}
										}
										if (deviceType == 2) {
											Debug.Out("111111111111");
											handler.sendEmptyMessage(0);
											gradevinIndex++;
										} else {
											Debug.Out("2222222222");
											handler.sendEmptyMessage(1);
											winecallerIndex++;
										}
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								handler.sendEmptyMessage(3);
							}
						}
					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(2);
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

	protected void updateBtnText() {
		// TODO Auto-generated method stub
		makesureBtn.setText("确定(" + checkedList.size() + ")");
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 更新酒柜列表。不请求，更新即可
				updateGardevinList(false);
				break;
			case 1:// 更新酒窖列表。不请求，更新即可
				updateWineCallerList(false);
				break;
			case 2:// 网络异常
				ToastUtils.toastMessage("网络异常!");
				break;
			case 3:// json解析异常
				ToastUtils.toastMessage("服务器返回数据错误，无法识别！");
				break;
			case 4:// 更新酒柜列表。强制请求，并更新
				updateGardevinList(true);
				break;
			case 5:// 更新酒窖列表。强制请求，并更新
				updateWineCallerList(true);
				break;
			case 6:// 加载报表页面
				addScenndView();
				break;
			case 7:
				ToastUtils.toastMessage("查询报表失败！");
				break;
			case 999:
				if (pDialog == null) {
					pDialog = CustomDialog.createProgressDialog(getActivity());
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

	protected void setTextDate(long currentTimeMillis2) {
		// TODO Auto-generated method stub
		switch (timeType) {
		case 0:
			stratTimeTextView.setText(DateUtil.FormatDay(currentTimeMillis2));
			break;
		case 1:
			stratTimeTextView
					.setText(DateUtil.FormatMounth(currentTimeMillis2));
			break;
		case 2:
			stratTimeTextView.setText(DateUtil.FormatYear(currentTimeMillis2));
			break;
		case 3:
			stratTimeTextView.setText(DateUtil.FormatDay(currentTimeMillis2));
			endTimeTextView.setText(DateUtil.FormatDay(currentTimeMillis2));
			break;
		default:
			break;
		}
	}

	protected void updateGardevinList(boolean needRequest) {
		// TODO Auto-generated method stub
		if (gradevinList == null) {
			gradevinList = new ArrayList<Device>();
		}
		if (needRequest) {
			requestForDeviceList(deviceType);
		}
		adapterForGradevin.notifyDataSetChanged();
	}

	protected void updateWineCallerList(boolean needRequest) {
		// TODO Auto-generated method stub
		if (winecallerList == null) {
			winecallerList = new ArrayList<Device>();
		}
		if (needRequest) {
			requestForDeviceList(deviceType);
		}
		adapterForWineCaller.notifyDataSetChanged();
	}

	private void changeAdapter(int devideType) {
		if (devideType == this.deviceType) {
			return;
		} else {

			if (devideType == 3) {
				if (winecallerList == null) {
					winecallerList = new ArrayList<Device>();
				}
				if (adapterForWineCaller == null) {
					adapterForWineCaller = new SaleReportDeviceAdapter(
							inflater, winecallerList, 3);
				}
				deviceListView.setAdapter(adapterForWineCaller);
			} else if (devideType == 2) {
				if (adapterForGradevin == null) {
					adapterForGradevin = new SaleReportDeviceAdapter(inflater,
							gradevinList, 2);
				}
				deviceListView.setAdapter(adapterForGradevin);
			}
		}

	}

	/**
	 * 
	 * @param index
	 * @param scendview
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
			// viewFlipper.setInAnimation(aActivity, R.anim.in_right);
			// viewFlipper.setOutAnimation(aActivity, R.anim.out_left);
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
			// viewFlipper.setInAnimation(aActivity, R.anim.in_left);
			// viewFlipper.setOutAnimation(aActivity, R.anim.out_right);
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
