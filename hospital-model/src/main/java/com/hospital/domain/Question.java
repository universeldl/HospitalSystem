package com.hospital.domain;

import java.util.*;

public class Question {
    private Integer questionId;
    private Integer sortId;
    private Integer surveyId;
    private String questionContent;
    private Integer textChoice;
    private Integer questionType;
    private Integer aid;
    private Integer startAge;
    private Integer endAge;
    //private Survey survey;
    private Set<Choice> choices = new HashSet<>();
    /*private List<Choice> sortedChoices;*/

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setStartAge(Integer startAge) {
        this.startAge = startAge;
    }

    public Integer getStartAge() {
        return startAge;
    }

    public void setEndAge(Integer endAge) {
        this.endAge = endAge;
    }

    public Integer getEndAge() {
        return endAge;
    }

    public Set<Choice> getChoices() {
        return choices;
    }

    public void setChoices(Set<Choice> choices) {
        this.choices = choices;
    }

    //public Survey getSurvey() { return survey; }
    //public void setSurvey(Survey survey) { this.survey = survey; }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getTextChoice() {
        return textChoice;
    }

    public void setTextChoice(Integer textChoice) {
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
    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Question() {

    }

/*    public List<Choice> getSortedChoices() {
        sortedChoices = new ArrayList<>(choices);
        Collections.sort(sortedChoices, new Comparator<Choice>() {
            @Override
            public int compare(Choice o1, Choice o2) {
                return Integer.compare(o1.getChoiceId(), o2.getChoiceId());
            }
        });
        return sortedChoices;
    }*/
    public Question(Integer aid, Integer surveyId, String questionContent,
                    Integer questionType, Integer textChoice, Integer startAge, Integer endAge) {
        super();
        this.aid = aid;
        this.surveyId = surveyId;
        this.questionContent = questionContent;
        this.questionType = questionType;
        this.textChoice = textChoice;
        this.startAge = startAge;
        this.endAge = endAge;
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
