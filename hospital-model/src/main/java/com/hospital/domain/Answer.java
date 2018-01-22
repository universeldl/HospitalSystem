package com.hospital.domain;

import java.util.HashSet;
import java.util.Set;

public class Answer {
    private int answerId;
    //private int choiceId;
    //private int questionId;
    //private int surveyId;
    private Survey survey;    //分发问卷
    private Patient patient;    //分发病人
    private Doctor doctor;    //操作员
    //private String choiceContent;
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

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
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
    public String getChoiceContent() { return choiceContent; }
    public void setChoiceContent(String choiceContent) { this.choiceContent = choiceContent; }

    public int getAid() {
        return aid;
    }
    public void setAid(int aid) {
        this.aid = aid;
    }
*/
}
