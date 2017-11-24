package com.hospital.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.hospital.domain.Doctor;
import com.hospital.domain.Survey;
import com.hospital.domain.SurveyType;
import com.hospital.domain.PageBean;

public interface SurveyService {



	PageBean<Survey> findSurveyByPage(int pageCode, int pageSize);

	boolean addSurvey(Survey survey);


	Survey getSurveyById(Survey survey);

	Survey updateSurveyInfo(Survey updateSurvey);

	PageBean<Survey> querySurvey(Survey survey, int pageCode, int pageSize);

	int deleteSurvey(Survey survey);

	JSONObject batchAddSurvey(String fileName, Doctor doctor);

	String exportSurvey();


}
