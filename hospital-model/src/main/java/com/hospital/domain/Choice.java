package com.hospital.domain;

import javax.persistence.*;
import java.util.Set;

public class Choice {
    private int choiceId;
    //private int questionId;
    private int answerId;
    private int score;
    //private int surveyId;
    private String choiceContent;
    private int aid;
    //private Question question;
    //private Answer answer;

    //public Answer getAnswer() { return answer; }
    //public void setAnswer(Answer answer) { this.answer = answer; }

    //public Question getQuestion() { return question; }
    //public void setQuestion(Question question) { this.question = question; }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    //public int getQuestionId() {
    //    return questionId;
    //}
    //public void setQuestionId(int questionId) {
    //    this.questionId = questionId;
    //}

    //public int getSurveyId() {
    //    return surveyId;
    //}
    //public void setSurveyId(int surveyId) {
    //    this.surveyId = surveyId;
    //}

    public String getChoiceContent() {
        return choiceContent;
    }

    public void setChoiceContent(String choiceContent) {
        this.choiceContent = choiceContent;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }


    public Choice() {
    }

    public Choice(int aid, /*int surveyId, int questionId, */ String choiceContent, int score) {
        super();
        this.aid = aid;
        //this.surveyId = surveyId;
        //this.questionId = questionId;
        this.choiceContent = choiceContent;
        this.score = score;
    }


}
