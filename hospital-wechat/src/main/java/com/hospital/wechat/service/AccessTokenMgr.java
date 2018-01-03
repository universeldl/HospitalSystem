package com.hospital.wechat.service;

public class AccessTokenMgr {
	public AccessTokenMgr(){
		
	}
	
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";


	public static synchronized AccessTokenMgr getInstance() {
		return null;
	}
	
	public String getAppSecret() {
		return null;
	}

	public String getAppId(){
		return null;
	}

	public synchronized String getAccessToken(){
		return null;
	}
}
