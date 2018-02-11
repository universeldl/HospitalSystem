package com.hospital.wechat.service;

/**
 * Created by QQQ on 2018/2/11.
 */
public class AccessTokenService {
    static public AccessTokenMgr getAccessTokenByID(String id) {
        if (id == "wxf7622c6c9856841c") {
            return AccessTokenMgrHXTS.getInstance();
        } else {
            return null;
        }
    }
}

