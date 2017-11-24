package com.hospital.action;

import java.io.IOException;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.Patient;
import com.hospital.service.PatientService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class PatientInfoAction extends ActionSupport{
	private PatientService patientService;

	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}
	
	private String name;
	private String phone;
	private String email;
	private String oldPwd;
	private String newPwd;
	private String confirmPwd;
	
	
	
	
	public void setEmail(String email) {
		this.email = email;
	}


	public PatientService getPatientService() {
		return patientService;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}


	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}


	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}


	/**
	 * 病人个人资料
	 * @return
	 */
	public String patientInfo(){
		Map<String, Object> session = ServletActionContext.getContext().getSession();
		Patient patient = (Patient) session.get("patient");
		patient.setName(name);
		patient.setPhone(phone);
		patient.setEmail(email);
		Patient newPatient = patientService.updatePatientInfo(patient);
		int success = 0;
		if(newPatient!=null){
			success = 1;
			//重新存入session
			session.put("patient", newPatient);
		}
		try {
			ServletActionContext.getResponse().getWriter().print(success);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 病人密码修改
	 * @return
	 */
	public String patientPwd(){
		Patient patient = (Patient) ServletActionContext.getContext().getSession().get("patient");
		int state = -1;//原密码错误
		//取出原密码进行比对
		if(patient.getPwd().equals(Md5Utils.md5(oldPwd))){
			if(newPwd.equals(confirmPwd)){
				state = 1;//修改成功
				patient.setPwd(Md5Utils.md5(newPwd));
				patient = patientService.updatePatientInfo(patient);
				//重新存入session
				ServletActionContext.getContext().getSession().put("patient", patient);
			}else{
				state = 0;//确认密码不一致
			}
		}
		try {
			ServletActionContext.getResponse().getWriter().print(state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}

}
