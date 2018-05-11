package com.hospital.util;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

public class AccessTokenMgr {
    public AccessTokenMgr() {

    }


    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // secret for huxitianshi
    //final static String AppId = "";
    //final static String AppSecret = "";

    // for test wechat id
    final static String AppId = "wxf7622c6c9856841c";
    final static String AppSecret = "d4624c36b6795d1d99dcf0547af5443d";


    static AccessTokenMgr m_instance = new AccessTokenMgr();
    static Jedis jedis = new Jedis("127.0.0.1", 6379); //新建Jedis对象


    public static synchronized AccessTokenMgr getInstance() {
        return m_instance;
    }

    //private String m_AccessToken = null;
    //private long m_TokenTime = 0;
    // 凭证有效时间
    //private long m_expiresIn;

    public String getAppSecret() {
        return AppSecret;
    }

    public String getAppId() {
        return AppId;
    }

    public synchronized String getAccessToken() {
        if (!jedis.isConnected()) {
            jedis.connect();
        }
        jedis.auth("ILfr6LTKhpNJ0x5i");
        String token = jedis.get(AppId);
        if (token == null) {
            try {
                String requestUrl = access_token_url.replace("APPID", AppId).replace("APPSECRET", AppSecret);
                JSONObject jsonObject = WeixinUtil.HttpsRequest(requestUrl, "GET", null);
                if (jsonObject != null) {
                    if (jsonObject.containsKey("errcode")) {
                        System.out.println("error = " + jsonObject.toString());
                    } else {
                        //请求成功
                        System.out.println("return token =" + jsonObject.toString());
                        token = jsonObject.getString("access_token");
                        Integer tokenTime = jsonObject.getInteger("expires_in") - 60;
                        jedis.setex(AppId, tokenTime, token);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return token;

/*        long currentTime = new Date().getTime();
        if (m_AccessToken == null || (currentTime - m_TokenTime) > m_expiresIn) {
            //重新取得凭证
            try {
                String requestUrl = access_token_url.replace("APPID", AppId).replace("APPSECRET", AppSecret);
                JSONObject jsonObject = WeixinUtil.HttpsRequest(requestUrl, "GET", null);
                if (jsonObject != null) {
                    if (jsonObject.containsKey("errcode")) {
                        System.out.println("error = " + jsonObject.toString());
                        m_AccessToken = null;
                        m_TokenTime = 0;
                    } else {
                        //请求成功
                        System.out.println("return token =" + jsonObject.toString());
                        m_AccessToken = jsonObject.getString("access_token");
                        m_TokenTime = new Date().getTime();
*//*
                        m_expiresIn = jsonObject.getLong("expires_in") * 1000;
*//*
                        m_expiresIn = 270 * 1000; // workaround to resolve token bug

                    }
                } else {
                    m_AccessToken = null;
                    m_TokenTime = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                m_AccessToken = null;
            }
            return m_AccessToken;
        }
        return m_AccessToken;*/
    }
}
