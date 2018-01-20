package com.hospital.action.interceptor;

import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.Doctor;
import com.hospital.domain.Authorization;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

@SuppressWarnings("serial")
public class DeliveryInterceptor implements Interceptor {

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
            if (authorization.getDeliverySet() == 1 || authorization.getSuperSet() == 1) {
                return invocation.invoke();
            }
        }
        return "nopass";
    }

}
