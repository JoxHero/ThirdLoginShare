package com.zyp.thirdloginlib.impl;


import android.graphics.Bitmap;

import com.zyp.thirdloginlib.common.ShareConstants;

/**
 * Created by echo on 5/18/15.
 * 分享网页模式
 */
public class ShareContentWebpage extends ShareContent {

    private String title;

    private String content;

    private String url;

    private String imageUrl;

    private Bitmap imageBitMap;

    public ShareContentWebpage(String title, String content,
                               String url, String imageUrl,Bitmap imageBitMap) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.imageUrl = imageUrl;
        this.imageBitMap = imageBitMap;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public int getShareWay() {
        return ShareConstants.SHARE_WAY_WEBPAGE;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String getMusicUrl() {
        return null;
    }

    @Override
    public Bitmap getImageBitMap() {
        return imageBitMap;
    }


}
