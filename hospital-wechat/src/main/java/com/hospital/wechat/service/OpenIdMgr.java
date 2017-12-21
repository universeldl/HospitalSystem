package com.hospital.wechat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hospital.util.WeixinUtil;

import java.util.ArrayList;
/**
 * Created by QQQ on 2017/12/17.
 */
public class OpenIdMgr {
    final static String get_openid_url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

    static public ArrayList<String> getAllOpenIds(AccessTokenMgr mgr) {
        try {
            ArrayList<String> open_ids = new ArrayList<>();

            String token = mgr.getAccessToken();
            if (token == null) {
                System.out.println("get token failed");
                return open_ids;
            }

            String next_openid = null;
            String requestUrl = get_openid_url.replace("ACCESS_TOKEN", token).replace("NEXT_OPENID", "");

            // first catch
            JSONObject js_obj = WeixinUtil.HttpsRequest(requestUrl, "GET", null);
            if (js_obj != null && js_obj.containsKey("data")) {
                JSONArray open_id_jsarray = js_obj.getJSONObject("data").getJSONArray("openid");
                if (!open_id_jsarray.isEmpty()) {
                    for (int i=0; i<open_id_jsarray.size(); i++){
                        open_ids.add(open_id_jsarray.getString(i));
                    }
                }
            } else {
                System.out.println("request failed");
                return open_ids;
            }

            next_openid = js_obj.getString("next_openid");
            // continue to pull openids
            while (true) {
                token = mgr.getAccessToken();
                requestUrl = get_openid_url.replace("ACCESS_TOKEN", token).replace("NEXT_OPENID", next_openid);
                js_obj = WeixinUtil.HttpsRequest(requestUrl, "GET", null);
                if (js_obj != null && js_obj.containsKey("data")) {
                    JSONArray open_id_jsarray = js_obj.getJSONObject("data").getJSONArray("openid");
                    for (int i=0; i<open_id_jsarray.size(); i++){
                        open_ids.add(open_id_jsarray.getString(i));
                    }
                    next_openid = js_obj.getString("next_openid");
                } else {
                    break;
                }
            }
            return open_ids;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };
    
}
