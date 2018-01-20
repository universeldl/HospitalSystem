package com.hospital.action;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.RetrieveInfo;
import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.PageBean;
import com.hospital.service.RetrieveService;
import com.hospital.service.DeliveryService;
import com.opensymphony.xwork2.ActionSupport;

public class DeliverySearchAction extends ActionSupport {

    private RetrieveService retrieveService;

    public void setRetrieveService(RetrieveService retrieveService) {
        this.retrieveService = retrieveService;
    }


    private int pageCode;
    private String openID;
    private int deliveryId;


    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }


    public void setOpenID(String openID) {
        this.openID = openID;
    }


    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }


    public String findRetrieveInfoByPage() {

        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<RetrieveInfo> pb = retrieveService.findRetrieveInfoByPage(pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findRetrieveInfoByPage.action?");
        }
        //存入request域中
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
        if ("".equals(openID.trim()) && deliveryId == 0) {
            pb = retrieveService.findRetrieveInfoByPage(pageCode, pageSize);
        } else {
            pb = retrieveService.queryRetrieveInfo(openID, deliveryId, pageCode, pageSize);
        }
        if (pb != null) {
            pb.setUrl("queryDeliverySearchInfo.action?openID=" + openID + "&deliveryId=" + deliveryId + "&");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }

}
