package com.tempus.weijiujiao.TitleLayout;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.ParamsUtils;
import com.tempus.weijiujiao.Utils.ScreenUtils;
import com.tempus.weijiujiao.Utils.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CustomPopup extends PopupWindow {
	private Context mContext;

	protected final int LIST_PADDING = 10;

	private Rect mRect = new Rect();

	private final int[] mLocation = new int[2];

	private boolean mIsDirty;

	private OnItemOnClickListener mItemOnClickListener;
	private ListView mListView;
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();
	private LayoutInflater inflater;
	private BaseAdapter itemAdapter;

	public CustomPopup(Context context) {
		this(context, null, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	public CustomPopup(Context context, LayoutInflater inflater, int width,
			int height) {
		Debug.Out("00000000");
		if (context == null) {
			Debug.Out("111111111111");
		}
		if (inflater == null) {
			Debug.Out("222222222");
		}
		this.inflater = inflater;
		this.mContext = context;
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		ParamsUtils.getInstance(mContext).getScreenW();
		ParamsUtils.getInstance(mContext).getScreenH();
		setWidth(width);
		setHeight(height);
		setBackgroundDrawable(new BitmapDrawable());
		// 设置弹窗的布局界面
		setContentView(LayoutInflater.from(mContext).inflate(
				R.layout.custom_popup, null));
		initUI();
	}

	/**
	 * 初始化弹窗列表
	 */
	private void initUI() {
		mListView = (ListView) getContentView().findViewById(R.id.title_list);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				dismiss();
				if (mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems.get(index),
							index);
			}
		});
	}

	/**
	 * 显示弹窗列表界面
	 */
	public void show(View view) {
		view.getLocationOnScreen(mLocation);
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
				mLocation[1] + view.getHeight());
		if (mIsDirty) {
			populateActions();
		}
		showAsDropDown(
				view,
				-getWidth() + view.getWidth() / 2
						+ ScreenUtils.dip2px(mContext, 30), 2);
	}

	/**
	 * 设置弹窗列表子项
	 */
	private void populateActions() {
		mIsDirty = false;
		itemAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.pop_item_layout,
							null);
				}
				ActionItem item = mActionItems.get(position);
				final ImageView iv = (ImageView) convertView
						.findViewById(R.id.pop_item_iv);
				TextView tv = (TextView) convertView.findViewById(R.id.pop_item_tv);
				tv.setText(item.getTitle());
				if (!StringUtils.isNull(item.getImageurl())) {
					iv.setImageResource(R.drawable.img_user_small_src);
					Debug.Out(item.getImageurl());
					MyApplication
							.getinstance()
							.getImageLoader()
							.loadImage(item.getImageurl(),
									new SimpleImageLoadingListener() {
										@Override
										public void onLoadingComplete(
												String arg0, View arg1,
												Bitmap arg2) {
											iv.setBackgroundDrawable(new BitmapDrawable(arg2));
										}
									});
				} else if (mActionItems.size() == 4 && position == 0
						&& MyApplication.getinstance().getUser().isLogin()) {
					iv.setImageResource(R.drawable.img_user_small_src);
					iv.setBackgroundResource(R.drawable.img_user_head_default_bg);
				} else {
					if(mActionItems.size() == 4 && position == 0){
						iv.setImageResource(R.drawable.img_user_small_src);
						iv.setBackgroundResource(R.drawable.img_user_head_default_bg);
					}else{
						iv.setBackgroundResource(item.getImageResource());
					}
					
				}
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return mActionItems.get(position);
			}

			@Override
			public int getCount() {
				return mActionItems.size();
			}
		};
		mListView.setAdapter(itemAdapter);
	}

	public void update() {
		itemAdapter.notifyDataSetChanged();
	}

	/**
	 * 添加子类项
	 */
	public void addAction(ActionItem action) {
		if (action != null) {
			mActionItems.add(action);
			mIsDirty = true;
		}
	}

	/**
	 * 清除子类项
	 */
	public void cleanAction() {
		if (mActionItems.isEmpty()) {
			mActionItems.clear();
			mIsDirty = true;
		}
	}

	/**
	 * 根据位置得到子类项
	 */
	public ActionItem getAction(int position) {
		if (position < 0 || position >= mActionItems.size())
			return null;
		return mActionItems.get(position);
	}

	/**
	 * 设置监听事件
	 */
	public void setItemOnClickListener(
			OnItemOnClickListener onItemOnClickListener) {
		this.mItemOnClickListener = onItemOnClickListener;
	}

	public OnItemOnClickListener getItemOnClickListener() {
		if (this.mItemOnClickListener != null) {
			return this.mItemOnClickListener;
		}
		return null;
	}

	/**
	 * @author yangyu 功能描述：弹窗子类项按钮监听事件
	 */
	public static interface OnItemOnClickListener {
		public void onItemClick(ActionItem item, int position);
	}
}
