package com.hospital.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.Patient;
import com.hospital.service.PatientService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class PatientLoginAction extends ActionSupport {

	private PatientService patientService;
	
	
	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}




	private String openID;
	private String pwd;


	
	
	
	

	public void setOpenID(String openID) {
		this.openID = openID;
	}


	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	

	/**
	 * Ajax异步请求获得登录许可
	 * @return 返回登录状态
	 */
	public String login(){
		//病人
		Patient patient = new Patient();
		patient.setOpenID(openID);
		patient.setPwd(Md5Utils.md5(pwd));
		int login = 1;
		Patient newPatient = patientService.getPatientByOpenID(patient);
		if(newPatient==null){
			//用户名不存在
			login = -1;
		}else if(!Md5Utils.md5(newPatient.getPwd()).equals(patient.getPwd())){
			//密码不正确
			login =  -2;
		}else{
			//存储入session
			ServletActionContext.getContext().getSession().put("patient", newPatient);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		 try {	
			response.getWriter().print(login);
			
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		
			
		return null;
	}
	
	/**
	 * 退出登录
	 * @return
	 */
	public String logout(){
		ServletActionContext.getContext().getSession().remove("patient");
		return "logout";
	}
	
	
}
