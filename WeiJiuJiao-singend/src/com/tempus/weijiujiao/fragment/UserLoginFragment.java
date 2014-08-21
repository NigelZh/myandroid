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
 * 用户登录页面
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
	 * 忘记密码找回密码
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
		aActivity.setTitle("找回密码");
		changeVp(1, lyFindPassword);
	}

	/**
	 * 新用户注册
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
		aActivity.setTitle("新用户注册");
		changeVp(1, lyNewregister);
	}

	/**
	 * 用户设置密码
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
		aActivity.setTitle("用户设置");
		changeVp(2, lySetPassword);
	}

	/**
	 * 点击了登录按钮
	 */
	private void isLoginBtn() {
		String account = etAccount.getText().toString().trim();
		String password = login_password.getText().toString().trim();
		if (account == null || account.equals("")) {
			ToastMessage("请输入用户名");
		} else if (!StringUtils.isPhoneNumber(account)
				&& !StringUtils.isMail(account)) {
			ToastMessage("用户名格式不对");
		} else if (password == null || password.equals("")) {
			ToastMessage("请输入密码");
		} else if (!StringUtils.isNumLet(password)) {
			ToastMessage("密码格式必须为数字和字母");
		} else if (!StringUtils.chackLenth(password)) {
			ToastMessage("密码长度不对");
		} else {
			isLogin(etAccount.getText().toString().trim(), login_password.getText().toString().trim());
		}
	}

	/**
	 * 登录
	 * 
	 * @param account
	 *            账户
	 * @param Password
	 *            密码
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
						Debug.Out("网络异常");
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
	 * 点击了获取验证码
	 * 
	 * @param phoneNum
	 *            手机号码
	 * @param codeType
	 *            请求验证码的类型：0 注册，2 找回密码
	 */
	private void isGetVerificationCode(String phoneNum, int codeType) {
		TimeCount time = new TimeCount(60000, 1000, codeType);// 构造CountDownTimer对象
		time.start();// 开始计时
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
	 *            手机号码
	 * @param code
	 *            用户输入的验证码
	 * @param codeType
	 *            验证验证码的类型
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
	 * 密码验证是的监听器
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
	 * 注册或者找回密码
	 * 
	 * @param phoneNumer
	 *            手机
	 * @param password
	 *            密码
	 * @param type
	 *            重置密码类型 1：注册/REWREGESTER，2：忘记密码找回密码/FINDPASSWORD
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
				ToastMessage("登录成功");
				checkRemenberPassword();
				goBack();
				break;
			case 11:
				ToastMessage("用户名不存在");
				break;
			case 12:
				ToastMessage("密码不匹配");
				break;
			case 13:
				ToastMessage("子账户不能登录");
				break;
			case 14:
				ToastMessage("用户被禁用或被删除");
				break;
			case 20:
				ToastMessage("获取验证码成功");
				break;
			case 21:
				ToastMessage("该手机号码已经被注册");// 一个手机号码不能重复注册
				showDialog();
				break;
			case 22:
				ToastMessage("该手机号码没有注册");// 没有注册的手机号码不能找回密码
				break;
			case 30:
				ToastMessage("验证通过");
				break;
			case 31:
				ToastMessage("验证码失效");
				break;
			case 40:
				ToastMessage("密码设置成功");// 密码设置成功后自动登录
				isLogin(setPasswordPhone, surePassword.getText().toString()
						.trim());
//				跳到登录页面，去登录
//				viewFlipper.setDisplayedChild(0);
				break;
			case 98:
				ToastMessage("服务器异常");
				break;
			case 99:
				ToastMessage("网络异常");
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
		CustomDialog.createDialog(getActivity(), "提示", "该手机号码已经被注册，是否去登录？",
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
		// 7月21日添加了密码设置页面的记住密码
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
	 * onclick监听
	 * tag：login，findPassword，newRegister，userSetPassword：登录，找回密码，新注册，设置密码
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
				ToastMessage("密码不能为空");
			} else if (!StringUtils.chackLenth(surePassword.getText()
					.toString().trim())
					|| !StringUtils.chackLenth(setPassword.getText().toString()
							.trim())) {
				ToastMessage("密码只能设置6-20位");
			} else if (!StringUtils.isNumLet(surePassword.getText().toString()
					.trim())
					|| (!StringUtils.isNumLet(setPassword.getText().toString()
							.trim()))) {
				ToastMessage("密码格式不对");
			} else if (!setPassword.getText().toString().trim()
					.equals(surePassword.getText().toString().trim())) {
				ToastMessage("密码不一致");
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
				ToastMessage("请输入手机号码");
			} else if (!StringUtils.isPhoneNumber(phone)) {
				ToastMessage("手机号码格式错误");
			} else {
				isGetVerificationCode(phone, 0);// 新用户验证类型获取验证码
			}
			break;
		case R.id.btn_user_login_forgot_next:
			if (phone == null || phone.equals("")) {
				ToastMessage("请输入手机号码");
			} else if (!StringUtils.isPhoneNumber(phone)) {
				ToastMessage("手机号码格式错误");
			} else if (verification_code.equals("")
					|| verification_code == null) {
				ToastMessage("验证码为空");
			} else if (!verifyCode.equals(verification_code)) {
				ToastMessage("验证码输入不正确");
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
				ToastMessage("请输入手机号码");
			} else if (!StringUtils.isPhoneNumber(phoneNum)) {
				ToastMessage("手机号码格式错误");
			} else {
				isGetVerificationCode(phoneNum, FINDPASSWORD);
			}
			break;
		case R.id.btn_user_login_forgot_next:
			if (phoneNum == null || phoneNum.equals("")) {
				ToastMessage("请输入手机号码");
			} else if (!StringUtils.isPhoneNumber(phoneNum)) {
				ToastMessage("手机号码格式错误");
			} else if (code == null || code.equals("")) {
				ToastMessage("验证码为空");
			} else if (!verifyCode.equals(code)) {
				ToastMessage("验证码输入不正确");
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
			newRegister();// 新用户注册
			break;
		default:
			break;
		}
	}

	/**
	 * 获取验证码倒计时用 定义一个倒计时的内部类
	 */
	class TimeCount extends CountDownTimer {
		private int code = 10;// 0为注册，2为忘记密码

		public TimeCount(long millisInFuture, long countDownInterval, int code) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
			this.code = code;
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			if (code == 0) {
				get_verification_code.setText("重新验证");
				get_verification_code.setClickable(true);
			} else if (code == 2) {
				get_verification_code_forgot.setText("重新验证");
				get_verification_code_forgot.setClickable(true);
			} else {
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			if (code == 0) {
				get_verification_code.setClickable(false);
				get_verification_code.setText(millisUntilFinished / 1000 + "秒");
			} else if (code == 2) {
				get_verification_code_forgot.setClickable(false);
				get_verification_code_forgot.setText(millisUntilFinished / 1000
						+ "秒");
			} else {
			}
		}
	}

	/**
	 * vp切换
	 * 
	 * @param index
	 *            要添加view的index
	 * @param scendview
	 *            要添加的view
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
	 * 判断是否还有下一页
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
	 * 判断是否还有上一页
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
