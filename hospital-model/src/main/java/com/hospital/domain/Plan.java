package com.hospital.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 随访计划类
 *
 * @author c
 */
public class Plan implements Serializable {

    private Integer planId;    //自动编号
    private Integer beginAge;    //年龄区间下限
    private Integer endAge;    //年龄区间上限
    private Integer active;    //激活状态
    private Integer sex;    //性别

    private PatientType patientType;
    private Doctor doctor;    //操作医生
    private Set<Survey> surveys = new HashSet<>();


    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getBeginAge() {
        return beginAge;
    }

    public void setBeginAge(Integer beginAge) {
        this.beginAge = beginAge;
    }

    public Integer getEndAge() {
        return endAge;
    }

    public void setEndAge(Integer endAge) {
        this.endAge = endAge;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public PatientType getPatientType() {
        return patientType;
    }

    public void setPatientType(PatientType patientType) {
        this.patientType = patientType;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Set<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
    }

    public Plan() {

    }

    public Plan(Integer beginAge, Integer endAge, Integer sex, Integer active,
                PatientType patientType, Doctor doctor) {
        super();
        this.beginAge = beginAge;
        this.endAge = endAge;
        this.sex = sex;
        this.active = active;
        this.patientType = patientType;
        this.doctor = doctor;
    }

}
