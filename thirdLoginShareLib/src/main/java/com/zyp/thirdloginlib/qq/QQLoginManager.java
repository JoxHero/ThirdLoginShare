package com.zyp.thirdloginlib.qq;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zyp.thirdloginlib.ShareBlock;
import com.zyp.thirdloginlib.common.ShareConstants;
import com.zyp.thirdloginlib.impl.ILoginManager;
import com.zyp.thirdloginlib.impl.PlatformActionListener;
import com.zyp.thirdloginlib.qq.model.QQUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectStreamException;
import java.util.HashMap;

/**
 * Created by echo on 5/19/15.
 */
public class QQLoginManager implements ILoginManager {

    private final String TAG = "QQLoginManager";
    private Context mContext;

    private String mAppId;

    private Tencent mTencent;

    protected PlatformActionListener mPlatformActionListener;

    public QQLoginManager(Context context) {
        mContext = context;
        mAppId = ShareBlock.getInstance().getQQAppId();
        if (!TextUtils.isEmpty(mAppId)) {
            mTencent = Tencent.createInstance(mAppId, context);
        }
    }

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void login(PlatformActionListener platformActionListener) {

        if (mTencent == null) {
            return;
        }

        if (!mTencent.isSessionValid()) {

            mPlatformActionListener = platformActionListener;
            mTencent.login((Activity) mContext, "all", iUiListener);

        } else {
            mTencent.logout(mContext);
        }
    }

    public IUiListener getIuListener() {
        return iUiListener;
    }

    public Tencent getTencent() {
        return mTencent;
    }

    public IUiListener iUiListener = new IUiListener() {
        @Override
        public void onComplete(Object object) {
            JSONObject jsonObject = (JSONObject) object;
            initOpenidAndToken(jsonObject);
            UserInfo info = new UserInfo(mContext, mTencent.getQQToken());
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object object) {
                    try {
                        JSONObject jsonObject = (JSONObject) object;
                        Log.d(TAG, "onComplete: " + object.toString());
                       /* HashMap<String, Object> userInfoHashMap
                                = new HashMap<String, Object>();
                        userInfoHashMap.put(ShareConstants.PARAMS_NICK_NAME,
                                jsonObject.getString("nickname"));
                        userInfoHashMap.put(ShareConstants.PARAMS_SEX,
                                jsonObject.getString("gender"));
                        userInfoHashMap.put(ShareConstants.PARAMS_IMAGEURL,
                                jsonObject.getString("figureurl_qq_2"));
                        userInfoHashMap
                                .put(ShareConstants.PARAMS_USERID, mTencent.getOpenId());*/
                        Gson gson = new Gson();
                        QQUser qqUser = gson.fromJson(jsonObject.toString(), QQUser.class);
                        if (mPlatformActionListener != null) {
                            if (qqUser != null) {
                                mPlatformActionListener
                                        .onComplete(qqUser);
                            } else {
                                Log.d(TAG, "QQUser is null!");
                                mPlatformActionListener.onError();
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        if (mPlatformActionListener != null) {
                            mPlatformActionListener
                                    .onError();
                        }
                    }


                }

                @Override
                public void onError(UiError uiError) {
                    Log.d(TAG, "mTencent.login onError: " + uiError.errorMessage);
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener
                                .onError();
                    }
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "onError: ");
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener
                                .onCancel();
                    }
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            Log.d(TAG, "onError: ");
            if (mPlatformActionListener != null) {
                mPlatformActionListener
                        .onError();
            }
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onError: ");
            if (mPlatformActionListener != null) {
                mPlatformActionListener
                        .onCancel();
            }
        }
    };
}


