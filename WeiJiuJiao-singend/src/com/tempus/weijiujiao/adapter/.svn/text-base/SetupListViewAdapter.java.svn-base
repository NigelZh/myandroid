package com.tempus.weijiujiao.adapter;

import java.util.List;

import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.Debug;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author lenovo 用户设置页面的listview适配器
 * 
 */
public class SetupListViewAdapter extends BaseAdapter {

	private List<String> mARRAYlsit;
	private LayoutInflater inflater;
	public SetupListViewAdapter(LayoutInflater inflater,List<String> marryList) {
		this.inflater = inflater;
		this.mARRAYlsit = marryList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mARRAYlsit == null ? null :mARRAYlsit.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mARRAYlsit == null ? null :mARRAYlsit.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gradevin_infor_list_otheritem, null);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.tv_gradevin_infor_list_otheritem_title);
		TextView value = (TextView) convertView
				.findViewById(R.id.tv_gradevin_infor_list_otheritem_num);
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.imbtn_gradevin_infor_list_otheritem_num);

		value.setVisibility(View.INVISIBLE);//布局存在，但不可见
		iv.setVisibility(View.VISIBLE);
		switch (position) {
		case 0:
			title.setText("系统设置");
			break;
		case 1:
			title.setText("关于我们");
			break;
		default:
			break;
		}
		return convertView;
	}


}
