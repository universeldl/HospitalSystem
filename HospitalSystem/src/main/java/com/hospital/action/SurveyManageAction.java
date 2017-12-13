package com.hospital.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.http.HttpServletResponse;

import com.hospital.domain.*;
import com.hospital.service.ChoiceService;
import com.hospital.service.QuestionService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.struts2.ServletActionContext;

import com.hospital.service.SurveyService;
import com.hospital.service.SurveyTypeService;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class SurveyManageAction extends ActionSupport{

	
	private SurveyService surveyService;
	private SurveyTypeService surveyTypeService;

	private QuestionService questionService;
    private ChoiceService choiceService;


	public void setSurveyTypeService(SurveyTypeService surveyTypeService) {
		this.surveyTypeService = surveyTypeService;
	}


	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public void setChoiceService(ChoiceService choiceService) {
		this.choiceService= choiceService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}

	private int pageCode;


	private Integer questionType;
	private String questionContent;
	private String choiceOption1;
	private String choiceOption2;
	private String choiceOption3;
	private String choiceOption4;
	private String choiceOption5;

	
	public void setPageCode(int pageCode) {
		this.pageCode = pageCode;
	}
	
	private Integer surveyTypeId;	//问卷类型
	private String surveyName;	//问卷名称
	private String author;	//作者名称
	private String department;	//科室
	private Integer num;	//总分发数
	private String description;	//简介
	private int surveyId;//问卷编号

	private String fileName;
	

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}


	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}


	public void setChoiceOption1(String choiceOption1) {
		this.choiceOption1 = choiceOption1;
	}


	public void setChoiceOption2(String choiceOption2) {
		this.choiceOption2 = choiceOption2;
	}


	public void setChoiceOption3(String choiceOption3) {
		this.choiceOption3 = choiceOption3;
	}


	public void setChoiceOption4(String choiceOption4) {
		this.choiceOption4 = choiceOption4;
	}


	public void setChoiceOption5(String choiceOption5) {
		this.choiceOption5 = choiceOption5;
	}


	public void setSurveyTypeId(Integer surveyTypeID) {
		this.surveyTypeId = surveyTypeID;
	}


	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public void setNum(Integer num) {
		this.num = num;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}


	/**
	 * 得到问卷类型的集合
	 * ajax请求该方法
	 * 返回问卷类型集合的json对象
	 * @return
	 */
	public String getAllSurveyTypes(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		List<SurveyType> allSurveyTypes = surveyTypeService.getAllSurveyTypes();
		
		
		
		String json = JSONArray.fromObject(allSurveyTypes).toString();//List------->JSONArray
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据页码查询问卷
	 * @return
	 */
	public String findSurveyByPage(){
		//获取页面传递过来的当前页码数
		if(pageCode==0){
			pageCode = 1;
		}
		//给pageSize,每页的记录数赋值
		int pageSize = 5;
		
		PageBean<Survey> pb = surveyService.findSurveyByPage(pageCode,pageSize);
		if(pb!=null){
			pb.setUrl("findSurveyByPage.action?");
		}
		//存入request域中
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return  "success";
	}
	
	/**
	 * 添加问卷
	 * @return
	 */
	public String addSurvey(){
		SurveyType surveyType = new SurveyType();
		surveyType.setTypeId(surveyTypeId);
		Date putdate = new Date(System.currentTimeMillis());//得到当前时间,作为生成时间
		Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");//得到操作医生
		Survey survey = new Survey(surveyType, surveyName, author, department, putdate, num, num, description, doctor);//设置问卷
		boolean b = surveyService.addSurvey(survey);//添加问卷.返回是否成功添加
		int success = 0;
		if(b){
			success = 1;
		}else{
			success = 0;
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
	 * 添加问题
	 * @return
	 */
	public String addQuestion(){

        Set<String> chos = new HashSet<>();
        chos.add(choiceOption1);
        chos.add(choiceOption2);
        chos.add(choiceOption3);
        chos.add(choiceOption4);
        chos.add(choiceOption5);
        boolean b = true;

        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
        Question question = new Question(doctor.getAid(), surveyId, questionContent, questionType);
        b = questionService.addQuestion(question);

        for(String choiceOption : chos) {
            if(!"".equals(choiceOption.trim())) {
                Choice choice = new Choice(doctor.getAid(), question.getQuestionId(), choiceOption);
                question.getChoices().add(choice);
                //choice.setQuestionId(question.getQuestionId());
                b = choiceService.addChoice(choice);
                if(!b) break;//break whenever add failing
                //TODO for robustness, we should remove the question and added choices if failed
            }
        }

		int success = 0;
		if(b){
			success = 1;
		}else{
			success = 0;
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
	 * 得到指定问卷编号的问卷信息
	 * ajax请求该方法
	 * 返回该问卷信息的json对象
	 * @return
	 */
	public String getSurvey(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		Survey survey = new Survey();
		survey.setSurveyId(surveyId);
		Survey newSurvey = surveyService.getSurveyById(survey);//得到问卷
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
		    public boolean apply(Object obj, String name, Object value) {
                return obj instanceof Authorization || name.equals("authorization");
		   }
		});
		
		
		JSONObject jsonObject = JSONObject.fromObject(newSurvey,jsonConfig);
		try {
			response.getWriter().print(jsonObject);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}

	
	
	
	/**
	 * 修改问卷
	 * @return
	 */
	public String updateSurvey(){
		Survey survey = new Survey();
		survey.setSurveyId(surveyId);
		Survey updateSurvey = surveyService.getSurveyById(survey);//得到修改的问卷信息
		updateSurvey.setSurveyName(surveyName);
		updateSurvey.setAuthor(author);//对问卷进行修改
		SurveyType type = new SurveyType();
		type.setTypeId(surveyTypeId);
		updateSurvey.setSurveyType(type);//设置问卷类型
		updateSurvey.setDescription(description);
		updateSurvey.setDepartment(department);
		Survey newSurvey = surveyService.updateSurveyInfo(updateSurvey);//修改问卷信息对象
		int success = 0;
		if(newSurvey!=null){
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
	 * 多条件查询问卷
	 * @return
	 */
	public String querySurvey(){
		//获取页面传递过来的当前页码数
		if(pageCode==0){
			pageCode = 1;
		}
		//给pageSize,每页的记录数赋值
		int pageSize = 5;
		PageBean<Survey> pb = null;

		if("".equals(surveyName.trim()) && surveyTypeId==-1 && "".equals(department.trim()) && "".equals(author.trim())){
			pb = surveyService.findSurveyByPage(pageCode,pageSize);
		}else{
			SurveyType surveyType = new SurveyType();
			surveyType.setTypeId(surveyTypeId);
			Survey survey = new Survey(surveyType, surveyName, author, department);

			pb = surveyService.querySurvey(survey,pageCode,pageSize);
		}
		if(pb!=null){
			pb.setUrl("querySurvey.action?surveyName="+surveyName+"&surveyTypeId="+surveyTypeId+"&department="+department+"&author="+author+"&");
		}
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "success";
	}
	
	
	
	/**
	 * 删除指定问卷
	 * @return
	 */
	public String deleteSurvey(){
		//删除问卷需要注意的事项：如果该答卷有尚未答卷的记录或者尚未设置的延期记录,则不能删除
		Survey survey = new Survey();
		survey.setSurveyId(surveyId);
		int success = surveyService.deleteSurvey(survey);//删除问卷

		try {
			ServletActionContext.getResponse().getWriter().print(success);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		
		return null;
	}
	
	
	/**
	 * 新增问卷，对问卷的库存数量进行增加
	 * @return
	 */
	public String addSurveyNum(){
		Survey survey = new Survey();
		survey.setSurveyId(surveyId);
		Survey updateSurvey = surveyService.getSurveyById(survey);
		updateSurvey.setNum(updateSurvey.getNum()+num);
		updateSurvey.setCurrentNum((updateSurvey.getCurrentNum()+num));
		Survey newSurvey = surveyService.updateSurveyInfo(updateSurvey);
		int success = 0;
		if(newSurvey!=null){
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
	
	public String batchAddSurvey() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");
		JSONObject batchAddSurvey = surveyService.batchAddSurvey(fileName,doctor);
		try {
			response.getWriter().print(batchAddSurvey);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
	
	
	public String exportSurvey(){
		HttpServletResponse response = ServletActionContext.getResponse();
		String filePath = surveyService.exportSurvey();
		try {
			response.getWriter().print(filePath);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return null;
	}
}
