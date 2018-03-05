package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.DeliveryService;
import com.hospital.service.PatientService;
import com.hospital.service.RetrieveService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RetrieveManageAction extends ActionSupport {

    private RetrieveService retrieveService;
    private DeliveryService deliveryService;
    private PatientService patientService;

    public void setRetrieveService(RetrieveService retrieveService) {
        this.retrieveService = retrieveService;
    }

    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    private int pageCode;
    private Set<Answer> myAnswers;

    private int deliveryId;
    private int patientId;
    private int surveyId;
    private String openID;

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public void setMyAnswers(Set<Answer> myAnswers) {
        this.myAnswers = myAnswers;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public String findRetrieveInfoByPage() {

        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<RetrieveInfo> pb = retrieveService.findRetrieveInfoByPage(pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findRetrieveInfoByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    public String getAnswerByDeliveryId() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        RetrieveInfo retrieveInfo = new RetrieveInfo();
        retrieveInfo.setDeliveryId(deliveryId);
        myAnswers = retrieveService.getAnswerByDeliveryId(retrieveInfo);

        ActionContext actionContext = ActionContext.getContext();
        actionContext.put("myAnswers", myAnswers);
        ServletActionContext.getRequest().setAttribute("myAnswers", myAnswers);
        return "check";
    }


    public String getRetrieveInfoById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        RetrieveInfo retrieveInfo = new RetrieveInfo();
        retrieveInfo.setDeliveryId(deliveryId);
        RetrieveInfo newRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                return obj instanceof Authorization || name.equals("authorization") || obj instanceof Set || name.equals("deliveryInfos");
            }
        });


        JSONObject jsonObject = JSONObject.fromObject(newRetrieveInfo, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String queryRetrieveInfo() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<RetrieveInfo> pb = null;
        if ("".equals(openID.trim()) && deliveryId == 0) {
            pb = retrieveService.findRetrieveInfoByPage(pageCode, pageSize);
        } else {
            pb = retrieveService.queryRetrieveInfo(openID, deliveryId, pageCode, pageSize);
        }
        if (pb != null) {
            pb.setUrl("queryRetrieveInfo.action?openID=" + openID + "&deliveryId=" + deliveryId + "&");
        }

        if(deliveryId != 0) {
            RetrieveInfo retrieveInfo = new RetrieveInfo();
            retrieveInfo.setDeliveryId(deliveryId);
            RetrieveInfo newRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }

    public String retrieveSurvey() {
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        RetrieveInfo retrieveInfo = new RetrieveInfo();
        retrieveInfo.setDeliveryId(deliveryId);
        retrieveInfo.setDoctor(doctor);
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setDeliveryId(deliveryId);
        DeliveryInfo deliveryInfo1 = deliveryService.getDeliveryInfoById(deliveryInfo);
        retrieveInfo.setDeliveryInfo(deliveryInfo1);

        //TODO 大神，加入微信获取retrieveInfo中所有answer的code并setAnswer,删除此行

        int success = retrieveService.addRetrieveInfo(retrieveInfo);
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.getWriter().print(success);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String patientRetrieveManage() {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Patient newPatient = patientService.getPatientById(patient);

        ActionContext actionContext = ActionContext.getContext();
        actionContext.put("patient", newPatient);
        ServletActionContext.getRequest().setAttribute("patient", newPatient);
        return "success";
    }


}

