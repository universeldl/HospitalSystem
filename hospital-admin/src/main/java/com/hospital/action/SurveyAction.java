package com.hospital.action;

import com.hospital.domain.Authorization;
import com.hospital.domain.PageBean;
import com.hospital.domain.Survey;
import com.hospital.domain.SurveyType;
import com.hospital.service.SurveyService;
import com.hospital.service.SurveyTypeService;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class SurveyAction extends ActionSupport {


    private SurveyService surveyService;
    private SurveyTypeService surveyTypeService;


    public void setSurveyTypeService(SurveyTypeService surveyTypeService) {
        this.surveyTypeService = surveyTypeService;
    }


    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    private int pageCode;


    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    private Integer surveyTypeId;    //问卷类型
    private String surveyName;    //问卷名称
    private String author;    //作者名称
    private String department;    //科室
    private int surveyId;//问卷编号


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


    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }


    /**
     * 得到问卷类型的集合
     * ajax请求该方法
     * 返回问卷类型集合的json对象
     *
     * @return
     */
    public String getAllSurveyTypes() {
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
     *
     * @return
     */
    public String findSurveyByPage() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;

        PageBean<Survey> pb = surveyService.findSurveyByPage(pageCode, pageSize);
        if (pb != null) {
            pb.setUrl("findSurveyByPage.action?");
        }
        //存入request域中
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


    /**
     * 得到指定问卷编号的问卷信息
     * ajax请求该方法
     * 返回该问卷信息的json对象
     *
     * @return
     */
    public String getSurvey() {
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


        JSONObject jsonObject = JSONObject.fromObject(newSurvey, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 多条件查询问卷
     *
     * @return
     */
    public String querySurvey() {
        //获取页面传递过来的当前页码数
        if (pageCode == 0) {
            pageCode = 1;
        }
        //给pageSize,每页的记录数赋值
        int pageSize = 5;
        PageBean<Survey> pb = null;

        if ("".equals(surveyName.trim()) && surveyTypeId == -1 && "".equals(department.trim()) && "".equals(author.trim())) {
            pb = surveyService.findSurveyByPage(pageCode, pageSize);
        } else {
            SurveyType surveyType = new SurveyType();
            surveyType.setTypeId(surveyTypeId);
            Survey survey = new Survey(surveyType, surveyName, author, department);

            pb = surveyService.querySurvey(survey, pageCode, pageSize);
        }
        if (pb != null) {
            pb.setUrl("querySurvey.action?surveyName=" + surveyName + "&surveyTypeId=" + surveyTypeId + "&department=" + department + "&author=" + author + "&");
        }
        ServletActionContext.getRequest().setAttribute("pb", pb);
        return "success";
    }


}
