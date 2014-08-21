package com.tempus.weijiujiao.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.tempus.weijiujiao.DetailActivity;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.bean.Device;
import com.tempus.weijiujiao.bean.User;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 设备列表
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class DeviceListFrament extends Fragment {
	private String tag = "GradevinFrament";
	private List<Device> deviceList = new ArrayList<Device>();
	private LayoutInflater inflater;
	private gradevinListViewAdapter adapter;
	private int currentIndex = 0;
	private int currentTag = -1;
	int deviceType;
	ListView lv;
	private TextView tv_login,tv_addDevice;
	BroadcastReceiver deviceReceiver;
	boolean needRefresh=false;
	User currentUser;
	ProgressDialog pDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
		Bundle b = getArguments();
		currentTag = b.getInt("tag");
		deviceType = (currentTag == Constant.Tag.TAG_WINLLER_LIST ? 3 : 2);
		deviceReceiver =new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				if(deviceType==arg1.getIntExtra("deviceType", -1)){
					needRefresh=true;
				}
			}
		};
		getActivity().registerReceiver(deviceReceiver, new IntentFilter(Constant.Action.ACTION_ADDDEVICE_DONE));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(tag, "onAttach");
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(tag, "onDestroy");
	}
	@Override
	public void onResume() {
		super.onResume();
		 currentUser = MyApplication.getinstance().getUser();
		if (currentUser.isLogin()) {
			lv.setVisibility(View.VISIBLE);
			tv_login.setVisibility(View.GONE);
			if (deviceList == null) {
				deviceList = new ArrayList<Device>();
			}
			if (deviceList.size() <= 0) {
				adapter.notifyDataSetChanged();
				requestGradevinFramentList();	
			}else if(needRefresh){
				deviceList.clear();
				currentIndex=0;
				requestGradevinFramentList();
				needRefresh=false;
			}
		} else {
			tv_login.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
			deviceList.clear();
			currentIndex=0;
		}
	}

	private void requestGradevinFramentList() {
		// TODO Auto-generated method stub
		Service.getInstance(getActivity()).getDeviceList(currentUser.getUserID(),deviceType, currentIndex, new onResultListener() {
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						try {
							ParserResult presult = JsonParser.parserDeviceList(result.getJsonBody());
							if (presult.getCode() == 0) {
								List<Device> dlist = (List<Device>) presult.getObj();
								if (dlist != null && dlist.size() >= 0) {
									for (Device d : dlist) {
										deviceList.add(d);
									}
									Debug.Out("get device");
									handler.sendEmptyMessage(0);
									currentIndex++;
								} else {
									Debug.Out("get device null");
									handler.sendEmptyMessage(1);
								}
							} else if (presult.getCode() == 1) {
								handler.sendEmptyMessage(4);
							} else {
								handler.sendEmptyMessage(5);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							handler.sendEmptyMessage(2);
						}
					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub						
						handler.sendEmptyMessage(3);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(999);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(998);
					}
				});
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				if(currentUser.isLogin()){
					lv.setVisibility(View.VISIBLE);
					tv_addDevice.setVisibility(View.GONE);
				}
				break;
			case 1:
				ToastUtils.toastMessage("没有设备！");
				if(currentUser.isLogin()){
					lv.setVisibility(View.GONE);
					tv_addDevice.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				ToastUtils.toastMessage("服务器返回数据异常，无法解析！");
				break;
			case 3:
				ToastUtils.toastMessage("请求失败，网络错误！！");
				break;
			case 4:
				ToastUtils.toastMessage("失败，没有找到设备！！");
				break;
			case 5:
				ToastUtils.toastMessage("失败，服务器异常！！");
				break;
			case 20:
				toDetailActivity((Device)msg.obj);
				break;
			case 21:
				ToastUtils.toastMessage("此酒柜不属于您！");//他的上级如何处理
				break;
			case 98:
				ToastUtils.toastMessage("服务器异常！");
				break;
			case 99:
				ToastUtils.toastMessage("网络异常");
				break;
			case 999:
				if(pDialog==null){
					pDialog=CustomDialog.createProgressDialog(getActivity());
				}
				pDialog.show();
				break;
			case 998:
				CustomDialog.hideProgressDialog(pDialog);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(tag, "onCreateView");
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.gradevin_fragment_layout, null);
		lv = (ListView) view.findViewById(R.id.fragment_gradevin_listview);
		tv_login = (TextView) view.findViewById(R.id.tv_for_login);
		tv_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), DetailActivity.class);
				i.putExtra("tag", Constant.Tag.TAG_USER_LOGIN);
				getActivity().startActivity(i);
			}
		});
		tv_addDevice=(TextView)view.findViewById(R.id.tv_for_addDevice);
		tv_addDevice.setText("您还没有"+(deviceType==2?"酒柜":"酒窖")+"哦，赶紧去添加吧~");
		tv_addDevice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				toAddDevice();
			}
		});
		adapter = new gradevinListViewAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//如果是个人用户是否是他的设备
				if(MyApplication.getinstance().getUser().getUserType()==2){
					isOwner(arg2,deviceList.get(arg2).getId(),deviceList.get(arg2));					
				}else{
					toDetailActivity(deviceList.get(arg2));
				}
			}
		});
		lv.setOnScrollListener(new OnScrollListener() {
			private int first, count, total;

			@Override
			public void onScrollStateChanged(AbsListView view, int state) {
				// TODO Auto-generated method stub
				if (state == SCROLL_STATE_IDLE) {
					if (first + count >= total) {
						requestGradevinFramentList();
					}
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				first = firstVisibleItem;
				count = visibleItemCount;
				total = totalItemCount;
			}
		});
		return view;
	}

	protected void isOwner(int position,String deviceId,final Device device) {

		Service.getInstance(getActivity()).requestIsUserDevice(MyApplication.getinstance().getUser().getUserID(), deviceId, new onResultListener(){
			@Override
			public void onResult(Result result) {
				// TODO Auto-generated method stub
				if (result.getStatucCode() == 0) {
					try {
						ParserResult parserResult = JsonParser.parserDeviceAuthority(result.getJsonBody());
						int code = parserResult.getCode();
						if (code == 0) {							
							Message msg = new Message();
							msg.what = 20;
 						    msg.obj = device;
							handler.sendMessage(msg);//是他的设备							
						} else if (code == 1) {
							handler.sendEmptyMessage(21);//不是他的设备						
						} else {
							handler.sendEmptyMessage(98);//服务器异常
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onNetError(Result result) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(99);
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(999);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(998);
			}});		
	}

	protected void toAddDevice() {
		// TODO Auto-generated method stub
		int tag=(deviceType==2?Constant.Tag.TAG_USER_ADDGRADEVIN:Constant.Tag.TAG_USER_ADDWINECELLAR);
		Intent intent =new Intent(getActivity(), DetailActivity.class);
		intent.putExtra("tag", tag);
		getActivity().startActivity(intent);
	}

	protected void toLogin() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getActivity(), DetailActivity.class);
		i.putExtra("tag", Constant.Tag.TAG_USER_LOGIN);
		getActivity().startActivity(i);
	}
//	private void showProgressDialog() {
//		// TODO Auto-generated method stub
//		if (pd == null) {
//			pd = CustomDialog.createProgressDialog(getActivity(),"123",
//					"123", true,);
//		}
//		pd.show();
//	}
//
//	private void hideProgressDialog() {
//		if (pd != null && pd.isShowing()) {
//			pd.dismiss();
//		}
//	}
	protected void toDetailActivity(Device device) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getActivity(), DetailActivity.class);
		i.putExtra("tag",currentTag == Constant.Tag.TAG_GARVIDEN_LIST ? Constant.Tag.TAG_GRADEVIN_MANAGER: Constant.Tag.TAG_WINECELLAR_MANAGER);
		i.putExtra("device", device);
		getActivity().startActivity(i);
	}

	class gradevinListViewAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return deviceList == null ? null : deviceList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return deviceList == null ? null : deviceList.get(arg0);
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
				convertView = inflater.inflate(
						R.layout.gradevin_list_item_layout, null);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.gradevin_list_item_iv);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.gardevin_list_item_title_tv);
				holder.tv_id = (TextView) convertView
						.findViewById(R.id.gardevin_list_item_id_tv);
				holder.tv_size = (TextView) convertView
						.findViewById(R.id.gardevin_list_item_size_tv);
				holder.tv_stock = (TextView) convertView
						.findViewById(R.id.gardevin_list_item_stock_tv);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.tv_name.setText(deviceList.get(position).getName());
			holder.tv_id.setText(deviceList.get(position).getId());
			holder.tv_size.setText(deviceList.get(position).getSize() + "");
			holder.tv_stock.setText(deviceList.get(position).getStock() + "");
			if (currentTag == Constant.Tag.TAG_GARVIDEN_LIST) {
				holder.iv.setImageResource(R.drawable.jiugui_default);
			} else {
				holder.iv.setImageResource(R.drawable.jiujiao_default);
			}
			if (!StringUtils.isNull(deviceList.get(position).getImgURL())) {
				MyApplication
						.getinstance()
						.getImageLoader()
						.displayImage(deviceList.get(position).getImgURL(),
								holder.iv);
			}
			return convertView;
		}

		class ViewHolder {
			ImageView iv;
			TextView tv_name, tv_id, tv_size, tv_stock;
		}
	}
}
