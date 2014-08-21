package com.tempus.weijiujiao.adapter;

import java.util.HashMap;
import java.util.List;

import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.bean.Device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class SaleReportDeviceAdapter extends BaseAdapter {
	public HashMap<Integer, Boolean> checkMap;
	private List<Device> deviceList;
	private LayoutInflater inflater;
	private int deviceType;

	public SaleReportDeviceAdapter(LayoutInflater inflater,
			List<Device> deviceList,int deviceType) {
		this.inflater = inflater;
		this.deviceList = deviceList;
		this.deviceType=deviceType;
		checkMap = new HashMap<Integer, Boolean>();
	}

	public void removeCheckedDevice(Device d) {
		int position = getPosition(d);
		setIitemChecked(position, false);
		notifyDataSetChanged();
	}

	private int getPosition(Device d) {
		// TODO Auto-generated method stub
		int position = -1;
		int i = 0;
		for (Device d1 : deviceList) {
			if (d1.getId().equals(d.getId())) {
				position = i;
			}
			i++;
		}
		return position;
	}

	/**
	 * 设置选中与否
	 * 
	 * @param Position
	 * @param checked
	 */
	public void setIitemChecked(int Position, boolean checked) {
		checkMap.put(Position, checked);
	}

	/**
	 * 获得选择的map
	 * 
	 * @return
	 */
	public HashMap<Integer, Boolean> getCheckedMap() {
		return checkMap;
	}

	@Override
	public int getCount() {
		return deviceList == null ? 0 : deviceList.size();
	}

	@Override
	public Object getItem(int position) {
		return deviceList == null ? null : deviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position > checkMap.size() - 1) {
			checkMap.put(position, false);
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.sale_report_gradevin_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.contactitem_select_cb);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.contactitem_picture);
			viewHolder.tv_name = (TextView) convertView
					.findViewById(R.id.report_list_item_name);
			viewHolder.tv_size = (TextView) convertView
					.findViewById(R.id.report_list_item_size);
			viewHolder.tv_stock = (TextView) convertView
					.findViewById(R.id.report_list_item_stock);
			viewHolder.tv_id = (TextView) convertView
					.findViewById(R.id.report_list_item_id);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.checkBox.setChecked(checkMap.get(position));
		viewHolder.tv_name.setText(deviceList.get(position).getName());
		viewHolder.tv_id.setText(deviceList.get(position).getId());
		viewHolder.tv_size.setText(deviceList.get(position).getSize() + "");
		viewHolder.tv_stock.setText(deviceList.get(position).getStock() + "");
		if(StringUtils.isNull(deviceList.get(position).getImgURL())){
			if(deviceType==2){
				viewHolder.iv.setImageResource(R.drawable.jiugui_default);
			}else{
				viewHolder.iv.setImageResource(R.drawable.jiujiao_default);
			}
		}else{
			MyApplication.getinstance().getImageLoader().displayImage(deviceList.get(position).getImgURL(), viewHolder.iv);
		}
		return convertView;
	}

	static class ViewHolder {
		CheckBox checkBox;
		ImageView iv;
		TextView tv_name, tv_id, tv_size, tv_stock;
	}
}