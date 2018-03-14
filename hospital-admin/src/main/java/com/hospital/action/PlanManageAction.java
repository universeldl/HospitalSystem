package com.hospital.action;

import com.hospital.domain.Doctor;
import com.hospital.domain.PatientType;
import com.hospital.domain.Plan;
import com.hospital.domain.Survey;
import com.hospital.service.PlanService;
import com.hospital.service.SurveyService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanManageAction extends ActionSupport {

    public PlanService planService;
    public SurveyService surveyService;

    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }

    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    //private Integer beginAge;
    //private Integer endAge;
    private Integer oldPatientOnly;
    private Integer active;
    private Integer sex;
    private Integer patientType;
    private Integer planId;

    /*
    public void setBeginAge(Integer beginAge) {
        this.beginAge = beginAge;
    }


    public void setEndAge(Integer endAge) {
        this.endAge = endAge;
    }
    */

    public void setOldPatientOnly(Integer oldPatientOnly) {
        this.oldPatientOnly = oldPatientOnly;
    }

    public void setActive(Integer active) {
        this.active = active;
    }


    public void setSex(Integer sex) {
        this.sex = sex;
    }


    public void setPlanId(Integer planId) {
        this.planId = planId;
    }


    public void setPatientType(Integer patientType) {
        this.patientType = patientType;
    }


    public String getAllPlan() {

        List<Plan> allPlan = planService.getAllPlan();
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("plans", allPlan);
        return "success";
    }


    /**
     * 得到指定的病人类型信息
     *
     * @return
     */
    public String getPlan() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Plan plan = new Plan();
        plan.setPlanId(planId);
        Plan newPlan = planService.getPlanById(plan);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONObject jsonObject = JSONObject.fromObject(newPlan, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String updatePlan() {

        System.out.println("update plan oldPatientOnly = " + oldPatientOnly);
        Plan plan = new Plan();
        plan.setPlanId(planId);
        Plan updatePlan = planService.getPlanById(plan);
        //updatePlan.setBeginAge(beginAge);
        //updatePlan.setEndAge(endAge);
        updatePlan.setOldPatientOnly(oldPatientOnly);
        updatePlan.setActive(active);
        updatePlan.setSex(sex);
        PatientType type = new PatientType();
        type.setPatientTypeId(patientType);
        updatePlan.setPatientType(type);

        updatePlan.getSurveys().clear();
        List<Integer> surveyIds = new ArrayList<>();
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        Map<String, String[]> pMap = request.getParameterMap();
        int idx = 0;
        for (String[] value : pMap.values()) {
            if (idx < 5) {
                idx++;
            } else {
                for (String v : value) {
                    surveyIds.add(Integer.parseInt(v));
                }
                idx++;
            }
        }
        for (int i = 0; i < surveyIds.size(); i++) {
            Survey survey = new Survey();
            survey.setSurveyId(surveyIds.get(i));
            Survey addSurvey = surveyService.getSurveyById(survey);//得到问卷
            updatePlan.getSurveys().add(addSurvey);
        }

        Plan newPlan = planService.updatePlan(updatePlan);
        int success = 0;
        if (newPlan != null) {
            success = 1;
            //由于是转发并且js页面刷新,所以无需重查
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String addPlan() {
        boolean b = false;
        //得到当前医生
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        //得到当前病人类型
        PatientType type = new PatientType();
        type.setPatientTypeId(patientType);
        //Plan plan = new Plan(beginAge, endAge, sex, active, type, doctor);
        Plan plan = new Plan(oldPatientOnly, sex, active, type, doctor);

        List<Integer> surveyIds = new ArrayList<>();
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        Map<String, String[]> pMap = request.getParameterMap();
        int idx = 0;
        for (String[] value : pMap.values()) {
            if (idx < 4) {
                idx++;
            } else {
                for (String v : value) {
                    surveyIds.add(Integer.parseInt(v));
                }
                idx++;
            }
        }
        for (int i = 0; i < surveyIds.size(); i++) {
            Survey survey = new Survey();
            survey.setSurveyId(surveyIds.get(i));
            Survey addSurvey = surveyService.getSurveyById(survey);//得到问卷
            b = plan.getSurveys().add(addSurvey);
            if (!b) break;//break whenever add failing
        }
        b = planService.addPlan(plan);
        int success = 0;
        if (b) {
            success = 1;
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);//向浏览器响应是否成功的状态码
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String getAllPlans() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<Plan> allPlans = planService.getAllPlan();

        String json = JSONArray.fromObject(allPlans).toString();//List------->JSONArray
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 删除随访计划
     *
     * @return
     */
    public String deletePlan() {
        Plan plan = new Plan();
        plan.setPlanId(planId);
        boolean deletePlan = planService.deletePlan(plan);
        int success = 0;
        if (deletePlan) {
            success = 1;
            //由于是转发并且js页面刷新,所以无需重查
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

}
