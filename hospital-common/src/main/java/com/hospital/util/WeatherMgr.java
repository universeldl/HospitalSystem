package com.hospital.util;


import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

public class WeatherMgr {
    private final static String weather_info_url= "http://61.152.122.108:8283/HeathWS/Publish.asmx/GetCrows";
    private final static String authcode = "authCode=shjkqxxc";

    static private HashMap<String, String> map1;
    static private HashMap<String, String> map2;
    private String w;
    static private int info_day = -1;

    static WeatherMgr m_instance = new WeatherMgr();

    public static synchronized WeatherMgr getInstance() { return m_instance; }

    public JSONObject getWeatherString() {
        getWeatherInfo();

        JSONObject obj1 = new JSONObject();
        obj1.put("year", getYear1());
        obj1.put("month", getMonth1());
        obj1.put("date", getDay1());
        obj1.put("warning", getWarningDesc1());
        obj1.put("influ", getInflu1());
        obj1.put("guide", getGuide1());

        JSONObject obj2 = new JSONObject();
        obj2.put("year", getYear2());
        obj2.put("month", getMonth2());
        obj2.put("date", getDay2());
        obj2.put("warning", getWarningDesc2());
        obj2.put("influ", getInflu2());
        obj2.put("guide", getGuide2());

        JSONObject weatherInfo = new JSONObject();
        weatherInfo.put("map1", obj1);
        weatherInfo.put("map2", obj2);

        return weatherInfo;

    }

    public void getWeatherInfo(){

        Calendar now = Calendar.getInstance();
        int current = now.get(Calendar.DAY_OF_MONTH);
        //System.out.println("current =  " + current);
        //System.out.println("info_day=  " + info_day);
        synchronized(this) {
            if (current != info_day) {
                info_day = current;
                try {
                    String requestUrl = weather_info_url;
                    w = WeixinUtil.HttpRequestString(requestUrl, "POST", authcode);
                    parse_weather_string(w);
                } catch (Exception e) {
                    System.out.println("jsonobject exception");
                    e.printStackTrace();
                }
            }
        }
    }

    public String getYear2() {
        String date = map2.get("Date");
        String year = "";
        for(int i = 0; i < date.length(); i++){
            if ( date.charAt(i) != '-' ) {
                year += date.charAt(i);
            } else {
                break;
            }
        }
        return year;
    }
    public String getMonth1() {
        String date = map1.get("Date");
        String month = "";
        int count = 0;
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i) == '-') {
                count += 1;
            } else if (count == 1){
                month += date.charAt(i);
            } else if (count == 2) {
                break;
            }
        }
        return month;
    }

    public String getMonth2() {
        String date = map2.get("Date");
        String month = "";
        int count = 0;
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i) == '-') {
                count += 1;
            } else if (count == 1){
                month += date.charAt(i);
            } else if (count == 2) {
                break;
            }
        }
        return month;
    }
    public String getDay1() {
        String date = map1.get("Date");
        String month = "";
        int count = 0;
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i) == '-' || date.charAt(i) == 'T') {
                count += 1;
            } else if (count == 2){
                month += date.charAt(i);
            } else if (count == 3) {
                break;
            }
        }
        return month;
    }
    public String getDay2() {
        String date = map2.get("Date");
        String month = "";
        int count = 0;
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i) == '-' || date.charAt(i) == 'T') {
                count += 1;
            } else if (count == 2){
                month += date.charAt(i);
            } else if (count == 3) {
                break;
            }
        }
        return month;
    }

    public String getYear1() {
        String date = map1.get("Date");
        String year = "";
        for(int i = 0; i < date.length(); i++){
            if ( date.charAt(i) != '-' ) {
                year += date.charAt(i);
            } else {
                break;
            }
        }
        return year;
    }
    public String getWarningDesc1(){
        return map1.get("WarningDesc");
    }
    public String getWarningDesc2(){
        return map2.get("WarningDesc");
    }

    public String getGuide1(){
        return map1.get("Wat_guid");
    }
    public String getGuide2(){
        return map2.get("Wat_guid");
    }

    public String getInflu1(){
        return map1.get("Influ");
    }
    public String getInflu2(){
        return map2.get("Influ");
    }

    public void parse_weather_string(String s) {
        if ( s.length() == 0) return;
        synchronized(this) {
            try {
                System.out.println("weather_string = " + s);
                Vector<String> v = new Vector<String>();

                String tmp = "";
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ' || s.charAt(i) == '\n') {
                        if (tmp.equals(" ") || tmp.length() == 0) {
                            continue;
                        }
                        v.add(tmp);
                        tmp = "";
                    } else {
                        tmp += s.charAt(i);
                    }
                }

                HashMap m = new HashMap<String, String>();
                for (int i = 0; i < v.size(); i++) {
                    System.out.println("v" + i + "=" + v.elementAt(i));
                    String e = v.elementAt(i);
                    if (e.contains("ID") && e.contains("2")) {
                        map1 = (HashMap) m.clone();
                        System.out.println("1map1=" + map1.toString());
                    } else if (e.contains("Date")) {
                        m.put("Date", ignore_brease(e));
                    } else if (e.contains("WarningLevel")) {
                        m.put("WarningLevel", ignore_brease(e));
                    } else if (e.contains("WarningDesc")) {
                        m.put("WarningDesc", ignore_brease(e));
                    } else if (e.contains("Influ")) {
                        m.put("Influ", ignore_brease(e));
                    } else if (e.contains("Wat_guid")) {
                        m.put("Wat_guid", ignore_brease(e));
                    }
                }

                map2 = m;
                System.out.println("map1=" + map1.toString());
                System.out.println("map2=" + map2.toString());
                return;
            } catch (Exception e) {
                System.out.println(e.toString());
                return;
            }
        }
    }

    private String ignore_brease(String s) {
        String e="";

        int read=0;
        for(int i = 0; i<s.length(); i++) {
            if(s.charAt(i)== '<') {
                read=0;
            } else if (s.charAt(i) =='>') {
                read=1;
            } else if (read==0) {
                continue;
            } else if (read==1) {
                e+=s.charAt(i);
            }
        }
        return e;
    }


/*    public WeatherMgr() {

    }


    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // secret for huxitianshi
    //final static String AppId = "";
    //final static String AppSecret = "";

    // for test wechat id
    final static String AppId = "wxf7622c6c9856841c";
    final static String AppSecret = "d4624c36b6795d1d99dcf0547af5443d";


    static WeatherMgr m_instance = new WeatherMgr();
    static Jedis jedis = new Jedis("127.0.0.1", 6379); //新建Jedis对象


    public static synchronized WeatherMgr getInstance() {
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

*//*        long currentTime = new Date().getTime();
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
*//**//*
                        m_expiresIn = jsonObject.getLong("expires_in") * 1000;
*//**//*
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
        return m_AccessToken;*//*
    }*/
}
