package com.tempus.weijiujiao.TitleLayout;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.qq.wx.voice.recognizer.VoiceRecognizerDialog;
import com.qq.wx.voice.recognizer.VoiceRecognizerListener;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult;
import com.qq.wx.voice.recognizer.VoiceRecordState;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult.Word;
import com.tempus.weijiujiao.MainActivity;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.TitleLayout.CustomPopup.OnItemOnClickListener;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.ScreenUtils;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.adapter.SearchResultAdapter;
import com.tempus.weijiujiao.bean.ProductInfo;
import com.tempus.weijiujiao.bean.User;
import com.tempus.weijiujiao.bean.ProductInfo.BasicInfo;
import com.tempus.weijiujiao.constant.Constant;
import com.tempus.weijiujiao.db.DBManager;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 自定义标题布局
 * 
 * @author _blank :24611015@qq.com
 * 
 */
public class CustomTitleLayout extends LinearLayout {
	private ImageButton img_btn_seach, img_btn_add, img_btn_more,
			img_btn_speachSearch, img_btn_del;
	private Button btn_doSearch;
	private EditText search_ed;
	private OnClickListener listener;
	private LinearLayout searchLayout;
	private CustomPopup pAdd, pMore;
	// private String[] addArray = { "添加酒柜", "添加酒窖", "消费报表" };
	// private int[] addArrayImageResource = { R.drawable.icon_add_gravedin,
	// R.drawable.icon_add_winecaller, R.drawable.icon_add_report };
	private String[] addArray = { "添加酒柜", "消费报表" };
	private int[] addArrayImageResource = { R.drawable.icon_add_gravedin,
			R.drawable.icon_add_report };
	private String[] MoreArray = { "点击登录", "分享APP", "设置", "意见反馈" };
	private int[] moreArrayImageResource = { R.drawable.icon,
			R.drawable.icon_more_share, R.drawable.icon_more_setting,
			R.drawable.icon_add_report };
	LayoutInflater inflater;
	private boolean islogin = false;
	private String[] historyArray;
	private PopupWindow historyPop, searchResultPop;
	private List<BasicInfo> bInfoList;
	private VoiceRecognizerDialog mVoiceRecognizerDialog;
	private DBManager dbmanger;
	ProgressDialog pDialog;

