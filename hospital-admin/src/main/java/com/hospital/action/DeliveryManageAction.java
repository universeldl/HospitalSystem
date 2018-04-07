package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.DeliveryService;
import com.hospital.service.PatientService;
import com.hospital.service.SurveyService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class DeliveryManageAction extends ActionSupport {

    private DeliveryService deliveryService;
    private SurveyService surveyService;
    private PatientService patientService;

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    private int pageCode;
    private int deliveryId;
    private int queryType;
    private int patientId;
    private String openID;
    private String pwd;
    private String patientName;
    private int surveyId;

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
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
            pb.setUrl("findDeliveryInfoByPage.action?patientId=" + patientId + "&");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "delivery";
    }


    public String findDeliveryInfoByPageByPatient() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        Patient patient = new Patient();
        patient.setPatientId(patientId);
        PageBean<DeliveryInfo> pb = deliveryService.findDeliveryInfoByPage(pageCode, pageSize, patient);
        if (pb != null) {
            pb.setUrl("findDeliveryInfoByPageByPatient.action?patientId=" + patientId + "&");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    public String findPatientAllInOneBK() {
        Patient patient = new Patient();
        patient.setPatientId(patientId);

        Patient patient1 = patientService.getPatientById(patient);
        // arr is a 2-D array of {surveyId-questionId}.
        // If questionId==-1, will output total scores of this survey
        // If questionId>0, will output the answers of this specified question
        /*
        int[][] arr = {{4, 112}, {4, 113},{1,-1},{6,-1},{5, -1},
                {4,121},{4, 122},{4,123},{4,124},
                {4,125},{4,177},{4,126},{4,127}};//answers for 1st question of survey1; total score for survey1

        String[] titles = {
        "哮喘发作次数", "感染次数","TRACK儿童呼吸和哮喘控制测试总分","哮喘控制测试评分总分","生命质量随访问卷总分",
        "在过去4周内，您的孩子白天出现哮喘症状（持续几分钟）是否多于1次/周？","在过去4周内，您的孩子是否有因哮喘而出现至少1次的活动受限？",
        "在过去4周内，您的孩子是否需要使用缓解药物多于1次/周？","在过去4周内，您的孩子是否因哮喘而出现至少1次的夜间醒来或夜间咳嗽？",
        "在过去4周内，您的孩子白天出现哮喘症状是否多于2次/周？","在过去4周内，您的孩子是否因哮喘而出现至少1次的活动受限？",
        "在过去4周内，您的孩子是否需要使用缓解药物多于2次/周？","在过去的4周内，您的孩子是否因哮喘而出现至少1次的夜间醒来或夜间咳嗽？"
        };
        */

        int[][] arr = {{4, 112}, {4, 113}, {1, -1}, {5, -1}, {4, -1}};
        String[] titles = {"哮喘发作次数", "感染次数", "哮喘控制测试评分", "生命质量评分", "GINA"};

        int maxTotalMonth = 0;//max total months to display
        //get maxTotalMonth
        for (int i = 0; i < arr.length; i++) {
            Survey s = new Survey();
            s.setSurveyId(arr[i][0]);
            Survey survey = surveyService.getSurveyById(s);
            if (survey != null) {
                int totalMonth = survey.getFrequency() * survey.getTimes() + 2;// +1 to include the titles in first col; +1 again to include the date of sign-in in 2nd col.
                if (totalMonth > maxTotalMonth) {
                    maxTotalMonth = totalMonth;
                }
            }
        }

        JSONArray jsonArray = new JSONArray();

        //create the first row
        JSONObject object = new JSONObject();
        object.put("col0", "");//1st col is ""
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(patient1.getCreateTime());
        Date createDate = calendar.getTime();
        String dateString = formatter.format(createDate);
        object.put("col1", dateString);//2nd col, create date
        for (int j = 2; j < maxTotalMonth; j++) {//from the 3rd col till the end
            calendar.add(Calendar.MONTH, 1);
            Date sendDate = calendar.getTime();
            String ds = formatter.format(sendDate);
            object.put("col" + j, ds);
        }
        jsonArray.add(0, object);


        //calculate the remaining rows of the table
        for (int i = 0; i < arr.length; i++) {
            Survey s = new Survey();
            s.setSurveyId(arr[i][0]);
            Survey survey = surveyService.getSurveyById(s);
            if (survey != null) {
                if (survey.isSendOnRegister()) {
                    //there will be only one retrieveInfo for this survey, get it and the remaining col will all be "".
                    if (arr[i][1] == -1) {//calculate total scores
                        if (survey.getSurveyId() == 1) {
                            JSONObject obj = new JSONObject();
                            obj.put("col0", titles[i]);//1st col
                            for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();

                                if (retrieveInfo != null && (deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId()) ||
                                        deliveryInfo.getSurvey().getSurveyId().equals(-6))) {//there will be only one
                                    BigDecimal totalScore = new BigDecimal("0");
                                    for (Answer answer : retrieveInfo.getAnswers()) {
                                        for (Choice choice : answer.getChoices()) {//should be single choice
                                            totalScore = totalScore.add(choice.getScore());
                                        }
                                    }
                                    obj.put("col1", totalScore);//2nd col
                                    break;
                                }
                            }
                            for (int j = 2; j < maxTotalMonth; j++) {//from the 3rd col will all be ""
                                obj.put("col" + j, "");
                            }
                            jsonArray.add(i + 1, obj);
                        } else if (survey.getSurveyId() == 4) {
                            JSONObject obj = new JSONObject();
                            obj.put("col0", titles[i]);//1st col
                            for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();

                                if (retrieveInfo != null && (deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId()) ||
                                        deliveryInfo.getSurvey().getSurveyId().equals(-6))) {//there will be only one
                                    BigDecimal totalScore = new BigDecimal("0");
                                    for (Answer answer : retrieveInfo.getAnswers()) {
                                        if (answer.getQuestion().getQuestionId() != 121 ||
                                                answer.getQuestion().getQuestionId() != 122 ||
                                                answer.getQuestion().getQuestionId() != 123 ||
                                                answer.getQuestion().getQuestionId() != 124 ||
                                                answer.getQuestion().getQuestionId() != 125 ||
                                                answer.getQuestion().getQuestionId() != 126 ||
                                                answer.getQuestion().getQuestionId() != 127 ||
                                                answer.getQuestion().getQuestionId() != 177) {
                                            continue;
                                        }
                                        for (Choice choice : answer.getChoices()) {//should be single choice
                                            totalScore = totalScore.add(choice.getScore());
                                        }
                                    }
                                    String str = new String();
                                    if (totalScore.compareTo(new BigDecimal("0")) == 0) {
                                        str = "完全控制";
                                    } else if (totalScore.compareTo(new BigDecimal("1")) == 0 ||
                                            totalScore.compareTo(new BigDecimal("2")) == 0) {
                                        str = "部分控制";
                                    } else {
                                        str = "未控制";
                                    }
                                    obj.put("col1", str);//2nd col
                                    break;
                                }
                            }
                            for (int j = 2; j < maxTotalMonth; j++) {//from the 3rd col will all be ""
                                obj.put("col" + j, "");
                            }
                            jsonArray.add(i + 1, obj);
                        } else {
                            JSONObject obj = new JSONObject();
                            obj.put("col0", titles[i]);//1st col
                            for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();

                                if (retrieveInfo != null && deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) {//there will be only one
                                    BigDecimal totalScore = new BigDecimal("0");
                                    for (Answer answer : retrieveInfo.getAnswers()) {
                                        for (Choice choice : answer.getChoices()) {//should be single choice
                                            totalScore = totalScore.add(choice.getScore());
                                        }
                                    }
                                    obj.put("col1", totalScore);//2nd col
                                    break;
                                }
                            }
                            for (int j = 2; j < maxTotalMonth; j++) {//from the 3rd col will all be ""
                                obj.put("col" + j, "");
                            }
                            jsonArray.add(i + 1, obj);
                        }
                    } else {//get all answers
                        for (Question question : survey.getQuestions()) {
                            if (question.getQuestionId() == arr[i][1]) {//question is found

                                JSONObject obj = new JSONObject();
                                obj.put("col0", titles[i]);//1st col
                                //for each retrieveInfo, get the answer choice of that question
                                for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                    RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();

                                    if (retrieveInfo != null && deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) {

                                        //for each answer, get the choice of that question
                                        for (Answer answer : retrieveInfo.getAnswers()) {
                                            if (answer.getQuestion().getQuestionId() == question.getQuestionId()) {//answer is found
                                                if (answer.getTextChoice() == 1) {//text choice
                                                    obj.put(answer.getTextChoiceContent(), answer.getTextChoiceContent());
                                                } else {//choice
                                                    for (Choice choice : answer.getChoices()) {//should be single choice
                                                        obj.put("col1", choice.getChoiceContent());//2nd col
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                                for (int j = 2; j < maxTotalMonth; j++) {//from the 3rd col will all be ""
                                    obj.put("col" + j, "");
                                }
                                jsonArray.add(i + 1, obj);
                                break;
                            }
                        }
                    }
                } else {
                    //tmp can start from 1, set tmp[0] col to titles and tmp[1] to "".
                    if (arr[i][1] == -1) {//calculate total scores
                        if (survey.getSurveyId() == 1) {
                            int shift = 2;
                            int tmp = 2;
                            JSONObject obj = new JSONObject();
                            obj.put("col0", titles[i]);//1st col
                            obj.put("col1", "");//2nd col
                            //for each retrieveInfo, get the total score
                            for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();
                                shift += survey.getFrequency();
                                while (tmp < shift) {
                                    obj.put("col" + tmp, "");
                                    tmp++;
                                }

                                if (retrieveInfo != null && (deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId()) ||
                                        deliveryInfo.getSurvey().getSurveyId().equals(6))) {
                                    BigDecimal totalScore = new BigDecimal("0");
                                    for (Answer answer : retrieveInfo.getAnswers()) {
                                        for (Choice choice : answer.getChoices()) {//should be single choice
                                            totalScore = totalScore.add(choice.getScore());
                                        }
                                    }
                                    obj.put("col" + shift, totalScore);
                                    tmp++;
                                }
                            }
                            for (int j = shift; j < maxTotalMonth; j++) {
                                obj.put("col" + j, "");
                            }
                            jsonArray.add(i + 1, obj);
                        } else if (survey.getSurveyId() == 4) {
                            int shift = 2;
                            int tmp = 2;
                            JSONObject obj = new JSONObject();
                            obj.put("col0", titles[i]);//1st col
                            obj.put("col1", "");//2nd col
                            //for each retrieveInfo, get the total score
                            for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();
                                shift += survey.getFrequency();
                                while (tmp < shift) {
                                    obj.put("col" + tmp, "");
                                    tmp++;
                                }

                                if (retrieveInfo != null && deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) {

                                    BigDecimal totalScore = new BigDecimal("0");
                                    for (Answer answer : retrieveInfo.getAnswers()) {
                                        if (answer.getQuestion().getQuestionId() != 121 ||
                                                answer.getQuestion().getQuestionId() != 122 ||
                                                answer.getQuestion().getQuestionId() != 123 ||
                                                answer.getQuestion().getQuestionId() != 124 ||
                                                answer.getQuestion().getQuestionId() != 125 ||
                                                answer.getQuestion().getQuestionId() != 126 ||
                                                answer.getQuestion().getQuestionId() != 127 ||
                                                answer.getQuestion().getQuestionId() != 177) {
                                            continue;
                                        }
                                        for (Choice choice : answer.getChoices()) {//should be single choice
                                            totalScore = totalScore.add(choice.getScore());
                                        }
                                    }
                                    String str;
                                    if (totalScore.compareTo(new BigDecimal("0")) == 0) {
                                        str = "完全控制";
                                    } else if (totalScore.compareTo(new BigDecimal("1")) == 0 ||
                                            totalScore.compareTo(new BigDecimal("2")) == 0) {
                                        str = "部分控制";
                                    } else {
                                        str = "未控制";
                                    }
                                    obj.put("col" + shift, str);
                                    tmp++;
                                }
                            }
                            for (int j = shift; j < maxTotalMonth; j++) {
                                obj.put("col" + j, "");
                            }
                            jsonArray.add(i + 1, obj);
                        } else {
                            int shift = 2;
                            int tmp = 2;
                            JSONObject obj = new JSONObject();
                            obj.put("col0", titles[i]);//1st col
                            obj.put("col1", "");//2nd col
                            //for each retrieveInfo, get the total score
                            for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();
                                shift += survey.getFrequency();
                                while (tmp < shift) {
                                    obj.put("col" + tmp, "");
                                    tmp++;
                                }

                                if (retrieveInfo != null && deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) {

                                    BigDecimal totalScore = new BigDecimal("0");
                                    for (Answer answer : retrieveInfo.getAnswers()) {
                                        for (Choice choice : answer.getChoices()) {//should be single choice
                                            totalScore = totalScore.add(choice.getScore());
                                        }
                                    }
                                    obj.put("col" + shift, totalScore);
                                    tmp++;
                                }
                            }
                            for (int j = shift; j < maxTotalMonth; j++) {
                                obj.put("col" + j, "");
                            }
                            jsonArray.add(i + 1, obj);
                        }
                    } else {//get all answers
                        for (Question question : survey.getQuestions()) {
                            if (question.getQuestionId() == arr[i][1]) {//question is found

                                int shift = 2;
                                int tmp = 2;
                                JSONObject obj = new JSONObject();
                                obj.put("col0", titles[i]);//1st col
                                obj.put("col1", "");//2nd col
                                //for each retrieveInfo, get the answer choice of that question
                                for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                    RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();
                                    shift += survey.getFrequency();
                                    while (tmp < shift) {
                                        obj.put("col" + tmp, "");
                                        tmp++;
                                    }

                                    if (retrieveInfo != null && deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) {

                                        //for each answer, get the choice of that question
                                        for (Answer answer : retrieveInfo.getAnswers()) {
                                            if (answer.getQuestion().getQuestionId() == question.getQuestionId()) {//answer is found
                                                if (answer.getTextChoice() == 1) {//text choice
                                                    obj.put(answer.getTextChoiceContent(), answer.getTextChoiceContent());
                                                } else {//choice
                                                    for (Choice choice : answer.getChoices()) {//should be single choice
                                                        obj.put("col" + shift, choice.getChoiceContent());
                                                    }
                                                    tmp++;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                                for (int j = shift; j < maxTotalMonth; j++) {
                                    obj.put("col" + j, "");
                                }
                                jsonArray.add(i + 1, obj);
                                break;
                            }
                        }
                    }
                }
            }
        }

        //存入request域中
        ServletActionContext.getRequest().setAttribute("allInOne", jsonArray);

        return "allInOne";
    }


    public String findPatientAllInOne() {
        Patient patient = new Patient();
        patient.setPatientId(patientId);

        Patient patient1 = patientService.getPatientById(patient);
        // arr is a 2-D array of {surveyId-questionId}.
        // If questionId==-1, will output total scores of this survey
        // If questionId>0, will output the answers of this specified question

        int[][] arr = {{4, 112}, {4, 113}, {1, -1}, {5, -1}, {4, -1}};
        String[] titles = {"哮喘发作次数", "感染次数", "哮喘控制测试评分", "生命质量评分", "GINA"};

        int maxTotalMonth = 0;//max total months to display
        //get maxTotalMonth
        for (int i = 0; i < arr.length; i++) {
            Survey s = new Survey();
            s.setSurveyId(arr[i][0]);
            Survey survey = surveyService.getSurveyById(s);
            if (survey != null) {
                int totalMonth = survey.getFrequency() * survey.getTimes() + 2;// +1 to include the titles in first col; +1 again to include the date of sign-in in 2nd col.
                if (totalMonth > maxTotalMonth) {
                    maxTotalMonth = totalMonth;
                }
            }
        }

        JSONArray jsonArray = new JSONArray();
        List<String> headers = new LinkedList<>();// store the first row, to be headers of the remaining rows

        //create the first row
        JSONObject object = new JSONObject();
        object.put("col0", "");//1st col is ""
        headers.add("col0");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(patient1.getCreateTime());
        Date createDate = calendar.getTime();
        String dateString = formatter.format(createDate);
        object.put(dateString, dateString);//2nd col, create date
        headers.add(dateString);
        for (int j = 2; j < maxTotalMonth; j++) {//from the 3rd col till the end
            calendar.add(Calendar.MONTH, 1);
            Date sendDate = calendar.getTime();
            String ds = formatter.format(sendDate);
            object.put(ds, ds);
            headers.add(ds);
        }
        jsonArray.add(0, object);


        //fill in the remaining rows of the table with ""
        //for (int i = 1; i < arr.length; i++) {
        //    JSONObject obj = new JSONObject();
        //    for (String header : headers) {
        //        obj.put(header, "");
        //    }
        //    jsonArray.add(i, obj);
        //}


        //calculate the remaining rows of the table
        for (int i = 0; i < arr.length; i++) {
            Survey s = new Survey();
            s.setSurveyId(arr[i][0]);
            Survey survey = surveyService.getSurveyById(s);

            JSONObject obj = new JSONObject();
            for (String header : headers) {
                obj.put(header, "");
            }
            obj.put("col0", titles[i]);//1st col

            if (survey != null) {
                //tmp can start from 1, set tmp[0] col to titles and tmp[1] to "".
                if (arr[i][1] == -1) {//calculate total scores
                    if (survey.getSurveyId() == 1) {
                        //for each retrieveInfo, get the total score
                        for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                            RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();

                            if (retrieveInfo != null && (deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId()) ||
                                    deliveryInfo.getSurvey().getSurveyId().equals(6))) {
                                BigDecimal totalScore = new BigDecimal("0");
                                for (Answer answer : retrieveInfo.getAnswers()) {
                                    for (Choice choice : answer.getChoices()) {//should be single choice
                                        totalScore = totalScore.add(choice.getScore());
                                    }
                                }
                                obj.put(formatter.format(deliveryInfo.getDeliveryDate()), totalScore);
                            }
                        }
                    } else if (survey.getSurveyId() == 4) {
                        //for each retrieveInfo, get the total score
                        for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                            RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();

                            if (retrieveInfo != null && deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) {
                                BigDecimal totalScore = new BigDecimal("0");
                                for (Answer answer : retrieveInfo.getAnswers()) {
                                    if (answer.getQuestion().getQuestionId() != 121 &&
                                            answer.getQuestion().getQuestionId() != 122 &&
                                            answer.getQuestion().getQuestionId() != 123 &&
                                            answer.getQuestion().getQuestionId() != 124 &&
                                            answer.getQuestion().getQuestionId() != 125 &&
                                            answer.getQuestion().getQuestionId() != 126 &&
                                            answer.getQuestion().getQuestionId() != 177 &&
                                            answer.getQuestion().getQuestionId() != 178) {
                                        continue;
                                    }
                                    for (Choice choice : answer.getChoices()) {//should be single choice
                                        totalScore = totalScore.add(choice.getScore());
                                    }
                                }
                                String str;
                                if (totalScore.compareTo(new BigDecimal("0")) == 0) {
                                    str = "完全控制";
                                } else if (totalScore.compareTo(new BigDecimal("1")) == 0 ||
                                        totalScore.compareTo(new BigDecimal("2")) == 0) {
                                    str = "部分控制";
                                } else {
                                    str = "未控制";
                                }
                                obj.put(formatter.format(deliveryInfo.getDeliveryDate()), str);
                            }
                        }
                    } else {
                        //for each retrieveInfo, get the total score
                        for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                            RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();

                            if (retrieveInfo != null && deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) {
                                BigDecimal totalScore = new BigDecimal("0");
                                for (Answer answer : retrieveInfo.getAnswers()) {
                                    for (Choice choice : answer.getChoices()) {//should be single choice
                                        totalScore = totalScore.add(choice.getScore());
                                    }
                                }
                                obj.put(formatter.format(deliveryInfo.getDeliveryDate()), totalScore);
                            }
                        }
                    }
                } else {//get all answers
                    for (Question question : survey.getQuestions()) {
                        if (question.getQuestionId() == arr[i][1]) {//question is found

                            //for each retrieveInfo, get the answer choice of that question
                            for (DeliveryInfo deliveryInfo : deliveryService.getDeliveryInfosByPatientId(patient1)) {
                                RetrieveInfo retrieveInfo = deliveryInfo.getRetrieveInfo();

                                if (retrieveInfo != null && deliveryInfo.getSurvey().getSurveyId().equals(survey.getSurveyId())) {
                                    //for each answer, get the choice of that question
                                    for (Answer answer : retrieveInfo.getAnswers()) {
                                        if (answer.getQuestion().getQuestionId() == question.getQuestionId()) {//answer is found
                                            if (answer.getTextChoice() == 1) {//text choice
                                                obj.put(formatter.format(deliveryInfo.getDeliveryDate()), answer.getTextChoiceContent());
                                            } else {//choice
                                                for (Choice choice : answer.getChoices()) {//should be single choice
                                                    obj.put(formatter.format(deliveryInfo.getDeliveryDate()), choice.getChoiceContent());
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            jsonArray.add(i + 1, obj);
        }

        //存入request域中
        ServletActionContext.getRequest().setAttribute("allInOne", jsonArray);

        return "allInOne";
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
        for (DeliveryInfo deliveryInfo : unanswered) {
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


    public String queryDeliverySearchInfoNew() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<DeliveryInfo> pb = null;
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        if (queryType == 0) {
            pb = deliveryService.findDeliveryInfoByPage(pageCode, pageSize, doctor);
        } else {
            pb = deliveryService.queryDeliveryInfo(queryType, pageCode, pageSize, doctor);
        }
        if (pb != null) {
            pb.setUrl("queryDeliverySearchInfoNew.action?");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "delivery";
    }


    /**
     * 得到问卷的集合
     * ajax请求该方法
     * 返回问卷集合的json对象
     *
     * @return
     */
    public String findAllSurveys() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<Survey> allSurveys = surveyService.findAllSurveys();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                return !name.equals("surveyId") && !name.equals("surveyName");
            }
        });

        String json = JSONArray.fromObject(allSurveys, jsonConfig).toString();//List------->JSONArray
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String queryDeliverySearchInfoForPatient() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<DeliveryInfo> pb = null;
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        if (surveyId == -1) {
            pb = deliveryService.findDeliveryInfoByPage(pageCode, pageSize, patient);
        } else {
            pb = deliveryService.queryDeliveryInfo(surveyId, pageCode, pageSize, patient);
        }
        if (pb != null) {
            pb.setUrl("queryDeliverySearchInfoForPatient.action?surveyId=" + surveyId + "&patientId=" + patientId + "&");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


}
