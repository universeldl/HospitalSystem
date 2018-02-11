package com.hospital.domain;

import java.io.Serializable;

public class PatientType implements Serializable {

    private Integer patientTypeId;
    private String patientTypeName;//病人类型名称
    private Integer bday;    //允许填卷天数

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

    public Integer getBday() {
        return bday;
    }

    public void setBday(Integer bday) {
        this.bday = bday;
    }

    public PatientType() {

    }

}
