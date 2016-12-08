package com.zyp.thirdloginlib.data;


import com.zyp.thirdloginlib.data.resultModel.AccessTokenResult;
import com.zyp.thirdloginlib.data.resultModel.WeChartUserInfoResult;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by echo on 5/20/15.
 */
public interface WechatApiService {


    /**
     * 通过code获取access_token
     * @return
     */
    @GET("/sns/oauth2/access_token")
    Observable<AccessTokenResult> getAccessToken(@QueryMap Map<String, Object> option);

    /**
     * 获取用户个人信息（UnionID机制）
     * @return
     */
    @GET("/sns/userinfo")
    Observable<WeChartUserInfoResult> getWechatUserInfo(@QueryMap Map<String, Object> option);


}
