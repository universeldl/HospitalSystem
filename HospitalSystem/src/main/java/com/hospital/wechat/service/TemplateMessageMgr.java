package com.hospital.wechat.service;

import com.hospital.util.WeixinUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by QQQ on 2017/12/11.
 */
public class TemplateMessageMgr {
    final static String send_template_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    static public boolean sendTemplateMessage(JSONObject data, String openid, String redirect_url, String[] template_id, AccessTokenMgr mgr){
        try{

            JSONObject f1 = new JSONObject();
            f1.put("touser", openid);
            f1.put("template_id", template_id);
            f1.put("url", redirect_url);
            f1.put("data", data);

            System.out.println("post f ="+f1.toString());

            String token = mgr.getAccessToken();

            String requestUrl = send_template_url.replace("ACCESS_TOKEN", token);
            JSONObject jsonObject = WeixinUtil.HttpsRequest(requestUrl, "POST", f1.toString());
            if (jsonObject != null) {
                System.out.println("return = "+jsonObject.toString());
                return true;
            } else {
                System.out.println("Cannot find openids");
                return false;
            }
        }catch(Exception e ){
            e.printStackTrace();
            return false;
        }
    }


}
