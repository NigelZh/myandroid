package com.tempus.weijiujiao.adapter;

import java.util.ArrayList;
import java.util.List;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.bean.User;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author lenovo 用户信息页面的listview适配器
 * 
 */
public class UserInforListViewAdapter extends BaseAdapter {

	private List<String> userTitle = new ArrayList<String>();
	private LayoutInflater inflater;
	private User user;
	public UserInforListViewAdapter(LayoutInflater inflater,User user) {
		this.inflater = inflater;
		this.user = user;
	   initTitle();
	}

	private void initTitle() {
		userTitle.add("手机");
		userTitle.add("邮箱");
		userTitle.add("昵称");
		userTitle.add("修改密码");
		userTitle.add("地址");
		userTitle.add("注册时间");		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userTitle == null ? null :userTitle.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userTitle == null ? null :userTitle.get(position);
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
		TextView value = (TextView) convertView	.findViewById(R.id.tv_gradevin_infor_list_otheritem_num);
		ImageView iv = (ImageView) convertView	.findViewById(R.id.imbtn_gradevin_infor_list_otheritem_num);
		if(position > 4||position==0){
			iv.setVisibility(View.GONE);//隐藏箭头图片
		}else{
			iv.setVisibility(View.VISIBLE);
		}
		title.setText(userTitle.get(position));
		switch (position) {
		case 0:	
			value.setText(user.getUserNumber());
			break;
		case 1:
			value.setText(user.getUserEmail());
			break;
		case 2:
			value.setText(user.getUserName());
			break;
		case 3:	
			value.setText("");//还是要设置为空，否则会记录上个视图的内容
			break;
		case 4:
			value.setText(user.getUserAddress());
			break;
		case 5:		
			value.setText(user.getUserRegisterTime());
			break;
		default:
			break;
		}
		return convertView;
	}
	



}
