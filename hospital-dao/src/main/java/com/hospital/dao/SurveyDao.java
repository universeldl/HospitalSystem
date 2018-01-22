package com.hospital.dao;

import com.hospital.domain.PageBean;
import com.hospital.domain.Question;
import com.hospital.domain.Survey;

import java.util.List;

public interface SurveyDao {

    PageBean<Survey> findSurveyByPage(int pageCode, int pageSize);

    boolean addSurvey(Survey survey);

    boolean addQuestion(Question question);

    Survey getSurveyById(Survey survey);

    Survey updateSurveyInfo(Survey updateSurvey);

    PageBean<Survey> querySurvey(Survey survey, int pageCode, int pageSize);

    boolean deleteSurvey(Survey survey);

    int batchAddSurvey(List<Survey> surveys, List<Survey> failSurveys);

    List<Survey> findAllSurveys();

}
