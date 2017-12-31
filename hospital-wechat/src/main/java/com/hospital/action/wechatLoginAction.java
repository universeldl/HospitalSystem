package com.hospital.action;

import com.hospital.service.PatientService;
import com.hospital.wechat.service.AccessTokenMgr;
import com.hospital.wechat.service.AccessTokenMgrHXTS;
import com.hospital.wechat.service.GetOpenIdOauth2;
import com.opensymphony.xwork2.ActionSupport;
import com.hospital.domain.Patient;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import com.opensymphony.xwork2.ActionContext;

/**
 * Created by QQQ on 2017/12/23.
 */
public class wechatLoginAction extends ActionSupport {

    private PatientService patientService;

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public String login() {
        System.out.println("login called");
        AccessTokenMgr mgr = AccessTokenMgrHXTS.getInstance();
        ActionContext context=ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest)context.get(ServletActionContext.HTTP_REQUEST);
        String code = request.getParameter("code");

        if (code == null) {
            String open_id = GetOpenIdOauth2.getOpenId(code, mgr);
            if (open_id != null) {
                Patient patient = new Patient();
                patient.setOpenID(open_id);
                Patient new_patient = patientService.getPatientByOpenID(patient);
                if (new_patient == null) {
                    System.out.println("new paient not found");
                    return "failed";
                } else {
                    System.out.println("new patient found");
                    ServletActionContext.getContext().getSession().put("patient", new_patient);
                    return "succeed";
                }
            } else {
                return "failed";
            }
        } else {

        }
        return "failed";
    }

}
