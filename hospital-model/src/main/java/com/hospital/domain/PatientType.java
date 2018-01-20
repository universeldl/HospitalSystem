package com.hospital.domain;

import java.io.Serializable;
import java.util.Set;

public class PatientType implements Serializable {

    private Integer patientTypeId;
    private String patientTypeName;//病人类型名称
    private Integer maxNum;    //最大发送问卷量
    private Double penalty;    //几日提醒
    private Integer bday;    //允许填卷天数
    private Integer resendDays;    //重发天数

    public Integer getPatientTypeId() {
        return patientTypeId;
    }

    public void setPatientTypeId(Integer patientTypeId) {
        this.patientTypeId = patientTypeId;
    }

    public String getPatientTypeName() {
        return patientTypeName;
    }

    public void setPatientTypeName(String patientTypeName) {
        this.patientTypeName = patientTypeName;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Integer getBday() {
        return bday;
    }

    public void setBday(Integer bday) {
        this.bday = bday;
    }

    public PatientType() {

    }


    public Integer getResendDays() {
        return resendDays;
    }

    public void setResendDays(Integer resendDays) {
        this.resendDays = resendDays;
    }


}
