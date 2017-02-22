package com.zyp.thirdloginlib.sina;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zyp.thirdloginlib.R;
import com.zyp.thirdloginlib.ShareBlock;
import com.zyp.thirdloginlib.common.ShareConstants;
import com.zyp.thirdloginlib.impl.IShareManager;
import com.zyp.thirdloginlib.impl.ShareContent;
import com.zyp.thirdloginlib.util.BitmapUtil;
import com.zyp.thirdloginlib.util.ShareUtil;

/**
 * Created by echo on 5/18/15.
 */
public class WeiboShareManager implements IShareManager {

    private final String TAG = "WeiboShareManager";
    private static String mSinaAppKey;

    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    private Context mContext;

    public static final int WEIBO_SHARE_TYPE = 0;


    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mSinaAPI;


    public WeiboShareManager(Context context) {
        mContext = context;
        mSinaAppKey = ShareBlock.getInstance().getWeiboAppId();
        if (!TextUtils.isEmpty(mSinaAppKey)) {
            // 创建微博 SDK 接口实例
            mSinaAPI = WeiboShareSDK.createWeiboAPI(context, mSinaAppKey);
            mSinaAPI.registerApp();
        }
    }


    private void shareText(ShareContent shareContent) {

        //初始化微博的分享消息
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
        //初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction("sinatext");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);
    }

    private void sharePicture(ShareContent shareContent) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.imageObject = getImageObj(shareContent.getImageUrl());
        //初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction("sinapic");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);
    }

    /*private void shareWebPage(ShareContent shareContent) {

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
        ImageObject imageObject = new ImageObject();
        //Bitmap bmp = BitmapUtil.scaleCenterCrop(shareContent.getImageBitMap(),116,116);
        Bitmap bmp = shareContent.getImageBitMap();
        Log.d(TAG, "getImageObj: bmp is null : "+(bmp == null));
        imageObject.setImageObject(bmp);
        //weiboMultiMessage.imageObject = getImageObj(shareContent.getImageUrl());
        weiboMultiMessage.imageObject = imageObject;
        weiboMultiMessage.mediaObject = getWebpageObj(shareContent);
        // 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = ShareUtil.buildTransaction("sinawebpage");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);

    }*/

    private void shareWebPage(final ShareContent shareContent) {
        final WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = getTextObj(shareContent.getContent());
        final ImageObject imageObject = new ImageObject();
        Picasso.with(mContext).load(shareContent.getImageUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Log.d(TAG, "onBitmapLoaded: bitmap is null : "+ (bitmap == null));
                        if(bitmap != null){
                            imageObject.setImageObject(BitmapUtil.scaleCenterCrop(bitmap , 116 , 116));
                        }
                        weiboMultiMessage.imageObject = imageObject;
                        weiboMultiMessage.mediaObject = getWebpageObj(shareContent);
                        // 初始化从第三方到微博的消息请求
                        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                        // 用transaction唯一标识一个请求
                        request.transaction = ShareUtil.buildTransaction("sinawebpage");
                        request.multiMessage = weiboMultiMessage;
                       // mSinaAPI.sendRequest((Activity) mContext,request);
                        allInOneShare(mContext, request);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d(TAG, "onBitmapFailed: ");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.d(TAG, "onPrepareLoad: placeHolderDrawable is null : " + (placeHolderDrawable == null));
                    }
                });


    }


    private void shareMusic(ShareContent shareContent) {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.mediaObject = getMusicObj(shareContent);
        //初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = ShareUtil.buildTransaction("sinamusic");
        request.multiMessage = weiboMultiMessage;
        allInOneShare(mContext, request);
    }


    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(String imageUrl) {
        ImageObject imageObject = new ImageObject();
        Bitmap bmp = BitmapFactory.decodeFile(imageUrl);
        Log.d(TAG, "getImageObj: bmp is null : "+(bmp == null));
        imageObject.setImageObject(bmp);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(final ShareContent shareContent) {
        final WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareContent.getTitle();
        mediaObject.description = shareContent.getContent();
        Picasso.with(mContext).load(shareContent.getImageUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Log.d(TAG, "getWebpageObj onBitmapLoaded: bitmap is null : "+ (bitmap == null));
                        if(bitmap != null){
                            Bitmap btp = BitmapUtil.scaleCenterCrop(bitmap , 116 , 116);
                            mediaObject.setThumbImage(btp);
                            mediaObject.actionUrl = shareContent.getURL();
                            mediaObject.defaultText = shareContent.getContent();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d(TAG, "getWebpageObj onBitmapFailed: ");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.d(TAG, "getWebpageObj onPrepareLoad: placeHolderDrawable is null : " + (placeHolderDrawable == null));
                    }
                });

        return mediaObject;
    }


    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj(ShareContent shareContent) {
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = shareContent.getTitle();
        musicObject.description = shareContent.getContent();

        // 设置 Bitmap 类型的图片到视频对象里
        Bitmap bmp = BitmapFactory.decodeFile(shareContent.getImageUrl());
        musicObject.setThumbImage(bmp);
        musicObject.actionUrl = shareContent.getURL();
        musicObject.dataUrl = REDIRECT_URL;
        musicObject.dataHdUrl = REDIRECT_URL;
        musicObject.duration = 10;
        musicObject.defaultText = shareContent.getContent();
        return musicObject;
    }


    private void allInOneShare(final Context context, SendMultiMessageToWeiboRequest request) {

        AuthInfo authInfo = new AuthInfo(context, mSinaAppKey, REDIRECT_URL, SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context.getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }

        boolean issend = mSinaAPI.sendRequest((Activity) context, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                Log.d(TAG, "onWeiboException: ");
                Toast.makeText(context, context.getString(
                        R.string.share_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle bundle) {
                Log.d(TAG, "onComplete: ");
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context, newToken);
                Toast.makeText(context, context.getString(
                        R.string.share_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
                Toast.makeText(context, context.getString(
                        R.string.share_cancel), Toast.LENGTH_SHORT).show();

            }
        });
        Log.d(TAG, "allInOneShare: issend : "+issend);
    }


    @Override
    public void share(ShareContent shareContent, int shareType) {

        Log.d(TAG, "share: mSinaApi is null : " + (mSinaAPI == null));
        if (mSinaAPI == null) {
            return;
        }

        switch (shareContent.getShareWay()) {
            case ShareConstants.SHARE_WAY_TEXT:
                shareText(shareContent);
                break;
            case ShareConstants.SHARE_WAY_PIC:
                sharePicture(shareContent);
                break;
            case ShareConstants.SHARE_WAY_WEBPAGE:
                shareWebPage(shareContent);
                break;
            case ShareConstants.SHARE_WAY_MUSIC:
                shareMusic(shareContent);
                break;
        }
    }

    public IWeiboShareAPI getSinaApi() {
        return mSinaAPI;
    }
}
