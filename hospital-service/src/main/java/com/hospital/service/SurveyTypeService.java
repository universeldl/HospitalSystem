package com.hospital.service;

import com.hospital.domain.PageBean;
import com.hospital.domain.SurveyType;

import java.util.List;

public interface SurveyTypeService {

    PageBean<SurveyType> findSurveyTypeByPage(int pageCode, int pageSize);

    SurveyType getSurveyTypeByName(SurveyType surveyType);

    boolean addSurveyType(SurveyType surveyType);

    SurveyType getSurveyTypeById(SurveyType surveyType);

    SurveyType updateSurveyTypeInfo(SurveyType surveyType);

    boolean deleteSurveyType(SurveyType surveyType);

    PageBean<SurveyType> querySurveyType(SurveyType surveyType, int pageCode,
                                         int pageSize);

    SurveyType getSurveyType(SurveyType surveyType);

    List<SurveyType> getAllSurveyTypes();
}