	public CustomTitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tittle_layout, this);
		dbmanger = MyApplication.getinstance().getDbmanager();
		initListener();
		initView();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		listener = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != arg0.getTag()) {
					return;
				}
				switch (arg0.getId()) {
				case R.id.img_btn_search:
					doSearch();
					break;
				case R.id.img_btn_add:
					doAdd(arg0);
					break;
				case R.id.img_btn_more:
					doMore(arg0);
					break;
				case R.id.img_btn_search_speach:
					doSpeachSearch();
					break;
				case R.id.img_btn_search_del:
					clearEd();
					break;
				case R.id.btn_doSearch:
					doTextSearch();
					break;
				default:
					break;
				}
			}
		};
	}

	protected void doTextSearch() {
		// TODO Auto-generated method stub
		if (StringUtils.isNull(search_ed.getText().toString())) {
			ToastUtils.toastMessage("检索关键字不能为空！");
			return;
		}
		dbmanger.addKey(search_ed.getText().toString());
		if (bInfoList == null) {
			bInfoList = new ArrayList<BasicInfo>();
		} else {
			bInfoList.clear();
		}
		requestSearch();
	}

	private void requestSearch() {
		// TODO Auto-generated method stub
		Service.getInstance(getContext()).requestSearch(0,
				search_ed.getText().toString().trim(), new onResultListener() {
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						try {
							ParserResult presult = JsonParser
									.parserSearch(result.getJsonBody());
							if (presult.getCode() == 0) {
								List<ProductInfo> pInfoList = (List<ProductInfo>) presult
										.getObj();
								if (pInfoList != null && pInfoList.size() > 0) {
									if (bInfoList == null) {
										bInfoList = new ArrayList<BasicInfo>();
									}
									for (ProductInfo pinfo : pInfoList) {
										bInfoList.add(pinfo.getBasicinfo());
									}
									uihandler.sendEmptyMessage(0);
								} else {
									uihandler.sendEmptyMessage(1);
								}
							} else {
								uihandler.sendEmptyMessage(1);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							uihandler.sendEmptyMessage(2);
						}

					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub、
						Debug.Out(result.getErrorInfo());
						uihandler.sendEmptyMessage(3);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						uihandler.sendEmptyMessage(999);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						uihandler.sendEmptyMessage(998);
					}
				});
	}

	Handler uihandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (bInfoList != null && bInfoList.size() > 0) {
					showResult();
				}
				break;
			case 1:
				ToastUtils.toastMessage("没有找到相关商品");
				break;
			case 2:
				ToastUtils.toastMessage("服务器返回数据异常");
				break;
			case 3:
				ToastUtils.toastMessage("网络异常");
				break;
			case 4:

				break;
			case 999:
				if(pDialog==null){
					pDialog=CustomDialog.createProgressDialog(getContext());
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

	private void showResult() {
		// TODO Auto-generated method stub
		initSearchResultPop();
		searchResultPop.showAsDropDown(searchLayout);
	}

	TextView tv_search_key;
	TextView tv_searchresult_count;
	ListView searchResultListView;

	private void initSearchResultPop() {
		// TODO Auto-generated method stub
		searchResultPop = new PopupWindow(getContext());
		searchResultPop.setFocusable(true);
		searchResultPop.setTouchable(true);
		searchResultPop.setOutsideTouchable(false);
		searchResultPop.setBackgroundDrawable(getContext().getResources()
				.getDrawable(R.drawable.wheel_bg));
		searchResultPop
				.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		searchResultPop
				.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		View rootview = inflater.inflate(R.layout.search_result_layout, null);
		tv_search_key = (TextView) rootview
				.findViewById(R.id.search_result_key);
		tv_searchresult_count = (TextView) rootview
				.findViewById(R.id.search_result_numCount);
		searchResultListView = (ListView) rootview
				.findViewById(R.id.search_result_list);
		tv_search_key.setText("”" + search_ed.getText().toString() + "“");
		tv_searchresult_count.setText(bInfoList == null ? "0" : bInfoList
				.size() + "");

		searchResultListView.setAdapter(new SearchResultAdapter(inflater,
				bInfoList));
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				MainActivity.getInstance().startDetailActivityForProduct(
						bInfoList.get(arg2).getId());
			}
		});
		searchResultPop.setContentView(rootview);
	}

	protected void clearEd() {
		// TODO Auto-generated method stub
		if (search_ed != null) {
			search_ed.setText("");
		}
	}

	protected void doSpeachSearch() {
		// TODO Auto-generated method stub

		if (mVoiceRecognizerDialog == null) {
			preInitVoiceRecognizer();
		}
		mVoiceRecognizerDialog.show();

	}

	private void preInitVoiceRecognizer() {
		mVoiceRecognizerDialog = new VoiceRecognizerDialog(getContext(), -1);
		mVoiceRecognizerDialog.setSilentTime(1000);
		mVoiceRecognizerDialog
				.setOnRecognizerResultListener(new VoiceRecognizerListener() {

					@Override
					public void onVolumeChanged(int arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onGetVoiceRecordState(VoiceRecordState arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onGetResult(VoiceRecognizerResult result) {
						// TODO Auto-generated method stub
						String mRecognizerResult = "";
						if (result != null && result.words != null) {
							int wordSize = result.words.size();
							StringBuilder results = new StringBuilder();
							for (int i = 0; i < wordSize; ++i) {
								Word word = (Word) result.words.get(i);
								if (word != null && word.text != null) {
									results.append(word.text.replace(" ", ""));
								}
							}
							mRecognizerResult = results.toString();
						}
						search_ed.setText(mRecognizerResult);
						mVoiceRecognizerDialog.onDestroy();
						mVoiceRecognizerDialog = null;
					}

					@Override
					public void onGetError(int arg0) {
						// TODO Auto-generated method stub
						mVoiceRecognizerDialog.onDestroy();
						mVoiceRecognizerDialog = null;
					}
				});
		int ret = mVoiceRecognizerDialog.init(Constant.WXVoice.WXVOICE_KEY);
		if (0 != ret) {
			mVoiceRecognizerDialog.onDestroy();
			mVoiceRecognizerDialog = null;
		}
	}

	protected void doMore(View v) {
		// TODO Auto-generated method stub
		initPopMore();
		if (pMore == null) {
			return;
		}
		if (pMore.getItemOnClickListener() == null) {
			pMore.setItemOnClickListener(new OnItemOnClickListener() {
				@Override
				public void onItemClick(ActionItem item, int position) {

					newUserSetup(position);
					// if (position != 0) {
					// if (MyApplication.getinstance().getUser().isLogin()) {
					// newUserSetup(position);
					// } else {
					// Toast.makeText(getContext(), "您需要先登录~",
					// Toast.LENGTH_SHORT).show();
					// }
					// } else {
					//
					// }
				}
			});
		}
		pMore.show(v);
	}

	private void initPopMore() {
		// TODO Auto-generated method stub
		pMore = new CustomPopup(this.getContext(), inflater,
				ScreenUtils.dip2px(getContext(), 160),
				LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < MoreArray.length; i++) {
			ActionItem action = new ActionItem();
			action.setTitle(MoreArray[i]);
			action.setImageResource(moreArrayImageResource[i]);
			if (i == 0) {
				User user = MyApplication.getinstance().getUser();
				if (user.isLogin()) {
					action.setImageurl(user.getUserImagerURL());
					action.setTitle(StringUtils.isNull(user.getUserName()) ? user
							.getUserID() : user.getUserName());
				}
			}
			pMore.addAction(action);
		}
	}

	/**
	 * @param positon
	 * 
	 */
	private void newUserSetup(int positon) {
		switch (positon) {
		case 0:
			// 用户信息设置与登录
			MainActivity.getInstance().startUserInforAndLogin();
			break;
		case 1:
			// 分享app
			MainActivity.getInstance().startDetailActivity(
					Constant.Tag.TAG_USER_SHAREAPP);
			break;
		case 2:
			// app设置;
			MainActivity.getInstance().startDetailActivity(
					Constant.Tag.TAG_USER_SETUP);
			break;
		case 3:
			// 意见反馈
			MainActivity.getInstance().startDetailActivity(
					Constant.Tag.TAG_USER_FEEDBACK);
			break;
		default:
			break;
		}
	}

	private void newGradevinAndWineCellar(int positon) {
		switch (positon) {
		case 0:
			// 添加酒柜
			MainActivity.getInstance().startDetailActivity(
					Constant.Tag.TAG_USER_ADDGRADEVIN);
			break;
//		case 1:
//			// 添加酒窖
//			MainActivity.getInstance().startDetailActivity(
//					Constant.Tag.TAG_USER_ADDWINECELLAR);
//			break;
		case 1:
			// 消费报表;
			MainActivity.getInstance().startDetailActivity(
					Constant.Tag.TAG_SALE_REPORT);
			break;
		default:
			break;
		}
	}

	protected void doAdd(View v) {
		// TODO Auto-generated method stub
		initPopAdd();
		if (pAdd == null) {
			return;
		}
		if (pAdd.getItemOnClickListener() == null) {
			pAdd.setItemOnClickListener(new OnItemOnClickListener() {
				@Override
				public void onItemClick(ActionItem item, int position) {
					// TODO Auto-generated method stub
					if (MyApplication.getinstance().getUser().isLogin()) {
						newGradevinAndWineCellar(position);
					} else {
						ToastUtils.toastMessage("您需要先登录");
					}
				}
			});
		}
		pAdd.show(v);
	}

	private void initPopAdd() {
		// TODO Auto-generated method stub
		if (this.getContext() == null) {
			Debug.Out("getContext  is null");
		}
		if (inflater == null) {
			Debug.Out("inflater  is null");
		}
		pAdd = new CustomPopup(this.getContext(), inflater, ScreenUtils.dip2px(
				getContext(), 160), LayoutParams.WRAP_CONTENT);
		if (addArray == null) {
			Debug.Out("add array is null");
		}
		for (int i = 0; i < addArray.length; i++) {
			ActionItem action = new ActionItem();
			action.setTitle(addArray[i]);
			action.setImageResource(addArrayImageResource[i]);
			pAdd.addAction(action);
		}
	}

	protected void doSearch() {
		// TODO Auto-generated method stub
		if (searchLayout.getVisibility() == View.GONE) {
			if (!search_ed.isEnabled()) {
				search_ed.setEnabled(true);
			}
			searchLayout.setVisibility(View.VISIBLE);
			showHistoryListPop();
		} else {
			if (search_ed.isEnabled()) {
				search_ed.setEnabled(false);
			}
			hideHistoryListPop();
			searchLayout.setVisibility(View.GONE);
		}
	}

	private void hideHistoryListPop() {
		// TODO Auto-generated method stub
		if (historyPop != null && historyPop.isShowing()) {
			historyPop.dismiss();
		}
	}

	private void showHistoryListPop() {
		// TODO Auto-generated method stub
		historyArray = dbmanger.getKeyArray();
		if (historyArray != null && historyArray.length > 0) {
			initHistoryPop();
			// historyKeyListAdapter.notifyDataSetChanged();
			historyPop.showAsDropDown(searchLayout);
		}
	}

	ArrayAdapter<String> historyKeyListAdapter;

	private void initHistoryPop() {
		// TODO Auto-generated method stub
		historyPop = new PopupWindow(getContext());
		historyPop.setFocusable(true);
		historyPop.setTouchable(true);
		historyPop.setOutsideTouchable(false);
		historyPop.setBackgroundDrawable(getContext().getResources()
				.getDrawable(R.drawable.wheel_bg));
		historyPop.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		historyPop.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		ListView contentList = new ListView(getContext());
		historyKeyListAdapter = new ArrayAdapter<String>(getContext(),
				R.layout.search_history_list_item_layout, historyArray);
		contentList.setAdapter(historyKeyListAdapter);
		contentList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				changeEdtext(arg2);
			}
		});
		historyPop.setContentView(contentList);
	}

	protected void changeEdtext(int position) {
		// TODO Auto-generated method stub
		search_ed.setText(historyArray[position]);
	}

	private void initView() {
		// TODO Auto-generated method stub
		search_ed = (EditText) findViewById(R.id.title_search_ed);
		searchLayout = (LinearLayout) findViewById(R.id.title_search);
		img_btn_seach = (ImageButton) findViewById(R.id.img_btn_search);
		img_btn_seach.setOnClickListener(listener);
		img_btn_add = (ImageButton) findViewById(R.id.img_btn_add);
		img_btn_add.setOnClickListener(listener);
		img_btn_more = (ImageButton) findViewById(R.id.img_btn_more);
		img_btn_more.setOnClickListener(listener);
		img_btn_speachSearch = (ImageButton) findViewById(R.id.img_btn_search_speach);
		img_btn_speachSearch.setOnClickListener(listener);
		img_btn_del = (ImageButton) findViewById(R.id.img_btn_search_del);
		img_btn_del.setOnClickListener(listener);
		btn_doSearch = (Button) findViewById(R.id.btn_doSearch);
		btn_doSearch.setOnClickListener(listener);
		search_ed.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				int length = search_ed.getText().toString().length();
				if (length > 0) {
					if (img_btn_del.getVisibility() == View.GONE) {
						img_btn_del.setVisibility(View.VISIBLE);
					}
					if (img_btn_speachSearch.getVisibility() == View.VISIBLE) {
						img_btn_speachSearch.setVisibility(View.GONE);
					}
				} else {
					if (img_btn_speachSearch.getVisibility() == View.GONE) {
						img_btn_speachSearch.setVisibility(View.VISIBLE);
					}
					if (img_btn_del.getVisibility() == View.VISIBLE) {
						img_btn_del.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int start,
					int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 更新popwindows列表
	 */
	public void notifyUserSateChanged() {
		User user = MyApplication.getinstance().getUser();
		if (user.isLogin() != islogin) {
			islogin = user.isLogin();
			String userName = user.getUserName();
			String userId = user.getUserID();
			ActionItem item = pMore.getAction(0);
			if (userName != null) {
				item.setTitle(userName);
			} else {
				item.setTitle(userId);
			}
			String url = user.getUserImagerURL();
			if (!StringUtils.isNull(url)) {
				item.setImageurl(url);
			} else {
				item.setImageurl(null);
			}
			pMore.update();
		}
	}
}
