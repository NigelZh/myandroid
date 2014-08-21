package com.tempus.weijiujiao.adapter;

import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.bean.Device;
import com.tempus.weijiujiao.bean.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author lenovo æ∆πÒ–≈œ¢“≥√Êµƒlistview  ≈‰∆˜
 * 
 */
public class GradevinInforListViewAdapter extends BaseAdapter {
	private String[] title_for_gradevin={"æ∆πÒ±‡∫≈","æ∆πÒÍ«≥∆"," ÷ª˙","” œ‰","æ∆πÒŒª÷√","µÿ÷∑","±∏◊¢"};
	private String[] title_for_winecaller={"æ∆Ω—±‡∫≈","æ∆Ω—Í«≥∆"," ÷ª˙","” œ‰","æ∆Ω—Œª÷√","µÿ÷∑","±∏◊¢"};
	private String[] currentTitle;
	private Device currentDevice;
	private LayoutInflater inflater;
	public GradevinInforListViewAdapter(LayoutInflater inflater,Device device,int deviceType) {
		this.inflater = inflater;
		this.currentDevice=device;
		if(deviceType==2){
			this.currentTitle=title_for_gradevin;
		}else{
			this.currentTitle=title_for_winecaller;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return currentTitle == null ? null :currentTitle.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return currentTitle == null ? null :currentTitle.length;
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
		TextView title = (TextView) convertView.findViewById(R.id.tv_gradevin_infor_list_otheritem_title);
		TextView value = (TextView) convertView.findViewById(R.id.tv_gradevin_infor_list_otheritem_num);
		ImageView iv = (ImageView) convertView.findViewById(R.id.imbtn_gradevin_infor_list_otheritem_num);
		if (position == 1 || position == 5||position==6) {
			iv.setVisibility(View.VISIBLE);
		} else {
			iv.setVisibility(View.GONE);
		}
		title.setText(currentTitle[position]);
		switch (position) {
		case 0:
			value.setText(currentDevice.getId());
			break;
		case 1:
			value.setText(currentDevice.getName());
			break;
		case 2:
			//value.setText(user.getUserNumber());
			value.setText(currentDevice.getNumber());
			break;
		case 3:
			//value.setText(user.getUserEmail());
			value.setText(currentDevice.getEmail());
			break;
		case 4:
			value.setText(currentDevice.getPositon());
			break;
		case 5:
			value.setText(currentDevice.getAddress());
			break;
		case 6:
			value.setText(currentDevice.getRemark());
			break;
		default:
			break;
		}
		return convertView;
	}


}
