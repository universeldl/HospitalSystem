package com.hospital.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
/**
    *
    * @param time  支持 2017-01的时间字符串格式
    * @return
    */
   public static String[] getLast12Months(String time){
       //处理月份输入条件
       if(time.length()==7){
           time=time+"-01 00:00:00";
       }else if(time.length()==110){
           time=time.substring(0,7)+"-01 00:00:00";
       }
       Date date = new Date();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       try {
            date= sdf.parse(time);
       } catch (ParseException e) {
           return null;
       }

       String[] last12Months = new String[12];
       Calendar cal = Calendar.getInstance();
       //设置输入条件时间
       cal.setTime(date);

       cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1); //要先+1,才能把本月的算进去
       for(int i=0; i<12; i++){
           cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月
           last12Months[11-i] = cal.get(Calendar.YEAR)+ "-" + addZeroForNum(String.valueOf(cal.get(Calendar.MONTH)+1), 2);
       }

       return last12Months;
   }
   public static String addZeroForNum(String str, int strLength) {
       int strLen = str.length();
       if (strLen < strLength) {
           while (strLen < strLength) {
               StringBuffer sb = new StringBuffer();
               sb.append("0").append(str);// 左补0
               // sb.append(str).append("0");//右补0
               str = sb.toString();
               strLen = str.length();
           }
       }
       return str;
   }
}
