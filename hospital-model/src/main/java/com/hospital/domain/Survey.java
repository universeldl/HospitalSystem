package com.hospital.domain;

import java.io.Serializable;
import java.util.*;

/**
 * 问卷信息类
 *
 * @author c
 */
public class Survey implements Serializable {
    private Integer surveyId;    //问卷编号
    private SurveyType surveyType;    //问卷类型
    private String surveyName;    //问卷名称
    private String author;    //作者名称
    private String department;    //科室
    private Date putdate;    //生成日期
    private Integer num;    //总分发数
    private Integer currentNum;    //总回收数
    private String description;    //简介
    private Doctor doctor;    //操作医生
    private Integer frequency;    //随访频率
    private Integer times;    //随访次数
    private boolean sendOnRegister;
    private Set<Question> questions = new HashSet<>();
    //private Set<RetrieveInfo> retrieveInfos = new HashSet<>();
    private Integer bday;  // overdue day

    public void setBday(Integer bday) {
        this.bday = bday;
    }

    public Integer getBday() {
        return bday;
    }

    public void setSendOnRegister(boolean sendOnRegister) {
        this.sendOnRegister = sendOnRegister;
    }

    public boolean isSendOnRegister() {
        return sendOnRegister;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

/*    public Set<RetrieveInfo> getRetrieveInfos() {
        return retrieveInfos;
    }

    public void setRetrieveInfos(Set<RetrieveInfo> retrieveInfos) {
        this.retrieveInfos = retrieveInfos;
    }*/

    public SurveyType getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(SurveyType surveyType) {
        this.surveyType = surveyType;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getPutdate() {
        return putdate;
    }

    public void setPutdate(Date putdate) {
        this.putdate = putdate;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(Integer currentNum) {
        this.currentNum = currentNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Survey() {


    }

    public List<Question> getSortedQuestions() {
        List<Question> sortedQuestions = new ArrayList<Question>(questions);
        Collections.sort(sortedQuestions, new Comparator<Question>() {
            @Override
            public int compare(Question o1, Question o2) {
                return Integer.compare(o1.getQuestionId(), o2.getQuestionId());
            }
        });
        return sortedQuestions;
    }

    public Survey(SurveyType surveyType, String surveyName, String author,
                  String department, Date putdate, String description,
                  Doctor doctor, Integer frequency, Integer times, boolean sendOnRegister, Integer bday) {
        super();
        this.surveyType = surveyType;
        this.surveyName = surveyName;
        this.author = author;
        this.department = department;
        this.putdate = putdate;
        this.description = description;
        this.doctor = doctor;
        this.frequency = frequency;
        this.times = times;
        this.sendOnRegister = sendOnRegister;
        this.bday = bday;
    }

    public Survey(SurveyType surveyType, String surveyName, String author,
                  String department) {
        super();
        this.surveyType = surveyType;
        this.surveyName = surveyName;
        this.author = author;
        this.department = department;
    }


}
