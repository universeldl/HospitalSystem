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
import java.util.List;
import java.util.Set;

/**
 * Created by QQQ on 2017/12/23.
 */
public class surveyAction extends ActionSupport {
    private String patientID;
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    private String surveyID;
    public void setSurveyID(String surveyID) {
        this.surveyID = surveyID;
    }

    private String startDate;
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

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


    public String doSurvey() {
        Patient inPatient = new Patient();
        inPatient.setPatientId(Integer.valueOf(patientID));
        System.out.println("patientID=" + patientID);
        Patient patient = patientService.getPatientById(inPatient);
        if (patient == null) {
            System.out.println("Patient not found");
            return ERROR;
        }

        Survey inSurvey = new Survey();
        inSurvey.setSurveyId(Integer.valueOf(surveyID));
        Survey survey = surveyService.getSurveyById(inSurvey);
        if (survey == null) {
            System.out.println("Survey not found");
            return ERROR;
        }
        ServletActionContext.getRequest().setAttribute("survey", survey);

        List<Question> questions = survey.getSortedQuestions();
        ServletActionContext.getRequest().setAttribute("questions", questions);

        /*
        List<Choice> choices = new ArrayList<>();
        for(Question question : questions) {
            choices.addAll(question.getSortedChoices());
        }
        ServletActionContext.getRequest().setAttribute("choice", choices);
        */

        return SUCCESS;
    }

}
