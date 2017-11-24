package com.hospital.action;

import com.hospital.domain.Doctor;
import com.hospital.service.DoctorService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings("serial")
public class DoctorInfoAction extends ActionSupport{

	private DoctorService  doctorService;

	public void setDoctorService(DoctorService doctorService) {
		this.doctorService = doctorService;
	}
	
	private String username;
	private String name;
	private String phone;
	private String oldPwd;
	private String newPwd;
	private String confirmPwd;
	
	
	
	




	public void setUsername(String username) {
		this.username = username;
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




	public void setName(String name) {
		this.name = name;
	}




	public void setPhone(String phone) {
		this.phone = phone;
	}




	/**
	 * 医生个人资料
	 * @return
	 */
	public String doctorInfo(){
		Map<String, Object> session = ServletActionContext.getContext().getSession();
		Doctor doctor = (Doctor) session.get("doctor");//从session得到医生对象
		doctor.setUsername(username);
		doctor.setName(name);
		doctor.setPhone(phone);//重新设置doctor对象
		Doctor newDoctor = doctorService.updateDoctorInfo(doctor);//修改doctor对象
		int success = 0;
		if(newDoctor!=null){
			success = 1;
			//重新存入session
			session.put("doctor", newDoctor);
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
	 * 医生密码修改
	 * @return
	 */
	public String doctorPwd(){
		Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
		int state = -1;//原密码错误
		//取出原密码进行比对
		if(doctor.getPwd().equals(Md5Utils.md5(oldPwd))){
			if(newPwd.equals(confirmPwd)){
				state = 1;//修改成功
				doctor.setPwd(Md5Utils.md5(newPwd));
				doctor = doctorService.updateDoctorInfo(doctor);
				//重新存入session
				ServletActionContext.getContext().getSession().put("doctor", doctor);
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
