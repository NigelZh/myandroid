package com.tempus.weijiujiao.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tempus.weijiujiao.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author lenovo 监控页面的listview适配器 gradevin_infor_list_otheritem:这个是共用布局
 */
public class GradevinMonitorListViewAdapter extends BaseAdapter {

	private List<String> titleList;
	private LayoutInflater inflater;
	private boolean isLock = true;
	private boolean isLightOn = false;
	private int intemp = 0;
	private int humidity = 0;
	private int inHumidity = 0;

	public GradevinMonitorListViewAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
		titleList = new ArrayList<String>();
		titleList.add("室内温度");
		titleList.add("酒柜湿度");
		titleList.add("室内湿度");
		titleList.add("酒柜门锁");
		titleList.add("灯光控制");
		titleList.add("修改密码");
	}

	public void updateInTemputer(int temp) {
		this.intemp = temp;
		notifyDataSetChanged();
	}

	public void updateHumidity(int humidity) {
		this.humidity = humidity;
		notifyDataSetChanged();
	}

	public void updateInHumidity(int inHumidity) {
		this.inHumidity = inHumidity;
		notifyDataSetChanged();
	}

	public void updateLightSate(boolean isOn) {
		if (isOn != this.isLightOn) {
			this.isLightOn = isOn;
			notifyDataSetChanged();
		}
	}

	public void updateLockState(boolean isLocked) {
		if (this.isLock != isLocked) {
			this.isLock = isLocked;
			notifyDataSetChanged();
		}
	}

	public boolean getLightSate() {
		return this.isLightOn;
	}

	public boolean getLockState() {
		return this.isLock;
	}

	@Override
	public int getCount() {
		return titleList == null ? null : titleList.size();
	}

	@Override
	public Object getItem(int position) {
		return titleList == null ? null : titleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.gradevin_infor_list_otheritem, null);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.tv_gradevin_infor_list_otheritem_title);
		TextView value = (TextView) convertView
				.findViewById(R.id.tv_gradevin_infor_list_otheritem_num);
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.imbtn_gradevin_infor_list_otheritem_num);

		if (position < 3) {
			value.setVisibility(View.VISIBLE);
			iv.setVisibility(View.GONE);

		} else {
			value.setVisibility(View.INVISIBLE);
			iv.setVisibility(View.VISIBLE);
		}
		title.setText(titleList.get(position));
		if (position == 0) {
			value.setText(this.intemp + "℃");
		} else if (position == 1) {
			value.setText(this.humidity + "%");
		} else if (position == 2) {
			value.setText(this.inHumidity + "%");
		} else if (position == 3) {
			iv.setBackgroundResource(isLock ? R.drawable.shishi_lock_lock: R.drawable.shishi_lock_open);
		} else if (position == 4) {
			iv.setBackgroundResource(isLightOn ? R.drawable.shishi_light_on: R.drawable.shishi_light_off);
		}
		return convertView;
	}

}