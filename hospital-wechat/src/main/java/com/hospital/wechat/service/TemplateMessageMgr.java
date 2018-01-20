package com.hospital.wechat.service;

import com.hospital.util.WeixinUtil;
import com.alibaba.fastjson.*;

/**
 * Created by QQQ on 2017/12/11.
 */
public class TemplateMessageMgr {
    final static String send_template_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    // get all template ids for current wechat
    final static String get_template_list_url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    static public boolean sendSurveyTemplate(JSONObject data, String openid, String redirect_url, AccessTokenMgr mgr) {
        try {
            System.out.println("sendSurveyTemplate data = " + data.toString());
            String template_id = getSurveyTemplateId(mgr);
            if (template_id == null) {
                System.out.println("get survey template id failed!");
                return false;
            }
            return sendTemplateMessage(data, openid, redirect_url, template_id, mgr);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static public String getSurveyTemplateId(AccessTokenMgr mgr) {
        try {
            JSONObject templates = getAllTemplate(mgr);
            JSONArray template_list = templates.getJSONArray("template_list");
            for (int i = 0; i < template_list.size(); i++) {
                JSONObject template = template_list.getJSONObject(i);
                if (template.getString("title").equals("随访提醒")) {
                    return template.getString("template_id");
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public JSONObject getAllTemplate(AccessTokenMgr mgr) {
        try {
            String token = mgr.getAccessToken();

            String requestUrl = get_template_list_url.replace("ACCESS_TOKEN", token);
            JSONObject template_list = WeixinUtil.HttpsRequest(requestUrl, "GET", "");
            if (template_list != null) {
                System.out.println("return = " + template_list.toString());
                return template_list;
            } else {
                System.out.println("get template list failed!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public boolean sendTemplateMessage(JSONObject data, String openid, String redirect_url, String template_id, AccessTokenMgr mgr) {
        try {

            JSONObject f1 = new JSONObject();
            f1.put("touser", openid);
            f1.put("template_id", template_id);
            f1.put("url", redirect_url);
            f1.put("data", data);

            System.out.println("post f =" + f1.toString());

            String token = mgr.getAccessToken();

            String requestUrl = send_template_url.replace("ACCESS_TOKEN", token);
            JSONObject jsonObject = WeixinUtil.HttpsRequest(requestUrl, "POST", f1.toString());
            if (jsonObject != null) {
                System.out.println("return = " + jsonObject.toString());
                return true;
            } else {
                System.out.println("Cannot find openids");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
