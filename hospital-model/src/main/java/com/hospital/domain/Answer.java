package com.hospital.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Answer {
    private Integer answerId;
    //private int choiceId;
    //private int questionId;
    //private int surveyId;
    private String lastModified; //最后修改人
    private String modifiedDate;//最后修改日期
    private Survey survey;    //分发问卷
    private Patient patient;    //分发病人
    private Doctor doctor;    //操作员
    private String textChoiceContent;
    private Integer textChoice;
    //private int aid;
    private RetrieveInfo retrieveInfo;
    private Question question;
    private Set<Choice> choices = new HashSet<>();

    public Set<Choice> getChoices() {
        return choices;
    }

    public void setChoices(Set<Choice> choices) {
        this.choices = choices;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public RetrieveInfo getRetrieveInfo() {
        return retrieveInfo;
    }

    public void setRetrieveInfo(RetrieveInfo retrieveInfo) {
        this.retrieveInfo = retrieveInfo;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getTextChoice() {
        return textChoice;
    }

    public void setTextChoice(Integer textChoice) {
        this.textChoice = textChoice;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

/*    public int getChoiceId() {
        return choiceId;
    }
    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public int getQuestionId() {
        return questionId;
    }
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getSurveyId() {
        return surveyId;
    }
    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }
    */

    public String getTextChoiceContent() { return textChoiceContent; }
    public void setTextChoiceContent(String textChoiceContent) { this.textChoiceContent = textChoiceContent; }

/*
    public int getAid() {
        return aid;
    }
    public void setAid(int aid) {
        this.aid = aid;
    }
*/
}
