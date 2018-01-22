package com.hospital.action;

import com.hospital.domain.Authorization;
import com.hospital.domain.Doctor;
import com.hospital.service.AuthorizationService;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationAction extends ActionSupport {

    private AuthorizationService authorizationService;

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private int id;
    private String power;

    public void setPower(String power) {
        this.power = power;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorization() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Authorization authorization = new Authorization();
        authorization.setAid(id);
        Authorization newAuthorization = authorizationService.getAuthorizationByaid(authorization);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                //to filter doctor in Authorization
                return obj instanceof Doctor || name.equals("doctor");
            }
        });

        JSONObject jsonObject = JSONObject.fromObject(newAuthorization, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String addAuthorization() {
        Authorization authorization = new Authorization();
        authorization.setAid(id);
        String[] str = power.split(",");
        for (String s : str) {
            if (s.equals("typeSet")) {
                authorization.setTypeSet(1);
            }
            if (s.equals("surveySet")) {
                authorization.setSurveySet(1);
            }
            if (s.equals("patientSet")) {
                authorization.setPatientSet(1);
            }
            if (s.equals("deliverySet")) {
                authorization.setSurveySet(1);
            }
            if (s.equals("retrieveSet")) {
                authorization.setRetrieveSet(1);
            }
            if (s.equals("forfeitSet")) {
                authorization.setForfeitSet(1);
            }
            if (s.equals("sysSet")) {
                authorization.setSysSet(1);
            }
        }
//		Doctor doctor = new Doctor();
//		doctor.setAid(id);
//		authorization.setDoctor(doctor);
        Authorization newAuthorization = authorizationService.updateAuthorization(authorization);
        int success = 0;
        if (newAuthorization != null) {
            success = 1;
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
