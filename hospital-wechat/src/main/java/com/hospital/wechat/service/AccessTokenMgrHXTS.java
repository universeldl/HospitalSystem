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

    // secret for huxitianshi
    //final static String AppId = "";
    //final static String AppSecret = "";

    // for test wechat id
    final static String AppId = "wxf7622c6c9856841c";
    final static String AppSecret = "d4624c36b6795d1d99dcf0547af5443d";


    static AccessTokenMgrHXTS m_instance = new AccessTokenMgrHXTS();

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
                    if (jsonObject.containsKey("errcode")) {
                        System.out.println("error = " + jsonObject.toString());
                        m_AccessToken = null;
                        m_TokenTime = 0;
                    } else {
                        //请求成功
                        System.out.println("return token =" + jsonObject.toString());
                        m_AccessToken = jsonObject.getString("access_token");
                        m_TokenTime = new Date().getTime();
                        m_expiresIn = jsonObject.getLong("expires_in") * 1000;
                    }
                } else{
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
