package com.zyp.thirdloginlib.data.resultModel;

import java.util.List;

/**
 * Created by zyp on 2016/10/11.
 */

public class WeChartUserInfoResult extends AccountResult{
    /**
     * openid : OPENID
     * nickname : NICKNAME
     * sex : 1
     * province : PROVINCE
     * city : CITY
     * country : COUNTRY
     * headimgurl : http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0
     * privilege : ["PRIVILEGE1","PRIVILEGE2"]
     * unionid :  o6_bmasdasdsad6_2sgVt7hMZOPfL
     *
     * 参数	说明
     openid	普通用户的标识，对当前开发者帐号唯一
     nickname	普通用户昵称
     sex	普通用户性别，1为男性，2为女性
     province	普通用户个人资料填写的省份
     city	普通用户个人资料填写的城市
     country	国家，如中国为CN
     headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     privilege	用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     unionid	用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */

    private String openid;
    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<String> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<String> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
    }
}
