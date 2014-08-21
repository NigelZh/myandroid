package com.tempus.weijiujiao.adapter;

import java.util.List;

import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ¾Æ½Ñ¡¢¾Æ¹ñµÄ¾ÆÎ»Õ¹Ê¾ÊÊÅäÆ÷
 * 
 * @author lenovo
 * 
 */
public class GradevinLocationShowAdapter extends BaseAdapter {
	
	private List <BasicInfo> basicList =null;
	private LayoutInflater inflater;
	
	public GradevinLocationShowAdapter(LayoutInflater inflater,
			List<BasicInfo> BasicInfoList) {
		this.basicList = BasicInfoList;
		this.inflater = inflater;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return basicList == null ? null: basicList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return basicList == null ? null
				: basicList.get(arg0);
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
			holder=new viewHolder();
			convertView = inflater.inflate(R.layout.gradevin_locationshow_list_item_layout, null);
			holder.iv=(ImageView)convertView.findViewById(R.id.gradevinlocationshow_list_item_iv);
			holder.tv_name=(TextView)convertView.findViewById(R.id.gradevinlocationshow_list_item_title_tv);
			holder.tv_year=(TextView)convertView.findViewById(R.id.tv_gradevin_locationshow_list_item_year);
			holder.tv_position=(TextView)convertView.findViewById(R.id.tv_gradevin_locationshow_list_item_positon);
			holder.tv_volume=(TextView)convertView.findViewById(R.id.tv_gradevin_locationshow_list_item_volume);
			holder.tv_seller=(TextView)convertView.findViewById(R.id.tv_gradevin_locationshow_list_item_seller);
			convertView.setTag(holder);
			}
		holder=(viewHolder) convertView.getTag();
		holder.tv_name.setText(basicList.get(position).getName());
		holder.tv_year.setText(basicList.get(position).getYear());
		holder.tv_position.setText(basicList.get(position).getLocation());
		holder.tv_volume.setText(basicList.get(position).getVolume());
		holder.tv_seller.setText(basicList.get(position).getSeller());
		if(!StringUtils.isNull(basicList.get(position).getImageURL())){
			MyApplication.getinstance().getImageLoader().displayImage(basicList.get(position).getImageURL(), holder.iv);
		}else{
			Debug.Out("imageurl==null");
		}
		
		return convertView;
	}

	class viewHolder{
		ImageView iv;
		TextView tv_name,tv_year,tv_position,tv_volume,tv_seller;
	}
}
