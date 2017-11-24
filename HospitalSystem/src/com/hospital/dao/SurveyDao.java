package com.hospital.dao;

import java.util.List;

import com.hospital.domain.Survey;
import com.hospital.domain.SurveyType;
import com.hospital.domain.PageBean;

public interface SurveyDao {

	PageBean<Survey> findSurveyByPage(int pageCode, int pageSize);

	boolean addSurvey(Survey survey);

	Survey getSurveyById(Survey survey);

	Survey updateSurveyInfo(Survey updateSurvey);

	PageBean<Survey> querySurvey(Survey survey, int pageCode, int pageSize);

	boolean deleteSurvey(Survey survey);

	int batchAddSurvey(List<Survey> surveys, List<Survey> failSurveys);

	List<Survey> findAllSurveys();

}
