package com.hospital.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by QQQ on 2017/12/17.
 */
public class WechatUserMgr {
    final static String get_ursr_info_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN\n";

    static public JSONObject getUserInfo(String openid, AccessTokenMgr mgr) {
        try {
            System.out.println("WechatUserMgr.getUserInfo openid = " + openid);

            String token = mgr.getAccessToken();
            if (token == null) {
                System.out.println("get token failed");
                return null;
            }

            String requestUrl = get_ursr_info_url.replace("ACCESS_TOKEN", token).replace("OPENID", openid);

            // first catch
            JSONObject js_obj = WeixinUtil.HttpsRequest(requestUrl, "GET", null);
            System.out.println("WechatUserMgr.getUserInfo return = " + js_obj);
            if (js_obj != null) {
                return js_obj;
            } else {
                System.out.println("request failed");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public String getUserHeadimgUrl(String openid, AccessTokenMgr mgr) {
        try {

            JSONObject jsonObject = getUserInfo(openid, mgr);

            if (jsonObject.containsKey("headimgurl")) {
                return jsonObject.getString("headimgurl");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
