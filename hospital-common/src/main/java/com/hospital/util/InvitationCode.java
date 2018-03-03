package com.hospital.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by QQQ on 2018/2/27.
 *
 * According to Doctor's request, use 6 digital invitation code to screen unexpected patients.
 * the invitation code = YEAR + MONTH, 201802 for example.
 */
public class InvitationCode {
    static public String getInvitationCode() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        String invitationCode = String.valueOf(year);
        if (month < 10) {
            invitationCode += String.valueOf(0);
        }
        invitationCode += String.valueOf(month);
        return invitationCode;
    }
}
