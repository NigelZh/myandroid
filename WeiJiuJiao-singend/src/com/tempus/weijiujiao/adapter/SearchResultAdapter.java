package com.tempus.weijiujiao.adapter;

import java.util.List;

import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchResultAdapter extends BaseAdapter {
	List<BasicInfo> infoList;
	LayoutInflater inflater;

	public SearchResultAdapter(LayoutInflater inflater,
			List<BasicInfo> basicInfoList) {
		// TODO Auto-generated constructor stub
		this.inflater = inflater;
		this.infoList = basicInfoList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoList == null ? 0 : infoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infoList == null ? null : infoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.search_result_list_item_layout, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.search_result_item_name);
			holder.tv_name_en = (TextView) convertView.findViewById(R.id.search_result_item_name_en);
			holder.tv_seller = (TextView) convertView.findViewById(R.id.search_result_seller_value);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();

		holder.tv_name.setText(infoList.get(position).getName());
		holder.tv_name_en.setText(infoList.get(position).getName_en());
		holder.tv_seller.setText(infoList.get(position).getSeller());
//		holder.tv_name.setText("name "+position);
//		holder.tv_name_en.setText("name_en "+position);
//		holder.tv_seller.setText("seller "+position);
		return convertView;
	}

	class ViewHolder {
		TextView tv_name, tv_name_en, tv_seller;
	}
}
