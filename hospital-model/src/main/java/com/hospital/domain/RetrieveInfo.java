package com.hospital.domain;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;


public class RetrieveInfo {

    private  DeliveryInfo deliveryInfo;

	private Integer deliveryId;
	private Date retrieveDate;	//答卷时间

    //private int answerId;

    //private int surveyId;
	private Survey survey;	//分发问卷
	private Patient patient;	//分发病人
	private Doctor doctor;	//操作员
    //private String choiceContent;
    private int aid;
	private Set<Answer> answers = new HashSet<>();

    public Set<Answer> getAnswers() {  return answers;  }
    public void setAnswers(Set<Answer> answers) {  this.answers = answers;  }

	public Integer getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) { this.survey = survey; }

	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public DeliveryInfo getDeliveryInfo() { return deliveryInfo; }
	public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Date getRetrieveDate() {
		return retrieveDate;
	}
	public void setRetrieveDate(Date retrieveDate) {
		this.retrieveDate = retrieveDate;
	}
	
	public RetrieveInfo() { }

    //public int getAnswerId() { return answerId; }
    //public void setAnswerId(int answerId) { this.answerId = answerId; }

    //public int getSurveyId() { return surveyId; }
    //public void setSurveyId(int surveyId) { this.surveyId = surveyId; }

    //public String getChoiceContent() { return choiceContent; }
    //public void setChoiceContent(String choiceContent) { this.choiceContent = choiceContent; }

    public int getAid() {
        return aid;
    }
    public void setAid(int aid) {
        this.aid = aid;
    }

	
}
