package com.ccd.hehuoren.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.util.L;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity222222";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, App.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		try {
			L.e(TAG, resp.errCode);
			L.e(TAG, resp.errStr);
		}catch (Exception e){
			e.printStackTrace();
		}


		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode==0){//支付成功

				setResult(Activity.RESULT_OK);
			}else {//支付失败
				setResult(Activity.RESULT_CANCELED);

			}
			Intent intent = new Intent();
			intent.setAction("WEI_XIN_PAY_BROADCAST");
			intent.putExtra("success",resp.errCode==0);
			sendBroadcast(intent);
			finish();
		}
	}
}