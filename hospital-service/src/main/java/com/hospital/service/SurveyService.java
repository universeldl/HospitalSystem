package com.hospital.service;

import com.hospital.domain.Doctor;
import com.hospital.domain.PageBean;
import com.hospital.domain.Question;
import com.hospital.domain.Survey;
import net.sf.json.JSONObject;

public interface SurveyService {


    PageBean<Survey> findSurveyByPage(int pageCode, int pageSize);

    boolean addSurvey(Survey survey);

    boolean addQuestion(Question question);

    Survey getSurveyById(Survey survey);

    Survey updateSurveyInfo(Survey updateSurvey);

    PageBean<Survey> querySurvey(Survey survey, int pageCode, int pageSize);

    int deleteSurvey(Survey survey);

    JSONObject batchAddSurvey(String fileName, Doctor doctor);

    String exportSurvey();


}
