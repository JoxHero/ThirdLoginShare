package com.zyp.thirdloginlib.sina;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.zyp.thirdloginlib.ShareBlock;
import com.zyp.thirdloginlib.common.ShareConstants;
import com.zyp.thirdloginlib.impl.ILoginManager;
import com.zyp.thirdloginlib.impl.PlatformActionListener;
import com.zyp.thirdloginlib.sina.model.User;
import com.zyp.thirdloginlib.sina.model.UsersAPI;

import java.util.HashMap;

/**
 * Created by echo on 5/19/15.
 */
public class WeiboLoginManager implements ILoginManager {

    private static final String SCOPE =
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog";

    private Context mContext;

    private static String mSinaAppKey;

    private AuthInfo mAuthInfo = null;

    private UsersAPI userAPI;

    private PlatformActionListener mPlatformActionListener;


    private String mRedirectUrl ;


    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private static SsoHandler mSsoHandler;
    private Gson gson;

    public WeiboLoginManager(Context context) {

        mContext = context;
        mSinaAppKey = ShareBlock.getInstance().getWeiboAppId();
        mRedirectUrl = ShareBlock.getInstance().getRedriectUrl();
        gson = new Gson();

    }


    public static SsoHandler getSsoHandler() {
        return mSsoHandler;
    }

    @Override
    public void login(PlatformActionListener platformActionListener) {
        mPlatformActionListener = platformActionListener;
        AccessTokenKeeper.clear(mContext);
        mAuthInfo = new AuthInfo(mContext, mSinaAppKey, mRedirectUrl, SCOPE);
        mSsoHandler = new SsoHandler((Activity) mContext, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());

    }

    /**
     * * 1. SSO 授权时，需要在 onActivityResult 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非SSO 授权时，当授权结束后，该回调就会被执行
     *
     */
    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            final Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext, accessToken);
                userAPI = new UsersAPI(mContext, mSinaAppKey, accessToken);
                userAPI.show(Long.parseLong(accessToken.getUid()), mListener);
            }else {
                // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
                String code = values.getString("code", "");
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mPlatformActionListener != null) {
                mPlatformActionListener.onError();
            }
        }

        @Override
        public void onCancel() {
            if (mPlatformActionListener != null) {
                mPlatformActionListener.onCancel();
            }
        }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {

                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    HashMap<String, Object> userInfoHashMap
                            = new HashMap<String, Object>();
                    userInfoHashMap.put(ShareConstants.PARAMS_NICK_NAME, user.name);
                    userInfoHashMap.put(ShareConstants.PARAMS_SEX, user.gender);
                    userInfoHashMap.put(ShareConstants.PARAMS_IMAGEURL, user.avatar_large);
                    userInfoHashMap.put(ShareConstants.PARAMS_USERID, user.id);
                    userInfoHashMap.put(ShareConstants.PARAMS_DESCRIPTION,user.description);
                    userInfoHashMap.put(ShareConstants.PARAMS_LOCATION,user.location);
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener.onComplete(userInfoHashMap);
                    }
                }
        }

        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };

}
