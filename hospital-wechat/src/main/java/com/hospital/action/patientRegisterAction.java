package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.hospital.util.AgeUtils;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.*;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by QQQ on 2017/12/23.
 */
public class patientRegisterAction extends ActionSupport {

    String username;
    String tel;
    String sex;
    String hospitalID;
    String doctorID;
    String birthday;
    String captcha;
    int typeID;
    PatientService patientService;
    DoctorService doctorService;
    PlanService planService;
    SurveyService surveyService;
    DeliveryService deliveryService;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public void setPatientType(String typeID) {
        try {
            this.typeID = Integer.parseInt(typeID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }
    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }
    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }
    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }



    public String findDoctorsByHospital() {
        if (hospitalID!=null) {
            System.out.println("find doctor from hospital id = " + hospitalID);
            Hospital tmpHospital = new Hospital();
            tmpHospital.setAid(Integer.valueOf(hospitalID));
            List<Doctor> list = doctorService.findDoctorByHospital(tmpHospital);
            JSONArray jsonArray = new JSONArray();

            for(int i = 0; i < list.size(); i++) {
                Doctor doctor = list.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", doctor.getAid().toString());
                jsonObject.put("name", doctor.getName());
                jsonArray.add(jsonObject);
            }
            System.out.println("json doctors = " + jsonArray.toString());
            try {
                HttpServletResponse response = ServletActionContext.getResponse();
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(jsonArray.toString());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }


    public String register() {
        System.out.println("calling patientRegisterAction_register.action");
        String ref_captcha = ServletActionContext.getContext().getSession().get("captcha").toString();
        System.out.println("ref captcha = " + ref_captcha);

        captcha = captcha.toLowerCase();
        System.out.println("est captcha = " + captcha);

        System.out.println("hospitalID = " + hospitalID);

        Patient patient = (Patient) ServletActionContext.getContext().getSession().get("patient");
        String appID = (String) ServletActionContext.getContext().getSession().get("appID");

        /* status : 1 add patient successfully, redirect to info page
         * status : -1 captcha error
         * status : -2 unexpected error
         */
        int status = 1;
        if (ref_captcha.length() == 0 || patient == null) {
            status = -2;
        } else if (!ref_captcha.equals(captcha)) {
            status = -1;
            System.out.println("captcha error");
        } else {

            Patient oldPatient = patientService.getPatientByopenID(patient);//检查是否已经存在该openID的病人
            if (oldPatient == null) {
                // 写入新病人
                System.out.println("patient openid = " + patient.getOpenID());
                System.out.println("captcha correct");

                Doctor tmp_doctor = new Doctor();
                // modify later
                tmp_doctor.setAid(Integer.valueOf(doctorID));
                Doctor doctor = doctorService.getDoctorById(tmp_doctor);
                if (doctor == null) {
                    status = -2;
                } else {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthday_date;
                    try {
                        birthday_date = sdf.parse(birthday);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }

                    PatientType type = new PatientType();
                    type.setPatientTypeId(typeID);
                    patient.setAppID(appID);
                    patient.setUniqID("na");
                    patient.setName(username);
                    patient.setPwd(Md5Utils.md5("NA"));
                    patient.setPhone(tel);
                    patient.setDoctor(doctor);
                    patient.setCreateTime(new Date(System.currentTimeMillis()));
                    patient.setBirthday(birthday_date);
                    if (sex.toUpperCase().equals("MALE")) {
                        patient.setSex(1);
                    } else {
                        patient.setSex(0);
                    }
                    System.out.println("add patient ! " + username);
                    System.out.println("add patient ! " + patient.getOpenID());

                    if (patientService.addPatient(patient)) {
                        System.out.println("add patient succeed");
                        status = 1;
                    } else {
                        System.out.println("add patient failed");
                        status = -2;
                    }

                    int age = 0;
                    age = AgeUtils.getAgeFromBirthTime(birthday);
                    Plan plan = new Plan();
                    plan.setBeginAge(age);
                    plan.setEndAge(age);  //trick here, set beginAge=endAge to get plan
                    if (sex.toUpperCase().equals("MALE")) {
                        plan.setSex(2);  //be careful about sex, Patient.sex is not compatible with Plan.sex
                    } else if (sex.toUpperCase().equals("FEMALE")) {
                        plan.setSex(1);
                    }
                    plan.setPatientType(type);
                    Plan newPlan = planService.getPlan(plan);
                    Set<Survey> surveySet = newPlan.getSurveys();

                    for (Survey survey : surveySet) {
                        DeliveryInfo deliveryInfo = new DeliveryInfo();
                        deliveryInfo.setPatient(patient);
                        deliveryInfo.setSurvey(survey);
                        deliveryInfo.setDoctor(doctor);
                        deliveryInfo.setDeliveryDate(new Date(System.currentTimeMillis()));
                        deliveryInfo.setState(0);
                        deliveryService.sendTemplateMessage(deliveryInfo);
                    }
                }
            } else {
                status = -2;
            }
        }

        System.out.println("status = " + status);

        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.getWriter().print(status);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


}
