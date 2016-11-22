package com.zyp.thirdloginlib.data;


import com.zyp.thirdloginlib.data.resultModel.AccessTokenResult;
import com.zyp.thirdloginlib.data.resultModel.WeChartUserInfoResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
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
    Observable<AccessTokenResult> getAccessToken(@Path("appid") String wechatAppId,
                                                 @Path("secret") String wechatAppId1,
                                                 @Path("code") int errCode,
                                                 @Path("grant_type") int authorizationCode);

    /**
     * 获取用户个人信息（UnionID机制）
     * @return
     */
    @GET("/sns/userinfo")
    Observable<WeChartUserInfoResult> getWechatUserInfo(@Path("access_token") String accessToken,
                                                        @Path("openid") String openid);


}
