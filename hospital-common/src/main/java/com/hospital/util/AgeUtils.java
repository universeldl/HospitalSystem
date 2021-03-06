package com.hospital.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by qing on 2017/3/28.
 */
public class AgeUtils {
    // 根据年月日计算年龄,birthTimeString:"11/14/1994"
    public static int getAgeFromBirthTime(String birthTimeString) {
        // 先截取到字符串中的年、月、日
        String strs[] = birthTimeString.trim().split("/");
        int selectYear = Integer.parseInt(strs[2]);
        int selectMonth = Integer.parseInt(strs[0]);
        int selectDay = Integer.parseInt(strs[1]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年就是0周岁
            /*
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }*/

        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
                age = age - 1;
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                    age = age - 1;
                }
            }
        }
        return age;
    }

    // 根据时间戳计算年龄
    public static int getAgeFromBirthTime(long birthTimeLong) {
        Date date = new Date(birthTimeLong * 1000l);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String birthTimeString = format.format(date);
        return getAgeFromBirthTime(birthTimeString);
    }

    // 根据Date类型计算年龄
    public static int getAgeFromBirthTime(Date birthDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String birthTimeString = format.format(birthDate);
        return getAgeFromBirthTime(birthTimeString);
    }

    public static int getAgeFromBirthTime(Date birthDate, Date endDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String birthTimeString = format.format(birthDate);

        // 先截取到字符串中的年、月、日
        String strs[] = birthTimeString.trim().split("/");
        int selectYear = Integer.parseInt(strs[2]);
        int selectMonth = Integer.parseInt(strs[0]);
        int selectDay = Integer.parseInt(strs[1]);

        String endDateString = format.format(endDate);

        // 先截取到字符串中的年、月、日
        String strs2[] = endDateString.trim().split("/");
        int yearNow = Integer.parseInt(strs2[2]);
        int monthNow = Integer.parseInt(strs2[0]);
        int dayNow = Integer.parseInt(strs2[1]);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年就是0周岁
            /*
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }*/

        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
                age = age - 1;
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                    age = age - 1;
                }
            }
        }
        return age;
    }
}
