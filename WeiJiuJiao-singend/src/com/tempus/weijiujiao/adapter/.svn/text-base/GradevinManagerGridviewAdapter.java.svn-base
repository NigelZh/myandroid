package com.tempus.weijiujiao.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.ParamsUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

/**
 * 酒柜管理Gridview的adapter(可以和酒窖管理合并为同一个)
 * 
 * */
public class GradevinManagerGridviewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	Context context;
	List<String> itemList = new ArrayList<String>();
	List<Integer> imgResourceList = new ArrayList<Integer>();

	public GradevinManagerGridviewAdapter(LayoutInflater inflater,
			Context context) {
		this.inflater = inflater;
		this.context = context;
		itemList.add("酒位展示");
		itemList.add("酒柜监控");
		itemList.add("存储比例");
		itemList.add("酒柜信息");
		itemList.add("酒柜授权");
		imgResourceList.add(R.drawable.bg_img_jiuweizhanshi_bg);
		imgResourceList.add(R.drawable.bg_img_jiankong_bg);
		imgResourceList.add(R.drawable.bg_img_cunchu_bg);
		imgResourceList.add(R.drawable.bg_img_xinxi_bg);
		imgResourceList.add(R.drawable.bg_img_shouquan_bg);
	}

	public void hideAutoryItem() {
		if (itemList.size() == 5) {
			itemList.remove(4);
			imgResourceList.remove(4);
			notifyDataSetChanged();
		}
	}

	public void showAutoryItem() {
		if (itemList.size() == 4) {
			itemList.add("酒柜授权");

		}
		if (imgResourceList.size() == 4) {
			imgResourceList.add(R.drawable.icon_shouquan);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.gradevin_manage_item_adapter, null);
		}
		int Sw = ParamsUtils.getInstance(context).getScreenW();
		convertView.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, Sw / 3));
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.img_gradevinItemImage);
		TextView tv = (TextView) convertView
				.findViewById(R.id.tv_gradevinItemText);
		iv.setBackgroundResource(imgResourceList.get(position));
		tv.setText(itemList.get(position));
		return convertView;
	}
}
