package com.hospital.wechat.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.Patient;
import com.hospital.domain.Survey;
import com.hospital.util.WeixinUtil;

import java.util.Date;

/**
 * Created by QQQ on 2017/12/11.
 */
public class TemplateMessageMgr {
    final static String send_template_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    // get all template ids for current wechat
    final static String get_template_list_url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    static String survey_template_id = null;

    static public boolean sendSurveyTemplateByDeliveryInfo(DeliveryInfo deliveryInfo) {

        JSONObject d1 = new JSONObject();
        JSONObject d2 = new JSONObject();
        JSONObject d3 = new JSONObject();
        JSONObject d4 = new JSONObject();
        JSONObject d5 = new JSONObject();
        JSONObject data = new JSONObject();

        d1.put("value","上海儿童医学中心呼吸科随访问卷");
        d1.put("color","#173177");

        Patient patient = deliveryInfo.getPatient();
        d2.put("value",patient.getName());
        d2.put("color","#173177");

        Date date = deliveryInfo.getDeliveryDate();
        d3.put("value", date.toString());
        d3.put("color","#173177");

        Survey survey = deliveryInfo.getSurvey();
        d4.put("value", survey.getSurveyName());
        d4.put("color","#173177");

        d5.put("value","请点击详情开始随访。");
        d5.put("color","#173177");

        data.put("first",    d1);
        data.put("keyword1", d2);
        data.put("keyword2", d3);
        data.put("keyword3", d4);
        data.put("remark", d5);

        JSONObject json = new JSONObject();
        AccessTokenMgr mgr = AccessTokenService.getAccessTokenByID(patient.getAppID());

        String url = "localhost:8080/hospital-wechat/doSurvey.jsp?deliveryID=" + deliveryInfo.getDeliveryId();

        return sendSurveyTemplate(json, patient.getOpenID(), url, mgr);
    }

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
        if (survey_template_id == null) {
            try {
                JSONObject templates = getAllTemplate(mgr);
                JSONArray template_list = templates.getJSONArray("template_list");
                for (int i = 0; i < template_list.size(); i++) {
                    JSONObject template = template_list.getJSONObject(i);
                    if (template.getString("title").equals("随访提醒")) {
                        survey_template_id = template.getString("template_id");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return survey_template_id;
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
