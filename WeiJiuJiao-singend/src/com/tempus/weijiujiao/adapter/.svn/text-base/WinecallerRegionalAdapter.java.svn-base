package com.tempus.weijiujiao.adapter;

import java.util.List;

import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.bean.Regional;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * æ∆Ω—«¯”Ú  ≈‰
 * 
 * @author lenovo
 * 
 */
public class WinecallerRegionalAdapter extends BaseAdapter {
	private List<Regional> RegionalList;
	private LayoutInflater inflater;

	public WinecallerRegionalAdapter(LayoutInflater inflater,
			List<Regional> RegionalList) {
		this.RegionalList=RegionalList;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return RegionalList == null ? null: RegionalList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return RegionalList == null ? null: RegionalList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.winecellar_locationshow_listitem,null);
		}
		TextView tv_area=(TextView)convertView.findViewById(R.id.winecellarmanager_regional_area_tv);
		TextView tv_stock=(TextView)convertView.findViewById(R.id.winecellarmanager_regional_stock_tv);
		tv_area.setText(RegionalList.get(position).getName());
		tv_stock.setText(RegionalList.get(position).getStock()+"");
		return convertView;
	}

}
