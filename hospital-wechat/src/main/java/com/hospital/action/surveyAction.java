package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.hospital.wechat.service.AccessTokenMgr;
import com.hospital.wechat.service.AccessTokenMgrHXTS;
import com.hospital.wechat.service.GetOpenIdOauth2;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    private QuestionService questionService;
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    private DeliveryService deliveryService;
    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    private RetrieveService retrieveService;
    public void setRetrieveService(RetrieveService retrieveService) {
        this.retrieveService = retrieveService;
    }

    private AnswerService answerService;
    public void setAnswerService(AnswerService answerService) {
        this.answerService = answerService;
    }

    private ChoiceService choiceService;
    public void setChoiceService(ChoiceService choiceService) {
        this.choiceService = choiceService;
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
        ServletActionContext.getRequest().setAttribute("deliveryID", deliveryID);

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

        RetrieveInfo retrieveInfo = retrieveService.getRetrieveInfoByDeliveryID(deliveryInfo.getDeliveryId());
        if (retrieveInfo != null) {
            System.out.println("already has retrieveinfo for this delivery");
            //return ERROR;
        }

        Survey survey = deliveryInfo.getSurvey();
        if (survey == null) {
            System.out.println("Survey not found");
            //return ERROR;
        }

        ServletActionContext.getRequest().setAttribute("survey", survey);
        List<Question> questions = survey.getSortedQuestions();
        ServletActionContext.getRequest().setAttribute("questions", questions);
        System.out.println("redirect to jsp");
        return SUCCESS;
    }

    public String retrieveAnswer() {
        System.out.println("retrieveAnswer called!");
        System.out.println("deliveryID = " + deliveryID);

        if (deliveryID == null) {
            return ERROR;
        }

        DeliveryInfo tmpDelevery = new DeliveryInfo();
        tmpDelevery.setDeliveryId(Integer.valueOf(deliveryID));
        DeliveryInfo deliveryInfo = deliveryService.getDeliveryInfoById(tmpDelevery);
        Date retrieveDate = new Date();
        Survey survey = deliveryInfo.getSurvey();
        Patient patient = deliveryInfo.getPatient();
        Doctor doctor = deliveryInfo.getDoctor();

        RetrieveInfo retrieveInfo = new RetrieveInfo();
        retrieveInfo.setDeliveryId(Integer.valueOf(deliveryID));
        retrieveInfo.setDeliveryInfo(deliveryInfo);
        retrieveInfo.setRetrieveDate(retrieveDate);
        retrieveInfo.setSurvey(survey);
        retrieveInfo.setPatient(patient);
        retrieveInfo.setDoctor(doctor);

        Set<Answer> answers = new HashSet<Answer>();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                .get(ServletActionContext.HTTP_REQUEST);
        Map<String, String[]> pMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : pMap.entrySet()) {
            String key = entry.getKey();
            int questionid = -1;
            if (key.startsWith("question")) {
                questionid = Integer.valueOf(key.substring(8));
                Question tmpQuestion = new Question();
                tmpQuestion.setQuestionId(questionid);
                Question question = questionService.getQuestionById(tmpQuestion);

                String[] value = entry.getValue();
                Set<Choice> choiceset = new HashSet<Choice>();
                for (int i = 0; i < value.length; i++) {
                    int choidId = Integer.valueOf(entry.getValue()[i]);
                    System.out.println("\tchoice id = " + choidId);
                    Choice tmpChoice = new Choice();
                    tmpChoice.setChoiceId(Integer.valueOf(choidId));
                    Choice choice = choiceService.getChoiceById(tmpChoice);
                    if (choice != null) {
                        System.out.println("choice found");
                        choiceset.add(choice);
                        System.out.println("choice add to choiceset");
                    } else {
                        System.out.println("choice not found");
                    }
                }

                Answer answer = new Answer();
                answer.setSurvey(survey);
                answer.setPatient(patient);
                answer.setDoctor(doctor);
                answer.setRetrieveInfo(retrieveInfo);
                answer.setQuestion(question);
                answer.setChoices(choiceset);
                if (pMap.containsKey("textquestion"+questionid)) {
                    answer.setTextChoiceContent(pMap.get("textquestion"+questionid)[0]);
                }
                if (answerService.addAnswer(answer)) {
                    answers.add(answer);
                }
            } else {
                continue;
            }

            System.out.println("question id = " + questionid);
        }

        retrieveInfo.setAnswers(answers);
        System.out.println("add answers to retrieve info");

        //Integer i = retrieveService.addRetrieveInfo(retrieveInfo);
        //System.out.println("add retrieve info = " + i.toString());

        return SUCCESS;
    }
}
