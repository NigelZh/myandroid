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
 * @author lenovo 酒柜信息页面的listview适配器
 * 
 */
public class AboutUsListViewAdapter extends BaseAdapter {

	private List<String> mARRAYlsit;
	private LayoutInflater inflater;
	public AboutUsListViewAdapter(LayoutInflater inflater,List<String> marryList) {
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
		Debug.Log(position + "");
		if (convertView == null) {
			Debug.Log(position + "convertviwe=null");
			convertView = inflater.inflate(
					R.layout.about_us_list_item, null);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.tv_about_us_list_item_title);
		TextView value = (TextView) convertView.findViewById(R.id.tv_about_us_list_item_value);
		//ImageView iv = (ImageView) convertView.findViewById(R.id.img_about_us_list_item_pic_go);
		if(position == 0){
			value.setVisibility(View.GONE);//隐藏
		}
		switch (position) {
		case 0:
			title.setText("功能介绍");
			//iv.setBackgroundResource(R.drawable.img_btn_arrow);
			break;
		case 1:
			title.setText("新版本更新");
			title.setRight(R.drawable.img_new);
			//iv.setBackgroundResource(R.drawable.img_new);
			value.setText("2.1");
			break;

		default:
			break;
		}
		return convertView;
	}


}
