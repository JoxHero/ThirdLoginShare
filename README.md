 
##一.简介
  ThirdLoginShare这个库是基于[ShareLoginLib](https://github.com/lingochamp/ShareLoginLib) 上修改的，因为ShareLoginLib 里面用的三方jar比较老，并且用到的三方库版本也较老。
  
####1.初始化申请的第三方key
       ShareBlock.getInstance().initShare(wechatAppid, weiboId, qqId,wechatSecret);
       
####2.初始化微博回调地址
       ShareBlock.getInstance().initWeiboRedriectUrl(weiboRedriectUrl);
####3.分享到微信
       IShareManager iShareManager = new WechatShareManager(context);
       iShareManager.share(new ShareContentWebpage("title", "content", "dataUrl",
       "imageUrl",WechatShareManager.WEIXIN_SHARE_TYPE_TALK);
  
####4.微信登录

                ILoginManager iLoginManager = new WechatLoginManager(MainActivity.this);
                iLoginManager.login(new PlatformActionListener() {
                    @Override
                    public void onComplete(HashMap<String, Object> userInfo) {
                        //TODO
                    }

                    @Override
                    public void onError() {
                        //TODO
                    }

                    @Override
                    public void onCancel() {
                        //TODO
                    }
                });
		
       
##二.gradle配置    
     allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
  
     dependencies {
	        compile 'com.github.JoxHero:ThirdLoginShare:v1.0'
	}
  
