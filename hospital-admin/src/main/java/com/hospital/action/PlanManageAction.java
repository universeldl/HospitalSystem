package com.hospital.action;

import com.hospital.domain.PatientType;
import com.hospital.domain.Plan;
import com.hospital.service.PlanService;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PlanManageAction extends ActionSupport {

    public PlanService planService;

    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }

    private Integer beginAge;
    private Integer endAge;
    private Integer active;
    private Integer sex;
    private Integer patientType;
    private Integer id;


    public void setBeginAge(Integer beginAge) {
        this.beginAge = beginAge;
    }


    public void setEndAge(Integer endAge) {
        this.endAge = endAge;
    }


    public void setActive(Integer active) {
        this.active = active;
    }


    public void setSex(Integer sex) {
        this.sex = sex;
    }


    public void setId(Integer id) {
        this.id = id;
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
        plan.setPlanId(id);
        Plan newPlan = planService.getPlanById(plan);
        JSONObject jsonObject = JSONObject.fromObject(newPlan);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String updatePlan() {
        Plan plan = new Plan();
        plan.setPlanId(id);
        Plan updatePlan = planService.getPlanById(plan);
        updatePlan.setBeginAge(beginAge);
        updatePlan.setEndAge(endAge);
        updatePlan.setActive(active);
        updatePlan.setSex(sex);
        PatientType type = new PatientType();
        type.setPatientTypeId(patientType);
        updatePlan.setPatientType(type);
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
        Plan plan = new Plan();
        plan.setBeginAge(beginAge);
        plan.setEndAge(endAge);
        plan.setActive(active);
        plan.setSex(sex);
        PatientType type = new PatientType();
        type.setPatientTypeId(patientType);
        plan.setPatientType(type);
        boolean b = planService.addPlan(plan);
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


}
