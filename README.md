## 前言：
* 大多数App都支持三方登录（微博、QQ、微信等等），集成这一功能的时候有两种方式：
  * 到各个三方平台，下载sdk去集成（这种方式比较麻烦，因为要了解各个平台的使用方法，读Api）
  * 使用三方集成平台，比如友盟等（这种方式集成方便，比较简单）

​      但是有一些项目管理者总是以项目安全的说法，不让去使用友盟（个人感觉安全倒是没有什么，主要就是因为依赖友盟，可能如果友盟出bug，这样修改起来比较不方便）*ps ： 不让用就不让用吧，不让用友盟，那就用三方平台自己家的呗，麻烦是麻烦，那就集成一个呗，虽然集成更麻烦，但是以后使用的时候就会轻松许多 after ： 集成之后使用起来确实简单许多，甚至比集成友盟简单许多.*

* ThirdLoginShare这个库是基于[ShareLoginLib](https://github.com/lingochamp/ShareLoginLib) 上修改的，因为ShareLoginLib 里面用的三方jar比较老，并且用到的三方库版本也较老，所以我在这基础上修改了一下库，并且修改了对应的代码。
* ThirdLoginShare这个库只集成了（新浪微博、QQ、微信）可能之后还会集成其它的，以后的事以后再说吧


******

## 使用：

### Gradle配置

```
 allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
  
 dependencies {
        compile 'com.github.JoxHero:ThirdLoginShare:v1.0'
}
```

 [具体版本看这个地方](https://github.com/JoxHero/ThirdLoginShare/releases)

### 代码使用

### **sina** example：

#### 1.在AndroidManifest中添加：

```java
        <activity
            android:name="com.sina.weibo.sdk.component.WeiboSdkBrowser"
            android:configChanges="keyboardHidden|orientation"
            android:exported="false"
            android:windowSoftInputMode="adjustResize" />
        <activity android:name=".view.me.view.coupon.CouponActivity"/>
```

####2.初始化申请的第三方key

       ShareBlock.getInstance().initShare(wechatAppid, weiboId, qqId,wechatSecret);

####3.初始化微博回调地址
       ShareBlock.getInstance().initWeiboRedriectUrl(weiboRedriectUrl);
####4.sina分享
       IShareManager iShareManager = new WeiboShareManager(context);
       iShareManager.share(new ShareContentWebpage("title", "content", "dataUrl",
       "imageUrl",WechatShareManager.WEIXIN_SHARE_TYPE_TALK);

####5.sina登录

                ILoginManager iLoginManager = new WechatLoginManager(MainActivity.this);
                iLoginManager.login(new PlatformActionListener() {
                    @Override
                    public void onComplete(AccountResult accountResult) {
                        SinaUser sinaUser = (SinaUser) accountResult;
                        ......
                    }
    
                    @Override
                    public void onError() {}
    
                    @Override
                    public void onCancel() {}
                });

#### 6.在onActivityResult方法在添加回调

```java
 mSsoHandler = WeiboLoginManager.getSsoHandler();
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
```

### QQ example

#### 1.在AndroidManifest中添加：

```java
     <activity
            android:name="com.tencent.tauth.AuthActivity"
            android:launchMode="singleTask"
            android:noHistory="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="tencent********" />
            </intent-filter>
        </activity>
```

#### 2.初始化申请的第三方key

```
ShareBlock.getInstance().initQQ(Constants.LoginInfo.QQ_APP_ID);
```

#### 3.登录 

```
qqLoginManager = new QQLoginManager(this);
                qqLoginManager.login(new PlatformActionListener() {
                    @Override
                    public void onComplete(AccountResult accountResult) {
                        QQUser qqUser = (QQUser) accountResult;
                        ......
                    }

                    @Override
                    public void onError() {}

                    @Override
                    public void onCancel() {}
                });
```

#### 4.分享 

​     登录分享和微博差不多，分享需要调用 QQShareManager

#### 5.在onActivityResult方法在添加回调

```
if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data,qqLoginManager.getIuListener());
        }
```



### weChart example 

#### 1. 在根目录建名为wxapi的包，在这个包里新建一个名为WXEntryActivity，继承WechatHandlerActivity

#### 2.在AndroidManifest中添加：

```java
     <activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:screenOrientation="portrait" />
```

#### 2.初始化申请的第三方key

```java
ShareBlock.getInstance().initWechat(Constants.LoginInfo.WECHAT_APP_ID,Constants.LoginInfo.WECHAT_SECRET);
```

#### 3.登录

```java
WechatLoginManager wechatLoginManager = new WechatLoginManager(this);
                wechatLoginManager.login(new PlatformActionListener() {
                    @Override
                    public void onComplete(AccountResult accountResult) {
                        WeChartUserInfoResult weChartUserInfoResult = (WeChartUserInfoResult) accountResult;
                        .......
                    }

                    @Override
                    public void onError() {}

                    @Override
                    public void onCancel() {}
                });
```

#### 4.分享

使用方法和微博差不多，调用WechatShareManager

