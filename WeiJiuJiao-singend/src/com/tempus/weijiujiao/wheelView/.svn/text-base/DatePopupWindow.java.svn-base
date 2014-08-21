package com.tempus.weijiujiao.wheelView;

import java.util.Calendar;

import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.DateUtil;
import com.tempus.weijiujiao.Utils.Debug;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.PopupWindow;

public class DatePopupWindow extends PopupWindow {
	public static final int Type_FOR_DAY = 1;
	public static final int Type_FOR_MOUNTH = 2;
	public static final int Type_FOR_YEAR = 3;
	private int CurrentType;
	private onDateChangedListener listner;
	LayoutInflater inflater;
	Activity context;
	private WheelView yearWheel, monthWheel, dayWheel;
	public static String[] yearContent = null;
	public static String[] monthContent = null;
	public static String[] dayContent = null;
	private View view;
	private long currentTime = 0;

	public DatePopupWindow(final Activity context, int type) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.CurrentType = type;
		currentTime=System.currentTimeMillis();
		initPopWindow();
		intWheelViewcontent();
		initWheelView(type);
	}

	private void initPopWindow() {
		// TODO Auto-generated method stub
		 inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 view = inflater.inflate(R.layout.time_picker, null);
		 int w = context.getWindowManager().getDefaultDisplay().getWidth();
		 this.setContentView(view);
		 this.setWidth(w);
		 this.setHeight(LayoutParams.WRAP_CONTENT);
		 this.setFocusable(true);
		 this.setOutsideTouchable(true);
	}

	private void intWheelViewcontent() {
		// TODO Auto-generated method stub
		yearContent = new String[10];
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentTime);
		int year = calendar.get(Calendar.YEAR);
		// 只能查看前10年
		for (int i=0;i<10;i++) {
			yearContent[i] = String.valueOf(year-9+ i);
		}

		monthContent = new String[12];
		for (int i = 0; i < 12; i++) {
			monthContent[i] = String.valueOf(i + 1);
			if (monthContent[i].length() < 2) {
				monthContent[i] = "0" + monthContent[i];
			}
		}
		int month=calendar.get(Calendar.MONTH);
		int days=DateUtil.getMonthMaxDays(month+1, year);
		dayContent=new String[days];
		for(int i=1;i<=days;i++){
			dayContent[i-1]=i+"";
		}
	}

	private void initWheelView(int type) {
		// TODO Auto-generated method stub
		yearWheel = (WheelView) view.findViewById(R.id.yearwheel);
		monthWheel = (WheelView) view.findViewById(R.id.monthwheel);
		dayWheel = (WheelView) view.findViewById(R.id.daywheel);
		yearWheel.setAdapter(new StrericWheelAdapter(yearContent));
		yearWheel.setCyclic(true);
		yearWheel.setInterpolator(new AnticipateOvershootInterpolator());
		
		monthWheel.setAdapter(new StrericWheelAdapter(monthContent));
		monthWheel.setCyclic(true);
		monthWheel.setInterpolator(new AnticipateOvershootInterpolator());
		
		dayWheel.setAdapter(new  StrericWheelAdapter(dayContent));
		dayWheel.setCyclic(true);
		dayWheel.setInterpolator(new AnticipateOvershootInterpolator());
		switch (type) {
		case Type_FOR_DAY:
			break;
		case Type_FOR_MOUNTH:
			dayWheel.setVisibility(View.GONE);
			break;
		case Type_FOR_YEAR:
			dayWheel.setVisibility(View.GONE);
			monthWheel.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		yearWheel.addScrollingListener(mylistener);
		monthWheel.addScrollingListener(mylistener);
		dayWheel.addScrollingListener(mylistener);
		setdefault();
	}

	private void setdefault() {
		// TODO Auto-generated method stub
		Calendar cl=Calendar.getInstance();
		cl.setTimeInMillis(currentTime);
		yearWheel.setCurrentItem(yearContent.length-1);
		if(monthWheel.getVisibility()==View.VISIBLE){
			monthWheel.setCurrentItem(cl.get(Calendar.MONTH));
		}
		if(dayWheel.getVisibility()==View.VISIBLE){
			dayWheel.setCurrentItem(cl.get(Calendar.DAY_OF_MONTH)-1);
		}
	}

	OnWheelScrollListener mylistener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub
			Calendar cl = Calendar.getInstance();
			cl.setTimeInMillis(currentTime);
			switch (wheel.getId()) {
			case R.id.yearwheel:
				int year = Integer.parseInt(yearWheel.getCurrentItemValue());
				cl.set(Calendar.YEAR, year);
				break;
			case R.id.monthwheel:
				int Mounth = Integer.parseInt(monthWheel.getCurrentItemValue());
				cl.set(Calendar.DAY_OF_MONTH, 1);
				cl.set(Calendar.MONTH, Mounth-1);
				if(dayWheel.getVisibility()==View.VISIBLE){
					dayWheel.setCurrentItem(0);
				}
				break;
			case R.id.daywheel:
				int dayofMounth = Integer.parseInt(dayWheel.getCurrentItemValue());
				cl.set(Calendar.DAY_OF_MONTH, dayofMounth);
				break;

			default:
				break;
			}
			currentTime = cl.getTimeInMillis();
			if (listner != null) {
				listner.onDateChanged(CurrentType, currentTime);
			}
			updateWheelView();
		}
	};

	/**
	 * 显示popupWindow
	 * 
	 */
	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			// 显示
			this.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		} else {
			this.dismiss();
		}
	}

	protected void updateWheelView() {
		// TODO Auto-generated method stub
		Debug.Out("11111111111111111111111");
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(currentTime);
		int year = cl.get(Calendar.YEAR);
		int mounth = cl.get(Calendar.MONTH);
		int dayofmounth = cl.get(Calendar.DAY_OF_MONTH);
		int days=DateUtil.getMonthMaxDays(mounth+1,year );
		Debug.Out("year",year+"");
		Debug.Out("mounth",mounth+1+"");
		Debug.Out("dayofmounth",dayofmounth+"");
		Debug.Out("days",days+"");
		if(dayContent.length!=days){
			Debug.Out("222222222222222222222");
			updateDayWhell(days,dayofmounth);
		}
	}

	private void updateDayWhell(int monthDays,int dayofmounth) {
		// TODO Auto-generated method stub
		Debug.Out("333333333333333 monthDays="+monthDays+"&&dayofmounth="+dayofmounth);
		dayContent=new String[monthDays];
		for(int i=1;i<=monthDays;i++){
			dayContent[i-1]=i+"";
		}
		dayWheel.setAdapter(new  StrericWheelAdapter(dayContent));
		dayWheel.setCyclic(true);
		dayWheel.setInterpolator(new AnticipateOvershootInterpolator());
		if(dayofmounth<=monthDays){
			Debug.Out("444444444444444");
			dayWheel.setCurrentItem(dayofmounth-1);
		}else{
			Debug.Out("555555555555555");
			dayWheel.setCurrentItem(0);
			//更新月中的天份
		}
	}



	public void setOnDateChangedListener(onDateChangedListener listener) {
		this.listner = listener;
	}

	public interface onDateChangedListener {
		public void onDateChanged(int type, long newTime);
	}

}
