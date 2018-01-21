package com.hospital.action;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.Doctor;
import com.hospital.domain.Authorization;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.domain.PatientType;
import com.hospital.service.PatientService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;

public class PatientManageAction extends ActionSupport {

    private PatientService patientService;


    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }


    private Integer patientId;
    private String name;
    private String phone;
    private Integer patientType;
    private int pageCode;
    private String openID;
    private String email;

    private String fileName;


    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public void setOpenID(String openID) {
        this.openID = openID;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }


    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setPatientType(Integer patientType) {
        this.patientType = patientType;
    }


    /**
     * 添加病人
     *
     * @return
     */
    public String addPatient() {
        //得到当前时间
        Date createTime = new Date(System.currentTimeMillis());
        //得到当前医生
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        //得到当前病人类型
        PatientType type = new PatientType();
        type.setPatientTypeId(patientType);
        Patient patient = new Patient(name, Md5Utils.md5("123456"), phone, type, email, doctor, openID, createTime, 1);
        //TODO the "sex" for patient should be varied rather than hard-coded.

        Patient oldPatient = patientService.getPatientByopenID(patient);//检查是否已经存在该openID的病人
        int success = 0;
        if (oldPatient != null) {
            success = -1;//已存在该id
        } else {


            boolean b = patientService.addPatient(patient);
            if (b) {
                success = 1;
            }
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
     * 根据页码查询病人
     *
     * @return
     */
    public String findPatientByPage() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 10;
        PageBean<Patient> pb = patientService.findPatientByPage(pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findPatientByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    /**
     * 得到指定的病人
     *
     * @return
     */
    public String getSummary() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");

        Integer male=0, female = 0;
        //得到当前医生
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        List<Patient> allPatients = patientService.getPatientsByDoctor(doctor);

        for(Patient p: allPatients) {
            if(p.getSex() ==1)
                male++;
            else
                female++;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("male", male);
        jsonObject.put("female", female);

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * 得到指定的病人
     *
     * @return
     */
    public String getPatient() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Patient newPatient = patientService.getPatientById(patient);
        JsonConfig jsonConfig = new JsonConfig();

        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                //过滤掉集合
                return obj instanceof Set || name.equals("deliveryInfos") || obj instanceof Authorization || name.equals("authorization");
            }
        });


        JSONObject jsonObject = JSONObject.fromObject(newPatient, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 修改指定病人
     *
     * @return
     */
    public String updatePatient() {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Patient updatePatient = patientService.getPatientById(patient);//查出需要修改的病人对象;
        updatePatient.setName(name);
        updatePatient.setPhone(phone);
        updatePatient.setOpenID(openID);
        updatePatient.setEmail(email);
        //设置patient的值
        PatientType type = new PatientType();
        type.setPatientTypeId(patientType);
        updatePatient.setPatientType(type);
        Patient newPatient = patientService.updatePatientInfo(updatePatient);
        int success = 0;
        if (newPatient != null) {
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
     * 删除指定医生
     *
     * @return
     */
    public String deletePatient() {
        //删除病人需要注意的点：如果该病人有尚未答卷的问卷或者尚未设置的延期,则不能删除
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        int success = patientService.deletePatient(patient);

        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }


    public String queryPatient() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<Patient> pb = null;
        if ("".equals(openID.trim()) && "".equals(name.trim()) && patientType == -1) {
            pb = patientService.findPatientByPage(pageCode, pageSize);
        } else {
            Patient patient = new Patient();
            patient.setOpenID(openID);
            PatientType type = new PatientType();
            type.setPatientTypeId(patientType);
            patient.setPatientType(type);
            patient.setName(name);
            pb = patientService.queryPatient(patient, pageCode, pageSize);
        }
        if (pb != null) {
            pb.setUrl("queryPatient.action?openID=" + openID + "&name=" + name + "&patientType=" + patientType + "&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    public String batchAddPatient() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        JSONObject batchAddPatient = patientService.batchAddPatient(fileName, doctor);
        try {
            response.getWriter().print(batchAddPatient);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String exportPatient() {
        HttpServletResponse response = ServletActionContext.getResponse();
        String filePath = patientService.exportPatient();
        try {
            response.getWriter().print(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


}
