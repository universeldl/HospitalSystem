package com.hospital.action;

import com.hospital.domain.Doctor;
import com.hospital.domain.Authorization;
import com.hospital.domain.PageBean;
import com.hospital.service.DoctorService;
import com.hospital.service.AuthorizationService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class DoctorManageAction extends ActionSupport {

    private DoctorService doctorService;
    private AuthorizationService authorizationService;


    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    private int id;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    private String name;
    private String phone;
    private String pwd;


    private int pageCode;//当前页数


    private String doctorUserName;    //查询医生用户名
    private String doctorName;//查询医生姓名


    public void setDoctorUserName(String doctorUserName) {
        this.doctorUserName = doctorUserName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }


    /**
     * 得到指定的普通医生
     * Ajax请求该方法
     * 向浏览器返回该医生的json对象
     *
     * @return
     */
    public String getDoctor() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Doctor doctor = new Doctor();
        doctor.setAid(id);
        Doctor newDoctor = doctorService.getDoctorById(doctor);
        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                //过滤掉Authorization中的doctor
                return name.equals("doctor");
            }
        });

        JSONObject jsonObject = JSONObject.fromObject(newDoctor, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * 修改指定医生
     *
     * @return
     */
    public String updateDoctor() {
        Doctor doctor = new Doctor();
        doctor.setAid(id);
        Doctor updateDoctor = doctorService.getDoctorById(doctor);//查出需要修改的医生对象
        updateDoctor.setUsername(username);
        updateDoctor.setName(name);
        updateDoctor.setPhone(phone);
        Doctor newDoctor = doctorService.updateDoctorInfo(updateDoctor);//修改该医生
        int success = 0;
        if (newDoctor != null) {
            success = 1;
            //由于是转发并且js页面刷新,所以无需重查
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 添加医生
     *
     * @return
     */
    public String addDoctor() {
        Doctor doctor = new Doctor();
        doctor.setUsername(username);
        Doctor doctor2 = doctorService.getDoctorByUserName(doctor);//按照姓名查找医生，查看用户名是否已经存在
        int success = 0;
        if (doctor2 != null) {
            success = -1;//已经存在该医生
        } else {
            doctor.setName(name);
            doctor.setPhone(phone);
            doctor.setPwd(Md5Utils.md5("123456"));
            Authorization authorization = new Authorization();
            authorization.setDoctor(doctor);
            doctor.setAuthorization(authorization);//设置权限
            boolean b = doctorService.addDoctor(doctor);//添加医生,返回是否添加成功
            if (b) {
                success = 1;
            } else {
                success = 0;

            }
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);//向浏览器响应是否成功的状态码
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 根据页码查询医生
     *
     * @return
     */
    public String findDoctorByPage() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<Doctor> pb = doctorService.findDoctorByPage(pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findDoctorByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    /**
     * @return
     */
    public String queryDoctor() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<Doctor> pb = null;
        if ("".equals(doctorUserName.trim()) && "".equals(doctorName.trim())) {
            pb = doctorService.findDoctorByPage(pageCode, pageSize);
        } else {
            Doctor doctor = new Doctor();
            doctor.setUsername(doctorUserName);
            doctor.setName(doctorName);
            pb = doctorService.queryDoctor(doctor, pageCode, pageSize);

        }
        if (pb != null) {
            pb.setUrl("queryDoctor.action?doctorUserName=" + doctorUserName + "&doctorName=" + doctorName + "&");
        }

        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }

    /**
     * 删除指定医生
     *
     * @return
     */
    public String deleteDoctor() {
        Doctor doctor = new Doctor();
        doctor.setAid(id);
        boolean deleteDoctor = doctorService.deleteDoctor(doctor);
        int success = 0;
        if (deleteDoctor) {
            success = 1;
            //由于是转发并且js页面刷新,所以无需重查
        }
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

}
