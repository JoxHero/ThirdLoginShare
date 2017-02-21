package com.zyp.thirdloginexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.tauth.Tencent;
import com.zyp.thirdloginlib.ShareBlock;
import com.zyp.thirdloginlib.common.ShareConstants;
import com.zyp.thirdloginlib.data.resultModel.AccountResult;
import com.zyp.thirdloginlib.data.resultModel.WeChartUserInfoResult;
import com.zyp.thirdloginlib.impl.ILoginManager;
import com.zyp.thirdloginlib.impl.IShareManager;
import com.zyp.thirdloginlib.impl.PlatformActionListener;
import com.zyp.thirdloginlib.impl.ShareContentText;
import com.zyp.thirdloginlib.impl.ShareContentWebpage;
import com.zyp.thirdloginlib.qq.QQLoginManager;
import com.zyp.thirdloginlib.qq.QQShareManager;
import com.zyp.thirdloginlib.qq.model.QQUser;
import com.zyp.thirdloginlib.sina.WeiboLoginManager;
import com.zyp.thirdloginlib.sina.WeiboShareManager;
import com.zyp.thirdloginlib.sina.model.SinaUser;
import com.zyp.thirdloginlib.wechart.WechatLoginManager;
import com.zyp.thirdloginlib.wechart.WechatShareManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    @Bind(R.id.bt_sina_login)
    Button btSinaLogin;
    @Bind(R.id.bt_sina_share)
    Button btSinaShare;
    @Bind(R.id.bt_qq_login)
    Button btQqLogin;
    @Bind(R.id.bt_qq_share)
    Button btQqShare;
    @Bind(R.id.bt_wechart_login)
    Button btWechartLogin;
    @Bind(R.id.bt_wechart_share)
    Button btWechartShare;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.activity_main)
    ConstraintLayout activityMain;
    private QQLoginManager qqLoginManager;
    private QQShareManager qqShareManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBlock();
    }

    private void initBlock() {
        ShareBlock.getInstance().initWeibo(Constants.SINA_KEY);
        ShareBlock.getInstance().initWeiboRedriectUrl(Constants.SINA_REDIRECT_URL);
        ShareBlock.getInstance().initQQ(Constants.QQ_APP_ID);
        ShareBlock.getInstance().initWechat(Constants.WECHAT_APP_ID, Constants.WECHAT_SECRET);
    }

    @OnClick({R.id.bt_sina_login, R.id.bt_sina_share, R.id.bt_qq_login, R.id.bt_qq_share, R.id.bt_qq_qzone_share,R.id.bt_wechart_login, R.id.bt_wechart_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_sina_login:

                ILoginManager sinaLoginManager = new WeiboLoginManager(this);
                sinaLoginManager.login(new PlatformActionListener() {
                    @Override
                    public void onComplete(AccountResult accountResult) {
                        SinaUser sinaUser = (SinaUser) accountResult;
                        Log.d(TAG, "onComplete: weibo login resutl " + sinaUser.getName());
                        Log.d(TAG,"uid "+sinaUser.getAccessToken().getUid());
                        tvContent.setText("昵称 ：" + sinaUser.getName());
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "onError: weibo login resutl ");
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel: weibo login resutl ");
                    }
                });

                break;
            case R.id.bt_sina_share:
                IShareManager sinaShareManager = new WeiboShareManager(this);
                Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
                sinaShareManager.share(new ShareContentWebpage(getString(R.string.app_name), "我在中量金融操盘，盈利打败98%用户！不服来战~", "https://www.baidu.com", "https://mgw.zlw.com/all/icon/zhongliang_financial_icon.png",bitmap), WeiboShareManager.WEIBO_SHARE_TYPE);
                break;
            case R.id.bt_qq_login:

                qqLoginManager = new QQLoginManager(this);
                qqLoginManager.login(new PlatformActionListener() {
                    @Override
                    public void onComplete(AccountResult accountResult) {
                        Log.d(TAG, "onComplete: qq login resutl ");
                        QQUser qqUser = (QQUser) accountResult;
                        tvContent.setText("昵称 ：" + qqUser.getNickname());
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "onError: qq login resutl ");
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel: qq login resutl ");
                    }
                });
                break;
            case R.id.bt_qq_share:
                qqShareManager = new QQShareManager(this);
                qqShareManager.share(new ShareContentWebpage("英语流利说", "test", "http://www.liulishuo.com",
                        "https://mgw.zlw.com/all/icon/zhongliang_financial_icon.png",null), QQShareManager.QQ_SHARE_TYPE);
                break;
            case R.id.bt_qq_qzone_share:
                qqShareManager = new QQShareManager(this);
                qqShareManager.share(new ShareContentWebpage("英语流利说", "test", "http://www.liulishuo.com",
                        "https://mgw.zlw.com/all/icon/zhongliang_financial_icon.png",null), QQShareManager.QZONE_SHARE_TYPE);
                break;
            case R.id.bt_wechart_login:

                ILoginManager wechatLoginManager = new WechatLoginManager(this);
                wechatLoginManager.login(new PlatformActionListener() {
                    @Override
                    public void onComplete(AccountResult accountResult) {
                        Log.d(TAG, "onComplete: wechat login resutl ");
                        WeChartUserInfoResult weChartUserInfoResult = (WeChartUserInfoResult) accountResult;
                        tvContent.setText("昵称 ：" + weChartUserInfoResult.getNickname());
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "onError: qq login resutl ");
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel: qq login resutl ");
                    }
                });
                break;
            case R.id.bt_wechart_share:
                IShareManager wechtShareManager = new WechatShareManager(this);
                Bitmap image = ((BitmapDrawable)getResources().getDrawable(R.drawable.blank_bg)).getBitmap();
                wechtShareManager.share(new ShareContentWebpage("英语流利说", "test", "http://www.liulishuo.com",
                        null,image), WechatShareManager.WEIXIN_SHARE_TYPE_TALK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqLoginManager.getIuListener());
        }
    }

    /**
     * 截取对象是普通view
     */
    private String getImagePath(View view) {

        String imagePath =
                getPathTemp() + File.separator
                        + System.currentTimeMillis() + ".png";
        try {
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = view.getDrawingCache();
            if (bitmap != null) {
                FileOutputStream out = new FileOutputStream(imagePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            }
        } catch (OutOfMemoryError ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return imagePath;
    }

    private String mPathTemp = "";

    /**
     * 临时文件地址 *
     */
    public String getPathTemp() {
        if (TextUtils.isEmpty(mPathTemp)) {
            mPathTemp = MainActivity.this.getExternalCacheDir() + File.separator + "temp";
            File dir = new File(mPathTemp);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return mPathTemp;
    }
}
