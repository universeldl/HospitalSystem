package com.hospital.domain;

import javax.persistence.*;
import java.util.Set;

public class Choice {
    private int choiceId;
    private int questionId;
    private int surveyId;
    private String choiceContent;
    private int aid;
    private Question question;
    private Answer answer;

    public Answer getAnswer() { return answer; }
    public void setAnswer(Answer answer) { this.answer = answer; }

	public Question getQuestion() { return question; }
	public void setQuestion(Question question) { this.question = question; }

    public int getChoiceId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Choice choice = (Choice) o;

        if (choiceId != choice.choiceId) return false;
        if (questionId != choice.questionId) return false;
        if (surveyId != choice.surveyId) return false;
        if (aid != choice.aid) return false;
        if (choiceContent != null ? !choiceContent.equals(choice.choiceContent) : choice.choiceContent != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = choiceId;
        result = 31 * result + questionId;
        result = 31 * result + surveyId;
        result = 31 * result + (choiceContent != null ? choiceContent.hashCode() : 0);
        result = 31 * result + aid;
        return result;
    }


}
