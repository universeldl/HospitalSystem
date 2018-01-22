package com.hospital.action;

import com.hospital.domain.Authorization;
import com.hospital.domain.Doctor;
import com.hospital.domain.ForfeitInfo;
import com.hospital.domain.PageBean;
import com.hospital.service.ForfeitService;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class ForfeitManageAction extends ActionSupport {

    private ForfeitService forfeitService;

    public void setForfeitService(ForfeitService forfeitService) {
        this.forfeitService = forfeitService;
    }

    private int pageCode;

    private int deliveryId;
    private String openID;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }


    public String queryForfeitInfo() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<ForfeitInfo> pb = null;
        if ("".equals(openID.trim()) && deliveryId == 0) {
            pb = forfeitService.findForfeitInfoByPage(pageCode, pageSize);
        } else {
            pb = forfeitService.queryForfeitInfo(openID, deliveryId, pageCode, pageSize);
        }
        if (pb != null) {
            pb.setUrl("queryForfeitInfo.action?openID=" + openID + "&deliveryId=" + deliveryId + "&");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    public String findForfeitInfoByPage() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<ForfeitInfo> pb = forfeitService.findForfeitInfoByPage(pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findForfeitInfoByPage.action?");
        }
        //存入request域中
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


    public String payForfeit() {
        //支付提醒步骤
        /*
         * 1. 得到分发记录
         *
         * 2. 查看当前的分发状态
         * 		2.1 如果当前状态为未答卷(逾期未答卷,分发逾期未答卷),则提示病人先去答卷再来设置提醒,返回-1
         * 		2.2 如果当前状态为答卷,则继续下一步
         *
         * 3. 获得当前的医生
         *
         * 4. 为当前提醒记录进行设置为已支付并设置医生
         *
         * 5. 修改提醒记录
         */
        ForfeitInfo forfeitInfo = new ForfeitInfo();
        forfeitInfo.setDeliveryId(deliveryId);
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        forfeitInfo.setDoctor(doctor);
        int pay = forfeitService.payForfeit(forfeitInfo);
        try {
            ServletActionContext.getResponse().getWriter().print(pay);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
