package com.hospital.action;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.http.HttpServletResponse;

import com.hospital.domain.*;
import com.opensymphony.xwork2.ActionContext;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.struts2.ServletActionContext;

import com.hospital.service.RetrieveService;
import com.opensymphony.xwork2.ActionSupport;

public class RetrieveManageAction extends ActionSupport{

	private RetrieveService retrieveService;

	public void setRetrieveService(RetrieveService retrieveService) {
		this.retrieveService = retrieveService;
	}
	
	private int pageCode;
	private Set<Answer> myAnswers;
	
	private int deliveryId;
	private int surveyId;
	private String openID;

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public void setMyAnswers(Set<Answer> myAnswers) { this.myAnswers = myAnswers; }

	public void setSurveyId(int surveyId) { this.surveyId = surveyId; }

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public void setPageCode(int pageCode) {
		this.pageCode = pageCode;
	}

	public String findRetrieveInfoByPage(){
		
		//获取页面传递过来的当前页码数
		if(pageCode==0){
			pageCode = 1;
		}
		//给pageSize,每页的记录数赋值
		int pageSize = 5;
		
		PageBean<RetrieveInfo> pb = retrieveService.findRetrieveInfoByPage(pageCode,pageSize);
		if(pb!=null){
			pb.setUrl("findRetrieveInfoByPage.action?");
		}
		//存入request域中
		RetrieveInfo retrieveInfo = new RetrieveInfo();
		retrieveInfo.setDeliveryId(1);
		RetrieveInfo newRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);
		myAnswers = newRetrieveInfo.getAnswers();
		ActionContext actionContext = ActionContext.getContext();
		actionContext.put("myAnswers", myAnswers);
		ServletActionContext.getRequest().setAttribute("myAnswers", myAnswers);
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return  "success";
	}


	public String  getAnswerBySurveyId(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		RetrieveInfo retrieveInfo = new RetrieveInfo();
		retrieveInfo.setDeliveryId(surveyId);
		myAnswers = retrieveService.getAnswerBySurveyId(retrieveInfo);

		ActionContext actionContext = ActionContext.getContext();
		actionContext.put("myAnswers", myAnswers);
		ServletActionContext.getRequest().setAttribute("myAnswers", myAnswers);
		return  "success";
	}


	public String  getRetrieveInfoById(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		RetrieveInfo retrieveInfo = new RetrieveInfo();
		retrieveInfo.setDeliveryId(deliveryId);
		RetrieveInfo newRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);

		Answer answer = new Answer();
		Set<Answer> answers = new HashSet<>();
		answers.add(answer);
		newRetrieveInfo.setAnswers(answers);
		myAnswers = newRetrieveInfo.getAnswers();
		ActionContext actionContext = ActionContext.getContext();
		actionContext.put("myAnswers", myAnswers);
		ServletActionContext.getRequest().setAttribute("myAnswers", myAnswers);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
		    public boolean apply(Object obj, String name, Object value) {
                return obj instanceof Authorization || name.equals("authorization") || obj instanceof Set || name.equals("deliveryInfos");
		   }
		});
		
		
		JSONObject jsonObject = JSONObject.fromObject(newRetrieveInfo,jsonConfig);
		try {
			response.getWriter().print(jsonObject);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	
	public String queryRetrieveInfo(){
		//获取页面传递过来的当前页码数
		if(pageCode==0){
			pageCode = 1;
		}
		//给pageSize,每页的记录数赋值
		int pageSize = 5;
		PageBean<RetrieveInfo> pb = null;
		if("".equals(openID.trim()) && deliveryId==0){
			pb = retrieveService.findRetrieveInfoByPage(pageCode,pageSize);
		}else{
			pb = retrieveService.queryRetrieveInfo(openID,deliveryId,pageCode,pageSize);
		}
		if(pb!=null){
			pb.setUrl("queryRetrieveInfo.action?openID="+openID+"&deliveryId="+deliveryId+"&");
		}

		RetrieveInfo retrieveInfo = new RetrieveInfo();
		retrieveInfo.setDeliveryId(deliveryId);
		RetrieveInfo newRetrieveInfo = retrieveService.getRetrieveInfoById(retrieveInfo);

		Answer answer = new Answer();
		Set<Answer> answers = new HashSet<>();
		answers.add(answer);
		newRetrieveInfo.setAnswers(answers);
		myAnswers = newRetrieveInfo.getAnswers();
		ActionContext actionContext = ActionContext.getContext();
		actionContext.put("myAnswers", myAnswers);
		ServletActionContext.getRequest().setAttribute("myAnswers", myAnswers);

		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "success";
	}
	
	public String retrieveSurvey(){
		//答卷的步骤
		/*
		 * 1. 获得操作的分发编号
		 * 2. 获得当前的医生
		 * 3. 获得分发的问卷
		 * 		3.1 问卷的总回收数增加
		 * 4. 获取当前时间
		 * 5. 设置操作医生
		 * 6. 设置答卷时间
		 * 7. 设置答卷的状态
		 * 8. 设置分发的状态 
		 */
		Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
		RetrieveInfo retrieveInfo = new RetrieveInfo();
		retrieveInfo.setDeliveryId(deliveryId);
		retrieveInfo.setDoctor(doctor);
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		deliveryInfo.setDeliveryId(deliveryId);
		retrieveInfo.setDeliveryInfo(deliveryInfo);
		
		int success = retrieveService.addRetrieveInfo(retrieveInfo);
		 HttpServletResponse response = ServletActionContext.getResponse();
		 try {	
			response.getWriter().print(success);		
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	
	
	
}

