package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.hospital.util.AccessTokenMgr;
import com.hospital.util.AgeUtils;
import com.hospital.util.GetOpenIdOauth2;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    private String errorMsg;
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    private SurveyService surveyService;
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /*
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
            errorMsg = "获取用户失败";
            return ERROR;
        }

        if (deliveryID == null) {
            errorMsg = "发送ID错误";
            return ERROR;
        } else {
        }
        ServletActionContext.getRequest().setAttribute("deliveryID", deliveryID);

        AccessTokenMgr mgr = AccessTokenMgr.getInstance();
        String open_id = GetOpenIdOauth2.getOpenId(code, mgr);


        if (open_id == null) {
            errorMsg = "获取用户名失败，请稍后再试";
            return ERROR;
            //open_id = "oaBonw30UBjZkLW5rf19h7KunM7s";
        }


        DeliveryInfo tmpDelevery = new DeliveryInfo();
        tmpDelevery.setDeliveryId(Integer.valueOf(deliveryID));
        DeliveryInfo deliveryInfo = deliveryService.getDeliveryInfoById(tmpDelevery);

        if (deliveryInfo == null) {
            errorMsg = "问卷发送错误";
            return ERROR;
        }

        // check patient
        Patient patient = deliveryInfo.getPatient();
        if (!patient.getOpenID().equals(open_id)) {
            errorMsg = "用户名错误";
            return ERROR;
        }

        // check validate date
        Date cur_date = new Date(System.currentTimeMillis());
        if (cur_date.after(deliveryInfo.getEndDate())) {
            errorMsg = "问卷已过期";
            return ERROR;
        }

        RetrieveInfo retrieveInfo = retrieveService.getRetrieveInfoByDeliveryID(deliveryInfo.getDeliveryId());
        if (retrieveInfo != null) {
            errorMsg = "问卷已经完成，无需重新作答";
            return ERROR;
        }

        Survey survey = deliveryInfo.getSurvey();
        if (survey == null) {
            errorMsg = "没有找到问卷";
            return ERROR;
        }

        ServletActionContext.getRequest().setAttribute("survey", survey);
        //List<Question> questions = survey.getSortedQuestions();
        Set<Question> all_questions = survey.getQuestions();
        List<Question> questions = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = formatter.format(patient.getBirthday());

        int age = AgeUtils.getAgeFromBirthTime(dateString);
        for (Question question : all_questions) {
            if (question.getStartAge() == -1 && question.getEndAge() == -1) {
                questions.add(question);
            } else if (question.getStartAge() <= age && question.getEndAge() >= age) {
                questions.add(question);
            }
        }

        ServletActionContext.getRequest().setAttribute("questions", questions);
        return SUCCESS;
    }

    public String retrieveAnswer() {
        if (deliveryID == null) {
            errorMsg = "没有找到问卷";
            return ERROR;
        }

        int score = 0;

        DeliveryInfo tmpDelevery = new DeliveryInfo();
        tmpDelevery.setDeliveryId(Integer.valueOf(deliveryID));
        DeliveryInfo deliveryInfo = deliveryService.getDeliveryInfoById(tmpDelevery);
        Date retrieveDate = new Date(System.currentTimeMillis());

        if (deliveryInfo.getEndDate().before(retrieveDate)) {
            errorMsg = "问卷已经过期，无法作答";
            return ERROR;
        }

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

        retrieveService.addRetrieveInfo(retrieveInfo);
        RetrieveInfo newRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);

        Set<Answer> answers = new HashSet<Answer>();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                .get(ServletActionContext.HTTP_REQUEST);
        Map<String, String[]> pMap = request.getParameterMap();
        Set<Integer> processedQuestionId = new HashSet<Integer>();

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
                    if (entry.getValue()[i].equals("on")) {
                        // skip choice for "其他"
                        continue;
                    }
                    int choidId = Integer.valueOf(entry.getValue()[i]);
                    Choice tmpChoice = new Choice();
                    tmpChoice.setChoiceId(Integer.valueOf(choidId));
                    Choice choice = choiceService.getChoiceById(tmpChoice);
                    if (choice != null) {
                        choiceset.add(choice);
                        score += choice.getScore();
                    }
                }

                Answer answer = new Answer();
                answer.setSurvey(survey);
                answer.setPatient(patient);
                answer.setDoctor(doctor);
                answer.setRetrieveInfo(newRetrieveInfo);
                answer.setQuestion(question);
                answer.setChoices(choiceset);
                if (pMap.containsKey("textquestion"+questionid)) {
                    answer.setTextChoice(1);
                    answer.setTextChoiceContent(pMap.get("textquestion" + questionid)[0]);
                    processedQuestionId.add(questionid);
                }
                if (answerService.addAnswer(answer)) {
                    answers.add(answer);
                }
            } else {
                System.out.println("key " + key + " not processed");
            }
        }

        // add answer for text quesiton
        for (Map.Entry<String, String[]> entry : pMap.entrySet()) {
            String key = entry.getKey();
            int questionid = -1;
            if (key.startsWith("textquestion")) {
                questionid = Integer.valueOf(key.substring(12));
                if (processedQuestionId.contains(questionid)) {
                    continue;
                }
                Question tmpQuestion = new Question();
                tmpQuestion.setQuestionId(questionid);
                Question question = questionService.getQuestionById(tmpQuestion);
                Answer answer = new Answer();
                answer.setSurvey(survey);
                answer.setPatient(patient);
                answer.setDoctor(doctor);
                answer.setRetrieveInfo(newRetrieveInfo);
                answer.setQuestion(question);
                answer.setTextChoice(1);
                answer.setTextChoiceContent(pMap.get("textquestion" + questionid)[0]);
                if (answerService.addAnswer(answer)) {
                    answers.add(answer);
                }
            }
        }

        // add empty answers for doctor only questons
        Set<Question> all_questions = survey.getQuestions();
        for (Question question : all_questions) {
            if (question.getStartAge() == 99 && question.getEndAge() == 99) {
                Answer answer = new Answer();
                answer.setSurvey(survey);
                answer.setPatient(patient);
                answer.setDoctor(doctor);
                answer.setRetrieveInfo(newRetrieveInfo);
                answer.setQuestion(question);
                answer.setTextChoice(1);
                if (answerService.addAnswer(answer)) {
                    answers.add(answer);
                }
            }
        }

        newRetrieveInfo.setAnswers(answers);

        RetrieveInfo tmpRetrieveInfo = retrieveService.updateRetrieveInfo(newRetrieveInfo);
        Integer i = (tmpRetrieveInfo==null)?-1:1;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
        json.put("success", i);

        if (survey.getSurveyName().equals("TRACK儿童呼吸和哮喘控制测试")) {
            if (score >= 80) {
                json.put("msg", "本次TRACK测试评分为"+score+"分，<br/>恭喜您，您孩子的呼吸问题似乎得到了控制。");
            } else {
                json.put("msg", "本次TRACK测试评分为"+score+"分，<br/>您孩子的呼吸问题可能未得到控制。");
            }
        }

        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
