package com.hospital.service;

import com.hospital.domain.*;
import net.sf.json.JSONObject;

import java.util.List;

public interface SurveyService {


    PageBean<Survey> findSurveyByPage(int pageCode, int pageSize);

    List<Survey> findAllSurveys();

    boolean addSurvey(Survey survey);

    boolean addQuestion(Question question);

    Survey getSurveyById(Survey survey);

    Survey updateSurveyInfo(Survey updateSurvey);

    PageBean<Survey> querySurvey(Survey survey, int pageCode, int pageSize);

    int deleteSurvey(Survey survey);

    JSONObject batchAddSurvey(String fileName, Doctor doctor);

    String exportSurvey();


}
