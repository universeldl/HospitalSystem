package com.hospital.action;

import com.hospital.domain.Patient;
import com.hospital.domain.PatientType;
import com.hospital.service.PatientService;
import com.hospital.domain.Hospital;
import com.hospital.service.HospitalService;
import com.hospital.service.PatientTypeService;
import com.hospital.util.AccessTokenMgr;
import com.hospital.util.GetOpenIdOauth2;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import java.io.PrintWriter;
import java.util.List;
/**
 * Created by QQQ on 2017/12/23.
 */
public class wechatLoginAction extends ActionSupport {

    String code;

    public void setCode(String code) {
        this.code = code;
    }

    private String errorMsg;

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg(String errorMsg) {
        return errorMsg;
    }

    private PatientService patientService;

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    private HospitalService hospitalService;

    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    private PatientTypeService patientTypeService;
    public void setPatientTypeService(PatientTypeService patientTypeService) {
        this.patientTypeService = patientTypeService;
    }


    public String login() {

        AccessTokenMgr mgr = AccessTokenMgr.getInstance();
        ServletActionContext.getContext().getSession().put("appID", mgr.getAppId());

        List<Hospital> hospitalList = hospitalService.getAllVisibleHospitals();
        ServletActionContext.getRequest().setAttribute("hl", hospitalList);

        if (patientTypeService == null) {
        }

        List<PatientType> patientTypeList = patientTypeService.getAllPatientType();
        ServletActionContext.getRequest().setAttribute("ptl", patientTypeList);


        if (code != null) {
            String open_id = GetOpenIdOauth2.getOpenId(code, mgr);

            // testing
            //if(open_id == null) {
            //    open_id = "oaBonw30UBjZkLW5rf19h7KunM7s";
            //}

            if (open_id != null) {
                Patient patient = new Patient();
                patient.setOpenID(open_id);
                Patient new_patient = patientService.getPatientByOpenID(patient);
                if (new_patient == null) {
                    ServletActionContext.getContext().getSession().put("patient", patient);
                    return NONE;
                } else {
                    return SUCCESS;
                }
            } else {
                errorMsg = "用户名获取错误,请稍后再试";
                return ERROR;
            }
        }
        errorMsg = "无法获取用户名";
        return ERROR;
    }

    public String getAppId() {
        String app_id = AccessTokenMgr.getInstance().getAppId();
        try {
            PrintWriter pw = ServletActionContext.getResponse().getWriter();
            pw.print(app_id);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
