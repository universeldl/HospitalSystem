package com.hospital.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 病人类
 * @author c
 *
 */
public class Patient implements Serializable{

	private Integer patientId;	//自动编号
	private String name;	//真实名称
	private String pwd;	//密码
	private String phone;	//联系方式
	private PatientType patientType;	//病人类型(病人或者病人家属)
    private String email;	//邮箱
    private Doctor doctor;	//操作医生
    private String openID;	//用户名码
	private Date createTime;	//创建时间
	private Set<DeliveryInfo> deliveryInfos;	//该病人的分发信息




	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public PatientType getPatientType() {
		return patientType;
	}

	public void setPatientType(PatientType patientType) {
		this.patientType = patientType;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<DeliveryInfo> getDeliveryInfos() {
		return deliveryInfos;
	}

	public void setDeliveryInfos(Set<DeliveryInfo> deliveryInfos) {
		this.deliveryInfos = deliveryInfos;
	}




	public Patient() {

	
	
	}

	public Patient(String name, String pwd, String phone,
			PatientType patientType, String email, Doctor doctor, String openID,
			Date createTime) {
		super();
		this.name = name;
		this.pwd = pwd;
		this.phone = phone;
		this.patientType = patientType;
		this.email = email;
		this.doctor = doctor;
		this.openID = openID;
		this.createTime = createTime;
	}


	



	
	
	
	
	
}
