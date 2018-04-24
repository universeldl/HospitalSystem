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
    String typeID;
    String doctorID;
    String birthday;
    String captcha;
    String invitationCode;
    Integer oldPatient;
    PatientService patientService;
    DoctorService doctorService;
    PlanService planService;
    //SurveyService surveyService;
    DeliveryService deliveryService;
    HospitalService hospitalService;
    ProvinceService provinceService;
    CityService cityService;

    String provinceID;
    String cityID;

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public void setOldPatient(Integer oldPatient) {
        this.oldPatient = oldPatient;
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

    public void setTypeID(String typeID) {
        this.typeID = typeID;
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
    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }
    public void setHospitalService(HospitalService hospitalService) { this.hospitalService = hospitalService; }
    public void setProvinceService(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    public String findHospitalByCityID() {
        if (cityID != null) {
            City tmpCity = new City();
            tmpCity.setCityId(Integer.valueOf(cityID));
            City city = cityService.getCityByID(tmpCity);
            Set<Hospital> hospitals = city.getHospitals();
            JSONArray jsonArray = new JSONArray();
            for(Hospital hospital : hospitals) {
                if (hospital.isVisible()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", hospital.getHospitalId().toString());
                    jsonObject.put("name", hospital.getName());
                    jsonArray.add(jsonObject);
                }
            }
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

    public String findCityByProvinceID() {
        if (provinceID != null) {
            Province tmpProvince = new Province();
            tmpProvince.setProvinceId(Integer.valueOf(provinceID));
            Province province = provinceService.getProvinceByID(tmpProvince);
            Set<City> cites = province.getCities();
            JSONArray jsonArray = new JSONArray();
            for(City city : cites) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", city.getCityId().toString());
                jsonObject.put("name", city.getName());
                jsonArray.add(jsonObject);
            }
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

    public String findDoctorsByHospital() {
        if (hospitalID!=null) {
            Hospital tmpHospital = new Hospital();
            tmpHospital.setHospitalId(Integer.valueOf(hospitalID));
            Hospital hospital = hospitalService.getHospitalByID(tmpHospital);
/*
            List<Doctor> list = doctorService.findDoctorByHospital(tmpHospital);
*/
            Set<Doctor> list = hospital.getDoctors();
            JSONArray jsonArray = new JSONArray();

            for(Doctor doctor : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", doctor.getAid().toString());
                jsonObject.put("name", doctor.getName());
                jsonArray.add(jsonObject);
            }
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
        try {

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
                writeStatus(-4);
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
            type.setPatientTypeId(Integer.valueOf(typeID));
            patient.setPatientType(type);
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

            Patient newPatient = patientService.getPatientByopenID(patient);

            if (sendSurvey(newPatient)) {
                System.out.println("sendSurvey true");
            } else {
                System.out.println("sendSurvey false");
            }
            writeStatus(1);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            writeStatus(-3);
            return null;
        }
    }

    public boolean sendSurvey(Patient patient) {
        try {
            int age = AgeUtils.getAgeFromBirthTime(patient.getBirthday());
            Plan plan = new Plan();
            plan.setBeginAge(age);
            plan.setEndAge(age);  //trick here, set beginAge=endAge to get plan

            plan.setOldPatient(patient.getOldPatient());

            if (patient.getSex() == 1) {
                plan.setSex(2);  //be careful about sex, Patient.sex is not compatible with Plan.sex
            } else if (patient.getSex() == 0) {
                plan.setSex(1);
            }
            plan.setPatientType(patient.getPatientType());
            Plan newPlan = planService.getPlan(plan);

            if (newPlan != null) {
                patient.setPlan(newPlan);
                Patient new_patient = patientService.updatePatientInfo(patient);
                if (new_patient == null) {
                    System.out.println("update patient failed");
                }
            } else {
                System.out.println("patient " + patient.getName() + " don't have a plan!");
            }


            if (newPlan != null) {

                Set<Survey> surveySet = newPlan.getSurveys();

                for (Survey survey : surveySet) {
                    if (!survey.isSendOnRegister()) continue;
                    DeliveryInfo deliveryInfo = new DeliveryInfo();
                    deliveryInfo.setPatient(patient);
                    deliveryInfo.setSurvey(survey);
                    deliveryInfo.setDoctor(patient.getDoctor());
                    deliveryInfo.setDeliveryDate(new Date(System.currentTimeMillis()));
                    deliveryInfo.setState(0);
                    int deliveryId = deliveryService.addDelivery(deliveryInfo);
                    deliveryInfo.setDeliveryId(deliveryId);

                    DeliveryInfo newDeliveryInfo = deliveryService.getDeliveryInfoById(deliveryInfo);
                    if (newDeliveryInfo != null) {
                        deliveryService.sendTemplateMessage(newDeliveryInfo);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("send survey in register failed");
            e.printStackTrace();
            return false;
        }
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
