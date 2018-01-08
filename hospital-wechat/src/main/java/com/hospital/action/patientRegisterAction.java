package com.hospital.action;

import com.hospital.domain.Patient;
import com.hospital.domain.Doctor;
import com.hospital.domain.PatientType;
import com.hospital.service.PatientService;
import com.hospital.service.DoctorService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by QQQ on 2017/12/23.
 */
public class patientRegisterAction extends ActionSupport {

    String username;
    String tel;
    String sex;
    String hospital;
    String doctor;
    String birthday;
    String captcha;
    int typeID;
    PatientService patientService;
    DoctorService doctorService;
    public void setUsername(String username) {
        this.username = username;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
    public void setDoctor(String doctor) {
        this.doctor = doctor;
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
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }
    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public String register() {
        System.out.println("calling patientRegisterAction_register.action");
        String ref_captcha = ServletActionContext.getContext().getSession().get("captcha").toString();
        System.out.println("ref captcha = " + ref_captcha);

        captcha = captcha.toLowerCase();
        System.out.println("est captcha = " + captcha);


        Patient patient = (Patient) ServletActionContext.getContext().getSession().get("patient");

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
                tmp_doctor.setAid(1);
                Doctor doctor = doctorService.getDoctorById(tmp_doctor);
                if (doctor == null) {
                    status = -2;
                } else {
                    PatientType type = new PatientType();
                    type.setPatientTypeId(typeID);

                    patient.setName(username);
                    patient.setPwd(Md5Utils.md5("123456"));
                    patient.setPhone(tel);
                    patient.setDoctor(doctor);
                    patient.setCreateTime(new Date(System.currentTimeMillis()));

                    System.out.println("add patient ! " + username);
                    System.out.println("add patient ! " + patient.getOpenID());

                    if (patientService.addPatient(patient)) {
                        status = 1;
                    } else {
                        status = -2;
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
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


}
