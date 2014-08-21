package com.tempus.weijiujiao.fragment;

import org.json.JSONException;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tempus.weijiujiao.DetailActivity;
import com.tempus.weijiujiao.MyApplication;
import com.tempus.weijiujiao.R;
import com.tempus.weijiujiao.HTTP.ParserResult;
import com.tempus.weijiujiao.HTTP.Result;
import com.tempus.weijiujiao.HTTP.Service;
import com.tempus.weijiujiao.HTTP.onResultListener;
import com.tempus.weijiujiao.Utils.BitmapUtils;
import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.JsonParser;
import com.tempus.weijiujiao.Utils.StringUtils;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.adapter.UserInforListViewAdapter;
import com.tempus.weijiujiao.bean.User;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

/**
 * �û���Ϣ
 * 
 * @author lenovo
 * 
 */
public class UserInforFragment extends Fragment {
	private int currnetIndex = 0;
	private LayoutInflater inflater;
	private ListView lv_setup;
	private String frontTitle, frontAndUpTitle = null;
	private ViewFlipper viewFlipper = null;
	private DetailActivity aActivity;
	private User user;
	private LinearLayout userLinearLayout;
	private EditText ed_old, ed_new, ed_checkNew;
	private TextView nickName, gradevin_num, winecellar_num;
	private ImageView userPic;
	private Button btn_confirm, userQuit;
	private String newstr = null;
	private UserInforListViewAdapter userInforAdapte;
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		aActivity = (DetailActivity) getActivity();
		frontAndUpTitle = aActivity.getActivityTitle();
		viewFlipper = (ViewFlipper) inflater.inflate(
				R.layout.user_infor_layout, null);
		initView();
		return viewFlipper;
	}

	private void initView() {
		getUserinfor();
		userInforAdapte = new UserInforListViewAdapter(inflater, user);
		nickName = (TextView) viewFlipper
				.findViewById(R.id.tv_user_infor_user_nickname);
		gradevin_num = (TextView) viewFlipper
				.findViewById(R.id.tv_user_infor_user__gradevin_num);
		if (!StringUtils.isNull(user.getGravidenCount() + "")) {
			gradevin_num.setText(user.getGravidenCount() + "");
		}
		winecellar_num = (TextView) viewFlipper
				.findViewById(R.id.tv_user_infor_user__winecellar_num);
		if (!StringUtils.isNull(user.getWinecallerCount() + "")) {
			winecellar_num.setText(user.getWinecallerCount() + "");
		}
		userPic = (ImageView) viewFlipper
				.findViewById(R.id.iv_user_infor_user_pic);
		if (user.getUserImagerURL() != null
				&& !user.getUserImagerURL().equals("")) {
			MyApplication
					.getinstance()
					.getImageLoader()
					.loadImage(user.getUserImagerURL(),
							new SimpleImageLoadingListener() {

								@SuppressWarnings("deprecation")
								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									userPic.setBackgroundDrawable(new BitmapDrawable(
											arg2));
								}
							});
		}
		userLinearLayout = (LinearLayout) viewFlipper
				.findViewById(R.id.ll_user_infor_changerpic);
		userLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				UpdateDeviceImage();
			}
		});
		nickName.setText(user.getUserName());
		lv_setup = (ListView) viewFlipper.findViewById(R.id.lv_user_infor);
		lv_setup.setAdapter(userInforAdapte);
		lv_setup.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int positon, long arg3) {
				addScenndView(arg1, positon);
			}
		});
		userQuit = (Button) viewFlipper
				.findViewById(R.id.btn_user_infor_send_finish);
		userQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				user.setLogin(false);
				MyApplication.getinstance().setUser(user);
				aActivity.finish();
			}
		});
	}

	private void addScenndView(View view, int position) {
		String sValue = null;
		TextView value = (TextView) view
				.findViewById(R.id.tv_gradevin_infor_list_otheritem_num);
		sValue = value.getText().toString();
		switch (position) {
		case 1:
			textChanged(sValue, position);
			aActivity.setTitle("�����޸�");
			break;
		case 2:
			textChanged(sValue, position);
			aActivity.setTitle("�ǳ��޸�");
			break;
		case 3:
			passwordChanger(view);
			aActivity.setTitle("�����޸�");
			break;
		case 4:
			textChanged(sValue, position);
			aActivity.setTitle("��ַ�޸�");
			break;
		default:
			break;
		}
		frontTitle = aActivity.getActivityTitle();
	}

	/**
	 * �����޸�ҳ��
	 */
	private void passwordChanger(View view) {
		LinearLayout llChangerPassword = (LinearLayout) inflater.inflate(
				R.layout.user_login_changer_password_layout, null);
		ed_old = (EditText) llChangerPassword
				.findViewById(R.id.gradevin_manager_old_ed);
		ed_new = (EditText) llChangerPassword
				.findViewById(R.id.gradevin_manager_new_ed);
		ed_checkNew = (EditText) llChangerPassword
				.findViewById(R.id.gradevin_manager_check_ed);
		btn_confirm = (Button) llChangerPassword
				.findViewById(R.id.gradevin_manager_comfirm_btn);
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkAndSendRequest();
			}
		});
		changeVp(1, llChangerPassword);
	}

	protected void checkAndSendRequest() {
		String oldWord = ed_old.getText().toString();
		String newWord = ed_new.getText().toString();
		String checkNew = ed_checkNew.getText().toString();
		if (oldWord == null | oldWord.equals("")) {
			ToastMessage("ԭʼ���벻��Ϊ��");
			return;
		}
		if (newWord == null | newWord.equals("")) {
			ToastMessage("������������");
			return;
		}
		if (checkNew == null | checkNew.equals("")) {
			ToastMessage("������ȷ������");
			return;
		}
		if (!StringUtils.chackLenth(checkNew)
				|| !StringUtils.chackLenth(newWord)) {
			ToastMessage("���볤�Ȳ����Ϲ���ӦΪ6-20λ��");
			return;
		}
		if (!StringUtils.isNumLet(checkNew) || !StringUtils.isNumLet(newWord)) {
			ToastMessage("�����ʽ���ԣ���ֻ��Ϊ���ֺ���ĸ��");
			return;
		}
		if (!checkNew.equals(newWord)) {
			ToastMessage("�������벻һ�£���ȷ�Ϻ����ԣ�");
			return;
		}
		if (checkNew.equals(oldWord)) {
			ToastMessage("������������벻����ͬ��");
			return;
		}
		resetPassword(oldWord, newWord);
	}

	/**
	 * ���������û�����
	 * 
	 * @param oldWord
	 * 
	 * @param newWord
	 */
	private void resetPassword(String oldWord, String newWord) {
		Service.getInstance(aActivity).resetUserPassword(user.getUserNumber(),
				oldWord, newWord, new onResultListener() {

					@Override
					public void onResult(Result result) {
						if (result.getStatucCode() == 0) {
							try {
								ParserResult parserResult = JsonParser
										.parserVerifyCode(result.getJsonBody());
								int code = parserResult.getCode();
								if (code == 0) {
									Debug.Out("�������ý��ͨ����" + code);
									handler.sendEmptyMessage(20);
								} else if (code == 1) {
									Debug.Out("�û����벻��ȷ��" + code);
									handler.sendEmptyMessage(21);
								} else if (code == 2) {
									Debug.Out("�û������ڣ�" + code);
									handler.sendEmptyMessage(22);
								} else {
									Debug.Out("�������쳣��" + code);
									handler.sendEmptyMessage(98);
								}
							} catch (JSONException e) {
								Debug.Out("�����쳣" + result);
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onNetError(Result result) {
						Debug.Out("�����쳣" + result);
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
					}
				});
	}

	/**
	 * �ǳơ��ֻ���������޸�ҳ��
	 */
	private void textChanged(String str, final int positon) {
		newstr = str;
		LinearLayout llChangerStr = (LinearLayout) inflater.inflate(
				R.layout.user_login_changer_infro_layout, null);
		final EditText etv = (EditText) llChangerStr
				.findViewById(R.id.ed_user_login_changer_infro);
		etv.setText(str);
		Button bt = (Button) llChangerStr
				.findViewById(R.id.btn_user_login_changer_infro);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				newstr = etv.getText().toString().trim();
				if (newstr != null || !newstr.equals("")) {
					if (checkString(newstr, positon)) {
						changerString(newstr, positon);
					} else {
						massageType(positon);
					}
				} else {
					ToastMessage("��������Ϣ");
				}
			}
		});
		changeVp(1, llChangerStr);
	}

	/**
	 * �޸��û���Ϣ����ʾ������Ϣ
	 * 
	 * @param positon
	 */
	protected void massageType(int positon) {
		switch (positon) {
		case 1:
			ToastMessage("�����ʽ����");
			break;
		default:
			break;
		}
	}

	/**
	 * �����������ַ����������ȷ��
	 * 
	 * @param newstr
	 *            ���ַ���
	 * @param positon
	 *            adapt�����λ��
	 * @return true:�����ȷ
	 */
	protected boolean checkString(String newstr, int positon) {
		boolean flag = false;
		switch (positon) {
		case 1:
			if (StringUtils.isMail(newstr)) {
				flag = true;
			}
			break;
		case 2:
			flag = true;
			break;
		case 4:
			flag = true;
			break;
		default:
			break;
		}
		return flag;
	}

	/**
	 * 
	 * @param newString
	 *            ���ĺ���û���Ϣ
	 * @param positon
	 *            ���ĵ�λ�ã�1�����䣬2���ǳ�,4����ַ��(��ӿ�ֱ����Ҫת��0�����ǳƣ�1�����ֻ����룬2���������ַ��3���µ�ַ)
	 */
	protected void changerString(String newString, final int positon) {
		int newPositon = -1;
		switch (positon) {
		case 1:
			newPositon = 2;
			break;
		case 2:
			newPositon = 0;
			break;
		case 4:
			newPositon = 3;
			break;
		default:
			break;
		}
		if (newPositon != -1) {
			Service.getInstance(aActivity).userUpdate(user.getUserID(),
					newPositon, newstr, new onResultListener() {
						Message msg = new Message();

						@Override
						public void onResult(Result result) {
							if (result.getStatucCode() == 0) {
								try {
									ParserResult parserResult = JsonParser
											.parserInfoUpdate(result
													.getJsonBody());
									int code = parserResult.getCode();
									if (code == 0) {
										Debug.Out("���³ɹ���" + code);
										msg.what = 10;
										msg.arg1 = positon;
										msg.obj = newstr;
										handler.sendMessage(msg);
									} else {
										Debug.Out("����������" + result);
										handler.sendEmptyMessage(98);
									}
								} catch (JSONException e) {
									Debug.Out("�����쳣");
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onNetError(Result result) {
							Debug.Out("�������쳣" + result);
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
						}
					});
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:
				ToastMessage("���³ɹ�");
				onKeyBack();
				break;
			case 20:
				ToastMessage("�������óɹ�");
				onKeyBack();
				break;
			case 21:
				ToastMessage("�û�ԭ���벻��ȷ");
				break;
			case 22:
				ToastMessage("�û�������");
				break;
			case 30:
				ToastMessage("ͼƬ�ϴ��ɹ�");
				updateUserImage(msg.obj.toString());
				break;
			case 31:
				ToastMessage("ͼƬ�ϴ�ʧ��");
				break;
			case 98:
				ToastMessage("�������쳣");
				break;
			case 99:
				ToastMessage("�����쳣");
				break;
			case 999:
				if(pDialog==null){
					pDialog=	CustomDialog.createProgressDialog(getActivity());
				}
				pDialog.show();
				break;
			case 998:
				CustomDialog.hideProgressDialog(pDialog);
				break;
			default:
				break;
			}
			if (msg.obj != null && msg.what == 10) {
				switch (msg.arg1) {
				case 1:
					user.setUserEmail(msg.obj.toString());
					userInforAdapte.notifyDataSetChanged();
					break;
				case 2:
					user.setUserName(msg.obj.toString());
					nickName.setText(user.getUserName());
					userInforAdapte.notifyDataSetChanged();
					break;
				case 4:
					user.setUserAddress(msg.obj.toString());
					userInforAdapte.notifyDataSetChanged();
				default:
					break;
				}
			}
		}
	};

	public void ToastMessage(String message) {
		ToastUtils.toastMessage(message);
	}

	protected void updateUserImage(String imageUrl) {
		if (userPic != null) {
			user.setUserImagerURL(imageUrl);
			MyApplication
					.getinstance()
					.getImageLoader()
					.loadImage(user.getUserImagerURL(),
							new SimpleImageLoadingListener() {

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									userPic.setBackgroundDrawable(new BitmapDrawable(arg2));
								}
							});
		}
	}

	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @author lenovo
	 * 
	 */
	private void getUserinfor() {
		user = ((MyApplication) aActivity.getApplication()).getUser();
	}

	protected void UpdateDeviceImage() {
		// TODO Auto-generated method stub
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		innerIntent.putExtra("crop", "true");
		innerIntent.putExtra("aspectX", 1);
		innerIntent.putExtra("aspectY", 1);
		innerIntent.setType("image/*");
		innerIntent.putExtra("outputX", 200);
		innerIntent.putExtra("outputY", 200);
		startActivityForResult(innerIntent, 200);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode == 200) {
			Bitmap bmap = data.getParcelableExtra("data");
			if (bmap != null) {
				requestForUpdateImage(bmap);
			}
		}
	}

	private void requestForUpdateImage(Bitmap bitmap) {
		Service.getInstance(aActivity).uploadUserImage(
				MyApplication.getinstance().getUser().getUserID(),
				BitmapUtils.Bitmap2Bytes(bitmap), new onResultListener() {
					@Override
					public void onResult(Result result) {
						ParserResult parserResult;
						try {
							parserResult = JsonParser.parserUploadImage(result
									.getJsonBody());
							int code = parserResult.getCode();
							if (code == 0) {
								Debug.Out("ͼƬ�ϴ��ɹ�" + code);
								Message msg = new Message();
								msg.what = 30;
								msg.obj = parserResult.getObj().toString();
								handler.sendMessage(msg);
							} else {
								Debug.Out("ͼƬ�ϴ�ʧ��" + code);
								handler.sendEmptyMessage(31);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onNetError(Result result) {
						// TODO Auto-generated method stub
						Debug.Out(result.getErrorInfo());
						Debug.Out("�����쳣" + result);
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
					}
				});
	}

	/**
	 * vp�л�
	 * 
	 * @param index
	 *            Ҫ����view��index
	 * @param scendview
	 *            Ҫ���ӵ�view
	 */
	private void changeVp(int index, View scendview) {
		// TODO Auto-generated method stub
		int count = viewFlipper.getChildCount();
		while (count > index) {
			// ��֮ǰ��viewɾ����
			viewFlipper.removeViewAt(count - 1);
			count = viewFlipper.getChildCount();
		}
		viewFlipper.addView(scendview);
		showViewFillperNext();
	}

	/**
	 * �ж��Ƿ�����һҳ
	 * */
	private boolean showViewFillperNext() {
		// TODO Auto-generated method stub
		if (currnetIndex < viewFlipper.getChildCount() - 1) {
			viewFlipper.clearAnimation();
//			viewFlipper.setInAnimation(aActivity, R.anim.in_right);
//			viewFlipper.setOutAnimation(aActivity, R.anim.out_left);
			viewFlipper.showNext();
			currnetIndex++;
			return true;
		}
		return false;
	}

	/**
	 * �ж��Ƿ�����һҳ
	 * */
	private boolean showViewFillperPerious() {
		// TODO Auto-generated method stub
		if (currnetIndex > 0) {
			viewFlipper.clearAnimation();
//			viewFlipper.setInAnimation(aActivity, R.anim.in_left);
//			viewFlipper.setOutAnimation(aActivity, R.anim.out_right);
			viewFlipper.showPrevious();
			currnetIndex--;
			return true;
		}
		return false;
	}

	public boolean onKeyBack() {
		if (showViewFillperPerious()) {
			if (currnetIndex > 0) {
				aActivity.setTitle(frontTitle);
			} else {
				aActivity.setTitle(frontAndUpTitle);
			}
			return true;
		} else {
			return false;
		}
	}

}