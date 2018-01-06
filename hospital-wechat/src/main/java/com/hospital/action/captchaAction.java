package com.hospital.action;

import com.hospital.service.CaptchaService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by QQQ on 2018/1/6.
 */
public class captchaAction extends ActionSupport {


    public ByteArrayInputStream inputStream;
    CaptchaService captchaService;

    public void setInputStream(ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setCaptchaService(CaptchaService captchaService){
        this.captchaService = captchaService;
    }

    public String getCaptchaImg(){
        System.out.println("call getCaptcha");
        InputStream inputStream = this.captchaService.getCaptchaImg();
        ServletActionContext.getContext().put("inputStream", inputStream);
        return SUCCESS;
    }
}
