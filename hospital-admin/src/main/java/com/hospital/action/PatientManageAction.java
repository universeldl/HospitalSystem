package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.hospital.util.TimeUtils.getLast12Months;
import static com.hospital.util.CalculateUtils.getMax;

import com.hospital.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

public class PatientManageAction extends ActionSupport {

    private PatientService patientService;
/*
    private PlanService planService;
*/
    private DoctorService doctorService;
    private PatientTypeService patientTypeService;
    private ProvinceService provinceService;
/*
    private RedisService redisService;
*/

/*
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
*/

    public void setPatientTypeService(PatientTypeService patientTypeService) {
        this.patientTypeService = patientTypeService;
    }

/*
    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }
*/

    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setProvinceService(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    private Integer patientId;
    private String name;
    private String phone;
    private Integer patientType;
    private Integer addnDoctorId;
    private Integer doctorId;
    private Integer sex;
    private int pageCode;
    private String openID;
    private String email;
    private String birthday;
    private String fileName;
    private String createTime;
    private Integer province;
    private Integer city;
    private Integer hospital;
    private Integer oldPatient;
    private Integer bannedSurveyId;

    /**
     * @param fileName the fileName to set
     */
    public void setOldPatient(Integer oldPatient) { this.oldPatient = oldPatient; }

    public void setProvince(Integer province) { this.province = province; }

    public void setCity(Integer city) { this.city = city; }

    public void setHospital(Integer hospital) { this.hospital = hospital; }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

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


    public void setBannedSurveyId(Integer bannedSurveyId) {
        this.bannedSurveyId = bannedSurveyId;
    }


    public void setAddnDoctorId(Integer addnDoctorId) {
        this.addnDoctorId = addnDoctorId;
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
    /* obsolate this function since Patient should be added from wechat webpage

    public String addPatient() {
        int success = 0;
        //得到当前时间
        Date createTime = new Date(System.currentTimeMillis());
        //得到当前医生
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        //得到共享医生
        Doctor tmpDoc = new Doctor();
        tmpDoc.setAid(addnDoctorId);
        Doctor addnDoctor = doctorService.getDoctorById(tmpDoc);
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

        Patient patient = new Patient(name, Md5Utils.md5("123456"), phone, false, type, email, doctor, addnDoctor, openID, createTime, sex, newPlan);

        Patient oldPatient = patientService.getPatientByopenID(patient);//检查是否已经存在该openID的病人
        if (oldPatient != null) {
            success = -1;//已存在该id
        } else if (doctor.getAid() == addnDoctorId) {
            success = -2;//共享医生与直属医生不能相同
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
    */


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
        int pageSize = 20;
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        PageBean<Patient> pb = patientService.findPatientByPage(pageCode, pageSize, doctor);
        if (pb != null) {
            pb.setUrl("findPatientByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);

        List<Province> provinceList = provinceService.getAllProvinces();
        ServletActionContext.getRequest().setAttribute("pl", provinceList);

        List<PatientType> patientTypeList = patientTypeService.getAllPatientType();
        ServletActionContext.getRequest().setAttribute("ptl", patientTypeList);



        return "success";
    }

    public String findRecyclePatientByPage() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 20;
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        PageBean<Patient> pb = patientService.findRecyclePatientByPage(pageCode, pageSize, doctor);
        if (pb != null) {
            pb.setUrl("findRecyclePatientByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "recycle";
    }



    /**
     * 得到指定的病人
     *
     * @return
     */
    public String getSummary() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        //得到当前医生
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");

        //get Legends for Figure2
        SimpleDateFormat simdf = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        String[] legends = getLast12Months(simdf.format(cal.getTime()));

        //get additions numbers for each month in last 12 months
        Integer[] additions = patientService.getAdditionsForLast12Months(doctor);

        //calculate total numbers for each month in last 12 months
        Integer[] total = new Integer[12];

        Integer male = 0, female = 0;
        Integer units1 = 0;
        Integer addon1 = 0;
        Integer units2 = 0;
        Integer addon2 = 0;

        List<Integer> patients = patientService.getPatientSexByDoctor(doctor);
        if (patients != null) {
            total[11] = patients.size();
            for (int s = 11; s > 0; s--) {
                total[s - 1] = total[s] - additions[s];
            }

            //calculate units and add-on's for additions and total
            units1 = (getMax(additions) / 100 + 1) * 100;
            addon1 = units1 / 5;
            units2 = (total[11] / 100 + 1) * 100;
            addon2 = units2 / 5;

/*
            List<Patient> allPatients = patientService.getPatientsByDoctor(doctor);
*/

            for (Integer sex : patients) {
                if (sex.equals(1))
                    male++;
                else
                    female++;
            }
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
/*        boolean isOk = redisService.set("patient name is", "who knows");
        System.out.println("putting patient to redis!!!");*/

        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                //过滤掉集合
                return name.equals("deliveryInfos") || name.equals("retrieveInfos") ||
                        name.equals("doctor") || name.equals("addnDoctor") ||
                        name.equals("plan") || name.equals("pwd");
            }
        });

        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        JSONObject jsonObject = JSONObject.fromObject(newPatient, jsonConfig);

        Plan plan = newPatient.getPlan();
        if (plan != null) {
            jsonObject.put("planId", plan.getPlanId());
        } else {
            jsonObject.put("planId", "N/A");

        }

        if (newPatient.getDoctor() != null) {
            jsonObject.put("doctorName", newPatient.getDoctor().getName());
            jsonObject.put("doctorId", newPatient.getDoctor().getAid());
        } else {
            jsonObject.put("doctorName", "N/A");
            jsonObject.put("doctorId", "-1");

        }

        if (newPatient.getAddnDoctor() != null) {
            jsonObject.put("addnDoctorName", newPatient.getAddnDoctor().getName());
            jsonObject.put("addnDoctorId", newPatient.getAddnDoctor().getAid());
        } else {
            jsonObject.put("addnDoctorName", "N/A");
            jsonObject.put("addnDoctorId", "-1");
        }

        jsonObject.put("oldPatient", newPatient.getOldPatient());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(newPatient.getCreateTime());
        jsonObject.put("createTime", dateString);


        String birthdayStr = formatter.format(newPatient.getBirthday());
        jsonObject.put("birthday", birthdayStr);


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
        //得到当前医生
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        Patient updatePatient = patientService.getPatientById(patient);//查出需要修改的病人对象;
        updatePatient.setName(name);
        updatePatient.setPhone(phone);
        updatePatient.setOpenID(openID);
        updatePatient.setOldPatient(oldPatient);

        String email_format = "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
        if (email!= null && email.matches(email_format)) {
            updatePatient.setEmail(email);
        }

        Date cday = updatePatient.getCreateTime();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cday = format.parse(createTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updatePatient.setCreateTime(cday);

        Date bday = updatePatient.getBirthday();
        try {
            bday = format.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updatePatient.setBirthday(bday);

        //设置patient的值
        PatientType type = new PatientType();
        type.setPatientTypeId(patientType);
        updatePatient.setPatientType(type);
        if (addnDoctorId != -1) {
            if (updatePatient.getDoctor().getAid() != addnDoctorId) {
                Doctor addnDoctor = new Doctor();
                addnDoctor.setAid(addnDoctorId);
                Doctor newAddndoctor = doctorService.getDoctorById(addnDoctor);
                if (newAddndoctor != null) {
                    updatePatient.setAddnDoctor(newAddndoctor);
                }
            }
        } else {
            updatePatient.setAddnDoctor(null);
        }

        if (doctorId != -1) {
            if (updatePatient.getDoctor().getAid() != doctorId) {
                Doctor addnDoctor = new Doctor();
                addnDoctor.setAid(doctorId);
                Doctor newAddndoctor = doctorService.getDoctorById(addnDoctor);
                if (newAddndoctor != null) {
                    updatePatient.setDoctor(newAddndoctor);
                }
            }
        }

        Patient newPatient = patientService.updatePatientInfo(updatePatient);
        int success = 0;
        if (updatePatient.getDoctor().getAid() == addnDoctorId) {
            success = -2;
        } else if (doctorId == -1) {
            success = -1;
        } else if (newPatient != null) {
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

    public String restorePatient() {
        if (patientId != null) {
            Patient tmpPatient = new Patient();
            tmpPatient.setPatientId(patientId);

            Patient patient = patientService.getPatientById(tmpPatient);

            if (patient == null) {
                try {
                    ServletActionContext.getResponse().getWriter().print(-1);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
                return null;
            }

            patient.setState(1);
            Patient newPatient = patientService.updatePatientInfo(patient);
            try {
                ServletActionContext.getResponse().getWriter().print((newPatient == null)?-1:1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }
    public String updatePatientBannedSurveyList() {
        if (patientId != null) {
            Patient tmpPatient = new Patient();
            tmpPatient.setPatientId(patientId);

            Patient patient = patientService.getPatientById(tmpPatient);

            if (patient == null) {
                try {
                    ServletActionContext.getResponse().getWriter().print(-1);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
                return null;
            }

            String banList = patient.getBannedSurveyList();
            if ("".equals(banList)) {
                banList = bannedSurveyId.toString();
            }
            else {
                banList = banList + "," + bannedSurveyId.toString();
            }
            patient.setBannedSurveyList(banList);
            Patient newPatient = patientService.updatePatientInfo(patient);
            try {
                ServletActionContext.getResponse().getWriter().print((newPatient == null)?-1:1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }

    public String recyclePatient() {
        if (patientId != null) {
            Patient tmpPatient = new Patient();
            tmpPatient.setPatientId(patientId);

            Patient patient = patientService.getPatientById(tmpPatient);

            if (patient == null) {
                try {
                    ServletActionContext.getResponse().getWriter().print(-1);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
                return null;
            }

            patient.setState(-1);
            Patient newPatient = patientService.updatePatientInfo(patient);
            try {
                ServletActionContext.getResponse().getWriter().print((newPatient == null)?-1:1);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
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
        int pageSize = 20;
        PageBean<Patient> pb = null;
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        if ("".equals(name.trim()) && patientType == -1 && hospital == -1 && city == -1 && province == -1) {
            pb = patientService.findPatientByPage(pageCode, pageSize, doctor);
        } else {
            Patient patient = new Patient();
            PatientType type = new PatientType();
            type.setPatientTypeId(patientType);
            patient.setPatientType(type);
            patient.setName(name);
            pb = patientService.queryPatient(patient, pageCode, pageSize, doctor, hospital, city, province);
        }
        if (pb != null) {
            pb.setUrl("queryPatient.action?openID=" + openID + "&name=" + name + "&patientType="
                            + patientType + "&hospital=" + hospital + "&city=" + city + "&province=" + province + "&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pb);

        List<Province> provinceList = provinceService.getAllProvinces();
        ServletActionContext.getRequest().setAttribute("pl", provinceList);

        List<PatientType> patientTypeList = patientTypeService.getAllPatientType();
        ServletActionContext.getRequest().setAttribute("ptl", patientTypeList);

        return "success";
    }

    public String queryRecyclePatient() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 20;
        PageBean<Patient> pb = null;
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        if ("".equals(openID.trim()) && "".equals(name.trim()) && patientType == -1) {
            pb = patientService.findRecyclePatientByPage(pageCode, pageSize, doctor);
        } else {
            Patient patient = new Patient();
            patient.setOpenID(openID);
            PatientType type = new PatientType();
            type.setPatientTypeId(patientType);
            patient.setPatientType(type);
            patient.setName(name);
            pb = patientService.queryRecyclePatient(patient, pageCode, pageSize, doctor);
        }
        if (pb != null) {
            pb.setUrl("queryRecyclePatient.action?openID=" + openID + "&name=" + name + "&patientType=" + patientType + "&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "recycle";
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
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        HttpServletResponse response = ServletActionContext.getResponse();
        String filePath = patientService.exportPatient(doctor);
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


    public String getAllPatientTypes() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<PatientType> allPatientTypes = patientTypeService.getAllPatientType();

        String json = JSONArray.fromObject(allPatientTypes).toString();//List------->JSONArray
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String getAllDoctors() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<Doctor> allDoctors = doctorService.getAllDoctors();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                return name.equals("authorization") || name.equals("hospital") ;
            }
        });

        String json = JSONArray.fromObject(allDoctors, jsonConfig).toString();//List------->JSONArray
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
