package com.hospital.action;

import com.hospital.domain.Doctor;
import com.hospital.service.DoctorService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@SuppressWarnings("serial")
public class DoctorLoginAction extends ActionSupport {

    private DoctorService doctorService;

    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    private String username;
    private String pwd;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    /**
     * Ajax异步请求获得登录许可
     *
     * @return 返回登录状态
     */
    public String login() {
        //医生
        Doctor doctor = new Doctor();
        doctor.setUsername(username);
        doctor.setPwd(Md5Utils.md5(pwd));
        Doctor newDoctor = doctorService.getDoctorByUserName(doctor);
        int login = 1;
        if (newDoctor == null) {
            //用户名不存在
            login = -1;
        } else if (!newDoctor.getPwd().equals(doctor.getPwd())) {
            //密码不正确
            login = -2;
        } else {
            //存储入session
            ServletActionContext.getContext().getSession().put("doctor", newDoctor);
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.getWriter().print(login);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 退出登录
     */
    public String logout() {
        ServletActionContext.getContext().getSession().remove("doctor");
        return "logout";
    }


}
