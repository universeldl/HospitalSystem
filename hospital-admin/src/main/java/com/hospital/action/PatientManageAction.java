package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.PatientService;
import com.hospital.service.PlanService;
import com.hospital.util.Md5Utils;
import com.hospital.util.AgeUtils;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.hospital.util.TimeUtils.getLast12Months;
import static com.hospital.util.CalculateUtils.getMax;

public class PatientManageAction extends ActionSupport {

    private PatientService patientService;
    private PlanService planService;

    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }


    private Integer patientId;
    private String name;
    private String phone;
    private Integer patientType;
    private Integer sex;
    private int pageCode;
    private String openID;
    private String email;
    private String birthday;

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


    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }


    public void setSex(Integer sex) {
        this.sex = sex;
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

        int age = 0;
        age = AgeUtils.getAgeFromBirthTime(birthday);

        Plan plan = new Plan();
        plan.setBeginAge(age);
        plan.setEndAge(age);  //trick here, set beginAge=endAge to get plan
        if (sex == 0) {
            plan.setSex(2);  //be careful about sex, Patient.sex is not compatible with Plan.sex
        } else if (sex == 1) {
            plan.setSex(1);
        }
        plan.setPatientType(type);
        Plan newPlan = planService.getPlan(plan);

        Patient patient = new Patient(name, Md5Utils.md5("123456"), phone, type, email, doctor, openID, createTime, sex, newPlan);

        Patient oldPatient = patientService.getPatientByopenID(patient);//检查是否已经存在该openID的病人
        int success = 0;
        if (oldPatient != null) {
            success = -1;//已存在该id
        } else {
            boolean b = patientService.addPatient(patient);
            if (b) {
                //TODO 大神，添加完病人就要发送第一份随访(从上面的newPlan中取得)，请加入微信端发送code并删除此行
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

        //get Legends for Figure2
        SimpleDateFormat simdf = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        String[] legends = getLast12Months(simdf.format(cal.getTime()));

        //get additions numbers for each month in last 12 months
        Integer[] additions = patientService.getAdditionsForLast12Months();

        //calculate total numbers for each month in last 12 months
        Integer[] total = new Integer[12];
        total[11] = patientService.findAllPatients().size();
        for(int s=11; s>0; s--) {
            total[s-1] = total[s] - additions[s];
        }

        //calculate units and add-on's for additions and total
        Integer units1 = (getMax(additions)/100 + 1)*100;
        Integer addon1 = units1/5;
        Integer units2 = (total[11]/100 + 1)*100;
        Integer addon2 = units2/5;

        Integer male = 0, female = 0;
        //得到当前医生
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        List<Patient> allPatients = patientService.getPatientsByDoctor(doctor);

        for (Patient p : allPatients) {
            if (p.getSex() == 1)
                male++;
            else
                female++;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("male", male);
        jsonObject.put("female", female);
        jsonObject.put("legends", legends);
        jsonObject.put("additions", additions);
        jsonObject.put("total", total);
        jsonObject.put("units1", units1);
        jsonObject.put("addon1", addon1);
        jsonObject.put("units2", units2);
        jsonObject.put("addon2", addon2);

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

        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

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

    public String exportSinglePatient() {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Patient exportPatient = patientService.getPatientById(patient);//查出需要导出的病人对象;
        HttpServletResponse response = ServletActionContext.getResponse();
        String filePath = patientService.exportSinglePatient(exportPatient);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filePath", filePath);

        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
