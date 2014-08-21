package com.tempus.weijiujiao.adapter;

import java.util.List;

import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * ∑¿Œ±◊∑À›¡–±Ì  ≈‰
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class traceBackListViewAdapter extends BaseAdapter {
	private List<BasicInfo> dataList;
	private LayoutInflater minflater;
	private Context context;

	public traceBackListViewAdapter(Context context,LayoutInflater inflater,List<BasicInfo> historyList) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.dataList = historyList;
		this.minflater = inflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList == null ? null : dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataList == null || dataList.size() <= 0 ? null : dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			convertView = minflater.inflate(
					R.layout.history_scan_list_item_layout, null);
			holder.iv = (ImageView) convertView
					.findViewById(R.id.history_list_item_iv);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.history_list_item_title_tv);
			holder.tv_name_en = (TextView) convertView
					.findViewById(R.id.history_list_item_title_en_tv);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.history_list_item_price_tv);
			holder.tv_volume = (TextView) convertView
					.findViewById(R.id.history_list_item_content_tv);
			holder.tv_year = (TextView) convertView
					.findViewById(R.id.history_list_item_year_tv);
			holder.startGroup = (LinearLayout) convertView
					.findViewById(R.id.history_list_item_stars_contentlayout);
			convertView.setTag(holder);
		}
		holder = (viewHolder) convertView.getTag();
		holder.tv_name.setText(dataList.get(position).getName());
		holder.tv_name_en.setText(dataList.get(position).getName_en());
		holder.tv_price.setText(dataList.get(position).getPrice() + "");
		holder.tv_volume.setText(dataList.get(position).getVolume());
		holder.tv_year.setText(dataList.get(position).getYear());
		holder.startGroup.removeAllViews();
		holder.iv.setImageResource(R.drawable.wine_default_icon);
		if(!StringUtils.isNull(dataList.get(position).getImageURL())){
			MyApplication.getinstance().getImageLoader().displayImage(dataList.get(position).getImageURL(), holder.iv);
		}
		int startCount = dataList.get(position).getStar();
		for (int i = 0; i < 5; i++) {
			ImageView ivnew=new ImageView(context);
			ivnew.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			if (i < startCount ) {
				ivnew.setImageResource(R.drawable.list_item_layout_star_light);
			}else{
				ivnew.setImageResource(R.drawable.list_item_layout_star_dark);
			}
			holder.startGroup.addView(ivnew);
		}
		return convertView;
	}

	class viewHolder {
		ImageView iv;
		TextView tv_name, tv_name_en, tv_year, tv_volume, tv_price, tv;
		LinearLayout startGroup;
	}
}
