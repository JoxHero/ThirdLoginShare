package com.zyp.thirdloginlib.impl;

import android.graphics.Bitmap;

/**
 * Created by echo on 5/18/15.
 */

public abstract class ShareContent {

    /**
     * 分享的方式
     * @return
     */
    public abstract int getShareWay();

    /**
     * 分享的描述信息
     * @return
     */
    public abstract String getContent();

    /**
     * 分享的标题
     * @return
     */
    public abstract String getTitle();

    /**
     * 获取跳转的链接
     * @return
     */
    public abstract String getURL();

    /**
     * 分享的本地图片路径
     * @return
     */
    public abstract String getImageUrl();

    /**
     * 音频url
     */
    public abstract String getMusicUrl();

    /**
     * 本地图片
     * @return
     */
    public abstract Bitmap getImageBitMap();

}
