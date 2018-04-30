package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.hospital.util.AccessTokenMgr;
import com.hospital.util.AgeUtils;
import com.hospital.util.GetOpenIdOauth2;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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

/*    private String errorMsg;
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }*/

/*    private SurveyService surveyService;
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }*/

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

 /*   private AnswerService answerService;
    public void setAnswerService(AnswerService answerService) {
        this.answerService = answerService;
    }*/

    private ChoiceService choiceService;
    public void setChoiceService(ChoiceService choiceService) {
        this.choiceService = choiceService;
    }


    public String getQuestions() {

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        DeliveryInfo tmpDeliveryInfo = new DeliveryInfo();
        tmpDeliveryInfo.setDeliveryId(Integer.valueOf(deliveryID));
        DeliveryInfo deliveryInfo = deliveryService.getDeliveryInfoById(tmpDeliveryInfo);//得到问卷
        Survey survey = deliveryInfo.getSurvey();
        Patient patient = deliveryInfo.getPatient();

        Set<Question> all_questions = survey.getQuestions();
        List<Question> questions = new ArrayList<>();

        int age = AgeUtils.getAgeFromBirthTime(patient.getBirthday());
        for (Question question : all_questions) {
            if (question.getStartAge() == -1 && question.getEndAge() == -1) {
                questions.add(question);
            } else if (question.getStartAge() <= age && question.getEndAge() >= age) {
                questions.add(question);
            }
        }

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        String json = JSONArray.fromObject(questions, jsonConfig).toString();

        System.out.println(json);
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String doSurvey() {

        if (code == null) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "获取用户失败");
            return ERROR;
        }

        if (deliveryID == null) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "发送ID错误");
            return ERROR;
        } else {
        }
        ServletActionContext.getRequest().setAttribute("deliveryID", deliveryID);

        AccessTokenMgr mgr = AccessTokenMgr.getInstance();
        String open_id = GetOpenIdOauth2.getOpenId(code, mgr);


        if (open_id == null) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "获取用户名失败，请稍后再试");
            return ERROR;
            //open_id = "oaBonw30UBjZkLW5rf19h7KunM7s";
        }


        DeliveryInfo tmpDelevery = new DeliveryInfo();
        tmpDelevery.setDeliveryId(Integer.valueOf(deliveryID));
        DeliveryInfo deliveryInfo = deliveryService.getDeliveryInfoById(tmpDelevery);

        if (deliveryInfo == null) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "问卷发送错误");
            return ERROR;
        }

        // check patient
        Patient patient = deliveryInfo.getPatient();
        if (!patient.getOpenID().equals(open_id)) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "用户名错误");
            return ERROR;
        }

        // check validate date
        Date cur_date = new Date(System.currentTimeMillis());
        if (cur_date.after(deliveryInfo.getEndDate())) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "问卷已过期");
            return ERROR;
        }

        RetrieveInfo retrieveInfo = retrieveService.getRetrieveInfoByDeliveryID(deliveryInfo.getDeliveryId());
        if (retrieveInfo != null) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "问卷已经完成，无需重新作答");
            return ERROR;
        }

        Survey survey = deliveryInfo.getSurvey();
        if (survey == null) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "没有找到问卷");
            return ERROR;
        }

        ServletActionContext.getRequest().setAttribute("surveyName", survey.getSurveyName());
        ServletActionContext.getRequest().setAttribute("surveyDescription", survey.getDescription());

        Set<Question> all_questions = survey.getQuestions();
        List<Question> questions = new ArrayList<>();

        int age = AgeUtils.getAgeFromBirthTime(patient.getBirthday());
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
            ServletActionContext.getRequest().setAttribute("errorMsg", "没有找到问卷");
            return ERROR;
        }

        BigDecimal score = new BigDecimal("0");

        DeliveryInfo tmpDelevery = new DeliveryInfo();
        tmpDelevery.setDeliveryId(Integer.valueOf(deliveryID));
        DeliveryInfo deliveryInfo = deliveryService.getDeliveryInfoById(tmpDelevery);
        Date retrieveDate = new Date(System.currentTimeMillis());

        if (deliveryInfo.getEndDate().before(retrieveDate)) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "问卷已过期");
            return ERROR;
        }

        if (deliveryInfo.getRetrieveInfo() != null) {
            ServletActionContext.getRequest().setAttribute("errorMsg", "问卷已经完成，无需重新作答");
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

        //retrieveService.addRetrieveInfo(retrieveInfo);
        //RetrieveInfo newRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);

        Set<Answer> answers = new HashSet<Answer>();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                .get(ServletActionContext.HTTP_REQUEST);
        Map<String, String[]> pMap = request.getParameterMap();
        Set<Integer> processedQuestionId = new HashSet<Integer>();

        for (Map.Entry<String, String[]> entry : pMap.entrySet()) {
            String key = entry.getKey();
            int questionid = -1;
            if (key.startsWith("q")) {
                questionid = Integer.valueOf(key.substring(1));
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
                        score = score.add(choice.getScore());
                    }
                }

                Answer answer = new Answer();
                answer.setSurvey(survey);
                answer.setPatient(patient);
                answer.setDoctor(doctor);
                answer.setRetrieveInfo(retrieveInfo);
                answer.setQuestion(question);
                answer.setChoices(choiceset);
                if (pMap.containsKey("tq" + questionid)) {
                    String tmpKey = "tq"+questionid;
                    if (!pMap.get(tmpKey)[0].isEmpty()) {
                        answer.setTextChoice(1);
                        answer.setTextChoiceContent(pMap.get("tq" + questionid)[0]);
                    }
                    processedQuestionId.add(questionid);
                }
                answers.add(answer);

/*                if (answerService.addAnswer(answer)) {
                    answers.add(answer);
                }*/
            } else {
                System.out.println("key " + key + " not processed");
            }
        }

        // add answer for text quesiton
        for (Map.Entry<String, String[]> entry : pMap.entrySet()) {
            String key = entry.getKey();
            int questionid = -1;
            if (key.startsWith("tq")) {
                questionid = Integer.valueOf(key.substring(2));
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
                answer.setRetrieveInfo(retrieveInfo);
                answer.setQuestion(question);
                answer.setTextChoice(1);
                answer.setTextChoiceContent(pMap.get("tq" + questionid)[0]);
                answers.add(answer);

/*                if (answerService.addAnswer(answer)) {
                    answers.add(answer);
                }*/
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
                answer.setRetrieveInfo(retrieveInfo);
                answer.setQuestion(question);
                answer.setTextChoice(1);
                answers.add(answer);
/*                if (answerService.addAnswer(answer)) {
                    answers.add(answer);
                }*/
            }
        }

        retrieveInfo.setAnswers(answers);

        //RetrieveInfo tmpRetrieveInfo = retrieveService.updateRetrieveInfo(newRetrieveInfo);

        retrieveService.addRetrieveInfo(retrieveInfo);
        RetrieveInfo tmpRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);
        Integer i = (tmpRetrieveInfo==null)?-1:1;
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
        json.put("success", i);

        if (survey.getSurveyName().equals("TRACK儿童呼吸和哮喘控制测试")) {
            if (score.compareTo(new BigDecimal("80")) >= 0) {
                json.put("msg", "本次TRACK测试评分为"+score.toString()+"分，<br/>恭喜您，您孩子的呼吸问题似乎得到了控制。");
            } else {
                json.put("msg", "本次TRACK测试评分为"+score.toString()+"分，<br/>您孩子的呼吸问题可能未得到控制。");
            }
        } else if (survey.getSurveyName().equals("哮喘控制测试评分")) {
            if (score.compareTo(new BigDecimal("19")) <= 0) {
                json.put("msg", "您孩子本次哮喘控制测试得分为"+score.toString()+"分，<br/>您孩子的哮喘未得到有效控制。");
            } else if (score.compareTo(new BigDecimal("20")) >= 0 &&
                    score.compareTo(new BigDecimal("23")) <= 0) {
                json.put("msg", "您孩子本次哮喘控制测试得分为"+score.toString()+"分，<br/>您孩子的哮喘得到了部分控制。");
            } else {
                json.put("msg", "您孩子本次哮喘控制测试得分为"+score.toString()+"分，<br/>恭喜您，您孩子的哮喘得到了完全控制。");
            }
        } else {
            json.put("msg", " ");
        }

        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
