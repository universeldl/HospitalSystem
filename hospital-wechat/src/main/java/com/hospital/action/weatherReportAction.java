package com.hospital.action;

import com.hospital.util.WeatherMgr;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by QQQ on 2017/12/23.
 */
public class weatherReportAction extends ActionSupport {

    public String getWeather() {
        WeatherMgr weatherMgr = WeatherMgr.getInstance();
        JSONObject s = weatherMgr.getWeatherString();
        HttpServletRequest request = ServletActionContext.getRequest();

        request.setAttribute("year1", s.getJSONObject("map1").get("year"));
        request.setAttribute("month1", s.getJSONObject("map1").get("month"));
        request.setAttribute("date1", s.getJSONObject("map1").get("date"));
        request.setAttribute("warning1", s.getJSONObject("map1").get("warning"));
        request.setAttribute("influ1", s.getJSONObject("map1").get("influ"));
        request.setAttribute("guide1", s.getJSONObject("map1").get("guide"));

        request.setAttribute("year2", s.getJSONObject("map2").get("year"));
        request.setAttribute("month2", s.getJSONObject("map2").get("month"));
        request.setAttribute("date2", s.getJSONObject("map2").get("date"));
        request.setAttribute("warning2", s.getJSONObject("map2").get("warning"));
        request.setAttribute("influ2", s.getJSONObject("map2").get("influ"));
        request.setAttribute("guide2", s.getJSONObject("map2").get("guide"));


        return SUCCESS;
    }
}
