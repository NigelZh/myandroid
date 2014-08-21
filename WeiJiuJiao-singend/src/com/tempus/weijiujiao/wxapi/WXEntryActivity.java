package com.tempus.weijiujiao.wxapi;

import com.tempus.weijiujiao.Utils.Debug;
import com.tempus.weijiujiao.Utils.ToastUtils;
import com.tempus.weijiujiao.constant.Constant;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constant.WXShare.appid, true);
		api.registerApp(Constant.WXShare.appid);
		if(getIntent()!=null){
			api.handleIntent(getIntent(), this);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		Debug.Out("onreq=" + arg0.transaction);
//		switch (arg0.getType()) {
//		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//			goToGetMsg();		
//			break;
//		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//			goToShowMsg((ShowMessageFromWX.Req) req);
//			break;
//		default:
//			break;
//		}
	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		Debug.Out("onResp=" + arg0.transaction);
		String result = null;
		
		switch (arg0.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "分享成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消分享";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			break;
		default:
			result = "未知错误";
			break;
		}
		ToastUtils.toastMessage(result);
		WXEntryActivity.this.finish();
	}
}
