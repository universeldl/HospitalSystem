package com.hospital.util;


import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by QQQ on 2018/6/25.
 */
public class WechatTicketMgr {
    public final static String sign_ticket_create_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    static Jedis jedis = new Jedis("127.0.0.1", 6379); //新建Jedis对象

    static WechatTicketMgr m_instance = new WechatTicketMgr();

    public static synchronized WechatTicketMgr getInstance() {
        return m_instance;
    }

    public synchronized String getTicket(AccessTokenMgr accessToken) throws ParseException, IOException {
        if (!jedis.isConnected()) {
            jedis.connect();
        }
        jedis.auth("ILfr6LTKhpNJ0x5i");
        String key = accessToken.getAppId();
        key += "_ticket";
        String ticket = jedis.get(key);

        if (ticket == null) {
            String url = sign_ticket_create_url.replace("ACCESS_TOKEN", accessToken.getAccessToken());
            try {
                JSONObject jsonObject = WeixinUtil.HttpsRequest(url, "GET", null);
                if (jsonObject.containsKey("errcode") && !jsonObject.getString("errcode").equals("0")) {
                    System.out.println("error = " + jsonObject.toString());
                } else {
                    ticket = jsonObject.getString("ticket");
                    Integer tokenTime = jsonObject.getInteger("expires_in") - 60;
                    jedis.setex(key, tokenTime, ticket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ticket;
    };
}
