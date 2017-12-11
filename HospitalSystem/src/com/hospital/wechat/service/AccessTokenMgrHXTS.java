package com.hospital.wechat.service;

import java.util.Date;
import com.hospital.util.WeixinUtil;
import com.alibaba.fastjson.*;
/**
 * Created by QQQ on 2017/12/11.
 */
public class AccessTokenMgrHXTS extends AccessTokenMgr{
    public AccessTokenMgrHXTS(){
    }

    private static String AppId = "";
    private static String AppSecret = "";

    private static AccessTokenMgrHXTS m_instance = new AccessTokenMgrHXTS();

    public static synchronized AccessTokenMgrHXTS getInstance(){
        return m_instance;
    }

    private String m_AccessToken = null;
    private long m_TokenTime = 0;
    // 凭证有效时间
    private long m_expiresIn;

    public String getAppSecret() {
        return AppSecret;
    }

    public String getAppId() {
        return AppId;
    }

    public synchronized String getAccessToken(){
        long currentTime= new Date().getTime();
        if( m_AccessToken == null || ( currentTime - m_TokenTime ) > m_expiresIn ){
            //重新取得凭证
            try {
                String requestUrl = access_token_url.replace("APPID", AppId).replace("APPSECRET", AppSecret);
                JSONObject jsonObject = WeixinUtil.HttpsRequest(requestUrl, "GET", null);
                if( jsonObject != null ){
                    //请求成功
                    System.out.println("return token =" + jsonObject.toString());
                    m_AccessToken = jsonObject.getString("access_token");
                    m_TokenTime = new Date().getTime();
                    m_expiresIn = jsonObject.getLong("expires_in")*1000;
                }else{
                    m_AccessToken = null;
                    m_TokenTime = 0;
                }
            } catch(Exception e ) {
                e.printStackTrace();
                m_AccessToken = null;
            }
            return m_AccessToken;
        }
        return m_AccessToken;
    }

}
