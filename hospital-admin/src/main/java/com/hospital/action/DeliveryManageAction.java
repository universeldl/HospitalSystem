package com.hospital.action;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.Doctor;
import com.hospital.domain.Authorization;
import com.hospital.domain.Survey;
import com.hospital.domain.DeliveryInfo;
import com.hospital.domain.PageBean;
import com.hospital.domain.Patient;
import com.hospital.domain.PatientType;
import com.hospital.service.SurveyService;
import com.hospital.service.DeliveryService;
import com.hospital.service.PatientService;
import com.hospital.util.Md5Utils;
import com.opensymphony.xwork2.ActionSupport;

public class DeliveryManageAction extends ActionSupport{

	private DeliveryService deliveryService;

	public void setDeliveryService(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}
	

	private int pageCode;
	private int deliveryId;
	private String openID;
	private String pwd;

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public void setPageCode(int pageCode) {
		this.pageCode = pageCode;
	}


	/**
	 * 根据页码分页查询分发信息
	 * @return
	 */
	public String findDeliveryInfoByPage(){
		//获取页面传递过来的当前页码数
		if(pageCode==0){
			pageCode = 1;
		}
		//给pageSize,每页的记录数赋值
		int pageSize = 5;
		
		PageBean<DeliveryInfo> pb = deliveryService.findDeliveryInfoByPage(pageCode,pageSize);
		if(pb!=null){
			pb.setUrl("findDeliveryInfoByPage.action?");
		}
		//存入request域中
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return  "success";
	}
	
	
	
	/**
	 * 根据分发id查询该分发信息
	 * @return
	 */
	public String getDeliveryInfoById(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		DeliveryInfo info = new DeliveryInfo();
		info.setDeliveryId(deliveryId);
		DeliveryInfo newInfo = deliveryService.getDeliveryInfoById(info);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
		    public boolean apply(Object obj, String name, Object value) {
                return obj instanceof Authorization || name.equals("authorization") || obj instanceof Set || name.equals("deliveryInfos");
		   }
		});
		
		
		JSONObject jsonObject = JSONObject.fromObject(newInfo,jsonConfig);
		try {
			response.getWriter().print(jsonObject);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}

	
	
	/**
	 * 分发处理
	 * @return
	 */
	public String deliverySurvey(){
		//分发的主要步骤
		
		/*
		 * 1. 得到分发的病人
		 * 
		 * 2. 查看病人输入的密码是否匹配
		 * 		2.1 如果不匹配提示 密码错误
		 * 		2.2 如果匹配,执行第3步骤
		 * 
		 * 3. 查看该病人的分发信息
		 * 		3.1 查看分发信息的条数
		 * 		3.2 查看该病人的类型得出该病人的最大分发数量
		 * 		3.3 匹配分发的数量是否小于最大分发量
		 * 			3.3.1 小于,则可以分发
		 * 			3.3.2 大于,则不可以分发,直接返回分发数量已超过
		 * 		3.4 查看病人的延期信息,是否有未设置的延期
		 * 			3.4.1 如果没有,继续第3的操作步骤
		 * 			3.4.2 如果有,直接返回有尚未设置的提醒
		 * 
		 * 4. 查看分发的问卷
		 * 		4.1 查看该问卷的总回收数是否大于1,,如果为1则不能分发,必须留在馆内浏览
		 * 			4.1.1 如果大于1,进入第4操作步骤
		 * 			4.1.2 如果小于等于1,提示该问卷为馆内最后一本,无法分发
		 * 
		 * 5. 处理分发信息
		 * 		5.1 得到该病人的最大分发天数,和几日提醒
		 * 			5.1.1 获得当前时间
		 * 			5.1.2 根据最大分发天数,计算出截止日期
		 * 			5.1.3 为分发信息设置几日的提醒金额
		 * 		5.2 获得该病人的信息,为分发信息设置病人信息
		 * 		5.3 获得问卷信息,为分发信息设置问卷信息
		 * 		5.4 获得医生信息,为分发信息设置操作的医生信息
		 * 
		 * 6. 存储分发信息
		 * 
		 */
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		Patient patient = new Patient();
		patient.setOpenID(openID);
		patient.setPwd(Md5Utils.md5(pwd));
		deliveryInfo.setPatient(patient);
		Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
		deliveryInfo.setDoctor(doctor);
		Survey survey = new Survey();
		deliveryInfo.setSurvey(survey);
		int addDelivery = deliveryService.addDelivery(deliveryInfo);
		try {
			ServletActionContext.getResponse().getWriter().print(addDelivery);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	
	
	
	public String resendSurvey(){
		//重发步骤：
		/*
		 * 1. 得到分发的记录
		 * 
		 * 2. 得到分发的记录的状态
		 * 		2.1 如果已经是重发状态(包括重发未答卷,重发逾期未答卷),则返回已经重发过了,返回-2
		 * 		2.2 如果是答卷状态(包括答卷,重发答卷),则返回已经答卷,无法重发,返回-1
		 *		2.3 如果都不是以上情况,则进行下一步。
		 * 
		 * 3. 得到分发的病人
		 * 
		 * 4. 得到分发的病人类型
		 * 
		 * 5. 得到可重发的天数
		 * 
		 * 6. 在当前记录的截止日期上叠加可重发天数
		 * 		6.1 如果叠加后的时候比当前时间小,则返回你已超重发期了,无法进行重发,提示病人快去答卷和设置提醒  返回-3
		 * 		6.2如果大于当前时间进行下一步
		 * 
		 * 7. 得到当前分发记录的状态
		 * 		7.1 如果当前记录为逾期未答卷,则需要取消当前分发记录的提醒记录,并将该记录的状态设置为重发未答卷
		 * 		7.2如果为未答卷状态,直接将当前状态设置为重发未答卷
		 * 
		 * 8. 为当前分发记录进行设置,设置之后重新update该记录 返回1
		 */
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		deliveryInfo.setDeliveryId(deliveryId);
		int resendSurvey = deliveryService.resendSurvey(deliveryInfo);
		try {
			ServletActionContext.getResponse().getWriter().print(resendSurvey);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		
		return null;
	}
	
}
