package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.*;
import com.hospital.util.AgeUtils;
import com.hospital.util.InvitationCode;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.*;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    String invitationCode;
    boolean oldPatient;
    int typeID;
    PatientService patientService;
    DoctorService doctorService;
    PlanService planService;
    SurveyService surveyService;
    DeliveryService deliveryService;

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public void setOldPatient(String oldPatient) {
        if (oldPatient.equals("on")) {
            this.oldPatient = true;
        } else {
            this.oldPatient = false;
        }
    }

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
            //System.out.println("json doctors = " + jsonArray.toString());
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
        String ref_captcha = ServletActionContext.getContext().getSession().get("captcha").toString();
        captcha = captcha.toLowerCase();
        Patient patient = (Patient) ServletActionContext.getContext().getSession().get("patient");
        String appID = (String) ServletActionContext.getContext().getSession().get("appID");

        /* status : 1 add patient successfully, redirect to info page
         * status : -1 captcha error
         * status : -2 invitation code error
         * status : -3 other errors
         */
        int status = 1;
        if (ref_captcha.length() == 0 || patient == null) {
            writeStatus(-3);
            return null;
        } else if (!ref_captcha.equals(captcha)) {
            writeStatus(-1);
            return null;
        }
        String ref_invitationCode = InvitationCode.getInvitationCode();
        if (!ref_invitationCode.equals(invitationCode)) {
            writeStatus(-2);
            return null;
        }

        if (patientService.getPatientByOpenID(patient) != null) {
            writeStatus(-3);
            return null;
        }

        // 写入新病人
        Doctor tmp_doctor = new Doctor();
        // modify later
        tmp_doctor.setAid(Integer.valueOf(doctorID));
        Doctor doctor = doctorService.getDoctorById(tmp_doctor);
        if (doctor == null) {
            writeStatus(-3);
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday_date;
        try {
            birthday_date = sdf.parse(birthday);
        } catch (Exception e) {
            writeStatus(-3);
            return null;
        }

        PatientType type = new PatientType();
        type.setPatientTypeId(typeID);
        patient.setAppID(appID);
        patient.setUniqID(patient.getOpenID());
        patient.setName(username);
        patient.setPwd(Md5Utils.md5("NA"));
        patient.setPhone(tel);
        patient.setDoctor(doctor);
        patient.setCreateTime(new Date(System.currentTimeMillis()));
        patient.setBirthday(birthday_date);
        patient.setOldPatient(oldPatient);
        if (sex.toUpperCase().equals("MALE")) {
            patient.setSex(1);
        } else {
            patient.setSex(0);
        }
        if (!patientService.addPatient(patient)) {
            writeStatus(-3);
            return null;
        }

        System.out.println("Add patient finished. status = " + status);

        int age = 0;
        System.out.println("Add plan finished birthday = " + birthday);
        String[] bd = birthday.split("-");
        if (bd.length != 3) {
            writeStatus(-3);
            return null;
        }

        String tmp_bd = bd[1] + "/" + bd[2] + "/" + bd[0];
        System.out.println("Add plan finished birthday = " + birthday);
        age = AgeUtils.getAgeFromBirthTime(tmp_bd);
        System.out.println("Add plan finished age = " + age);
        Plan plan = new Plan();
        plan.setBeginAge(age);
        plan.setEndAge(age);  //trick here, set beginAge=endAge to get plan
        System.out.println("Add plan finished 1.");
        if (sex.toUpperCase().equals("MALE")) {
            plan.setSex(2);  //be careful about sex, Patient.sex is not compatible with Plan.sex
        } else if (sex.toUpperCase().equals("FEMALE")) {
            plan.setSex(1);
        }
        System.out.println("Add plan finished 2.");
        plan.setPatientType(type);
        System.out.println("Add plan finished 3.");
        Plan newPlan = planService.getPlan(plan);
        System.out.println("Add plan finished.");

        if (newPlan != null) {
            Set<Survey> surveySet = newPlan.getSurveys();
            System.out.println("survey length = " + surveySet.size());

            int survey_id = 0;
            for (Survey survey : surveySet) {
                System.out.println("provide survey id = " + survey_id);
                DeliveryInfo deliveryInfo = new DeliveryInfo();
                deliveryInfo.setPatient(patient);
                deliveryInfo.setSurvey(survey);
                deliveryInfo.setDoctor(doctor);
                deliveryInfo.setDeliveryDate(new Date(System.currentTimeMillis()));
                deliveryInfo.setState(0);
                deliveryService.sendTemplateMessage(deliveryInfo);
            }
            System.out.println("send template message finished");
        } else {
            System.out.println("new plan = null");
        }

        writeStatus(1);
        return null;
    }


    private void writeStatus(int status) {
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.getWriter().print(status);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
