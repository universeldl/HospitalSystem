package com.hospital.action;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.Doctor;
import com.hospital.domain.Authorization;
import com.hospital.domain.RetrieveInfo;
import com.hospital.domain.ForfeitInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.service.ForfeitService;
import com.opensymphony.xwork2.ActionSupport;

public class ForfeitAction extends ActionSupport {

    private ForfeitService forfeitService;

    public void setForfeitService(ForfeitService forfeitService) {
        this.forfeitService = forfeitService;
    }

    private int pageCode;

    private int deliveryId;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }


    public String queryForfeitInfo() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<ForfeitInfo> pb = null;
        Patient patient = (Patient) ServletActionContext.getContext().getSession().get("patient");
        if (deliveryId == 0) {
            pb = forfeitService.findMyForfeitInfoByPage(patient, pageCode, pageSize);
        } else {
            pb = forfeitService.queryForfeitInfo(patient.getOpenID(), deliveryId, pageCode, pageSize);
        }
        if (pb != null) {
            pb.setUrl("queryForfeitInfo.action?deliveryId=" + deliveryId + "&");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    public String findMyForfeitInfoByPage() {
        Patient patient = (Patient) ServletActionContext.getContext().getSession().get("patient");
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<ForfeitInfo> pb = null;
        pb = forfeitService.findMyForfeitInfoByPage(patient, pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findMyForfeitInfoByPage.action?");
        }
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }

    public String getForfeitInfoById() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        ForfeitInfo forfeitInfo = new ForfeitInfo();
        forfeitInfo.setDeliveryId(deliveryId);
        ForfeitInfo newForfeitInfo = forfeitService.getForfeitInfoById(forfeitInfo);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                return obj instanceof Authorization || name.equals("authorization") || obj instanceof Set || name.equals("deliveryInfos");
            }
        });


        JSONObject jsonObject = JSONObject.fromObject(newForfeitInfo, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


}
