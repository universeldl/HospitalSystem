package com.hospital.action;

import com.hospital.domain.*;
import com.hospital.service.ChoiceService;
import com.hospital.service.QuestionService;
import com.hospital.service.SurveyService;
import com.hospital.service.SurveyTypeService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class SurveyManageAction extends ActionSupport {


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
        this.choiceService = choiceService;
    }

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    private int pageCode;


    private Integer questionId;
    private Integer textChoice;
    private Integer questionType;
    private String questionContent;
    private Integer startAge;
    private Integer endAge;

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    private Integer surveyTypeId;    //问卷类型
    private Integer frequency;    //分发周期
    private Integer times;    //随访次数
    private String surveyName;    //问卷名称
    private String author;    //作者名称
    private String department;    //科室
    private Integer num;    //总分发数
    private String description;    //简介
    private int surveyId;//问卷编号
    private boolean sendOnRegister;
    private String fileName;
    private Integer bday;

    /**
     * @param fileName the fileName to set
     */

    public void setBday(Integer bday) {
        this.bday = bday;
    }

    public Integer getBday() {
        return bday;
    }

    public void setStartAge(Integer startAge) {
        this.startAge = startAge;
    }

    public void setEndAge(Integer endAge) {
        this.endAge = endAge;
    }

    public void setSendOnRegister(boolean sendOnRegister) {
        this.sendOnRegister = sendOnRegister;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }


    public void setTextChoice(Integer textChoice) {
        this.textChoice = textChoice;
    }


    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }


    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }


    public void setSurveyTypeId(Integer surveyTypeID) {
        this.surveyTypeId = surveyTypeID;
    }


    public void setTimes(Integer times) {
        this.times = times;
    }


    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
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
     * 封装接收客户端传过来的post数据
     *
     * @param ctx ActionContext的对象
     * @return
     */
    public static String getRequestBody(ActionContext ctx) {
        try {
            HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
            InputStream inputStream = request.getInputStream();
            String strMessage = "";
            Map<String, String[]> pMap = request.getParameterMap();
            StringBuffer buff = new StringBuffer();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            while ((strMessage = bufferReader.readLine()) != null) {
                buff.append(strMessage);
            }
            bufferReader.close();
            inputStream.close();
            return buff.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
     * 得到问卷的集合
     * ajax请求该方法
     * 返回问卷集合的json对象
     *
     * @return
     */
    public String findAllSurveys() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        List<Survey> allSurveys = surveyService.findAllSurveys();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                return !name.equals("surveyId") && !name.equals("surveyName");
            }
        });

        String json = JSONArray.fromObject(allSurveys, jsonConfig).toString();//List------->JSONArray
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
     * 添加问卷
     *
     * @return
     */
    public String addSurvey() {
        SurveyType surveyType = new SurveyType();
        surveyType.setTypeId(surveyTypeId);
        Date putdate = new Date(System.currentTimeMillis());//得到当前时间,作为生成时间
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");//得到操作医生
        Survey survey = new Survey(surveyType, surveyName, author, department, putdate, description, doctor, frequency, times, sendOnRegister, bday);//设置问卷
        boolean b = surveyService.addSurvey(survey);//添加问卷.返回是否成功添加
        int success = 0;
        if (b) {
            success = 1;
        } else {
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
     *
     * @return
     */
    public String addQuestion() {
        boolean b = true;
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");

        Question question = new Question(doctor.getAid(), surveyId, questionContent, questionType, textChoice, startAge, endAge);

        if (questionType == 1 || questionType == 2) {  //is a selection question
            List<String> choices = new ArrayList<>();
            List<Float> scores = new ArrayList<>();
            ActionContext ctx = ActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
            Map<String, String[]> pMap = request.getParameterMap();
            int idx = 0;
            for (String[] value : pMap.values()) {
                if (idx < 6) {
                    idx++;
                } else {
                    if (idx % 2 == 0) {
                        choices.add(value[0]);
                    } else {
                        scores.add(Float.parseFloat(value[0]));
                    }
                    idx++;
                }
            }

            for (int i = 0; i < choices.size(); i++) {
                Choice choice = new Choice(doctor.getAid(), choices.get(i), scores.get(i));
                question.getChoices().add(choice);
                //choice.setQuestionId(question.getQuestionId());
                choice.setQuestion(question);
                b = choiceService.addChoice(choice);
                if (!b) break;//break whenever add failing
                //TODO for robustness, we should remove the question and added choices if failed
            }
        } else if (questionType == 3) {
            question.setTextChoice(1);
            b = questionService.addQuestion(question);
        } else if (questionType == 4) {
            question.setTextChoice(1);
            b = questionService.addQuestion(question);
        }
        int success = 0;
        if (b) {
            success = 1;
        } else {
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
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object obj, String name, Object value) {
                return obj instanceof Authorization || name.equals("authorization")
                        || name.equals("questions") || name.equals("retrieveInfos");
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
     * 添加问题
     *
     * @return
     */
    public String getQuestion() {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
        Question question = new Question();
        question.setQuestionId(questionId);
        Question newQuestion = questionService.getQuestionById(question);//得到问题

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONObject jsonObject = JSONObject.fromObject(newQuestion, jsonConfig);
        try {
            response.getWriter().print(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 修改问题
     *
     * @return
     */
    public String updateQuestion() {
        Question question = new Question();
        question.setQuestionId(questionId);
        Question updateQuestion = questionService.getQuestionById(question);
        updateQuestion.setQuestionContent(questionContent);
        updateQuestion.setQuestionType(questionType);
        updateQuestion.setTextChoice(textChoice);
        updateQuestion.setSurveyId(surveyId);
        updateQuestion.setStartAge(startAge);
        updateQuestion.setEndAge(endAge);
        Doctor doctor = (Doctor) ServletActionContext.getContext().getSession().get("doctor");

        for (Choice choice : updateQuestion.getChoices()) {  //clean existing choices in db
            choice.setQuestion(null);
            choiceService.deleteChoice(choice);
        }
        updateQuestion.getChoices().clear();    //clean existing choices in question

        if (questionType == 1 || questionType == 2) {  //is a selection question
            List<String> choices = new ArrayList<>();
            List<Float> scores = new ArrayList<>();
            ActionContext ctx = ActionContext.getContext();
            HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
            Map<String, String[]> pMap = request.getParameterMap();
            int idx = 0;
            for (String[] value : pMap.values()) {
                if (idx < 7) {
                    idx++;
                } else {
                    if (idx % 2 == 1) {
                        choices.add(value[0]);
                    } else {
                        scores.add(Float.parseFloat(value[0]));
                    }
                    idx++;
                }
            }

            for (int i = 0; i < choices.size(); i++) {  //add new choices to db and question
                Choice choice = new Choice(doctor.getAid(), choices.get(i), scores.get(i));
                choice.setQuestion(updateQuestion);
                updateQuestion.getChoices().add(choice);
                choiceService.addChoice(choice);
            }
        } else if (questionType == 3) {
            updateQuestion.setTextChoice(1);
        } else if (questionType == 4) {
            updateQuestion.setTextChoice(1);
        }

        Question newQuestion = questionService.updateQuestionInfo(updateQuestion);  //update question
        int success = 0;
        if (newQuestion != null) {
            success = 1;
        } else {
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
     * 删除问题
     *
     * @return
     */
    public String deleteQuestion() {
        Question tmpQuestion = new Question();
        tmpQuestion.setQuestionId(questionId);
        Question question = questionService.getQuestionById(tmpQuestion);
        for (Choice choice : question.getChoices()) {
            choiceService.deleteChoice(choice);
        }
        question.getChoices().clear();
        int success = questionService.deleteQuestion(question);
        try {
            ServletActionContext.getResponse().getWriter().print(success);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     * 问题排序
     *
     * @return
     */
    public String sortQuestion() {
        ActionContext ctx = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        Map<String, String[]> pMap = request.getParameterMap();
        int success = 1;
        int idx = 1;
        for (String[] value : pMap.values()) {
            if (idx == 2) {
                for (int i = 1; i < value.length; i++) {//i increase from [1] since [0] is ""(table header).
                    Question question = new Question();
                    question.setQuestionId(Integer.parseInt(value[i]));
                    Question sortQuestion = questionService.getQuestionById(question);
                    sortQuestion.setSortId(i);
                    Question newQuestion = questionService.updateQuestionInfo(sortQuestion);  //update question
                    if (newQuestion == null) {
                        success = 0;
                    }
                }
            }
            idx++;
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
     * 修改问卷
     *
     * @return
     */
    public String updateSurvey() {

        Survey survey = new Survey();
        survey.setSurveyId(surveyId);
        Survey updateSurvey = surveyService.getSurveyById(survey);//得到修改的问卷信息
        updateSurvey.setSurveyName(surveyName);
        updateSurvey.setAuthor(author);//对问卷进行修改
        SurveyType type = new SurveyType();
        type.setTypeId(surveyTypeId);
        updateSurvey.setSurveyType(type);//设置问卷类型
        updateSurvey.setDescription(description);
        updateSurvey.setFrequency(frequency);
        updateSurvey.setDepartment(department);
        updateSurvey.setSendOnRegister(sendOnRegister);
        updateSurvey.setBday(bday);
        Survey newSurvey = surveyService.updateSurveyInfo(updateSurvey);//修改问卷信息对象
        int success = 0;
        if (newSurvey != null) {
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


    /**
     * 删除指定问卷
     *
     * @return
     */
    public String deleteSurvey() {
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
     *
     * @return
     */
    public String addSurveyNum() {
        Survey survey = new Survey();
        survey.setSurveyId(surveyId);
        Survey updateSurvey = surveyService.getSurveyById(survey);
        updateSurvey.setNum(updateSurvey.getNum() + num);
        updateSurvey.setCurrentNum((updateSurvey.getCurrentNum() + num));
        Survey newSurvey = surveyService.updateSurveyInfo(updateSurvey);
        int success = 0;
        if (newSurvey != null) {
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
        JSONObject batchAddSurvey = surveyService.batchAddSurvey(fileName, doctor);
        try {
            response.getWriter().print(batchAddSurvey);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public String exportSurvey() {
        HttpServletResponse response = ServletActionContext.getResponse();
        String filePath = surveyService.exportSurvey();
        try {
            response.getWriter().print(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public String questionManage() {
        Survey survey = new Survey();
        survey.setSurveyId(surveyId);
        Survey newSurvey = surveyService.getSurveyById(survey);

        ActionContext actionContext = ActionContext.getContext();
        actionContext.put("survey", newSurvey);
        ServletActionContext.getRequest().setAttribute("survey", newSurvey);
        return "question";
    }
}
