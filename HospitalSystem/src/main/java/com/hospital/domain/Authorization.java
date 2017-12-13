package com.hospital.domain;

import java.io.Serializable;

public class Authorization implements Serializable{
	private Integer aid;	//医生id
	private Integer sysSet;	//系统设置权限
	private Integer patientSet;	//病人设置权限
	private Integer surveySet;	//问卷设置权限
	private Integer typeSet;	//问卷分类设置权限
	private Integer deliverySet;	//分发设置权限
	private Integer retrieveSet;	//答卷设置权限
	private Integer forfeitSet;	//逾期设置权限
	private Integer superSet;	//超级管理权限
	private Doctor doctor;
	
	
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	//public Doctor getAdmin() {
	//	return admin;
	//}
	//public void setAdmin() {
	//	this.admin = admin;
	//}


	public Integer getForfeitSet() {
		return forfeitSet;
	}
	public void setForfeitSet(Integer forfeitSet) {
		this.forfeitSet = forfeitSet;
	}
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public Integer getSysSet() {
		return sysSet;
	}
	public void setSysSet(Integer sysSet) {
		this.sysSet = sysSet;
	}
	public Integer getPatientSet() {
		return patientSet;
	}
	public void setPatientSet(Integer patientSet) {
		this.patientSet = patientSet;
	}
	public Integer getSurveySet() {
		return surveySet;
	}
	public void setSurveySet(Integer surveySet) {
		this.surveySet = surveySet;
	}
	public Integer getTypeSet() {
		return typeSet;
	}
	public void setTypeSet(Integer typeSet) {
		this.typeSet = typeSet;
	}
	public Integer getDeliverySet() {
		return deliverySet;
	}
	public void setDeliverySet(Integer deliverySet) {
		this.deliverySet = deliverySet;
	}
	public Integer getRetrieveSet() {
		return retrieveSet;
	}
	public void setRetrieveSet(Integer retrieveSet) {
		this.retrieveSet = retrieveSet;
	}
	public Integer getSuperSet() {
		return superSet;
	}
	public void setSuperSet(Integer superSet) {
		this.superSet = superSet;
	}
	public Authorization() {

	
	}
	
	

}