package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import com.hospital.util.CalendarUtils;

public class RetrieveManageAction extends ActionSupport {

    private RetrieveService retrieveService;
    private DeliveryService deliveryService;
    private PatientService patientService;
    private AnswerService answerService;
    private ChoiceService choiceService;

    public void setRetrieveService(RetrieveService retrieveService) {
        this.retrieveService = retrieveService;
    }

    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setAnswerService(AnswerService answerService) {
        this.answerService = answerService;
    }

    public void setChoiceService(ChoiceService choiceService) {
        this.choiceService = choiceService;
    }

    private int pageCode;
    private Set<Answer> myAnswers;

    private int deliveryId;
    private int answerId;
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

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
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


    /**
     * 获取答案
     *
     * @return
     */
    public String getAnswerById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        Answer newAnswer = answerService.getAnswerById(answer);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONObject jsonObject = JSONObject.fromObject(newAnswer, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * 修改答案
     *
     * @return
     */
    public String updateAnswer() {
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        Answer updateAnswer = answerService.getAnswerById(answer);
        String modifiedDate = CalendarUtils.getNowTime();//得到当前时间作为最后修改时间
        updateAnswer.setModifiedDate(modifiedDate);
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        if(updateAnswer.getPatient().getDoctor().getAid().equals(doctor.getAid())) {
            updateAnswer.setLastModified(1);
        }
        else if(updateAnswer.getPatient().getAddnDoctor().getAid().equals(doctor.getAid())) {
            updateAnswer.setLastModified(2);
        }

        updateAnswer.getChoices().clear();    //clean existing choices in answer

        List<Integer> choices = new ArrayList<>();
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        Map<String, String[]> pMap = request.getParameterMap();

        if (updateAnswer.getQuestion().getQuestionType() == 1) {  //多选
            int idx = 1;
            if (pMap.size() == 2) {
                for (String[] value : pMap.values()) {
                    if (idx == 2) {
                        for (int i = 0; i < value.length; i++) {
                            choices.add(Integer.parseInt(value[i]));
                        }
                    }
                    idx++;
                }
            } else if (pMap.size() == 3) {
                boolean hasText = false;
                for (String[] value : pMap.values()) {
                    if (idx == 2) {
                        if (value[value.length - 1].equals("99999")) {
                            hasText = true;
                            for (int i = 0; i < value.length - 1; i++) {
                                choices.add(Integer.parseInt(value[i]));
                            }
                        } else {
                            for (int i = 0; i < value.length; i++) {
                                choices.add(Integer.parseInt(value[i]));
                            }
                        }
                    } else if (idx == 3) {
                        if (hasText) {
                            updateAnswer.setTextChoice(1);
                            updateAnswer.setTextChoiceContent(value[0]);
                        } else {
                            updateAnswer.setTextChoice(0);
                            updateAnswer.setTextChoiceContent("");
                        }
                    }
                    idx++;
                }
            }

            for (int i = 0; i < choices.size(); i++) {  //add new choices to db and question
                Choice choice = new Choice();
                choice.setChoiceId(choices.get(i));
                Choice cho = choiceService.getChoiceById(choice);
                updateAnswer.getChoices().add(cho);
            }

        } else if (updateAnswer.getQuestion().getQuestionType() == 2) {  //单选
            int idx = 1;
            if (pMap.size() == 2) {
                for (String[] value : pMap.values()) {
                    if (idx == 2) {
                        Choice choice = new Choice();
                        choice.setChoiceId(Integer.parseInt(value[0]));
                        Choice cho = choiceService.getChoiceById(choice);
                        updateAnswer.getChoices().add(cho);
                    }
                    idx++;
                }
            } else if (pMap.size() == 3) {
                boolean hasText = false;
                for (String[] value : pMap.values()) {
                    if (idx == 2) {
                        if (value[0].equals("99999")) {
                            hasText = true;
                        } else {
                            Choice choice = new Choice();
                            choice.setChoiceId(Integer.parseInt(value[0]));
                            Choice cho = choiceService.getChoiceById(choice);
                            updateAnswer.getChoices().add(cho);
                        }
                    } else if (idx == 3) {
                        if (hasText) {
                            updateAnswer.setTextChoice(1);
                            updateAnswer.setTextChoiceContent(value[0]);
                        } else {
                            updateAnswer.setTextChoice(0);
                            updateAnswer.setTextChoiceContent("");
                        }
                    }
                    idx++;
                }
            }

        } else if (updateAnswer.getQuestion().getQuestionType() == 3) {  //问答
            if (pMap.size() == 2) {
                for (String[] value : pMap.values()) {
                    updateAnswer.setTextChoice(1);
                    updateAnswer.setTextChoiceContent(value[0]);
                }
            }
        }

        Answer newAnswer = answerService.updateAnswerInfo(updateAnswer);  //update question
        RetrieveInfo retrieveInfo = retrieveService.getRetrieveInfoById(newAnswer.getRetrieveInfo());
        RetrieveInfo RI = retrieveService.updateRetrieveInfo(retrieveInfo);

        int success = 0;
        if (newAnswer != null && RI!=null) {
            success = 1;
        } else {
            success = 0;
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
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

        if (deliveryId != 0) {
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

