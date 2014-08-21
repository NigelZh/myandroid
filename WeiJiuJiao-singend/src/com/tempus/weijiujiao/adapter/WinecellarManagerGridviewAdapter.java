package com.tempus.weijiujiao.adapter;

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
 * 酒柜酒窖管理
 * 
 * */
public class WinecellarManagerGridviewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	Context context;
	private boolean isLock;

	public WinecellarManagerGridviewAdapter(LayoutInflater inflater,Context context) {
		this.inflater = inflater;
		this.context = context;
	}
	String[] data = { "区域展示", "灯光控制", "存储情况", "门禁控制", "酒窖设置" ,"酒窖信息"};
	int[] imgId = { R.drawable.bg_img_jiuweizhanshi_bg,R.drawable.bg_img_dengguang_bg, R.drawable.bg_img_cunchu_bg,
			R.drawable.bg_img_menjin_bg, 
			R.drawable.bg_img_shezhi_bg, 
			R.drawable.bg_img_xinxi_bg};

	public boolean  getLockState(){
		return this.isLock;
	}
	public void updateLockState(boolean isLock){
		this.isLock=isLock;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.winecellar_manage_item_adapter, null);
		}
		int Sw = ParamsUtils.getInstance(context).getScreenW();
		convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, Sw / 3));
		ImageView iv = (ImageView) convertView.findViewById(R.id.img_winecellar_manage_item_adapter);
		TextView tv = (TextView) convertView.findViewById(R.id.tv_winecellar_manage_item_adapter);
		if(position==3){
			iv.setBackgroundResource(isLock?R.drawable.bg_img_locked_bg:imgId[position]);
		}else{
			iv.setBackgroundResource(imgId[position]);
		}
		tv.setText(data[position]);
		return convertView;
	}
}
