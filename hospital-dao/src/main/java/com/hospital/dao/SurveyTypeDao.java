package com.hospital.dao;

import java.util.List;

import com.hospital.domain.Survey;
import com.hospital.domain.SurveyType;
import com.hospital.domain.PageBean;

public interface SurveyTypeDao {

    PageBean<SurveyType> findSurveyTypeByPage(int pageCode, int pageSize);

    SurveyType getSurveyTypeByName(SurveyType surveyType);

    boolean addSurveyType(SurveyType surveyType);

    SurveyType getSurveyTypeById(SurveyType surveyType);

    SurveyType updateSurveyTypeInfo(SurveyType surveyType);

    boolean deleteSurveyType(SurveyType surveyType);

    PageBean<SurveyType> querySurveyType(SurveyType surveyType, int pageCode,
                                         int pageSize);

    Survey getSurveyById(Survey survey);

    SurveyType getSurveyType(SurveyType surveyType);

    List<SurveyType> getAllSurveyTypes();
}
