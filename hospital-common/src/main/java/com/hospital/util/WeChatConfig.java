package com.hospital.util;


import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.net.URLDecoder;
import java.util.UUID;
import java.util.Formatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class WeChatConfig {

    /**
     * @desc ：4.获取前端jsapi需要的配置参数
     *
     * @param request
     * @return String
     */
    public static String getJsapiConfig(/*HttpServletRequest request*/String urlString){

        //1.准备好参与签名的字段
        //1.1 url
        /*
         *以http://localhost/test.do?a=b&c=d为例
         *request.getRequestURL的结果是http://localhost/test.do
         *request.getQueryString的返回值是a=b&c=d
         */

/*
        String uri = request.getRequestURI();
        System.out.println("uri = " + uri);

        String urlString = request.getRequestURL().toString();
        System.out.println("url =" + urlString);
        String queryString = request.getQueryString();
*/

        String queryString;
        try {
            URL aURL = new URL(urlString);
            queryString = aURL.getQuery();
        } catch (Exception e) {
            System.out.println("invalid urlString" + urlString);
            return null;
        }
        String url = urlString.substring(0,urlString.indexOf('?'));

        System.out.println("url = " + url);

        String queryStringEncode = null;
        //String url;
        if (queryString != null) {
            queryStringEncode = URLDecoder.decode(queryString);
            url = url + "?" + queryStringEncode;
        } else {
            url = urlString;
        }

        System.out.println("generate ticket for url : " + url);

        //1.2 noncestr
        String nonceStr=UUID.randomUUID().toString();      //随机数
        //1.3 timestamp
        long timeStamp = System.currentTimeMillis() / 1000;     //时间戳参数

        String signedUrl = url;

        AccessTokenMgr accessToken = null;
        String ticket = null;

        String signature = null;       //签名


        try {
            //1.4 jsapi_ticket
            accessToken=AccessTokenMgr.getInstance();
            WechatTicketMgr wechatTicketMgr = WechatTicketMgr.getInstance();
            ticket=wechatTicketMgr.getTicket(accessToken);

            //2.进行签名，获取signature
            signature=getSign(ticket,nonceStr,timeStamp,signedUrl);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("accessToken:" + accessToken);
        System.out.println("ticket:" + ticket);
        System.out.println("nonceStr:" + nonceStr);
        System.out.println("timeStamp:" + timeStamp);
        System.out.println("signedUrl:" + signedUrl);
        System.out.println("signature:" + signature);
        System.out.println("appId:" + accessToken.getAppId());


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("signature", signature);
        jsonObject.put("nonceStr", nonceStr);
        jsonObject.put("timeStamp", timeStamp);
        jsonObject.put("appId", accessToken.getAppId());

        System.out.println("configValue:" + jsonObject.toString());
        System.out.println("configValue:"+jsonObject.toJSONString());

        return jsonObject.toJSONString();
    }


    /**
     * @desc ： 4.1 生成签名的函数
     *
     * @param ticket jsticket
     * @param nonceStr 随机串，自己定义
     * @param timeStamp 生成签名用的时间戳
     * @param url 需要进行免登鉴权的页面地址，也就是执行dd.config的页面地址
     * @return
     * @throws Exception String
     */

    public static String getSign(String jsTicket, String nonceStr, Long timeStamp, String url) throws Exception {
        String plainTex = "jsapi_ticket=" + jsTicket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp + "&url=" + url;
        System.out.println(plainTex);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(plainTex.getBytes("UTF-8"));
            return byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @desc ：4.2 将bytes类型的数据转化为16进制类型
     *
     * @param hash
     * @return
     *   String
     */
    private static String byteToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", new Object[] { Byte.valueOf(b) });
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}