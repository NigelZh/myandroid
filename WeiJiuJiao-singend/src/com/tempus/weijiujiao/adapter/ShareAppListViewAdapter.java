package com.tempus.weijiujiao.adapter;

import com.tempus.weijiujiao.R;
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
public class ShareAppListViewAdapter extends BaseAdapter {

	private String[] titleArray={"分享到朋友圈","分享给微信好友","分享给QQ好友","分享到QQ空间","分享到腾讯微博","分享到新浪微博"};
	private int[] backGroundResource={R.drawable.icon_pengyouquan,R.drawable.icon_weixin,R.drawable.icon_qq_bg,R.drawable.icon_qqkongjian,R.drawable.icon_tengxunweibo,R.drawable.icon_weibo};
	private LayoutInflater inflater;
	public ShareAppListViewAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titleArray.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titleArray[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.share_app_list_item, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.tv_share_app_list_item_title);
		ImageView iv = (ImageView) convertView.findViewById(R.id.img_share_app_list_item_pic);
		title.setText(titleArray[position]);
		iv.setBackgroundResource(backGroundResource[position]);
		return convertView;
	}
}
