package com.zyp.thirdloginlib.wechart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.zyp.thirdloginlib.R;
import com.zyp.thirdloginlib.ShareBlock;
import com.zyp.thirdloginlib.common.ShareConstants;
import com.zyp.thirdloginlib.data.ServiceManager;
import com.zyp.thirdloginlib.data.WechatApiService;
import com.zyp.thirdloginlib.data.resultModel.AccessTokenResult;
import com.zyp.thirdloginlib.data.resultModel.WeChartUserInfoResult;
import com.zyp.thirdloginlib.impl.PlatformActionListener;
import com.zyp.thirdloginlib.util.SubscribeUtils;


import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

import static android.content.ContentValues.TAG;

/**
 * Created by echo on 5/19/15.
 */
public class WechatHandlerActivity extends Activity implements IWXAPIEventHandler {
    public static String TAG = "jox";
    private IWXAPI mIWXAPI;

    private PlatformActionListener mPlatformActionListener;

    private static final String API_URL = "https://api.weixin.qq.com";

    /**
     * BaseResp的getType函数获得的返回值，1:第三方授权， 2:分享
     */
    private static final int TYPE_LOGIN = 1;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = WechatHandlerActivity.this;
        mIWXAPI = WechatLoginManager.getIWXAPI();
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(getIntent(), this);
        }
        finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(getIntent(), this);
        }
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }


    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onResp: ");

        mPlatformActionListener = WechatLoginManager
                .getPlatformActionListener();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.d(TAG, "onResp: ERR_OK");
                if (resp.getType() == TYPE_LOGIN) {
                    getAccessToken(resp, mPlatformActionListener);

                } else {
                    Toast.makeText(mContext, mContext.getString(
                            R.string.share_success), Toast.LENGTH_SHORT).show();
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:

                if (resp.getType() == TYPE_LOGIN) {
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener
                                .onCancel();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(
                            R.string.share_cancel), Toast.LENGTH_SHORT).show();
                }

                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                if (resp.getType() == TYPE_LOGIN) {
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener
                                .onError();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(
                            R.string.share_failed), Toast.LENGTH_SHORT).show();
                }

                break;
        }
        finish();
    }

    private void getAccessToken(BaseResp resp, final PlatformActionListener platformActionListener) {
        final String code = ((SendAuth.Resp) resp).code;
        WechatApiService service = ServiceManager.getWeChatApiService();
        Map<String, Object> options = new HashMap<>();
        options.put("appid", ShareBlock.getInstance().getWechatAppId());
        options.put("secret", ShareBlock.getInstance().getWechatSecret());
        options.put("code", code);
        options.put("grant_type", "authorization_code");
        SubscribeUtils.applySchedulers(service.getAccessToken(options))
                .subscribe(new Subscriber<AccessTokenResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getAccessToken onError: "+e.getMessage());
                if (mPlatformActionListener != null) {
                    mPlatformActionListener
                            .onError();
                }
            }

            @Override
            public void onNext(AccessTokenResult accessTokenResult) {
                Log.d(TAG, "getAccessToken : onNext ");
                String accessToken = accessTokenResult.getAccess_token();
                String openid = accessTokenResult.getOpenid();
                getUserInfo(accessToken, openid);
            }
        });
    }

    private void getUserInfo(String accessToken, String openid) {
        Map<String, Object> options = new HashMap<>();
        options.put("access_token",accessToken);
        options.put("openid",openid);
        SubscribeUtils.applySchedulers(ServiceManager.getWeChatApiService().getWechatUserInfo(options))
                .subscribe(new Subscriber<WeChartUserInfoResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getUserInfo onError: "+e.getMessage());
                        if (mPlatformActionListener != null) {
                            mPlatformActionListener
                                    .onError();
                        }
                    }

                    @Override
                    public void onNext(WeChartUserInfoResult userInfoResult) {
                        Log.d(TAG, "getUserInfo onNext: ");
                        if (mPlatformActionListener != null) {
                            mPlatformActionListener
                                    .onComplete(
                                            userInfoResult);
                        }
                    }
                });
    }
}
