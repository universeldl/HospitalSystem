package com.hospital.action;

import com.hospital.domain.Authorization;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.domain.RetrieveInfo;
import com.hospital.service.RetrieveService;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class DeliveryAction extends ActionSupport {

    private RetrieveService retrieveService;

    public void setRetrieveService(RetrieveService retrieveService) {
        this.retrieveService = retrieveService;
    }


    private int pageCode;
    private int deliveryId;


    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }


    public String findMyDeliveryInfoByPage() {
        Patient patient = (Patient) ServletActionContext.getContext().getSession().get("patient");
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<RetrieveInfo> pb = null;
        pb = retrieveService.findMyDeliveryInfoByPage(patient, pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findMyDeliveryInfoByPage.action?");
        }
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    public String queryDeliverySearchInfo() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<RetrieveInfo> pb = null;
        Patient patient = (Patient) ServletActionContext.getContext().getSession().get("patient");
        if (deliveryId == 0) {
            retrieveService.findMyDeliveryInfoByPage(patient, pageCode, pageSize);
        } else {
            pb = retrieveService.queryRetrieveInfo(patient.getOpenID(), deliveryId, pageCode, pageSize);
        }
        if (pb != null) {
            pb.setUrl("queryDeliverySearchInfo.action?deliveryId=" + deliveryId + "&");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }

    public String getRetrieveInfoById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        RetrieveInfo retrieveInfo = new RetrieveInfo();
        retrieveInfo.setDeliveryId(deliveryId);
        RetrieveInfo newRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);
        JsonConfig jsonConfig = new JsonConfig();
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

}
