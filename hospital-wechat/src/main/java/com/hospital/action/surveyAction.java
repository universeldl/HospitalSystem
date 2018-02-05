package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.hospital.wechat.service.AccessTokenMgr;
import com.hospital.wechat.service.AccessTokenMgrHXTS;
import com.hospital.wechat.service.GetOpenIdOauth2;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by QQQ on 2017/12/23.
 */
public class surveyAction extends ActionSupport {
    private String code;
    public void setCode(String code) {
        this.code = code;
    }

    private String deliveryID;
    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    /*
    private SurveyService surveyService;
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    private QuestionService questionService;
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    private PatientService patientService;
    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }
    */

    private DeliveryService deliveryService;
    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    private RetrieveService retrieveService;
    public void setRetrieveService(RetrieveService retrieveService) {
        this.retrieveService = retrieveService;
    }

    public String doSurvey() {

        if (code == null) {
            System.out.println("no code provided");
            return ERROR;
        }

        if (deliveryID == null) {
            System.out.println("No deliveryID");
            return ERROR;
        } else {
            System.out.println("deliveryID = " + deliveryID);
        }

        AccessTokenMgr mgr = AccessTokenMgrHXTS.getInstance();
        String open_id = GetOpenIdOauth2.getOpenId(code, mgr);
        System.out.println("openid = " + open_id);

        if (open_id == null) {
            open_id = "oaBonw30UBjZkLW5rf19h7KunM7s";
        }

        DeliveryInfo tmpDelevery = new DeliveryInfo();
        tmpDelevery.setDeliveryId(Integer.valueOf(deliveryID));
        DeliveryInfo deliveryInfo = deliveryService.getDeliveryInfoById(tmpDelevery);

        System.out.println("deliveryInfo = " + deliveryInfo.toString());

        if (deliveryInfo == null) {
            System.out.println("delivery info not found");
            //return ERROR;
        }

        // check patient
        Patient patient = deliveryInfo.getPatient();
        if (!patient.getOpenID().equals(open_id)) {
            System.out.println("patient.getopenid = " + patient.getOpenID());
            System.out.println("open id not match");
            //return ERROR;
        }

        // check validate date
        Date cur_date = new Date();
        if (cur_date.after(deliveryInfo.getEndDate())) {
            System.out.println("overdue survey");
            //return ERROR;
        }

        System.out.println("redirect to jsp1");

        RetrieveInfo retrieveInfo = retrieveService.getRetrieveInfoByDeliveryID(deliveryInfo.getDeliveryId());
        if (retrieveInfo != null) {
            System.out.println("already has retrieveinfo for this delivery");
            //return ERROR;
        }
        System.out.println("redirect to jsp2");

        Survey survey = deliveryInfo.getSurvey();
        if (survey == null) {
            System.out.println("Survey not found");
            //return ERROR;
        }

        System.out.println("redirect to jsp3");
        ServletActionContext.getRequest().setAttribute("survey", survey);
        List<Question> questions = survey.getSortedQuestions();
        ServletActionContext.getRequest().setAttribute("questions", questions);
        System.out.println("redirect to jsp");
        return SUCCESS;
    }

}
