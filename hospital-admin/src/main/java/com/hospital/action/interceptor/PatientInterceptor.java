package com.hospital.action.interceptor;

import com.hospital.domain.Authorization;
import com.hospital.domain.Doctor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;

import java.util.Map;

@SuppressWarnings("serial")
public class PatientInterceptor implements Interceptor {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Map sessionMap = ServletActionContext.getContext().getSession();

        Object obj = sessionMap.get("doctor");
        if (obj != null && obj instanceof Doctor) {
            Doctor doctor = (Doctor) obj;
            Authorization authorization = doctor.getAuthorization();
            if (authorization.getPatientSet() == 1 || authorization.getSuperSet() == 1) {
                return invocation.invoke();
            }
        }
        return "nopass";
    }

}
