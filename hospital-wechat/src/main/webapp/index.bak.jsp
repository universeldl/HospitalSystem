<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.hospital.wechat.service.*" %>


<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>
<h2>Hello World!</h2>
</body>

<%
    /*
    response.sendRedirect("wechatLoginAction_login.action");
    */
%>


<!--
for test

https://open.weixin.qq.com/connect/oauth2/authorize?appid= wxf7622c6c9856841c&redirect_uri=http%3a%2f%2fec01657f.ngrok.io%2fhospital-wechat%2f&response_type=code&scope=snsapi_base&state=test#wechat_redirect
-->

<%
    /*
    AccessTokenMgr mgr = AccessTokenMgrHXTS.getInstance();
    ArrayList<String> openids = OpenIdMgr.getAllOpenIds(mgr);
    System.out.println("openid size = " + openids.size());
    */
    //for (String openid : openids) {
    //    System.out.println("openid = " + openid);
    //}
%>


<%
    /*
    System.out.println("test running");
    String auth_code = request.getParameter("code");
    if (auth_code == null) {
        System.out.println("no code provided. error out");
    } else {
        String open_id = GetOpenIdOauth2.getOpenId(request.getParameter("code"), mgr);
        System.out.println("open_id = " + open_id);

        if (open_id != null) {

            JSONObject data = new JSONObject();

            JSONObject d1 = new JSONObject();
            JSONObject d2 = new JSONObject();
            JSONObject d3 = new JSONObject();
            JSONObject d4 = new JSONObject();
            JSONObject d5 = new JSONObject();

            d1.put("value", "哮喘随访\n");
            d1.put("color", "#173177");

            d2.put("value", "姓名");
            d2.put("color", "#173177");

            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            d3.put("value", df.format(day).toString());
            d3.put("color", "#173177");

            d4.put("value", "哮喘随访：问卷1");
            d4.put("color", "#173177");

            d5.put("value", "\n请点击详情并回答相关问题。");
            d5.put("color", "#173177");


            data.put("first", d1);
            data.put("keyword1", d2);
            data.put("keyword2", d3);
            data.put("keyword3", d4);
            data.put("remark", d5);

            String redirect_url = "http://ec01657f.ngrok.io";

            boolean sent = TemplateMessageMgr.sendSurveyTemplate(data, open_id, redirect_url, mgr);

            if (sent) {
                System.out.println("template send successed");
            } else {
                System.out.println("template send failed");
            }
        } else {
            System.out.println("get openid failed");
        }
    }
    */

%>
</html>
