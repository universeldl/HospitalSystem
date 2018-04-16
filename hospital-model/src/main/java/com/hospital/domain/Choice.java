package com.hospital.domain;

import java.math.BigDecimal;

public class Choice {
    private Integer choiceId;
    //private int questionId;
    //private int answerId;
    private BigDecimal score;
    //private int surveyId;
    private String choiceContent;
    private Integer aid;
    private Question question;
    //private Answer answer;
    private String choiceImgPath;

    public void setChoiceImgPath(String choiceImgPath) {
        this.choiceImgPath = choiceImgPath;
    }

    public String getChoiceImgPath() {
        return choiceImgPath;
    }

    //public Answer getAnswer() { return answer; }
    //public void setAnswer(Answer answer) { this.answer = answer; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    //public int getAnswerId() { return answerId; }

    //public void setAnswerId(int answerId) { this.answerId = answerId; }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Integer choiceId) {
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

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }


    public Choice() {
    }

    public Choice(Integer aid, /*int surveyId, int questionId, */ String choiceContent, BigDecimal score) {
        super();
        this.aid = aid;
        //this.surveyId = surveyId;
        //this.questionId = questionId;
        this.choiceContent = choiceContent;
        this.score = score;
    }


}
