package com.zyp.thirdloginlib.impl;

import com.zyp.thirdloginlib.data.resultModel.AccountResult;

import java.util.HashMap;

/**
 * Created by echo on 5/20/15.
 */
public interface PlatformActionListener {

    /**
     * 登录成功
     */
    void onComplete(AccountResult accountResult);

    /**
     * 登录失败
     */
    void onError();

    /**
     * 取消登录
     */
    void onCancel();

}
