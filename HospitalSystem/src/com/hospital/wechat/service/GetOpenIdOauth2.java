package com.hospital.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.hospital.util.WeixinUtil;
/**
 * Created by QQQ on 2017/12/11.
 */
public class GetOpenIdOauth2 {
    final static String oauth2_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public static String getOpenId(String code, AccessTokenMgr mgr) {
        try {
            String requestURL = oauth2_url.replace("APPID", mgr.getAppId()).replace("SECRET", mgr.getAppSecret()).replace("CODE", code);
            JSONObject js_object = WeixinUtil.HttpsRequest(requestURL, "GET", null);
            String open_id = js_object.getString("openid");
            return open_id;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
