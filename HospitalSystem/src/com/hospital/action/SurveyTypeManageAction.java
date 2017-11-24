package com.hospital.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.struts2.ServletActionContext;

import com.hospital.domain.SurveyType;
import com.hospital.domain.PageBean;
import com.hospital.service.SurveyService;
import com.hospital.service.SurveyTypeService;
import com.opensymphony.xwork2.ActionSupport;

public class SurveyTypeManageAction extends ActionSupport{

	private SurveyTypeService surveyTypeService;

	public void setSurveyTypeService(SurveyTypeService surveyTypeService) {
		this.surveyTypeService = surveyTypeService;
	}
	
	private int pageCode;
	private String typeName;
	private Integer id;
	

	
	public void setId(Integer id) {
		this.id = id;
	}



	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}



	public void setPageCode(int pageCode) {
		this.pageCode = pageCode;
	}




	/**
	 * 根据页码查询问卷类型信息
	 * @return
	 */
	public String findSurveyTypeByPage(){
		//获取页面传递过来的当前页码数
		if(pageCode==0){
			pageCode = 1;
		}
		//给pageSize,每页的记录数赋值
		int pageSize = 5;
		
		PageBean<SurveyType> pb = surveyTypeService.findSurveyTypeByPage(pageCode,pageSize);
		if(pb!=null){
			pb.setUrl("findSurveyTypeByPage.action?");
		}
		//存入request域中
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return  "success";
	
	}
	
	
	
	/**
	 * 添加问卷类型
	 * @return
	 */
	public String addSurveyType(){
		SurveyType surveyType = new SurveyType();
		surveyType.setTypeName(typeName);
		SurveyType surveyType2 = surveyTypeService.getSurveyTypeByName(surveyType);//根据名字查询问卷类型，查看是否有重复的问卷类型名称(已存在的问卷类型)
		int success = 0;
		if(surveyType2!=null){
			success = -1;//已经存在该问卷分类
		}else{
			boolean b = surveyTypeService.addSurveyType(surveyType);
			if(!b){
				success = 0;
			}else{
				success = 1;
				//由于是转发并且js页面刷新,所以无需重查
			}
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
	 * 得到问卷类型
	 * ajax请求该方法
	 * 返回问卷类型集合的json对象
	 * @return
	 */
	public String getSurveyType(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		SurveyType surveyType = new SurveyType();
		surveyType.setTypeId(id);
		SurveyType newType = surveyTypeService.getSurveyTypeById(surveyType);
		
		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
		    public boolean apply(Object obj, String name, Object value) {
                //过滤掉集合
                return obj instanceof Set || name.equals("surveys");
		   }
		});
		
		
		JSONObject jsonObject = JSONObject.fromObject(newType,jsonConfig);
		try {
			response.getWriter().print(jsonObject);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	
	/**
	 * 修改问卷类型
	 * @return
	 */
	public String updateSurveyType(){
		SurveyType surveyType = new SurveyType();
		surveyType.setTypeId(id);
		SurveyType updateSurveyType = surveyTypeService.getSurveyTypeById(surveyType);//得到要修改的问卷类型对象
		updateSurveyType.setTypeName(typeName);//重新设置名称
		SurveyType newSurveyType = surveyTypeService.updateSurveyTypeInfo(updateSurveyType);//更新该问卷类型对象
		int success = 0;
		if(newSurveyType!=null){
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
	
	/**
	 * 删除问卷类型
	 * @return
	 */
	public String deleteSurveyType(){
		SurveyType surveyType = new SurveyType();
		surveyType.setTypeId(id);
		boolean deleteType = surveyTypeService.deleteSurveyType(surveyType);
		int success = 0;
		if(deleteType){
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
	
	
	/**
	 * 条件查询问卷类型
	 * @return
	 */
	public String querySurveyType(){
		//获取页面传递过来的当前页码数
		if(pageCode==0){
			pageCode = 1;
		}
		//给pageSize,每页的记录数赋值
		int pageSize = 5;
		PageBean<SurveyType> pb = null;
		if("".equals(typeName.trim())){
			pb = surveyTypeService.findSurveyTypeByPage(pageCode, pageSize);
		}else{
			SurveyType surveyType = new SurveyType();
			surveyType.setTypeName(typeName);
			pb = surveyTypeService.querySurveyType(surveyType,pageCode,pageSize);
		}
		if(pb!=null){
			pb.setUrl("querySurveyType.action?typeName="+typeName+"&");
		}

		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "success";
	}
}
