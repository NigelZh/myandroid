package com.tempus.weijiujiao.fragment;

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
import com.tempus.weijiujiao.bean.User;
import com.tempus.weijiujiao.view.CustomDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * �û���¼ҳ��
 * 
 * @author lenovo
 * 
 */
public class UserLoginFragment extends Fragment {
	public static final int FINDPASSWORD = 2;
	public static final int REWREGESTER = 1;
	private int setType, currnetIndex = 0;
	private boolean verificationIsOK;
	private LayoutInflater inflater;
	private OnClickListener listener;
	private String frontTitle, frontAndUpTitle, setPasswordPhone,
			verifyCode = null;
	private ViewFlipper viewFlipper = null;
	private DetailActivity aActivity;
	private Button get_verification_code, get_verification_code_forgot;
	private TextView tv_forgot_password, tv_register;
	private EditText login_password, etPhoneNum, etAccount, verification_code,
			etPhoneNumRegister, verification_codeRegister, setPassword,
			surePassword;
	private CheckBox ckRemember_password, ckDisplay_password,ckRememberSetPassword;
	ProgressDialog pDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		initListener();
		aActivity = (DetailActivity) getActivity();
		frontAndUpTitle = aActivity.getActivityTitle();
		viewFlipper = (ViewFlipper) inflater.inflate(
				R.layout.user_login_layout, null);
		initView();
		return viewFlipper;
	}

	private void initView() {
		ckRemember_password = (CheckBox) viewFlipper
				.findViewById(R.id.ck_user_login_Remember_password);
		ckDisplay_password = (CheckBox) viewFlipper
				.findViewById(R.id.ck_user_login_Display_password);
		tv_forgot_password = (TextView) viewFlipper
				.findViewById(R.id.tv_user_login_forgot_password);
		tv_forgot_password.setTag("login");
		tv_forgot_password.setOnClickListener(listener);
		tv_register = (TextView) viewFlipper
				.findViewById(R.id.tv_user_login_register);
		tv_register.setTag("login");
		tv_register.setOnClickListener(listener);
		login_password = (EditText) viewFlipper
				.findViewById(R.id.ed__user_login_password);
		etAccount = (EditText) viewFlipper
				.findViewById(R.id.ed__user_login_account);
		Button btnlogin = (Button) viewFlipper
				.findViewById(R.id.btn_user_login_login_btn);
		btnlogin.setTag("login");
		btnlogin.setOnClickListener(listener);
		ckDisplay_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							login_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						} else {
							login_password
									.setInputType(InputType.TYPE_CLASS_TEXT
											| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						}
					}
				});

		if (MyApplication.getinstance().getSp_setting()
				.getBoolean("autoLogin", false)) {
			final SharedPreferences sp_user = MyApplication.getinstance()
					.getSp_user();
			String id = sp_user.getString("id", null);
			String pw = sp_user.getString("pw", null);
			if (id != null && pw != null) {
				etAccount.setText(id);
				login_password.setText(pw);
				ckRemember_password.setChecked(true);
			}
		}
	}

	/**
	 * ���������һ�����
	 */
	private void findPassword() {
		LinearLayout lyFindPassword = (LinearLayout) inflater.inflate(
				R.layout.user_login_forgot_password_layout, null);
		etPhoneNum = (EditText) lyFindPassword
				.findViewById(R.id.et_user_login_forgot_password_phone);
		get_verification_code_forgot = (Button) lyFindPassword
				.findViewById(R.id.btn_user_login_forgot_get_verification_code);
		verification_code = (EditText) lyFindPassword
				.findViewById(R.id.et_user_login_forgot_verification_code);
		Button btnNext = (Button) lyFindPassword
				.findViewById(R.id.btn_user_login_forgot_next);
		get_verification_code_forgot.setTag("findPassword");
		get_verification_code_forgot.setOnClickListener(listener);
		btnNext.setTag("findPassword");
		btnNext.setOnClickListener(listener);
		aActivity.setTitle("�һ�����");
		changeVp(1, lyFindPassword);
	}

	/**
	 * ���û�ע��
	 */
	private void newRegister() {
		LinearLayout lyNewregister = (LinearLayout) inflater.inflate(
				R.layout.user_login_forgot_password_layout, null);
		Button btnNext = (Button) lyNewregister
				.findViewById(R.id.btn_user_login_forgot_next);
		etPhoneNumRegister = (EditText) lyNewregister
				.findViewById(R.id.et_user_login_forgot_password_phone);
		verification_codeRegister = (EditText) lyNewregister
				.findViewById(R.id.et_user_login_forgot_verification_code);
		get_verification_code = (Button) lyNewregister
				.findViewById(R.id.btn_user_login_forgot_get_verification_code);
		get_verification_code.setTag("newRegister");
		get_verification_code.setOnClickListener(listener);
		btnNext.setTag("newRegister");
		btnNext.setOnClickListener(listener);
		aActivity.setTitle("���û�ע��");
		changeVp(1, lyNewregister);
	}

	/**
	 * �û���������
	 */
	private void userSetPassword(String phone, int type) {
		setPasswordPhone = phone;
		setType = type;
		LinearLayout lySetPassword = (LinearLayout) inflater.inflate(
				R.layout.user_login_password_setup_layout, null);
		setPassword = (EditText) lySetPassword
				.findViewById(R.id.ed_user_login_password_setup_password);
		surePassword = (EditText) lySetPassword
				.findViewById(R.id.ed_user_login_password_setup_sure_password);
		Button btnFinish = (Button) lySetPassword
				.findViewById(R.id.btn_user_login_password_setup_finish);
		btnFinish.setTag("userSetPassword");
		btnFinish.setOnClickListener(listener);
		ckRememberSetPassword = (CheckBox) lySetPassword
				.findViewById(R.id.ck_user_login_password_setup_Remember_password);
		CheckBox ckDisplay_password = (CheckBox) lySetPassword
				.findViewById(R.id.ck_user_login_password_setup_Display_password);
		ckDisplay_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							setPassword
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
							surePassword
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						} else {
							setPassword.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							surePassword.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						}
					}
				});
		frontTitle = aActivity.getActivityTitle();
		aActivity.setTitle("�û�����");
		changeVp(2, lySetPassword);
	}

	/**
	 * ����˵�¼��ť
	 */
	private void isLoginBtn() {
		String account = etAccount.getText().toString().trim();
		String password = login_password.getText().toString().trim();
		if (account == null || account.equals("")) {
			ToastMessage("�������û���");
		} else if (!StringUtils.isPhoneNumber(account)
				&& !StringUtils.isMail(account)) {
			ToastMessage("�û�����ʽ����");
		} else if (password == null || password.equals("")) {
			ToastMessage("����������");
		} else if (!StringUtils.isNumLet(password)) {
			ToastMessage("�����ʽ����Ϊ���ֺ���ĸ");
		} else if (!StringUtils.chackLenth(password)) {
			ToastMessage("���볤�Ȳ���");
		} else {
			isLogin(etAccount.getText().toString().trim(), login_password.getText().toString().trim());
		}
	}

	/**
	 * ��¼
	 * 
	 * @param account
	 *            �˻�
	 * @param Password
	 *            ����
	 */
	private void isLogin(String account, String Password) {
		Service.getInstance(aActivity).requestLogin(account, Password,
				new onResultListener() {
					@Override
					public void onResult(Result result) {
						if (result.getStatucCode() == 0) {
							try {
								ParserResult parserResult = JsonParser
										.parserLogin(result.getJsonBody());
								int code = parserResult.getCode();
								if (code == 0) {
									User user = (User) parserResult.getObj();
									MyApplication.getinstance().setUser(user);
									handler.sendEmptyMessage(10);
								} else if (code == 1) {
									handler.sendEmptyMessage(11);
								} else if (code == 2) {
									handler.sendEmptyMessage(12);
								} else if (code == 4) {
									handler.sendEmptyMessage(13);
								} else if (code == 5) {
									handler.sendEmptyMessage(14);
								} else {
									handler.sendEmptyMessage(98);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
						
						}
					}

					@Override
					public void onNetError(Result result) {
						Debug.Out("�����쳣");
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
	 * ����˻�ȡ��֤��
	 * 
	 * @param phoneNum
	 *            �ֻ�����
	 * @param codeType
	 *            ������֤������ͣ�0 ע�ᣬ2 �һ�����
	 */
	private void isGetVerificationCode(String phoneNum, int codeType) {
		TimeCount time = new TimeCount(60000, 1000, codeType);// ����CountDownTimer����
		time.start();// ��ʼ��ʱ
		Service.getInstance(aActivity).requestCode(phoneNum, codeType,
				new onResultListener() {
					@Override
					public void onResult(Result result) {
						if (result.getStatucCode() == 0) {
							try {
								ParserResult parserResult = JsonParser.parserRandomCode(result.getJsonBody());
								int code = parserResult.getCode();
								if (code == 0) {
									verifyCode = (String) parserResult.getObj();
									Debug.Out(verifyCode);
									verificationIsOK = true;
									handler.sendEmptyMessage(20);
								} else if (code == 1) {
									handler.sendEmptyMessage(21);
								} else if (code == 2) {
									handler.sendEmptyMessage(22);
								} else {
									handler.sendEmptyMessage(98);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onNetError(Result result) {
						handler.sendEmptyMessage(99);
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						
					}
				});
	}

	/**
	 * 
	 * @param phone
	 *            �ֻ�����
	 * @param code
	 *            �û��������֤��
	 * @param codeType
	 *            ��֤��֤�������
	 */
	private void isVerification(String phone, String code, int codeType) {
		Service.getInstance(aActivity).requestVerify(phone, code, codeType,
				new onResultListener() {
					@Override
					public void onResult(Result result) {
						// TODO Auto-generated method stub
						if (result.getStatucCode() == 0) {
							try {
								ParserResult parserResult = JsonParser
										.parserVerifyCode(result.getJsonBody());
								int code = parserResult.getCode();
								if (code == 0) {
									handler.sendEmptyMessage(30);
								} else if (code == 1) {
									handler.sendEmptyMessage(31);
								} else {
									handler.sendEmptyMessage(98);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onNetError(Result result) {
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
	 * ������֤�ǵļ�����
	 */
	private onResultListener passwordSetListener = new onResultListener() {
		@Override
		public void onResult(Result result) {
			if (result.getStatucCode() == 0) {
				try {
					ParserResult parserResult = JsonParser.parserVerifyCode(result.getJsonBody());
					int code = parserResult.getCode();
					if (code == 0) {
						handler.sendEmptyMessage(40);
					} else {
						handler.sendEmptyMessage(98);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onNetError(Result result) {
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
	};

	/**
	 * ע������һ�����
	 * 
	 * @param phoneNumer
	 *            �ֻ�
	 * @param password
	 *            ����
	 * @param type
	 *            ������������ 1��ע��/REWREGESTER��2�����������һ�����/FINDPASSWORD
	 */
	private void isSetPassword(String phoneNumer, String password, int type) {
		if (type == REWREGESTER) {
			Service.getInstance(aActivity).requestRegister(phoneNumer,
					password, passwordSetListener);
		} else {
			Service.getInstance(aActivity).resetUserPassword(phoneNumer,
					password, passwordSetListener);
		} 
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:
				ToastMessage("��¼�ɹ�");
				checkRemenberPassword();
				goBack();
				break;
			case 11:
				ToastMessage("�û���������");
				break;
			case 12:
				ToastMessage("���벻ƥ��");
				break;
			case 13:
				ToastMessage("���˻����ܵ�¼");
				break;
			case 14:
				ToastMessage("�û������û�ɾ��");
				break;
			case 20:
				ToastMessage("��ȡ��֤��ɹ�");
				break;
			case 21:
				ToastMessage("���ֻ������Ѿ���ע��");// һ���ֻ����벻���ظ�ע��
				showDialog();
				break;
			case 22:
				ToastMessage("���ֻ�����û��ע��");// û��ע����ֻ����벻���һ�����
				break;
			case 30:
				ToastMessage("��֤ͨ��");
				break;
			case 31:
				ToastMessage("��֤��ʧЧ");
				break;
			case 40:
				ToastMessage("�������óɹ�");// �������óɹ����Զ���¼
				isLogin(setPasswordPhone, surePassword.getText().toString()
						.trim());
//				������¼ҳ�棬ȥ��¼
//				viewFlipper.setDisplayedChild(0);
				break;
			case 98:
				ToastMessage("�������쳣");
				break;
			case 99:
				ToastMessage("�����쳣");
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

	public void ToastMessage(String message) {
		ToastUtils.toastMessage(message);
	}

	protected void showDialog() {
		// TODO Auto-generated method stub
		CustomDialog.createDialog(getActivity(), "��ʾ", "���ֻ������Ѿ���ע�ᣬ�Ƿ�ȥ��¼��",
				true, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						if (arg1 == DialogInterface.BUTTON_POSITIVE) {
							onKeyBack();
						}
					}
				}).show();
	}

	protected void checkRemenberPassword() {
		SharedPreferences spuser = MyApplication.getinstance().getSp_user();
		SharedPreferences spSetting = MyApplication.getinstance()
				.getSp_setting();
		// 7��21���������������ҳ��ļ�ס����
		if (ckRemember_password != null) {
			if (ckRemember_password.isChecked()) {
				String count = etAccount.getText().toString().trim();
				String pw = login_password.getText().toString().trim();
				spuser.edit().putString("id", count).commit();
				spuser.edit().putString("pw", pw).commit();
				spSetting.edit().putBoolean("autoLogin", true).commit();
			} else {
				MyApplication.getinstance().clearUserSp();
			}
		}
		if (ckRememberSetPassword != null) {
			if (ckRememberSetPassword.isChecked()) {
				String count = setPasswordPhone;
				String pw = surePassword.getText().toString().trim();
				spuser.edit().putString("id", count).commit();
				spuser.edit().putString("pw", pw).commit();
				spSetting.edit().putBoolean("autoLogin", true).commit();
			} else {
				MyApplication.getinstance().clearUserSp();
			}
		}
	}

	private void goBack() {
		aActivity.finish();
	}

	/**
	 * onclick����
	 * tag��login��findPassword��newRegister��userSetPassword����¼���һ����룬��ע�ᣬ��������
	 */
	private void initListener() {
		listener = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if ((arg0.getTag().toString().equals("login"))) {
					isLoginListener(arg0);
				} else if ((arg0.getTag().toString().equals("findPassword"))) {
					isFindPasswordListener(arg0);
				} else if ((arg0.getTag().toString().equals("newRegister"))) {
					isNewRegisterListener(arg0);
				} else if ((arg0.getTag().toString().equals("userSetPassword"))) {
					isUserSertPasswordListener(arg0);
				} else {
				}
			}
		};
	}

	protected void isUserSertPasswordListener(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_user_login_password_setup_finish:
			if (surePassword.getText().toString().trim().equals("")
					|| surePassword.getText().toString().trim() == null
					|| setPassword.getText().toString().trim().equals("")
					|| setPassword.getText().toString().trim() == null) {
				ToastMessage("���벻��Ϊ��");
			} else if (!StringUtils.chackLenth(surePassword.getText()
					.toString().trim())
					|| !StringUtils.chackLenth(setPassword.getText().toString()
							.trim())) {
				ToastMessage("����ֻ������6-20λ");
			} else if (!StringUtils.isNumLet(surePassword.getText().toString()
					.trim())
					|| (!StringUtils.isNumLet(setPassword.getText().toString()
							.trim()))) {
				ToastMessage("�����ʽ����");
			} else if (!setPassword.getText().toString().trim()
					.equals(surePassword.getText().toString().trim())) {
				ToastMessage("���벻һ��");
			} else if (setPasswordPhone != null && setType == REWREGESTER) {
				isSetPassword(setPasswordPhone, surePassword.getText()
						.toString().trim(), REWREGESTER);
			} else if (setPasswordPhone != null && setType == FINDPASSWORD) {
				isSetPassword(setPasswordPhone, surePassword.getText()
						.toString().trim(), FINDPASSWORD);
			} else {
			}
			break;
		default:
			break;
		}
	}

	protected void isNewRegisterListener(View arg0) {
		String phone = etPhoneNumRegister.getText().toString().trim();
		String verification_code = verification_codeRegister.getText()
				.toString().trim();
		switch (arg0.getId()) {
		case R.id.btn_user_login_forgot_get_verification_code:
			if (phone == null || phone.equals("")) {
				ToastMessage("�������ֻ�����");
			} else if (!StringUtils.isPhoneNumber(phone)) {
				ToastMessage("�ֻ������ʽ����");
			} else {
				isGetVerificationCode(phone, 0);// ���û���֤���ͻ�ȡ��֤��
			}
			break;
		case R.id.btn_user_login_forgot_next:
			if (phone == null || phone.equals("")) {
				ToastMessage("�������ֻ�����");
			} else if (!StringUtils.isPhoneNumber(phone)) {
				ToastMessage("�ֻ������ʽ����");
			} else if (verification_code.equals("")
					|| verification_code == null) {
				ToastMessage("��֤��Ϊ��");
			} else if (!verifyCode.equals(verification_code)) {
				ToastMessage("��֤�����벻��ȷ");
			} else {
				isVerification(phone, verification_code, 0);
				if (verificationIsOK) {
					userSetPassword(phone, REWREGESTER);
					verificationIsOK = false;
				}
			}
			break;
		default:
			break;
		}
	}

	protected void isFindPasswordListener(View arg0) {
		String phoneNum = etPhoneNum.getText().toString().trim();
		String code = verification_code.getText().toString().trim();
		switch (arg0.getId()) {
		case R.id.btn_user_login_forgot_get_verification_code:
			if (phoneNum == null || phoneNum.equals("")) {
				ToastMessage("�������ֻ�����");
			} else if (!StringUtils.isPhoneNumber(phoneNum)) {
				ToastMessage("�ֻ������ʽ����");
			} else {
				isGetVerificationCode(phoneNum, FINDPASSWORD);
			}
			break;
		case R.id.btn_user_login_forgot_next:
			if (phoneNum == null || phoneNum.equals("")) {
				ToastMessage("�������ֻ�����");
			} else if (!StringUtils.isPhoneNumber(phoneNum)) {
				ToastMessage("�ֻ������ʽ����");
			} else if (code == null || code.equals("")) {
				ToastMessage("��֤��Ϊ��");
			} else if (!verifyCode.equals(code)) {
				ToastMessage("��֤�����벻��ȷ");
			} else {
				isVerification(phoneNum, code, FINDPASSWORD);
				if (verificationIsOK) {
					userSetPassword(phoneNum, FINDPASSWORD);
					verificationIsOK = false;
				}
			}
			break;
		default:
			break;
		}
	}

	protected void isLoginListener(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_user_login_login_btn:
			isLoginBtn();
			break;
		case R.id.tv_user_login_forgot_password:
			findPassword();
			break;
		case R.id.tv_user_login_register:
			newRegister();// ���û�ע��
			break;
		default:
			break;
		}
	}

	/**
	 * ��ȡ��֤�뵹��ʱ�� ����һ������ʱ���ڲ���
	 */
	class TimeCount extends CountDownTimer {
		private int code = 10;// 0Ϊע�ᣬ2Ϊ��������

		public TimeCount(long millisInFuture, long countDownInterval, int code) {
			super(millisInFuture, countDownInterval);// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
			this.code = code;
		}

		@Override
		public void onFinish() {// ��ʱ���ʱ����
			if (code == 0) {
				get_verification_code.setText("������֤");
				get_verification_code.setClickable(true);
			} else if (code == 2) {
				get_verification_code_forgot.setText("������֤");
				get_verification_code_forgot.setClickable(true);
			} else {
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {// ��ʱ������ʾ
			if (code == 0) {
				get_verification_code.setClickable(false);
				get_verification_code.setText(millisUntilFinished / 1000 + "��");
			} else if (code == 2) {
				get_verification_code_forgot.setClickable(false);
				get_verification_code_forgot.setText(millisUntilFinished / 1000
						+ "��");
			} else {
			}
		}
	}

	/**
	 * vp�л�
	 * 
	 * @param index
	 *            Ҫ���view��index
	 * @param scendview
	 *            Ҫ��ӵ�view
	 */
	private void changeVp(int index, View scendview) {
		// TODO Auto-generated method stub
		int count = viewFlipper.getChildCount();
		while (count > index) {
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
