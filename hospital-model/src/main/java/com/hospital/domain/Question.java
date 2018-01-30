package com.hospital.domain;

import java.util.*;

public class Question {
    private int questionId;
    private int surveyId;
    private String questionContent;
    private int textChoice;
    private int questionType;
    private int aid;
    //private Survey survey;
    private Set<Choice> choices = new HashSet<>();
    private List<Choice> sortedChoices;

    public Set<Choice> getChoices() {
        return choices;
    }

    public void setChoices(Set<Choice> choices) {
        this.choices = choices;
    }

    //public Survey getSurvey() { return survey; }
    //public void setSurvey(Survey survey) { this.survey = survey; }

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

    public int getTextChoice() {
        return textChoice;
    }

    public void setTextChoice(int textChoice) {
        this.textChoice = textChoice;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    /*
    public String getTextChoiceContent() {
        return textChoiceContent;
    }

    public void setTextChoiceContent(String textChoiceContent) {
        this.textChoiceContent = textChoiceContent;
    }

*/
    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public Question() {

    }

    public List<Choice> getSortedChoices() {
        sortedChoices = new ArrayList<>(choices);
        Collections.sort(sortedChoices, new Comparator<Choice>() {
            @Override
            public int compare(Choice o1, Choice o2) {
                return Integer.compare(o1.getChoiceId(), o2.getChoiceId());
            }
        });
        return sortedChoices;
    }
    public Question(int aid, int surveyId, String questionContent, int questionType, int textChoice) {
        super();
        this.aid = aid;
        this.surveyId = surveyId;
        this.questionContent = questionContent;
        this.questionType = questionType;
        this.textChoice = textChoice;
    }

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (questionId != question.questionId) return false;
        if (surveyId != question.surveyId) return false;
        if (questionType != question.questionType) return false;
        if (aid != question.aid) return false;
        if (questionContent != null ? !questionContent.equals(question.questionContent) : question.questionContent != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = questionId;
        result = 31 * result + surveyId;
        result = 31 * result + (questionContent != null ? questionContent.hashCode() : 0);
        result = 31 * result + questionType;
        result = 31 * result + aid;
        return result;
    }*/
}
