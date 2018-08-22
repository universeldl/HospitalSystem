package com.hospital.action;

import com.hospital.domain.Patient;
import com.hospital.service.PatientService;
import com.hospital.util.AccessTokenMgr;
import com.hospital.util.AliOssConfig;
import com.hospital.util.GetOpenIdOauth2;
import com.hospital.util.WeChatConfig;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by QQQ on 2017/12/23.
 */
public class uploadImgAction extends ActionSupport {
    private String code;
    public void setCode(String code) {
        this.code = code;
    }

    private String urlString;
    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }


    private PatientService patientService;
    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public String getSign() {
        if(urlString.isEmpty()) {
            return null;
        }
        String sign = WeChatConfig.getJsapiConfig(urlString);
        try {
            ServletActionContext.getResponse().getWriter().print(sign);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String upload() {

        HttpServletRequest request = ServletActionContext.getRequest();

/*        System.out.println("upload in");
        if (code == null) {
            request.setAttribute("errorMsg", "获取用户失败");
            return ERROR;
        }

        AccessTokenMgr mgr = AccessTokenMgr.getInstance();
        String open_id = GetOpenIdOauth2.getOpenId(code, mgr);

        if (open_id == null) {
            request.setAttribute("errorMsg", "获取用户名失败，请稍后再试");
            return ERROR;
        }

        Patient patient = new Patient();
        patient.setOpenID(open_id);
        Patient patient1 = patientService.getPatient(patient);*/

/*        if (patient1 == null) {
            request.setAttribute("errorMsg", "未注册的用户");
            return ERROR;
        }*/

        String wechatConfig = WeChatConfig.getJsapiConfig(request.getRequestURL().toString() + "?"
                + request.getQueryString());
        request.setAttribute("wechatConfig", wechatConfig);

        String ossConfig = AliOssConfig.getPostPolicyString();
        request.setAttribute("ossConfig", ossConfig);
/*
        String wechatToken = AliOssConfig.getPostPolicyString();
        request.setAttribute("ossConfig", ossConfig);*/

        return SUCCESS;
    }
}
