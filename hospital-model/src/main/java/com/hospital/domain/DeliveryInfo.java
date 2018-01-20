package com.hospital.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 分发信息类
 *
 * @author c
 */
public class DeliveryInfo implements Serializable {

    private Integer deliveryId;    //分发编号
    private Survey survey;    //分发问卷
    private Patient patient;    //分发病人
    private Date deliveryDate;    //分发日期
    private Doctor doctor;    //操作员
    private Date endDate;    //截止日期
    private Double penalty;    //几日提醒
    private Integer overday;    //逾期天数
    private Integer state; //状态 (未答卷=0,逾期未答卷=1,答卷=2,重发未答卷=3,重发逾期未答卷=4,重发答卷=5)


    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Integer getOverday() {
        return overday;
    }

    public void setOverday(Integer overday) {
        this.overday = overday;
    }


    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public DeliveryInfo() {

    }


}
