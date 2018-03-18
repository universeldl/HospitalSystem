package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.DeliveryService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class DeliveryManageAction extends ActionSupport {

    private DeliveryService deliveryService;

    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    private int pageCode;
    private int deliveryId;
    private int patientId;
    private String openID;
    private String pwd;
    private String patientName;

    public void setPatientId(Integer patientId) {this.patientId = patientId;}

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }


    /**
     * 根据页码分页查询分发信息
     *
     * @return
     */
    public String findDeliveryInfoByPage() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        PageBean<DeliveryInfo> pb = deliveryService.findDeliveryInfoByPage(pageCode, pageSize, doctor);
        if (pb != null) {
            pb.setUrl("findDeliveryInfoByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    /**
     * 根据分发id查询该分发信息
     *
     * @return
     */
    public String getDeliveryInfoById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        DeliveryInfo info = new DeliveryInfo();
        info.setDeliveryId(deliveryId);
        DeliveryInfo newInfo = deliveryService.getDeliveryInfoById(info);
        //JsonConfig jsonConfig = new JsonConfig();
        //jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        //jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
        //    public boolean apply(Object obj, String name, Object value) {
        //        return obj instanceof Authorization || name.equals("authorization") || obj instanceof Set || name.equals("deliveryInfos");
        //    }
        //});

        JSONObject jsonObject = new JSONObject();
        int deliveryId = newInfo.getDeliveryId();
        String surveyName = newInfo.getSurvey().getSurveyName();
        String typeName = newInfo.getSurvey().getSurveyType().getTypeName();
        String openId = newInfo.getPatient().getOpenID();
        String patientName = newInfo.getPatient().getName();
        String patientType = newInfo.getPatient().getPatientType().getPatientTypeName();
        int overday = newInfo.getOverday();
        int state = newInfo.getState();
        String doctorName = newInfo.getDoctor().getName();

        jsonObject.put("deliveryId", deliveryId);
        jsonObject.put("surveyName", surveyName);
        jsonObject.put("typeName", typeName);
        jsonObject.put("openId", openId);
        jsonObject.put("patientName", patientName);
        jsonObject.put("patientType", patientType);
        jsonObject.put("overday", overday);
        jsonObject.put("state", state);
        jsonObject.put("doctorName", doctorName);

        //JSONObject jsonObject = JSONObject.fromObject(newInfo, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String getUnansweredDeliveryInfos() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<DeliveryInfo> unanswered = deliveryService.getUnansweredDeliveryInfos(patientId);

        JSONArray jsonArray = new JSONArray();
        int idx = 0;
        for(DeliveryInfo deliveryInfo : unanswered) {
            JSONObject di = new JSONObject();
            di.put("deliveryId", deliveryInfo.getDeliveryId());
            di.put("surveyName", deliveryInfo.getSurvey().getSurveyName());
            jsonArray.add(idx, di);
            idx++;
        }
        //JsonConfig jsonConfig = new JsonConfig();
        //jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        //String json = JSONArray.fromObject(unanswered, jsonConfig).toString();//List------->JSONArray
        try {
            response.getWriter().print(jsonArray);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 分发处理
     *
     * @return
     */
    public String deliverySurvey() {
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        Patient patient = new Patient();
        patient.setOpenID(openID);
        deliveryInfo.setPatient(patient);
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        deliveryInfo.setDoctor(doctor);
        Survey survey = new Survey();
        deliveryInfo.setSurvey(survey);
        int addDelivery = deliveryService.addDelivery(deliveryInfo);
        try {
            ServletActionContext.getResponse().getWriter().print(addDelivery);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String resendSurvey() {//TODO: not used for now, DO NOT use this function
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setDeliveryId(deliveryId);
        int resendSurvey = deliveryService.resendSurvey(deliveryInfo);
        try {
            ServletActionContext.getResponse().getWriter().print(resendSurvey);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    public String queryDeliverySearchInfo() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<DeliveryInfo> pb = null;
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        if ("".equals(patientName.trim()) && deliveryId == 0) {
            pb = deliveryService.findDeliveryInfoByPage(pageCode, pageSize, doctor);
        } else {
            pb = deliveryService.queryDeliveryInfo(patientName, deliveryId, pageCode, pageSize, doctor);
        }
        if (pb != null) {
            pb.setUrl("queryDeliverySearchInfo.action?patientName=" + patientName + "&deliveryId=" + deliveryId + "&");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


}
