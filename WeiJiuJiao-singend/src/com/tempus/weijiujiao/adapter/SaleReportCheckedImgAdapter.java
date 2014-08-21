package com.tempus.weijiujiao.adapter;

import java.util.ArrayList;

import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.bean.Device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SaleReportCheckedImgAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Device> data;

	public ArrayList<Device> getData() {
		return data;
	}

	public SaleReportCheckedImgAdapter(LayoutInflater inflater,
			ArrayList<Device> data) {
		this.inflater = inflater;
		this.data = data;
	}

	public void addCheckedDevice(Device device) {
		data.add(device);
		notifyDataSetChanged();
	}

	public void removeCheckedDecive(Device device) {
		int index=-1;
		int i = 0;
		for (Device d : data) {
			if (d.getId().equals(device.getId())) {
			index=i;
			}
			i++;
		}
		if(index!=-1){
			data.remove(index);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return data==null?0:data.size()+1;
	}

	@Override
	public Object getItem(int position) {
		if(data==null){
			return null;
		}
		if(position>=data.size()){
			return null;
		}
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Device d ;
		if(position>=data.size()){
			d=null;
		}else{
			d=data.get(position);
		}
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.sale_report_checked_gradevin_item, null);
			viewHolder = new ViewHolder();
			viewHolder.picture = (ImageView) convertView.findViewById(R.id.contactitem_picture);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(d==null){
			viewHolder.picture.setImageResource(R.drawable.none);
		}else{
			if(StringUtils.isNull(d.getImgURL())){
				viewHolder.picture.setImageResource(R.drawable.jiugui_default);
			}else{
				MyApplication.getinstance().getImageLoader().displayImage(d.getImgURL(), viewHolder.picture);
			}
		}
		return convertView;
	}
	static class ViewHolder {
		ImageView picture;
	}
}