package com.hospital.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.PatientType;
import com.hospital.service.PatientTypeService;
import com.opensymphony.xwork2.ActionSupport;

public class PatientTypeManageAction extends ActionSupport{

	public PatientTypeService patientTypeService;

	public void setPatientTypeService(PatientTypeService patientTypeService) {
		this.patientTypeService = patientTypeService;
	}
	
	private Integer id;
	private Integer maxNum;
	private Integer bday;
	private Double penalty;
	private String patientTypeName;
	private Integer resendDays;
	
	
	
	
	
	
	
	
	
	public void setRenewDays(Integer resendDays) {
		this.resendDays = resendDays;
	}



	public void setPatientTypeName(String patientTypeName) {
		this.patientTypeName = patientTypeName;
	}



	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}



	public void setBday(Integer bday) {
		this.bday = bday;
	}



	public void setPenalty(Double penalty) {
		this.penalty = penalty;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getAllPatientType(){
	
		List<PatientType> allPatientType = patientTypeService.getAllPatientType();
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("patientTypes", allPatientType);
		return "success";
	} 
	
	
	/**
	 * 得到指定的病人类型信息
	 * @return
	 */
	public String getPatientType(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		PatientType patientType = new  PatientType();
		patientType.setPatientTypeId(id);
		PatientType newPatientType = patientTypeService.getTypeById(patientType);
		JSONObject jsonObject = JSONObject.fromObject(newPatientType);
		try {
			response.getWriter().print(jsonObject);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	public String updatePatientType(){
		PatientType patientType = new PatientType();
		patientType.setPatientTypeId(id);
		PatientType updatePatientType = patientTypeService.getTypeById(patientType);
		updatePatientType.setPatientTypeName(patientTypeName);
		updatePatientType.setMaxNum(maxNum);
		updatePatientType.setBday(bday);
		updatePatientType.setPenalty(penalty);
		updatePatientType.setResendDays(resendDays);
		PatientType newPatientType = patientTypeService.updatePatientType(updatePatientType);
		int success = 0;
		if(newPatientType!=null){
			success = 1;
			//由于是转发并且js页面刷新,所以无需重查
		}
		try {
			ServletActionContext.getResponse().getWriter().print(success);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	
	public String addPatientType(){
		PatientType patientType = new PatientType();
		patientType.setPatientTypeName(patientTypeName);
		patientType.setPenalty(penalty);
		patientType.setMaxNum(maxNum);
		patientType.setBday(bday);
		patientType.setResendDays(resendDays);
		boolean b = patientTypeService.addPatientType(patientType);
		int success = 0;
		if(b){
			success = 1;
		}
		try {
			ServletActionContext.getResponse().getWriter().print(success);//向浏览器响应是否成功的状态码
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	public String getAllPatientTypes(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		List<PatientType> allPatientTypes = patientTypeService.getAllPatientType();
			
		String json = JSONArray.fromObject(allPatientTypes).toString();//List------->JSONArray
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
}
